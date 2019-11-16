package com.anonym.module.user.login;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.RedisKeyConst;
import com.anonym.module.user.UserResponseCodeConst;
import com.anonym.module.user.basic.UserDao;
import com.anonym.module.user.basic.UserService;
import com.anonym.module.user.basic.domain.UserDTO;
import com.anonym.module.user.basic.domain.UserEntity;
import com.anonym.module.user.basic.domain.UserLoginVO;
import com.anonym.module.user.login.domain.UserLoginDTO;
import com.anonym.module.wechat.miniprogram.WeChatMiniService;
import com.anonym.module.wechat.miniprogram.domain.WeChatLoginResultDTO;
import com.anonym.third.SmartRedisService;
import com.anonym.utils.SmartBeanUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 用户业务类
 */
@Service
@Slf4j
public class UserLoginService {

    /**
     * 过期时间
     */
    public static final int EXPIRE_SECONDS = 300 * 24 * 3600;

    @Value("${jwt.key}")
    private String jwtKey;

    private static String CLAIM_ID_KEY = "id";

    @Autowired
    private WeChatMiniService weChatMiniService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SmartRedisService redisService;

    @Autowired
    private UserService userService;


    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    public ResponseDTO login(UserLoginDTO loginDTO) {

        // 根据微信code 获取用户open id
        ResponseDTO response = weChatMiniService.loginByCode(loginDTO.getCode());
        if (!response.isSuccess()) {
            return response;
        }
        WeChatLoginResultDTO loginResultDTO = (WeChatLoginResultDTO) response.getData();

        // 处理用户数据 是否存在
        UserEntity userEntity = this.handleUsrInfo(loginResultDTO, loginDTO);

        // 校验用户是否禁用
        if (userEntity.getDisabledFlag()) {
            return ResponseDTO.wrap(UserResponseCodeConst.ACCOUNT_DISABLED);
        }

        // 处理登录 返回前端
        UserLoginVO userVO = this.handleLogin(userEntity, loginDTO.getLastLoginIp());

        return ResponseDTO.succData(userVO);
    }

    /**
     * 根据open id 获取用户数据
     * 不存在则新增， 存在则更新用户的微信资料
     *
     * @param loginResult
     * @param loginDTO
     * @return
     */
    private UserEntity handleUsrInfo(WeChatLoginResultDTO loginResult, UserLoginDTO loginDTO) {
        String openId = loginResult.getOpenId();
        UserEntity userEntity = userService.getUserByOpenId(openId);
        if (null == userEntity) {
            // 用户不存在 则 新增用户
            userEntity = new UserEntity();
            userEntity.setWeChatOpenId(openId);
            userEntity.setAvatar(loginDTO.getAvatar());
            userEntity.setNickname(loginDTO.getNickname());
            userEntity.setGender(loginDTO.getGender());
            userEntity.setDisabledFlag(false);
            userEntity.setWeChatSessionKey(loginResult.getSessionKey());
            userDao.insert(userEntity);
            return userEntity;
        }

        // 存在 则更新用户信息
        userEntity.setAvatar(loginDTO.getAvatar());
        userEntity.setNickname(loginDTO.getNickname());
        userEntity.setGender(loginDTO.getGender());
        userEntity.setWeChatSessionKey(loginResult.getSessionKey());
        userDao.updateUser(userEntity);
        return userEntity;
    }

    /**
     * 登录逻辑处理
     *
     * @param userEntity
     * @param ip
     * @return
     */
    private UserLoginVO handleLogin(UserEntity userEntity, String ip) {
        // 更新上次登录时间、登录ip
        Long userId = userEntity.getUserId();
        userDao.updateLastLogin(userId, ip);

        // 生成token 保存登录信息至 redis
        UserDTO userDTO = SmartBeanUtil.copy(userEntity, UserDTO.class);
        String token = this.generateToken(userDTO);
        userDTO.setToken(token);

        // 返回前端
        UserLoginVO userVO = SmartBeanUtil.copy(userDTO, UserLoginVO.class);

        // 保存至 redis
        this.saveToken(userDTO);

        return userVO;
    }

    /**
     * 退出登录
     *
     * @param userId
     * @return
     */
    public ResponseDTO<String> logout(Long userId) {
        this.removeTokenByUserId(userId);
        return ResponseDTO.succ();
    }

    /**
     * 功能描述: 生成JWT TOKEN
     *
     * @param userDTO
     * @return
     * @date 2018/9/12 0012 上午 10:08
     */
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
     * 移除缓存
     *
     * @param userId
     */
    public void removeTokenByUserId(Long userId) {
        redisService.del(RedisKeyConst.TOKEN_USER + userId);
    }

    /**
     * 根据 token 获取用户登录信息
     *
     * @param token
     * @return
     */
    public UserDTO getUserTokenInfo(String token) {
        String userId;
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
            userId = claims.get(CLAIM_ID_KEY).toString();
        } catch (Exception e) {
            return null;
        }

        UserDTO user = redisService.getObject(RedisKeyConst.TOKEN_USER + userId, UserDTO.class);
        if (user == null) {
            return null;
        }

        if (!Objects.equals(user.getToken(), token)) {
            return null;
        }

        if (user.getDisabledFlag()) {
            return null;
        }

        return user;
    }

    /**
     * 将用户信息保存至 redis
     *
     * @param userDTO
     */
    public void saveToken(UserDTO userDTO) {
        redisService.set(RedisKeyConst.TOKEN_USER + userDTO.getUserId(), userDTO, EXPIRE_SECONDS);
    }

    public static void main(String[] args) {
        System.out.println(UserService.encryptPassword("123456"));
    }
}
