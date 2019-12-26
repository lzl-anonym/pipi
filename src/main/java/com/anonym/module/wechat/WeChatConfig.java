package com.anonym.module.wechat;

import com.anonym.common.domain.BaseEnum;

import java.util.HashMap;


public class WeChatConfig {


    public static final String MINI_APP_ID = "";


    public static final String MINI_APP_SECRET = "";


    public static final String PAYMENT_MERCHANT_ID = "";


    public static final String PAYMENT_MERCHANT_SECRET_KEY = "";


    public static final String PAYMENT_APP_NAME = "";


    public static final String PAYMENT_RETURN_CODE = "SUCCESS";


    public static final String MSG_TOKEN = "";


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


    public static String getErrorMsg(Integer errCode, String errMsg) {
        String msg = codeMap.get(errCode);
        return null == msg ? errMsg : msg;
    }

    public class URL {


        public static final String MINI_LOGIN = "https://api.weixin.qq.com/sns/jscode2session?appid=" + MINI_APP_ID + "&secret=" + MINI_APP_SECRET + "&js_code={1}&grant_type=authorization_code";


        public static final String GLOBAL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + MINI_APP_ID + "&secret=" + MINI_APP_SECRET;


        public static final String PAYMENT_GET_PREPAY_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";


        public static final String PAYMENT_QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";


        public static final String PAYMENT_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

        public static final String PAYMENT_APPLY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";


        public static final String PAYMENT_QUERY_REFUND = "https://api.mch.weixin.qq.com/pay/refundquery";

    }


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
