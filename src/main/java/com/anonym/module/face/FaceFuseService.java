package com.anonym.module.face;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.baiduutil.GsonUtils;
import com.anonym.module.baiduutil.HttpUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizongliang
 * @date 2019-11-15 16:52
 */
@Service
public class FaceFuseService {

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

    /**
     * 获取Access Token
     */
    public static final String GET_TOKEN = "https://aip.baidubce.com/oauth/2.0/token";

    @Autowired
    private RestTemplate restTemplate;


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
        return null;
    }

    /**
     * 人脸融合
     *
     * @return
     */
    public ResponseDTO faceMerge() {
        // 请求url
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> image_templateMap = new HashMap<>();
            image_templateMap.put("image", "sfasq35sadvsvqwr5q...");
            image_templateMap.put("image_type", "BASE64");
            image_templateMap.put("quality_control", "NONE");
            map.put("image_template", image_templateMap);
            Map<String, Object> image_targetMap = new HashMap<>();
            image_targetMap.put("image", "sfasq35sadvsvqwr5q...");
            image_targetMap.put("image_type", "BASE64");
            image_targetMap.put("quality_control", "NONE");
            map.put("image_target", image_targetMap);

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            String accessToken = "[调用鉴权接口获取的token]";
            String accessToken = this.getAccessToken().getData().toString();

            String result = HttpUtil.post(MERGE_FACE_API, accessToken, "application/json", param);
            System.out.println(result);
            return ResponseDTO.succData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}



