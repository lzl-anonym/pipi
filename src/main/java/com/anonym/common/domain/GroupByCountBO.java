package com.anonym.common.domain;

import lombok.Data;

/**
 * 分组查询条数BO
 */
@Data
public class GroupByCountBO {

    /**
     * 分组ID
     */
    private Long groupId;
    /**
     * 分组条数
     */
    private Long count;
}
