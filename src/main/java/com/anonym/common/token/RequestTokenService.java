package com.anonym.common.token;

import com.alibaba.fastjson.JSON;
import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.service.RedisCommonService;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.privilege.EmployeePrivilegeCacheService;
import com.anonym.module.roleemployee.RoleEmployeeDao;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
public class RequestTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestTokenService.class);

    /**
     * 过期时间一个星期
     */
    private static final int EXPIRE_SECONDS = 7 * 24 * 3600;

    /**
     * 二级缓存
     */
    private ConcurrentMap<Integer, IRequestToken> backTokenCache = new ConcurrentHashMap<>();

    @Autowired
    private RedisCommonService redisCommonService;

    @Value("${jwt.key}")
    private String jwtKey;

    @Autowired
    private RoleEmployeeDao roleRelationDao;

    @Autowired
    private EmployeePrivilegeCacheService employeePrivilegeCacheService;

    private static String CLAIM_ID_KEY = "id";

    /**
     * 功能描述: 生成JWT TOKEN
     */
    public String generateToken(RequestTokenDTO<EmployeeDTO> requestTokenDTO) {

        Integer id = requestTokenDTO.getId();
        List<Integer> roleIdList = roleRelationDao.selectRoleIdByEmployeeId(id);

        IRequestToken requestToken = new RequestBackToken();
        requestToken.setGroupIds(roleIdList);
        requestToken.setId(requestTokenDTO.getId());
        requestToken.setUserType(requestTokenDTO.getUserType());
        requestToken.setData(requestTokenDTO.getData());
        /**将token设置为jwt格式*/
        String baseToken = UUID.randomUUID().toString();

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(EXPIRE_SECONDS);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());

        Claims jwtClaims = Jwts.claims().setSubject(baseToken);
        jwtClaims.put(CLAIM_ID_KEY, requestTokenDTO.getId());
        String compactJws = Jwts.builder().setClaims(jwtClaims).setNotBefore(from).setExpiration(expire).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
        requestToken.setXAccessToken(compactJws);
        /**添加缓存*/
        backTokenCache.put(requestTokenDTO.getId(), requestToken);
        String requestTokenJson = JSON.toJSONString(requestToken);
        redisCommonService.set(baseToken, requestTokenJson, EXPIRE_SECONDS);
        return compactJws;
    }

    /**
     * 功能描述: 退出登录 删除二级缓存以及redis
     */
    public ResponseDTO<Boolean> logoutByToken(String xAccessToken) {
        try {
            IRequestToken requestToken = getEmployeeTokenInfo(xAccessToken);
            if (requestToken != null) {
                employeePrivilegeCacheService.removeCache(requestToken.getId());
                backTokenCache.remove(requestToken.getId());
            }
            //token jwt格式解密
            Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(xAccessToken).getBody();
            String unSignToken = claims.getSubject();
            redisCommonService.del(unSignToken);
            return ResponseDTO.succ();
        } catch (ExpiredJwtException e) {
            return ResponseDTO.wrap(ResponseCodeConst.COMMON_ERROR, "退出失败");
        } catch (UnsupportedJwtException e) {
            return ResponseDTO.wrap(ResponseCodeConst.COMMON_ERROR, "退出失败");
        } catch (MalformedJwtException e) {
            return ResponseDTO.wrap(ResponseCodeConst.COMMON_ERROR, "退出失败");
        } catch (SignatureException e) {
            return ResponseDTO.wrap(ResponseCodeConst.COMMON_ERROR, "退出失败");
        } catch (IllegalArgumentException e) {
            return ResponseDTO.wrap(ResponseCodeConst.COMMON_ERROR, "退出失败");
        }
    }

    /**
     * 功能描述: 根据登陆token获取登陆信息
     */
    public IRequestToken getEmployeeTokenInfo(String token) {
        //token jwt格式解密
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
            String unSignToken = claims.getSubject();
            String idStr = claims.get(CLAIM_ID_KEY).toString();
            Integer id = Integer.valueOf(idStr);
            IRequestToken requestToken = backTokenCache.get(id);

            if (requestToken == null) {
                synchronized (this) {
                    requestToken = backTokenCache.get(id);
                    if (requestToken == null) {
                        requestToken = redisCommonService.getObject(unSignToken, RequestBackToken.class);
                        backTokenCache.put(id, requestToken);
                    }
                }
            }
            return requestToken;
        } catch (Exception e) {
            LOG.error("", e);
            return null;
        }
    }

    public static void main(String[] args) {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(30 * 24 * 3600);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());

        Claims jwtClaims = Jwts.claims().setSubject(UUID.randomUUID().toString());
        jwtClaims.put(CLAIM_ID_KEY, 1);
        String compactJws = Jwts.builder().setClaims(jwtClaims).setNotBefore(from).setExpiration(expire).signWith(SignatureAlgorithm.HS512, "hsas-jwt-key").compact();
        System.out.println(compactJws);
    }

}
