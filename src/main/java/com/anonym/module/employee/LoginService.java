package com.anonym.module.employee;

import com.anonym.common.constant.CommonConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.token.RequestTokenDTO;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.employee.domain.EmployeeLoginDTO;
import com.anonym.module.employee.domain.EmployeeLoginDetailDTO;
import com.anonym.utils.SmartDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *
 */
@Service
public class LoginService {

    @Autowired
    private EmployeeDao employeeDao;


    public ResponseDTO<EmployeeLoginDetailDTO> login(@Valid EmployeeLoginDTO loginDTO, HttpServletRequest request) {

        String loginPwd = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, loginDTO.getLoginPwd());
        EmployeeDTO employeeDTO = employeeDao.login(loginDTO.getLoginName(), loginPwd);
        if (null == employeeDTO) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_FAILED);
        }
        if (EmployeeStatusEnum.DISABLED.equalsValue(employeeDTO.getIsDisabled())) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.IS_DISABLED);
        }
        //jwt token赋值
        RequestTokenDTO<EmployeeDTO> requestTokenDTO = new RequestTokenDTO();
        requestTokenDTO.setId(employeeDTO.getId());
        requestTokenDTO.setUserType(CommonConst.UserType.BACK);
        requestTokenDTO.setData(employeeDTO);


        EmployeeLoginDetailDTO employeeLoginDetailDTO = new EmployeeLoginDetailDTO();
        return ResponseDTO.succData(employeeLoginDetailDTO);
    }
}
