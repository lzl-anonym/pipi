package com.anonym.constant;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;


public class CommonConst {


    public static final String SEPARATOR = ",";


    public static final Character SEPARATOR_CHAR = ',';


    public static final String API_PREFIX_ADMIN = "/admin";


    public static final String API_PREFIX_COMMON = "/common";


    public static final String API_PREFIX_APP = "/app";

    public static final class FileService {


        public static final String FOLDER_PUBLIC = "public";


        public static final String FOLDER_PRIVATE = "private";

    }

    public static final class System {


        public static final Long SYSTEM_ID = 0L;
    }

    public static final class Page {

        public static final Integer SIZE = 10;
    }




    public static final class Password {


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



}
