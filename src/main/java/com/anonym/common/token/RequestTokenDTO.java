package com.anonym.common.token;

import lombok.Data;

@Data
public class RequestTokenDTO<T> {

    private Integer id;

    private Integer userType;

    private T data;
}
