package com.anonym.module.file;

import com.anonym.module.file.domain.domain.FileDTO;
import com.anonym.module.file.domain.domain.FileEntity;
import com.anonym.module.file.domain.domain.FileQueryDTO;
import com.anonym.module.file.domain.domain.FileVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface FileDao extends BaseMapper<FileEntity> {

    /**
     * 批量添加上传文件
     *
     * @param fileDTOList
     * @return
     */
    Integer insertFileBatch(List<FileDTO> fileDTOList);

    /**
     * 批量添加上传文件
     *
     * @param fileDTOList
     * @return
     */
    Integer insertFileEntityBatch(List<FileEntity> fileDTOList);

    /**
     * 批量删除
     *
     * @param moduleId
     * @return
     */
    Integer deleteFilesByModuleId(@Param("moduleId") String moduleId);

    /**
     * 批量删除
     *
     * @param moduleId
     * @param moduleType
     * @return
     */
    Integer deleteFilesByModuleIdAndModuleType(@Param("moduleId") String moduleId, @Param("moduleType") String moduleType);

    /**
     * @param moduleId
     * @return
     */
    List<FileVO> listFilesByModuleId(@Param("moduleId") String moduleId);

    List<FileVO> listFilesByFileIds(@Param("fileIds") List<Long> fileIds);

    List<FileVO> listFilesByModuleIdAndModuleType(@Param("moduleId") String moduleId, @Param("moduleType") String moduleType);

    List<FileVO> listFilesByModuleIdAndModuleTypes(@Param("moduleId") String moduleId, @Param("moduleTypes") List<String> moduleTypes);

    List<FileVO> listFilesByModuleIdsAndModuleType(@Param("moduleIds") List<String> moduleIds, @Param("moduleType") String moduleType);

    List<FileVO> queryListByPage(Page page, @Param("queryDTO") FileQueryDTO queryDTO);
}
