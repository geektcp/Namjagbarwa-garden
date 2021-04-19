package com.geektcp.garden.spring.model.qo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Mr.Tang on 2021/1/16.
 */
@Data
public class BaseQo {
    public static final String DEBUG_FIELD = "debug";
    public static final String TIMEOUT = "timeout";

    @ApiModelProperty(value = "内部可选参数")
    private JSONObject internalOption = new JSONObject();

    public boolean isDebug() {
        return internalOption.getBooleanValue(DEBUG_FIELD);
    }

    public void setDebug(boolean debugEnabled) {
        this.internalOption.put(DEBUG_FIELD, debugEnabled);
    }
}
