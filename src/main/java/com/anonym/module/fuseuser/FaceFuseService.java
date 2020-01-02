package com.anonym.module.fuseuser;

import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.baiduutil.GsonUtils;
import com.anonym.module.baiduutil.HttpUtil;
import com.anonym.module.file.constant.FileServiceTypeEnum;
import com.anonym.module.file.domain.domain.UploadVO;
import com.anonym.module.file.service.FileService;
import com.anonym.module.fuseuser.domain.FaceFuseQueryDTO;
import com.anonym.module.fuseuser.domain.FaceFuseUserAddDTO;
import com.anonym.module.fuseuser.domain.FaceFuseUserEntity;
import com.anonym.module.fuseuser.fuse.domian.*;
import com.anonym.module.fuseuser.record.FaceFuseRecordDao;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordEntity;
import com.anonym.module.user.basic.domain.UserDTO;
import com.anonym.module.user.basic.domain.UserLoginVO;
import com.anonym.module.user.login.UserLoginService;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.anonym.utils.SmartRandomUtil;
import com.baomidou.mybatisplus.plugins.Page;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author lizongliang
 * @date 2019-11-16 14:52
 */
@Service
public class FaceFuseService {

    @Autowired
    private FaceFuseUserDao faceFuseUserDao;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private FaceFuseDao faceFuseDao;

    private static String CLAIM_ID_KEY = "id";

    @Autowired
    private FaceFuseRecordDao faceFuseRecordDao;

    @Value("${jwt.key}")
    private String jwtKey;

    @Value("${file-upload-service.path}")
    private String fileParentPath;

    /**
     * base64 头
     */
    private final static String IMG_HEAD = "data:image/jpg;base64,";


    /**
     * 过期时间
     */
    public static final int EXPIRE_SECONDS = 300 * 24 * 3600;


    /**
     * API_KEY
     */
    public static final String API_KEY = "VIVVkX7Z8BGcii8WOCCHySrl";

    /**
     * SECRET_KEY
     */
    public static final String SECRET_KEY = "AH0BECQlL2hPdbeWYS1cez2WBmLZVpGs";

    /**
     * 融合图片 api
     */
    public static final String MERGE_FACE_API = "https://aip.baidubce.com/rest/2.0/face/v1/merge";

    @Autowired
    private FileService fileService;


    /**
     * 添加用户
     *
     * @return
     */
    public ResponseDTO addFaceFuseUser(FaceFuseUserAddDTO addDTO) {
        FaceFuseUserEntity user = faceFuseUserDao.selectByPhone(addDTO.getPhone());

        if (null != user) {
            return ResponseDTO.wrap(FaceFuseResponseCodeConst.PHONE_EXIST);
        }

        FaceFuseUserEntity addEntity = SmartBeanUtil.copy(addDTO, FaceFuseUserEntity.class);
        String password = SmartRandomUtil.generateRandomString(20);
        addEntity.setPassword(password);
        faceFuseUserDao.insert(addEntity);
        return ResponseDTO.succ();
    }


    /**
     * 登录
     *
     * @return
     */
    public ResponseDTO<UserLoginVO> faceFuseLogin(String phone) {

        FaceFuseUserEntity user = faceFuseUserDao.selectByPhone(phone);
        // 用户不存在
        if (null == user) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }

