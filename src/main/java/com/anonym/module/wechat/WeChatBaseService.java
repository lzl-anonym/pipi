package com.anonym.module.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.exception.SmartBusinessException;
import com.anonym.utils.SmartBigDecimalUtils;
import com.anonym.utils.SmartRandomUtil;
import com.anonym.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * 微信 业务 基类
 */
@Slf4j
@Service
public class WeChatBaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "RestTemplate4WeChatRefund")
    private RestTemplate refundRestTemplate;

    /**
     * 微信全局 access token
     */
    protected String GLOBAL_ACCESS_TOKEN;

    /**
     * 公用的向微信服务器发送api请求的方法
     * 失败则返回失败信息 ，成功返回 json 数据
     *
     * @param url
     * @param param
     * @return
     */
    public ResponseDTO<JSONObject> sendGetRequest(String url, Object... param) {

        // 发送 GET 请求
        String result = restTemplate.getForObject(url, String.class, param);
        JSONObject jsonObject = JSON.parseObject(result);
        Integer errCode = jsonObject.getInteger("errcode");
        if (null != errCode && !Objects.equals(WeChatConfig.SUCCESS_CODE, errCode)) {
            // 获取错误信息
            String errMsg = WeChatConfig.getErrorMsg(errCode, jsonObject.getString("errmsg"));
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, errMsg);
        }

        return ResponseDTO.succData(jsonObject);
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 暂时无用
     *
     * @return
     */
    // @Scheduled(fixedDelay = WeChatConfig.TOKEN_INVALID_SECOND * 1000)
    public String initGlobalAccessToken() {
        // 发送 GET 请求
        ResponseDTO<JSONObject> response = this.sendGetRequest(WeChatConfig.URL.GLOBAL_ACCESS_TOKEN, String.class, null);
        if (!response.isSuccess()) {
            throw new SmartBusinessException("初始化 WeChat Global Access Token失败：" + response.getMsg());
        }
        JSONObject jsonObject = response.getData();
        this.GLOBAL_ACCESS_TOKEN = jsonObject.getString("access_token");
        log.info("初始化 WeChat Global Access Token成功！");
        return this.GLOBAL_ACCESS_TOKEN;
    }

    /**
     * 公用的向微信服务器发送 支付 api POST 请求的方法
     * 1、将参数签名
     * 2、发送请求，获取通信结果
     * 3、失败则返回失败信息 ，成功返回具体业务的 data 数据
     *
     * @param url
     * @param param
     * @return
     */
    public ResponseDTO<TreeMap<String, String>> sendPaymentRequest(String url, TreeMap<String, Object> param) {

        // -------------------------------------------- 签名 -------------------------------
        // 小程序 ID
        param.put("appid", WeChatConfig.MINI_APP_ID);
        // 微信支付商户号
        param.put("mch_id", WeChatConfig.PAYMENT_MERCHANT_ID);
        // 随机字符串 随机字符串，长度要求在32位以内。
        param.put("nonce_str", SmartRandomUtil.generateRandomString(10));
        // 签名类型 sign_type 非必填 默认md5
        param.put("sign_type", "MD5");
        // 签名 通过签名算法计算得出的签名值
        param.put("sign", paymentSign(param));

        // -------------------------------------------- 转换xml 发起请求 -------------------------------

        // 转换参数为 xml，说明：微信支付参数的根节点是 xml
        String xml = XmlUtil.generateXml("xml", param);

        String randomNum = SmartRandomUtil.generateRandomNum(4);
        log.info("微信支付{}：发送请求URL-{}，XML参数：{}", randomNum, url, xml);

        // 设置请求头 放入参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> httpEntity = new HttpEntity<>(xml, headers);

        /**
         * 发送 POST 请求
         * 根据不同的业务 调用对应 RestTemplate 因为申请退款接口需要使用证书
         */
        RestTemplate tempRestTemplate = Objects.equals(WeChatConfig.URL.PAYMENT_APPLY_REFUND, url) ? refundRestTemplate : restTemplate;
        String result = tempRestTemplate.postForObject(url, httpEntity, String.class);

        log.info("微信支付{}：返回结果-{}", randomNum, result);

        // 解析xml
        TreeMap<String, String> map = XmlUtil.parseXml(result);

        // 获取并判断通信结果 return_code
        String returnCode = map.get("return_code");
        if (!Objects.equals(WeChatConfig.PAYMENT_RETURN_CODE, returnCode)) {
            // 获取错误信息返回
            String returnMsg = map.get("return_msg");
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, returnMsg);
        }

        // 获取并判断 业务结果 result_code
        String resultCode = map.get("result_code");
        if (!Objects.equals(WeChatConfig.PAYMENT_RETURN_CODE, resultCode)) {
            // 获取错误信息返回
            String errCodeDes = map.get("err_code_des");
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, errCodeDes);
        }

        // 成功则 返回结果 map
        return ResponseDTO.succData(map);
    }

    /**
     * 微信支付签名
     *
     * @param param
     * @return
     */
    public static String paymentSign(TreeMap<String, Object> param) {

        /**
         * 将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2
         */
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = param.keySet();
        for (String key : keySet) {
            Object obj = param.get(key);
            if (null == obj) {
                continue;
            }
            sb.append(key + "=" + obj + "&");
        }

        // 拼接加上商户平台设置的密钥key
        sb.append("key=" + WeChatConfig.PAYMENT_MERCHANT_SECRET_KEY);

        // 使用MD5签名 并转换为大写 ，就是最终的签名 sign
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }

    /**
     * 校验 微信支付回调 的签名 是否正确
     *
     * @param data
     * @return
     */
    public static boolean checkPaymentNotifySign(TreeMap<String, String> data) {
        // 拿到微信的签名
        String weChatSign = data.remove("sign");
        if (StringUtils.isBlank(weChatSign)) {
            return false;
        }

        TreeMap<String, Object> tempTreeMap = new TreeMap<>();
        tempTreeMap.putAll(data);
        // 重新签名后 比较是否相同
        String sign = WeChatBaseService.paymentSign(tempTreeMap);
        return Objects.equals(weChatSign, sign);
    }

    /**
     * 将金额的单位：元 转为 分
     * 小数点两位后 四舍五入
     *
     * @param amount
     * @return
     */
    public int convertToCent(BigDecimal amount) {
        return SmartBigDecimalUtils.multiply(amount, new BigDecimal(100), 0).intValue();
    }

    /**
     * 将金额的单位：分 转为 元
     *
     * @param fee
     * @return BigDecimal
     */
    public BigDecimal convertToYuan(int fee) {
        return SmartBigDecimalUtils.divide(new BigDecimal(fee), new BigDecimal(100), 2);
    }

    /**
     * 根据订单号 生成退款单号
     * 默认 TK+原订单号
     *
     * @param orderId
     * @return
     */
    public String generateRefundId(String orderId) {
        return "TK" + orderId;
    }

}
