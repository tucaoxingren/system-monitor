package pers.zhangch.systemmonitor.eslog.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pers.zhangch.systemmonitor.eslog.autoconfig.ElasticsearchConfig;
import pers.zhangch.systemmonitor.eslog.service.ESLogService;
import pers.zhangch.systemmonitor.eslog.vo.EsEntity;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Zhang ChangHao
 * @time 2021/10/9 13:35
 */
@Slf4j
@Service
public class ESLogServiceImpl implements ESLogService {
    
    @Autowired(required = false)
    RestHighLevelClient restHighLevelClient;
    
    @Resource
    ElasticsearchConfig elasticsearchConfig;
    
    @Async
    @Retryable(value = Exception.class, backoff = @Backoff(delay = 30000L, maxDelay = 300000L, multiplier = 1.5))
    @Override
    public <T> void saveAsync(String indexName, EsEntity<T> esEntity) throws IOException {
        save(indexName, esEntity);
    }
    
    @Override
    public <T> void saveSync(String indexName, EsEntity<T> esEntity) throws IOException {
        save(indexName, esEntity);
    }
    
    /**
     * 保存数据到es中
     * @param indexName 索引名称
     * @param esEntity 要保存的实体
     * @param <T> 实体
     */
    private  <T> void save(String indexName, EsEntity<T> esEntity) throws IOException {
        if (elasticsearchConfig.isEnable()) {
            IndexRequest indexRequest = new IndexRequest(indexName);
            if (Validator.isNotEmpty(esEntity.getId())) {
                indexRequest.id(esEntity.getId());
            }
            JSONObject jsonObject = JSONUtil.parseObj(esEntity.getData());
            jsonObject.set("timestamp", esEntity.getDate());
            indexRequest.source(jsonObject);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.debug("es 记录成功, id:{}, data:{}", esEntity.getId(), esEntity.getData());
        }
    }
}
