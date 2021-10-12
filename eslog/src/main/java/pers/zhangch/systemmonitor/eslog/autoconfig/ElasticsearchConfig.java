package pers.zhangch.systemmonitor.eslog.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zhang ChangHao
 * @time 2021/9/26 15:55
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch", ignoreInvalidFields = true)
@Getter
@Setter
public class ElasticsearchConfig {
    
    /**
     * 是否启用es
     */
    @Value("${enable:false}")
    private boolean enable;
    
    /**
     * ES ip
     */
    private String host;
    /**
     * ES 端口
     */
    private int port;
    
    /**
     * es 集群名称
     */
    private String clusterName;
    
    @Bean
    @ConditionalOnExpression("${elasticsearch.enable:false}")
    public RestHighLevelClient highLevelClient() {
        HttpHost httpHost = new HttpHost(host, port, "http");
        return new RestHighLevelClient(
                RestClient.builder(httpHost)
        );
    }
}
