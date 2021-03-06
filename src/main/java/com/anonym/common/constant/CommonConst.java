package com.anonym.common.constant;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections.CollectionUtils;

import java.util.Set;

/**
 * 常量
 */
public class CommonConst {

    public static final class Page {

        public static final Integer SIZE = 10;
    }

    public static final class Password {

        public static final String DEFAULT = "123456";

        public static final String SALT_FORMAT = "smart_%s_admin";
    }

    public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "/v2/api-docs", "Excel");

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

    public static final class UserType {

        public static final Integer BACK = 0;

        public static final Integer WEB = 1;
    }

}
