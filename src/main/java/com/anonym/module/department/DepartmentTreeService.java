package com.anonym.module.department;

import com.anonym.module.department.domain.DepartmentDTO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DepartmentTreeService {

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 构建部门树结构
     *
     * @param departmentDTOList
     * @return
     */
    public List<DepartmentDTO> buildTree(List<DepartmentDTO> departmentDTOList) {
        if (CollectionUtils.isEmpty(departmentDTOList)) {
            return Lists.newArrayList();
        }
        List<DepartmentDTO> list = departmentDTOList.stream().filter(e -> e.getParentId() == null || e.getParentId() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        this.buildTree(list, departmentDTOList);
        return list;
    }

    private void buildTree(List<DepartmentDTO> nodeList, List<DepartmentDTO> departmentDTOList) {
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            int preIndex = i - 1;
            int nextIndex = i + 1;
            DepartmentDTO node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getId());
            }
            buildTree(node, departmentDTOList);
        }
    }

    private void buildTree(DepartmentDTO node, List<DepartmentDTO> departmentDTOList) {
        List<DepartmentDTO> children = getChildren(node, departmentDTOList);
        if (CollectionUtils.isNotEmpty(children)) {
            node.setChildrenDepartment(children);
            this.buildTree(children, departmentDTOList);
        }
    }

    private List<DepartmentDTO> getChildren(DepartmentDTO node, List<DepartmentDTO> departmentDTOList) {
        Integer id = node.getId();
        return departmentDTOList.stream().filter(e -> id.equals(e.getParentId())).collect(Collectors.toList());
    }

    /**
     * 通过部门id,获取当前以及下属部门
     */
    public void buildIdList(Integer deptId, List<Integer> result) {
        List<DepartmentDTO> departmentDTOList = departmentDao.listAll();
        result.add(deptId);
        if (null == deptId) {
            result.addAll(departmentDTOList.stream().map(DepartmentDTO::getId).collect(Collectors.toList()));
            return;
        }
        List<DepartmentDTO> children = getChildrenIdList(deptId, departmentDTOList);
        if (!children.isEmpty()) {
            result.addAll(children.stream().map(DepartmentDTO::getId).collect(Collectors.toList()));
            for (DepartmentDTO child : children) {
                buildTree(child, departmentDTOList);
            }
        }
    }

    private List<DepartmentDTO> getChildrenIdList(Integer deptId, List<DepartmentDTO> departmentDTOList) {
        return departmentDTOList.stream().filter(e -> deptId.equals(e.getParentId())).collect(Collectors.toList());
    }

}
