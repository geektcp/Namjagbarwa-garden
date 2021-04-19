package com.geektcp.garden.spring.util;

import com.google.common.collect.Maps;
import com.geektcp.garden.spring.constant.TokenType;
import com.geektcp.garden.core.exception.BaseException;
import com.geektcp.garden.spring.model.vo.UserTokenVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    // user name
    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    // user id
    public static final String CLAIM_KEY_UID = "uid";
    // tenant id
    public static final String CLAIM_KEY_TID = "tid";
    // token id
    public static final String CLAIM_KEY_ID = "id";
    public static final String CLAIM_KEY_USERTYPE = "utype";
    public static final String CLAIM_KEY_OAUTHTYPE = "oauthType";

    @Value("${gate.jwt.secret:UNKNOWN}")
    private String secret;

    @Value("${gate.jwt.expiration:7200}")
    private Long expiration;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getTenantIdFromToken(String token) {
        String tenantId;
        try {
            final Claims claims = getClaimsFromToken(token);
            tenantId = claims.get("tid").toString();
        } catch (Exception e) {
            tenantId = null;
        }
        return tenantId;
    }


    public String getValueFromToken(String token, String key) {
        String Value;
        try {
            final Claims claims = getClaimsFromToken(token);
            Value = (String) claims.get(key);
        } catch (Exception e) {
            Value = null;
        }
        return Value;
    }


    private Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    private Date getExpirationDateFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException("get token expiration date failed!");
        }
    }

    public Boolean invalid(String token, TokenType tokenType) {
        String username = this.getUsernameFromToken(token);
        String tokenID = this.getValueFromToken(token, CLAIM_KEY_ID);

        List<String> keys = new ArrayList<>();
        // 删除用户token记录
        String key = getKey(IPUtil.getIp(), username, tokenID, tokenType);
        keys.add(key);
        // 删除用户权限数据
        keys.add("st:" + username + ":permission");
        redisTemplate.delete(keys);

        return true;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(String tenantId, String username, String id, String type, String ip, TokenType tokenType) {
        if (StringUtils.isEmpty(id)) {
            throw new BaseException("用户登录时id为空");
        }
        HttpRequestHeadUtil.setCurTenantId(tenantId);
        String strTokenID = UUID.randomUUID().toString().replace("-", "");
        String strKey = getKey(ip, username, strTokenID, tokenType);
        Map<String, Object> claims = Maps.newHashMap();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_UID, id);
        claims.put(CLAIM_KEY_TID, tenantId);
        claims.put(CLAIM_KEY_ID, strTokenID);
        claims.put(CLAIM_KEY_USERTYPE, type);
        claims.put(CLAIM_KEY_CREATED, new Date());
        String token = generateTokenByClaims(claims);
        redisTemplate.opsForValue().set(strKey, token, expiration, TimeUnit.SECONDS);
        return token;
    }

    public String generateToken(UserTokenVo userTokenVo) {
        return generateToken(userTokenVo.getTenantId(),
                userTokenVo.getUsername(),
                userTokenVo.getId(),
                userTokenVo.getType(),
                userTokenVo.getIp(),
                userTokenVo.getTokenType());
    }

    private String generateTokenByClaims(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token, TokenType tokenType) {
        String userName = getUsernameFromToken(token);
        String tokenID = getValueFromToken(token, CLAIM_KEY_ID);
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(tokenID)) {
            return null;
        }
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIp(request);
        String strKey = getKey(ip, userName, tokenID, tokenType);
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateTokenByClaims(claims);
            // tokenID 与token的映射
            redisTemplate.opsForValue().set(strKey, refreshedToken, expiration, TimeUnit.SECONDS);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, TokenType tokenType) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        final String tokenID = getValueFromToken(token, CLAIM_KEY_ID);
        final String username = getUsernameFromToken(token);
        if (username.isEmpty() || tokenID.isEmpty()) {
            return false;
        }
        // 过期判断
        if (isTokenExpired(token)) {
            return false;
        }

        // 非本系统登录验证,如OAuth2，不需要做登出判断
        String oauthType = getValueFromToken(token, CLAIM_KEY_OAUTHTYPE);
        if (!StringUtils.isEmpty(oauthType)) {
            return true;
        }

        // 本系统，判断是否已经登出
        String key = getKey(IPUtil.getIp(), username, tokenID, tokenType);
        Object existToken = redisTemplate.opsForValue().get(key);
        return (token.equals(existToken));
    }

    private String getKey(String ip, String username, String tokenId, TokenType tokenType) {
        return tokenType + ":" + ip + ":" + username + ":" + tokenId;
    }
}

