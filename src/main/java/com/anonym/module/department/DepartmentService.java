package com.anonym.module.department;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.department.domain.DepartmentCreateDTO;
import com.anonym.module.department.domain.DepartmentDTO;
import com.anonym.module.department.domain.DepartmentEntity;
import com.anonym.module.department.domain.DepartmentUpdateDTO;
import com.anonym.module.employee.EmployeeDao;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.utils.SmartBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 部门管理业务类
 * @date 2017/12/19 14:25
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    /**
     * 获取部门树形结构
     *
     * @return
     */
    public ResponseDTO<List<DepartmentDTO>> listDepartment() {
        List<DepartmentDTO> departmentDTOList = departmentDao.listAll();
        List<DepartmentDTO> result = departmentTreeService.buildTree(departmentDTOList);
        return ResponseDTO.succData(result);
    }

    /**
     * 获取所有部门和员工信息
     *
     * @param departmentName
     * @return
     */
    public ResponseDTO<List<DepartmentDTO>> listAllDepartmentEmployee(String departmentName) {

        // 获取全部部门列表
        List<DepartmentDTO> departmentDTOList = departmentDao.queryDepartmentList();
        if (StringUtils.isNotBlank(departmentName)) {
            // 检索条件不为空的时候 过滤部门列表
            departmentDTOList = filterDepartment(departmentDTOList, departmentName);
        }

        Map<Integer, DepartmentDTO> departmentMap = departmentDTOList.stream().collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));
        // 获取全部员工列表
        List<EmployeeDTO> employeeList = employeeDao.listAll();
        employeeList.forEach(employeeDTO -> {

            DepartmentDTO departmentDTO = departmentMap.get(employeeDTO.getDepartmentId());
            if (null == departmentDTO) {
                return;
            }
            List<EmployeeDTO> employeeDTOList = departmentDTO.getEmployees();
            if (null == employeeDTOList) {
                employeeDTOList = new ArrayList<>();
            }
            employeeDTOList.add(employeeDTO);
            departmentDTO.setEmployees(employeeDTOList);
        });
        List<DepartmentDTO> result = departmentTreeService.buildTree(departmentDTOList);
        return ResponseDTO.succData(result);
    }

    /**
     * 过滤部门名称，获取过滤后的结果
     */
    private List<DepartmentDTO> filterDepartment(List<DepartmentDTO> departmentDTOList, String departmentName) {
        Map<Integer, DepartmentDTO> departmentMap = new HashMap<>();
        departmentDTOList.forEach(item -> {
            if (item.getName().indexOf(departmentName) < 0) {
                return;
            }
            // 当前部门包含关键字
            departmentMap.put(item.getId(), item);
            Integer parentId = item.getParentId();
            if (null != parentId) {
                List<DepartmentDTO> filterResult = new ArrayList<>();
                getParentDepartment(departmentDTOList, parentId, filterResult);
                for (DepartmentDTO dto : filterResult) {
                    if (!departmentMap.containsKey(dto.getId())) {
                        departmentMap.put(dto.getId(), dto);
                    }
                }
            }
        });
        return departmentMap.values().stream().collect(Collectors.toList());
    }

    private List<DepartmentDTO> getParentDepartment(List<DepartmentDTO> departmentDTOList, Integer parentId, List<DepartmentDTO> result) {
        List<DepartmentDTO> deptList = departmentDTOList.stream().filter(e -> e.getId().equals(parentId)).collect(Collectors.toList());
        for (DepartmentDTO item : deptList) {
            result.add(item);
            if (item.getParentId() != null && item.getParentId() != 0) {
                result.addAll(getParentDepartment(departmentDTOList, item.getParentId(), result));
            }
        }
        return result;
    }

    /**
     * 新增添加部门
     *
     * @param departmentCreateDTO
     * @return AjaxResult
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> addDepartment(DepartmentCreateDTO departmentCreateDTO) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentCreateDTO, DepartmentEntity.class);
        departmentEntity.setSort(0);
        departmentDao.insert(departmentEntity);
        departmentEntity.setSort(departmentEntity.getId());
        departmentDao.updateById(departmentEntity);
        return ResponseDTO.succ();
    }

    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return AjaxResult<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateDepartment(DepartmentUpdateDTO updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.PARENT_ID_ERROR);
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(entity.getSort());
        departmentDao.updateById(departmentEntity);
        return ResponseDTO.succ();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param departmentId
     * @return AjaxResult<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delDepartment(Long departmentId) {
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(departmentId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.CANNOT_DEL_DEPARTMENT_WITH_CHILD);
        }

        // 是否有员工
        int employeeNum = employeeDao.countByDepartmentId(departmentId);
        if (employeeNum > 0) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.CANNOT_DEL_DEPARTMENT_WITH_EMPLOYEE);
        }
        departmentDao.deleteById(departmentId);
        return ResponseDTO.succ();
    }

    /**
     * 根据id获取部门信息
     *
     * @param departmentId
     * @return AjaxResult<DepartmentDTO>
     */
    public ResponseDTO<DepartmentDTO> getDepartmentById(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.DEPT_NOT_EXISTS);
        }
        DepartmentDTO departmentDTO = SmartBeanUtil.copy(departmentEntity, DepartmentDTO.class);
        return ResponseDTO.succData(departmentDTO);
    }

    /**
     * 获取所有部门
     *
     * @return
     */
    public ResponseDTO<List<DepartmentDTO>> listAll() {
        List<DepartmentDTO> departmentDTOList = departmentDao.listAll();
        return ResponseDTO.succData(departmentDTOList);
    }

    /**
     * 上下移动
     *
     * @param departmentId
     * @param swapId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> upOrDown(Integer departmentId, Integer swapId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        Integer departmentSort = departmentEntity.getSort();
        departmentEntity.setSort(swapEntity.getSort());
        departmentDao.updateById(departmentEntity);
        swapEntity.setSort(departmentSort);
        departmentDao.updateById(swapEntity);
        return ResponseDTO.succ();
    }

    /**
     * 部门升级
     *
     * @param departmentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> upgrade(Integer departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        if (departmentEntity.getParentId() == null || departmentEntity.getParentId().equals(0)) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.ERROR_PARAM, "此部门已经是根节点无法移动");
        }
        DepartmentEntity parentEntity = departmentDao.selectById(departmentEntity.getParentId());

        departmentEntity.setParentId(parentEntity.getParentId());
        departmentDao.updateById(departmentEntity);
        return ResponseDTO.succ();
    }

    /**
     * 部门降级
     *
     * @param departmentId
     * @param preId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> downgrade(Integer departmentId, Integer preId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity preEntity = departmentDao.selectById(preId);
        if (preEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        departmentEntity.setParentId(preEntity.getId());
        departmentDao.updateById(departmentEntity);
        return ResponseDTO.succ();
    }

}
