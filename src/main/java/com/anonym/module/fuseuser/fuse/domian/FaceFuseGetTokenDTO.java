package com.anonym.module.fuseuser.fuse.domian;

import lombok.Data;

/**
 * @author lizongliang
 * @date 2019-11-15 17:10
 */
@Data
public class FaceFuseGetTokenDTO {

    /**
     * client_credentials
     */
    private String grant_type;

    /**
     * API Key
     */
    private String client_id;

    /**
     * Secret Key
     */
    private String client_secret;

}
