package com.anonym.common.token;

import java.util.List;

/**
 * @author anders
 * @version 1.0
 * @description
 * @date 2019/3/26
 * @modified
 */
public interface IRequestToken<T> {

    void setId(Integer id);

    void setUserType(Integer userType);

    void setData(T data);

    void setXAccessToken(String xAccessToken);

    void setGroupIds(List<Integer> groupIds);

    Integer getId();

    String getName();

    Integer getUserType();

    T getData();

    String getXAccessToken();

    List<Integer> getGroupIds();
}
