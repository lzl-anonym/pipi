package com.anonym.module.employee;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.employee.domain.EmployeeLoginDTO;
import com.anonym.module.employee.domain.EmployeeLoginDetailDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
public class LoginService {


	public ResponseDTO<EmployeeLoginDetailDTO> login(@Valid EmployeeLoginDTO loginDTO, HttpServletRequest request) {
		EmployeeLoginDetailDTO employeeLoginDetailDTO = new EmployeeLoginDetailDTO();
		return ResponseDTO.succData(employeeLoginDetailDTO);
	}
}
