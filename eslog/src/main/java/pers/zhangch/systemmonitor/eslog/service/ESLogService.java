package pers.zhangch.systemmonitor.eslog.service;

import pers.zhangch.systemmonitor.eslog.vo.EsEntity;

import java.io.IOException;

/**
 * @author Zhang ChangHao
 * @time 2021/10/9 13:35
 */
public interface ESLogService {
    
    /**
     * 异步保存数据到es中
     * @param indexName 索引名称
     * @param esEntity 要保存的实体
     * @param <T> 实体
     */
    <T> void saveAsync(String indexName, EsEntity<T> esEntity) throws IOException;
    
    /**
     * 异步保存数据到es中
     * @param indexName 索引名称
     * @param esEntity 要保存的实体
     * @param <T> 实体
     */
    <T> void saveSync(String indexName, EsEntity<T> esEntity) throws IOException;
}
