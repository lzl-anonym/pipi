package com.anonym.module.employee;

import com.anonym.common.anno.NoNeedLogin;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.employee.domain.EmployeeLoginDTO;
import com.anonym.module.employee.domain.EmployeeLoginDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "用户登录")
public class LoginController {

	@Autowired
	private LoginService loginService;


	@PostMapping("/login")
	@ApiOperation("登录")
	@NoNeedLogin
	public ResponseDTO<EmployeeLoginDetailDTO> login(@Valid @RequestBody EmployeeLoginDTO loginDTO, HttpServletRequest request) {
		return loginService.login(loginDTO, request);
	}
}
