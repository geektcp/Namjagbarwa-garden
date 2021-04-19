package com.geektcp.garden.spring.model.vo;

import com.geektcp.garden.spring.constant.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mr.Tang on 2021/1/21 13:41.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenVo {
    String tenantId;
    String username;
    String id;
    String type;    // user type
    String ip;
    TokenType tokenType;  // token type
}
