package com.geektcp.garden.spring.cache;

/**
 * Created by chengmo on 2019/6/24.
 */
public interface Cacheable {

    void refresh(String key) throws Exception;
}
