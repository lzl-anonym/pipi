package com.anonym.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SmartCollectionUtil extends CollectionUtils {

    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 分批处理数据
     *
     * @param list  集合
     * @param func  执行方法
     * @param limit 每次执行数量
     * @author listen
     */
    public static <T> void batchExecute(List<T> list, Function<List<T>, Integer> func, int limit) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        limit = 0 >= limit ? 50 : limit;
        List<T> tempList = new ArrayList<>(limit);
        for (T e : list) {
            tempList.add(e);
            if (tempList.size() >= limit) {
                func.apply(tempList);
                tempList.clear();
            }
        }
        if (CollectionUtils.isNotEmpty(tempList)) {
            func.apply(tempList);
        }
    }

    public static <K, V> boolean isNotEmptyMap(Map<K, V> map) {
        return !isEmptyMap(map);
    }
}
