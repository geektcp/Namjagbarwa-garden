package com.geektcp.garden.spring.constant;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chengmo on 2018/8/17.
 */
public enum StoreType {
    GDB("GDB", false, new String[]{}),
    UNKNOWN("unknown", false, new String[]{});

    private static final Map<String, StoreType> codeLookup = new ConcurrentHashMap<>(6);

    static {
        for (StoreType type : EnumSet.allOf(StoreType.class)){
            codeLookup.put(type.name.toLowerCase(), type);
        }
    }

    private boolean isHadoopPlatform;
    private String name;
    private String[] configFile;
    StoreType(String name, boolean isHadoopPlatform, String[] configFile){
        this.name = name;
        this.isHadoopPlatform = isHadoopPlatform;
        this.configFile = configFile;
    }

    public String getName() {
        return name;
    }

    public boolean isHadoopPlatform() {
        return isHadoopPlatform;
    }

    public String[] getConfigFile() {
        return configFile;
    }

    public static StoreType fromCode(String code) {
        if (code == null){
            return StoreType.UNKNOWN;
        }
        StoreType data = codeLookup.get(code.toLowerCase());
        if (data == null) {
            return StoreType.UNKNOWN;
        }
        return data;
    }
}