package com.anonym.module.admin.employee.login;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.module.admin.employee.basic.EmployeeStatusEnum;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.employee.login.domain.LoginCacheDTO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
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
import java.util.UUID;


@Slf4j
@Service
public class EmployeeLoginTokenService {

    /**
     * 过期时间一个星期
     */
    private static final int EXPIRE_SECONDS = 7 * 24 * 3600;

    @Value("${jwt.key}")
    private String jwtKey;

    private static String CLAIM_ID_KEY = "id";

    @Autowired
    private EmployeeLoginCacheService loginCacheService;

    /**
     * 功能描述: 生成JWT TOKEN
     *
     * @param employeeDTO
     * @return
     * @auther yandanyang
     * @date 2018/9/12 0012 上午 10:08
     */
    public String generateToken(EmployeeDTO employeeDTO) {

        Long id = employeeDTO.getId();
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
     * 功能描述: 根据登陆token获取登陆信息
     *
     * @param
     * @return
     * @auther yandanyang
     * @date 2018/9/12 0012 上午 10:11
     */
    public LoginTokenDTO getEmployeeTokenInfo(String token) {
        Long employeeId = -1L;
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
            String idStr = claims.get(CLAIM_ID_KEY).toString();
            employeeId = Long.valueOf(idStr);
        } catch (Exception e) {
            log.error("getEmployeeTokenInfo error:{}", e);
            return null;
        }

        LoginCacheDTO loginCacheDTO = loginCacheService.getLoginCacheDTO(employeeId);
        if (loginCacheDTO == null) {
            return null;
        }
        EmployeeDTO employeeDTO = loginCacheDTO.getEmployeeDTO();
        if (EmployeeStatusEnum.DISABLED.getValue().equals(employeeDTO.getIsDisabled())) {
            return null;
        }
        if (JudgeEnum.YES.equals(employeeDTO.getIsLeave())) {
            return null;
        }
        if (JudgeEnum.YES.equals(employeeDTO.getIsDelete())) {
            return null;
        }
        Long currentTime = System.currentTimeMillis();
        if (currentTime.longValue() > loginCacheDTO.getExpireTime()) {
            loginCacheService.removeLoginCache(employeeId);
            return null;
        }

        LoginTokenDTO requestToken = new LoginTokenDTO();
        requestToken.setId(employeeId);
        requestToken.setName(employeeDTO.getActualName());
        loginCacheService.updateExpireTime(employeeId);

        return requestToken;
    }

}