        // 生成token 保存登录信息至 redis
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getFaceFuseUserId().longValue());
        userDTO.setPhone(user.getPhone());
        String token = this.generateToken(userDTO);
        userDTO.setToken(token);
        userDTO.setDisabledFlag(false);

        // 返回前端
        UserLoginVO userVO = SmartBeanUtil.copy(userDTO, UserLoginVO.class);
        // 保存至 redis
        userLoginService.saveToken(userDTO);

        return ResponseDTO.succData(userVO);

    }

    private String generateToken(UserDTO userDTO) {
        Long id = userDTO.getUserId();
        /**将token设置为jwt格式*/
        String baseToken = UUID.randomUUID().toString();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(EXPIRE_SECONDS);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());

        Claims jwtClaims = Jwts.claims().setSubject(baseToken);
        jwtClaims.put(CLAIM_ID_KEY, id);
        String compactJws = Jwts.builder().setClaims(jwtClaims).setNotBefore(from).setExpiration(expire).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
        return compactJws;
    }

    /**
     * 保存合成后的信息
     *
     * @param addDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO addFaceFuse(FaceFuseAddDTO addDTO, Long userId) {
        // 1.调用百度接口

        ResponseDTO responseDTO = this.faceMerge(addDTO);
        if (!responseDTO.isSuccess()) {
            return responseDTO;
        }
        // 2.保存合成后的信息
        String data = (String) responseDTO.getData();
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
        // 合成失败  返回百度接口提示信息
        if (!Objects.equals(jsonObject.getString("error_code"), "0")) {
            return ResponseDTO.wrapMsg(FaceFuseResponseCodeConst.FUSE_ERROR, jsonObject.getString("error_msg"));
        }
        String mergeImage = jsonObject.getString("result");
        com.alibaba.fastjson.JSONObject tar = com.alibaba.fastjson.JSONObject.parseObject(mergeImage);
        String str = tar.getString("merge_image");
        FaceFuseEntity entity = new FaceFuseEntity();
        entity.setFaceFuseUserId(userId.intValue());

        // 起线程  保存图片
        MultipartFile multipartFile = this.base64ToMultipart(IMG_HEAD + str);
        ResponseDTO<UploadVO> uploadVOResponseDTO = fileService.fileUpload(multipartFile, FileServiceTypeEnum.LOCAL, 1, 610L);

        if (!uploadVOResponseDTO.isSuccess()) {
            return uploadVOResponseDTO;
        }

        entity.setMergeImage(uploadVOResponseDTO.getData().getFileUrl());

        long startTime1 = System.currentTimeMillis();
        faceFuseDao.insert(entity);
        long endTime1 = System.currentTimeMillis();
        System.out.println("===============保存合成后数据时间===========： " + (endTime1 - startTime1) + "ms");

        // 3.插入参与记录
        FaceFuseRecordEntity faceFuseRecordEntity = new FaceFuseRecordEntity();
        faceFuseRecordEntity.setFaceFuseUserId(userId.intValue());

        long startTime2 = System.currentTimeMillis();
        faceFuseRecordDao.insert(faceFuseRecordEntity);
        long endTime2 = System.currentTimeMillis();
        System.out.println("===============保存参与记录时间===========： " + (endTime2 - startTime2) + "ms");

        return ResponseDTO.succData(IMG_HEAD + str);
    }


    /**
     * 获取token
     *
     * @return
     */
    public ResponseDTO getAccessToken() {
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + API_KEY
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + SECRET_KEY;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return ResponseDTO.succData(access_token);
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);

        }
        return ResponseDTO.wrap(FaceFuseResponseCodeConst.TOKEN_ERROR);
    }


    /**
     * 人脸融合
     *
     * @return
     */
    public ResponseDTO faceMerge(FaceFuseAddDTO addDTO) {
        // 请求url
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> image_templateMap = new HashMap<>();
            image_templateMap.put("image", addDTO.getTemplateBase64());
            image_templateMap.put("image_type", "BASE64");
            image_templateMap.put("quality_control", "NONE");
            map.put("image_template", image_templateMap);
            Map<String, Object> image_targetMap = new HashMap<>();
            image_targetMap.put("image", addDTO.getImageBase64());
            image_targetMap.put("image_type", "BASE64");
            image_targetMap.put("quality_control", "NONE");
            map.put("image_target", image_targetMap);

            String param = GsonUtils.toJson(map);


            // access_token有过期时间  这里每次都重新请求
            long startTime1 = System.currentTimeMillis();
            ResponseDTO tokenRes = this.getAccessToken();
            if (!tokenRes.isSuccess()) {
                // 获取token失败
                return tokenRes;
            }
            long endTime1 = System.currentTimeMillis();
            System.out.println("==============获取百度token时间==============： " + (endTime1 - startTime1) + "ms");

            String accessToken = tokenRes.getData().toString();

            long startTime2 = System.currentTimeMillis();
            String result = HttpUtil.post(MERGE_FACE_API, accessToken, "application/json", param);
            long endTime2 = System.currentTimeMillis();
            System.out.println("===================百度融合接口时间=============： " + (endTime2 - startTime2) + "ms");
            return ResponseDTO.succData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDTO.wrap(FaceFuseResponseCodeConst.FUSE_ERROR);
    }


    /**
     * 后管分页查询 融合活动结果
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<FaceFuseVO>> queryByPage(FaceFuseQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<FaceFuseVO> recordVOS = faceFuseDao.queryByPage(page, queryDTO);
        PageResultDTO<FaceFuseVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, recordVOS);
        return ResponseDTO.succData(pageResultDTO);
    }


    /**
     * 批量删除融合结果（后管）
     *
     * @param deleteDTO
     * @return
     */
    public ResponseDTO<String> batchDelete(FaceFuseDeleteDTO deleteDTO) {
        if (CollectionUtils.isEmpty(deleteDTO.getFaceFuseIdList())) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        faceFuseDao.batchUpdateDelete(deleteDTO.getFaceFuseIdList());
        return ResponseDTO.succ();
    }

    /**
     * @param BASE64str bas64字符串
     * @param path      存储地址
     * @param ext       图片后缀
     * @return 存储地址
     */
    public String BASE64CodeToBeImage(String BASE64str, String path, String ext) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //文件名称
        String uploadFileName = UUID.randomUUID().toString() + "." + ext;
        File targetFile = new File(path, uploadFileName);
        BASE64Decoder decoder = new BASE64Decoder();
        try (OutputStream out = new FileOutputStream(targetFile)) {
            byte[] b = decoder.decodeBuffer(BASE64str);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return path + "/" + uploadFileName + "." + ext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
