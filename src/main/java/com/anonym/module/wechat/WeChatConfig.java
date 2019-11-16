package com.anonym.module.wechat;

import com.anonym.common.domain.BaseEnum;

import java.util.HashMap;

/**
 * 微信常量配置类
 */
public class WeChatConfig {

    /**
     * 微信小程序 app id
     */
    public static final String MINI_APP_ID = "wx45f66d1526dcd06f";

    /**
     * 微信小程序 app secret
     */
    public static final String MINI_APP_SECRET = "06a9578a514d202bb46ba11acc5f5c62";

    /**
     * 微信支付 商户id 河南品讯网络科技有限公司
     */
    public static final String PAYMENT_MERCHANT_ID = "1560832731";

    /**
     * 微信支付 商户密钥 key
     */
    public static final String PAYMENT_MERCHANT_SECRET_KEY = "EI87s7AayycjDxv43gdi5gpbK6KoYTxv";

    /**
     * 微信支付 app 名称
     */
    public static final String PAYMENT_APP_NAME = "享游购";

    /**
     * 微信支付 通信成功状态码code
     */
    public static final String PAYMENT_RETURN_CODE = "SUCCESS";

    /**
     * 小程序推送自定义 token
     */
    public static final String MSG_TOKEN = "xyly";

    /**
     * 微信access token失效时间 /秒
     */
    public static final int TOKEN_INVALID_SECOND = 7200;

    public static final int SUCCESS_CODE = 0;

    private static HashMap<Integer, String> codeMap;

    static {
        // 初始化微信 错误码信息
        codeMap = new HashMap<>(16);
        codeMap.put(-1, "系统繁忙，请稍候重试");
        codeMap.put(40029, "code无效");
        codeMap.put(40163, "code已被使用");
        codeMap.put(45011, "您的频率太快了，请稍候重试");
    }

    /**
     * 根据微信code 获取错误信息
     * 未知code，则原样返回err msg
     *
     * @param errCode
     * @param errMsg
     * @return
     */
    public static String getErrorMsg(Integer errCode, String errMsg) {
        String msg = codeMap.get(errCode);
        return null == msg ? errMsg : msg;
    }

    public class URL {

        /**
         * 获取 小程序 登录信息 URL
         */
        public static final String MINI_LOGIN = "https://api.weixin.qq.com/sns/jscode2session?appid=" + MINI_APP_ID + "&secret=" + MINI_APP_SECRET + "&js_code={1}&grant_type=authorization_code";

        /**
         * 查询全局 access token
         */
        public static final String GLOBAL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + MINI_APP_ID + "&secret=" + MINI_APP_SECRET;

        /**
         * 微信支付：统一下单 获取预支付交易单
         */
        public static final String PAYMENT_GET_PREPAY_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        /**
         * 微信支付：微信支付订单的查询
         */
        public static final String PAYMENT_QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";

        /**
         * 微信支付：关闭订单
         */
        public static final String PAYMENT_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

        /**
         * 微信支付：申请退款接口
         */
        public static final String PAYMENT_APPLY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

        /**
         * 微信支付：查询退款状态
         */
        public static final String PAYMENT_QUERY_REFUND = "https://api.mch.weixin.qq.com/pay/refundquery";

    }

    /**
     * 微信支付状态 枚举类
     */
    public enum PaymentStateEnum implements BaseEnum {

        /**
         * 支付成功
         */
        SUCCESS("SUCCESS", "支付成功"),

        /**
         * 转入退款
         */
        REFUND("REFUND", "转入退款"),

        /**
         * 未支付
         */
        NOT_PAY("NOTPAY", "订单未支付"),

        /**
         * 已关闭
         */
        CLOSED("CLOSED", "已关闭"),

        /**
         * 已撤销（刷卡支付）
         */
        REVOKED("REVOKED", "已撤销（刷卡支付）"),

        /**
         * 用户支付中
         */
        USER_PAYING("USERPAYING", "用户支付中"),

        /**
         * 支付失败(其他原因，如银行返回失败)
         */
        PAY_ERROR("PAYERROR", "支付失败(其他原因，如银行返回失败)"),

        /**
         * FAIL 支付失败
         */
        FAIL("FAIL", "支付失败");

        private String state;

        private String desc;

        PaymentStateEnum(String state, String desc) {
            this.state = state;
            this.desc = desc;
        }

        /**
         * 获取枚举类的值
         *
         * @return Integer
         */
        @Override
        public String getValue() {
            return state;
        }

        /**
         * 获取枚举类的说明
         *
         * @return String
         */
        @Override
        public String getDesc() {
            return desc;
        }
    }

}
