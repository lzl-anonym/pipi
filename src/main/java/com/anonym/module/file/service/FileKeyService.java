package com.anonym.module.file.service;

import com.anonym.common.anno.FileKey;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.file.constant.FileServiceTypeEnum;
import com.anonym.module.file.domain.domain.FileSimpleVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 云服务商 的oss文件工具类
 */
@Slf4j
@Service
public class FileKeyService {

    @Autowired
    private FileServiceLocal fileServiceLocal;
    @Autowired
    private FileService fileService;

    /**
     * 设置 oss file url地址
     *
     * @param t
     * @param <T>
     */
    public <T> void setFileUrl(T t) {
        if (null == t) {
            return;
        }
        Class aClass = t.getClass();
        this.getFileUrl(Arrays.asList(t), aClass);
    }

    /**
     * 设置阿里云oss file url地址
     *
     * @param list
     * @param tClass
     * @param <T>
     */
    public <T> void setListFileUrl(List<T> list, Class<T> tClass) {
        this.getFileUrl(list, tClass);
    }

    /**
     * 获取ali file 的方法
     *
     * @param list
     * @param tClass
     * @param <T>
     */
    private <T> void getFileUrl(List<T> list, Class<T> tClass) {
        if (null == list || list.isEmpty()) {
            return;
        }

        // 这一段递归代码 是为了 从父类中获取属性
        Class tempClass = tClass;
        List<Field> fieldList = new ArrayList<>();
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        // 过虑出 有 OSSFileKey 注解的属性
        fieldList = fieldList.stream().filter(field -> field.isAnnotationPresent(FileKey.class)).collect(Collectors.toList());

        if (fieldList.isEmpty()) {
            return;
        }

        fieldList.stream().forEach(field -> {
            field.setAccessible(true);
            list.stream().forEach(t -> {
                try {
                    Object objKey = field.get(t);
                    if (null == objKey) {
                        return;
                    }
                    String fileKey = (String) objKey;
                    // 如果是逗号分隔的图片，则循环
                    List<String> stringList = Arrays.asList(fileKey.split(CommonConst.SEPARATOR));
                    stringList = stringList.stream().map(e -> {
                        ResponseDTO<String> responseDTO = fileServiceLocal.getFileUrl(e);
                        return responseDTO.getData();
                    }).collect(Collectors.toList());
                    field.set(t, Strings.join(stringList, CommonConst.SEPARATOR_CHAR));
                } catch (IllegalAccessException e) {
                    log.error("OSS File URL 设置文件失败", e);
                }
            });

        });

    }

    /**
     * 得到文件简单voList key-url
     *
     * @param fileKeyList 文件key 逗号分隔
     * @return
     */
    public List<FileSimpleVO> getFileSimpleList(String fileKeyList) {
        if (StringUtils.isBlank(fileKeyList)) {
            return Lists.newArrayList();
        }
        String[] split = fileKeyList.split(CommonConst.SEPARATOR);
        List<String> splitList = Lists.newArrayList(split);
        List<FileSimpleVO> fileSimpleVOList = splitList.stream().map(e -> {
            FileSimpleVO fileSimpleVO = new FileSimpleVO();
            fileSimpleVO.setFileKey(e);
            ResponseDTO<String> fileUrl = fileService.getFileUrl(e, FileServiceTypeEnum.LOCAL);
            if (StringUtils.isNotBlank(fileUrl.getData())) {
                fileSimpleVO.setFileUrl(fileUrl.getData());
            }
            return fileSimpleVO;
        }).collect(Collectors.toList());
        return fileSimpleVOList;
    }

    /**
     * 得到文件简单vo key-url
     *
     * @param fileKey
     * @return
     */
    public FileSimpleVO getFileSimple(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return null;
        }
        FileSimpleVO fileSimpleVO = new FileSimpleVO();
        fileSimpleVO.setFileKey(fileKey);
        ResponseDTO<String> fileUrl = fileService.getFileUrl(fileKey, FileServiceTypeEnum.LOCAL);
        if (StringUtils.isNotBlank(fileUrl.getData())) {
            fileSimpleVO.setFileUrl(fileUrl.getData());
        }
        return fileSimpleVO;
    }
}
