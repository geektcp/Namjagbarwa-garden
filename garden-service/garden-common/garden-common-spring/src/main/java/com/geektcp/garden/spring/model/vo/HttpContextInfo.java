package com.geektcp.garden.spring.model.vo;

import lombok.Data;

/**
 * @author zhy
 * @date 2019/9/9 14:14
 **/
@Data
public class HttpContextInfo {
    private String tenantId;
    private String hostIp;
    private String userName;
    private Integer userId;
    private Integer userType;
}
