package pers.zhangch.systemmonitor.eslog.utils;

import org.springframework.stereotype.Component;
import pers.zhangch.systemmonitor.eslog.service.ESLogService;
import pers.zhangch.systemmonitor.eslog.vo.EsEntity;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * 保存数据到ES中的工具类
 * @author Zhang ChangHao
 * @time 2021/10/9 13:28
 */
@Slf4j
@Component
public class ESLogUtil {
    
    @Resource
    private ESLogService esLogService;
    private static ESLogService esLogServiceUtil;
    
    //初始化
    @PostConstruct
    public void init() {
        ESLogUtil.esLogServiceUtil = this.esLogService;
    }
    
    public static  <T> void saveAsync(String index, EsEntity<T> esEntity) {
        try {
            esLogServiceUtil.saveAsync(index, esEntity);
        } catch (IOException e) {
            log.error("异步写入ES失败", e);
        }
    }
    
    public static <T> void saveSync(String index, EsEntity<T> esEntity) throws IOException {
        esLogServiceUtil.saveSync(index, esEntity);
    }
}
