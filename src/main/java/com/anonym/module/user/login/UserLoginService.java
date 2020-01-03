package com.anonym.module.user.login;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.RedisKeyConst;
import com.anonym.module.user.basic.UserDao;
import com.anonym.module.user.basic.UserService;
import com.anonym.module.user.basic.domain.UserDTO;
import com.anonym.third.SmartRedisService;
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
    private SmartRedisService redisService;










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
