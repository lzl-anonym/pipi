package com.anonym.constant;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

/**
 * [ 通用常量 ]
 */
public class CommonConst {

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符
     */
    public static final Character SEPARATOR_CHAR = ',';

    /**
     * 后管 api 前缀
     */
    public static final String API_PREFIX_ADMIN = "/admin";

    /**
     * 通用 api 前缀
     */
    public static final String API_PREFIX_COMMON = "/common";

    /**
     * app 业务 api 前缀
     */
    public static final String API_PREFIX_APP = "/app";

    public static final class FileService {

        /**
         * 公用读取文件夹
         */
        public static final String FOLDER_PUBLIC = "public";

        /**
         * 私有读取文件夹
         */
        public static final String FOLDER_PRIVATE = "private";

    }

    public static final class System {

        /**
         * 系统 id
         */
        public static final Long SYSTEM_ID = 0L;
    }

    public static final class Page {

        public static final Integer SIZE = 10;
    }

    /**
     * 长度类常量
     */
    public static final class NumberLimit {

        /**
         * 邀请码长度
         */
        public static final int POPULARIZE_CODE = 10;

        /**
         * 用户昵称
         */
        public static final int USER_NICKNAME = 11;

        /**
         * 用户密码长度
         */
        public static final int USER_PWD_MIN = 6;

        /**
         * 用户密码长度
         */
        public static final int USER_PWD_MAX = 15;

        /**
         * 短信验证码长度
         */
        public static final int SMS_VERIFICATION_CODE = 6;

        /**
         * 短信发送间隔 秒
         */
        public static final int SMS_SEND_INTERVAL = 60;

        /**
         * 短信有效期 秒
         */
        public static final int SMS_VALID_SECOND = 300;

        /**
         * 验证码短信 每天的限制
         */
        public static final int SMS_DAY_LIMIT = 15;
    }

    public static final class Password {

        public static final String DEFAULT = "123456";

        public static final String SALT_FORMAT = "listen_%s_1015";
    }

    public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel");

        public static final Set<String> IGNORE_URL_MAPPING = ImmutableSet.of(IGNORE_H5_URL_MAPPING);

        public static Boolean contain(Set<String> ignores, String uri) {
            if (CollectionUtils.isEmpty(ignores)) {
                return false;
            }
            for (String ignoreUrl : ignores) {
                if (uri.startsWith(ignoreUrl)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final class Order {

        /**
         * 订单兑换码长度
         */
        public static final int EXCHANGE_CODE_LENGTH = 6;
    }

}
