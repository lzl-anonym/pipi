package com.anonym.utils;

import com.anonym.common.domain.PageBaseDTO;
import com.anonym.common.domain.PageInfoDTO;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 分页工具类
 */

public class PaginationUtil {

    public static <T> PageInfoDTO<T> convert2PageInfoDTO(Page<T> page) {
        PageInfoDTO<T> result = new PageInfoDTO<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        return result;
    }

    public static Page<T> convert2PageQueryInfo(PageBaseDTO baseDTO) {
        Page<T> page = new Page<>();
        Boolean sort = baseDTO.getSort();
        if (null != sort) {
            page.setAsc(sort);
        }
        page.setCurrent(baseDTO.getPageNum());
        page.setSize(baseDTO.getPageSize());
        if (null != baseDTO.getSearchCount()) {
            page.setSearchCount(baseDTO.getSearchCount());
        }
        return page;
    }

    /**
     * 转换为 PageInfoDTO 对象
     *
     * @param page
     * @param sourceList  原list
     * @param targetClazz 目标类
     * @return
     */
    public static <T, E> PageInfoDTO<T> convertPageInfoDTO(Page page, List<E> sourceList, Class<T> targetClazz) {
        PageInfoDTO pageInfoDTO = setPage(page);
        List<T> records = SmartBeanUtil.copyList(sourceList, targetClazz);
        page.setRecords(records);
        pageInfoDTO.setList(records);
        return pageInfoDTO;
    }

    /**
     * 转换为 PageInfoDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     */
    public static <T> PageInfoDTO<T> convert2PageInfoDTO(Page page, List<T> sourceList) {
        PageInfoDTO pageInfoDTO = setPage(page);
        page.setRecords(sourceList);
        pageInfoDTO.setList(sourceList);
        return pageInfoDTO;
    }

    private static PageInfoDTO setPage(Page page) {
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setPageNum(page.getCurrent());
        pageInfoDTO.setPageSize(page.getSize());
        pageInfoDTO.setTotal(page.getTotal());
        pageInfoDTO.setPages(page.getPages());
        return pageInfoDTO;
    }
}
