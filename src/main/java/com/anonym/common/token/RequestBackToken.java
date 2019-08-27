package com.anonym.common.token;

import com.anonym.module.employee.domain.EmployeeDTO;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


@Data
public class RequestBackToken implements IRequestToken<EmployeeDTO> {

    /**
     * 用户Id
     */
    private Integer employeeId;

    /**
     * 前台登陆token
     */
    private String xAccessToken;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 登陆用户信息
     */
    private EmployeeDTO employeeDTO;

    private List<Integer> roleIdList;

    @Override
    public void setId(Integer id) {
        this.employeeId = id;
    }

    @Override
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public void setData(EmployeeDTO data) {
        this.employeeDTO = data;
    }

    @Override
    public void setXAccessToken(String xAccessToken) {
        this.xAccessToken = xAccessToken;
    }

    @Override
    public void setGroupIds(List<Integer> groupIds) {
        if (groupIds == null) {
            groupIds = Lists.newArrayList();
        }
        this.roleIdList = groupIds;
    }

    @Override
    public Integer getId() {
        return this.employeeId;
    }

    @Override
    public Integer getUserType() {
        return this.userType;
    }

    @Override
    public EmployeeDTO getData() {
        return this.employeeDTO;
    }

    @Override
    public String getXAccessToken() {
        return this.xAccessToken;
    }

    @Override
    public List<Integer> getGroupIds() {
        return this.roleIdList;
    }

    @Override
    public String getName() {
        return this.employeeDTO.getActualName();
    }
}
