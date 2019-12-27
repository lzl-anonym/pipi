package com.anonym.module.zteuser;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.RedisKeyConst;
import com.anonym.module.zteuser.domain.ZteLoginDTO;
import com.anonym.module.zteuser.domain.ZteUserDTO;
import com.anonym.module.zteuser.domain.ZteUserEntity;
import com.anonym.module.zteuser.domain.ZteUserLoginVO;
import com.anonym.third.SmartRedisService;
import com.anonym.utils.SmartBeanUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author lizongliang
 * @date 2019-12-27 10:47
 */
@Service
public class ZteUserService {

    /**
     * 过期时间
     */
    public static final int EXPIRE_SECONDS = 300 * 24 * 3600;

    private static String CLAIM_ID_KEY = "id";

    @Value("${jwt.key}")
    private String jwtKey;


    @Autowired
    private SmartRedisService redisService;

    @Autowired
    private ZteUserDao zteUserDao;


    public ResponseDTO login(ZteLoginDTO loginDTO) {

        // 1.根据用户id查询 是否存在  存在登录  不存在  插入
        ZteUserEntity zteUserEntity = zteUserDao.selectById(loginDTO.getZteUserId());
        ZteUserEntity addEntity = new ZteUserEntity();

        if (Objects.isNull(zteUserEntity)) {
            // 插入中兴用户表
            addEntity = SmartBeanUtil.copy(loginDTO, ZteUserEntity.class);
            zteUserDao.insert(addEntity);
        }

        // 2.生成token 保存登录信息至 redis
        ZteUserDTO userDTO = SmartBeanUtil.copy(null == zteUserEntity ? addEntity : zteUserEntity, ZteUserDTO.class);

        String token = this.generateToken(userDTO);
        userDTO.setToken(token);

        // 3.返回前端
        ZteUserLoginVO userVO = SmartBeanUtil.copy(userDTO, ZteUserLoginVO.class);

        // 4.保存至 redis
        this.saveToken(userDTO);

        return ResponseDTO.succData(userVO);
    }

    /**
     * 将用户信息保存至 redis
     *
     * @param userDTO
     */
    public void saveToken(ZteUserDTO userDTO) {
        redisService.set(RedisKeyConst.TOKEN_USER + userDTO.getZteUserId(), userDTO, EXPIRE_SECONDS);
    }


    private String generateToken(ZteUserDTO userDTO) {
        Long id = userDTO.getZteUserId();
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

}
