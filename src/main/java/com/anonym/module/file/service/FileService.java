package com.anonym.module.file.service;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.file.FileDao;
import com.anonym.module.file.constant.FileFolderTypeEnum;
import com.anonym.module.file.constant.FileResponseCodeConst;
import com.anonym.module.file.constant.FileServiceTypeEnum;
import com.anonym.module.file.domain.domain.*;
import com.anonym.utils.SmartBaseEnumUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Autowired
    private java.util.Map<String, IFileService> fileServiceMap;

    /**
     * 获取文件服务实现
     *
     * @param typeEnum
     * @return
     */
    private IFileService getFileService(FileServiceTypeEnum typeEnum) {
        /**
         * 获取文件服务
         */
        String serviceName = typeEnum.getServiceName();
        IFileService fileService = fileServiceMap.get(serviceName);
        if (null == fileService) {
            throw new RuntimeException("未找到文件服务实现类：" + serviceName);
        }
        return fileService;
    }

    /**
     * 文件上传服务
     *
     * @param file
     * @param typeEnum   文件服务类型枚举类
     * @param moduleType 文件夹类型
     * @return
     */
    public ResponseDTO<UploadVO> fileUpload(MultipartFile file, FileServiceTypeEnum typeEnum, Integer moduleType, Long userId) {
        FileFolderTypeEnum folderTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, FileFolderTypeEnum.class);
        if (null == folderTypeEnum) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_MODULE_ERROR);
        }

        if (null == file || file.getSize() == 0) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_EMPTY);
        }

        // 校验文件大小

        String maxSizeStr = maxFileSize.toLowerCase().replace("mb", "");
        Long maxSize = Integer.valueOf(maxSizeStr) * 1024 * 1024L;

        if (file.getSize() > maxSize) {
            return ResponseDTO.wrapMsg(FileResponseCodeConst.FILE_SIZE_ERROR, String.format(FileResponseCodeConst.FILE_SIZE_ERROR.getMsg(), maxSize));
        }

        // 获取文件服务
        IFileService fileService = this.getFileService(typeEnum);
        ResponseDTO<UploadVO> response = fileService.fileUpload(file, folderTypeEnum.getFolder());

        if (response.isSuccess()) {
            // 上传成功 保存记录数据库
            FileEntity fileEntity = new FileEntity();
            fileEntity.setServiceType(typeEnum.getServiceType());
            fileEntity.setFolderType(folderTypeEnum.getFolder());
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileSize(file.getSize() / 1024);
            fileEntity.setFileKey(response.getData().getFileKey());
            fileEntity.setCreateUser(userId);
            fileDao.insert(fileEntity);
        }

        return response;
    }

    /**
     * 根据文件绝对路径 获取文件URL
     *
     * @param fileKey
     * @return
     */
    public ResponseDTO<String> getFileUrl(String fileKey, FileServiceTypeEnum typeEnum) {
        IFileService fileService = this.getFileService(typeEnum);
        return fileService.getFileUrl(fileKey);
    }

    /**
     * 批量插入
     *
     * @param fileDTOList
     */
    public void insertFileBatch(List<FileDTO> fileDTOList) {
        fileDao.insertFileBatch(fileDTOList);
    }

    /**
     * 根据module 删除文件信息
     *
     * @param moduleId
     * @return
     */
    public void deleteFilesByModuleId(String moduleId) {
        fileDao.deleteFilesByModuleId(moduleId);
    }

    /**
     * 根据module 获取文件信息
     *
     * @param moduleId
     * @return
     */
    public List<FileVO> listFilesByModuleId(String moduleId) {
        return fileDao.listFilesByModuleId(moduleId);
    }

    /**
     * @param filesStr 逗号分隔文件id字符串
     * @return
     */
    public List<FileVO> getFileDTOList(String filesStr) {
        if (StringUtils.isEmpty(filesStr)) {
            return Lists.newArrayList();
        }
        String[] fileIds = filesStr.split(",");
        List<Long> fileIdList = Arrays.asList(fileIds).stream().map(e -> Long.valueOf(e)).collect(Collectors.toList());
        List<FileVO> files = fileDao.listFilesByFileIds(fileIdList);
        return files;
    }

    /**
     * 分页查询文件列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<FileVO>> queryListByPage(FileQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<FileVO> fileList = fileDao.queryListByPage(page, queryDTO);
        if (CollectionUtils.isNotEmpty(fileList)) {
            fileList.forEach(e -> {
                // 根据文件服务类 获取对应文件服务 查询 url
                FileServiceTypeEnum serviceTypeEnum = SmartBaseEnumUtil.getEnumByValue(e.getFileLocationType(), FileServiceTypeEnum.class);
                IFileService fileService = this.getFileService(serviceTypeEnum);
                e.setFileUrl(fileService.getFileUrl(e.getFilePath()).getData());
            });
        }
        PageResultDTO<FileVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, fileList);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 根据文件服务类型 和 FileKey 下载文件
     *
     * @param serviceTypeEnum
     * @param fileKey
     * @return
     * @throws IOException
     */
    public ResponseEntity<Object> downloadByFileKey(FileServiceTypeEnum serviceTypeEnum, String fileKey, String userAgent) throws IOException {
        // 根据文件服务类 获取对应文件服务 查询 url
        IFileService fileService = this.getFileService(serviceTypeEnum);
        ResponseDTO<byte[]> responseDTO = fileService.fileDownload(fileKey);
        if (!responseDTO.isSuccess()) {
            HttpHeaders heads = new HttpHeaders();
            heads.add(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
            return new ResponseEntity<>(responseDTO.getMsg() + "：" + fileKey, heads, HttpStatus.OK);
        }
        // 设置下载头
        HttpHeaders heads = new HttpHeaders();
        heads.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream; charset=utf-8");
        // 设置对应浏览器的文件名称编码
        String fileName = fileKey.substring(fileKey.lastIndexOf("/"));
        fileName = fileService.getDownloadFileNameByUA(fileName, userAgent);
        heads.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseDTO.getData(), heads, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 根据id 下载文件
     *
     * @param id
     * @return
     */
    public ResponseEntity<Object> downLoadById(Long id, String userAgent) throws IOException {
        FileEntity entity = fileDao.selectById(id);
        if (null == entity) {
            HttpHeaders heads = new HttpHeaders();
            heads.add(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
            return new ResponseEntity<>("文件不存在", heads, HttpStatus.OK);
        }

        // 根据文件服务类 获取对应文件服务 查询 url
        FileServiceTypeEnum serviceTypeEnum = SmartBaseEnumUtil.getEnumByValue(entity.getServiceType(), FileServiceTypeEnum.class);
        ResponseEntity<Object> responseEntity = this.downloadByFileKey(serviceTypeEnum, entity.getFileKey(), userAgent);
        return responseEntity;
    }

}
