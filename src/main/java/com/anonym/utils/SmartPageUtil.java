package com.anonym.utils;

import com.anonym.common.domain.PageBaseDTO;
import com.anonym.common.domain.PageResultDTO;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 分页工具类
 */

public class SmartPageUtil {

    public static Page convert2PageQuery(PageBaseDTO baseDTO) {
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

    public static <T> PageResultDTO<T> convert2PageResult(Page<T> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(Long.valueOf(page.getTotal()));
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        return result;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList  原list
     * @param targetClazz 目标类
     * @return
     * @author yandanyang
     * @date 2018年5月16日 下午6:05:28
     */
    public static <T, E> PageResultDTO<T> convert2PageResult(Page page, List<E> sourceList, Class<T> targetClazz) {
        PageResultDTO pageResultDTO = setPage(page);
        List<T> records = SmartBeanUtil.copyList(sourceList, targetClazz);
        page.setRecords(records);
        pageResultDTO.setList(records);
        return pageResultDTO;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     * @author yandanyang
     * @date 2018年5月16日 下午6:05:28
     */
    public static <E> PageResultDTO<E> convert2PageResult(Page page, List<E> sourceList) {
        PageResultDTO pageResultDTO = setPage(page);
        page.setRecords(sourceList);
        pageResultDTO.setList(sourceList);
        return pageResultDTO;
    }

    /**
     * 转换分页结果对象
     *
     * @param pageResultDTO
     * @param targetClazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> PageResultDTO<T> convert2PageResult(PageResultDTO<E> pageResultDTO, Class<T> targetClazz) {

        PageResultDTO newPageResultDTO = new PageResultDTO();
        newPageResultDTO.setPageNum(pageResultDTO.getPageNum());
        newPageResultDTO.setPageSize(pageResultDTO.getPageSize());
        newPageResultDTO.setTotal(pageResultDTO.getTotal());
        newPageResultDTO.setPages(pageResultDTO.getPages());

        List<E> list = pageResultDTO.getList();
        List<T> copyList = SmartBeanUtil.copyList(list, targetClazz);
        newPageResultDTO.setList(copyList);

        return newPageResultDTO;
    }

    private static PageResultDTO setPage(Page page) {
        PageResultDTO pageResultDTO = new PageResultDTO();
        pageResultDTO.setPageNum(page.getCurrent());
        pageResultDTO.setPageSize(page.getSize());
        pageResultDTO.setTotal(Long.valueOf(page.getTotal()));
        pageResultDTO.setPages(page.getPages());
        return pageResultDTO;
    }
}
