package com.anonym.module.department;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.department.domain.DepartmentCreateDTO;
import com.anonym.module.department.domain.DepartmentDTO;
import com.anonym.module.department.domain.DepartmentUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理路由器
 */
@Api(tags = {SwaggerTagConst.MANAGER_DEPARTMENT})
@OperateLog
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表", notes = "查询部门列表")
    @GetMapping("/department/list")
    public ResponseDTO<List<DepartmentDTO>> listDepartment() {
        return departmentService.listDepartment();
    }

    @ApiOperation(value = "查询部门及员工列表", notes = "查询部门及员工列表")
    @GetMapping("/department/listEmployee")
    public ResponseDTO<List<DepartmentDTO>> listDepartmentEmployee() {
        return departmentService.listAllDepartmentEmployee(null);
    }

    @ApiOperation(value = "根据部门名称查询部门及员工列表", notes = "根据部门名称查询部门及员工列表")
    @GetMapping("/department/listEmployeeByDepartmentName/{departmentName}")
    public ResponseDTO<List<DepartmentDTO>> listDepartmentEmployee(@PathVariable String departmentName) {
        return departmentService.listAllDepartmentEmployee(departmentName);
    }

    @ApiOperation(value = "添加部门", notes = "添加部门")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentCreateDTO departmentCreateDTO) {
        return departmentService.addDepartment(departmentCreateDTO);
    }

    @ApiOperation(value = "更新部门信息", notes = "更新部门信息")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateDTO departmentUpdateDTO) {
        return departmentService.updateDepartment(departmentUpdateDTO);
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @PostMapping("/department/delete/{departmentId}")
    public ResponseDTO<String> delDepartment(@PathVariable("departmentId") Long departmentId) {
        return departmentService.delDepartment(departmentId);
    }

    @ApiOperation(value = "获取部门信息", notes = "获取部门")
    @GetMapping("/department/query/{departmentId}")
    public ResponseDTO<DepartmentDTO> getDepartment(@PathVariable("departmentId") Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentDTO>> listAll() {
        return departmentService.listAll();
    }

    @ApiOperation(value = "上下移动")
    @GetMapping("/department/upOrDown/{departmentId}/{swapId}")
    public ResponseDTO<String> upOrDown(@PathVariable("departmentId") Integer departmentId, @PathVariable("swapId") Integer swapId) {
        return departmentService.upOrDown(departmentId, swapId);
    }

    @ApiOperation(value = "升级")
    @GetMapping("/department/upgrade/{departmentId}")
    public ResponseDTO<String> upgrade(@PathVariable("departmentId") Integer departmentId) {
        return departmentService.upgrade(departmentId);
    }

    @ApiOperation(value = "降级")
    @GetMapping("/department/downgrade/{departmentId}/{preId}")
    public ResponseDTO<String> downgrade(@PathVariable("departmentId") Integer departmentId, @PathVariable("preId") Integer preId) {
        return departmentService.downgrade(departmentId, preId);
    }

}
