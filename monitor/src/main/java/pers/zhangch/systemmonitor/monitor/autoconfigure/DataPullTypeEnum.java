package pers.zhangch.systemmonitor.monitor.autoconfigure;

import lombok.Getter;

/**
 * 数据来源类型
 * @author Zhang ChangHao
 * @time 2021/10/11 10:45
 */
@Getter
public enum DataPullTypeEnum {
    
    /**
     * 数据来源类型 RESTful接口
     */
    RESTful("RESTful"),
    /**
     * 数据来源类型 数据库
     */
    DB("db");
    
    private String dataPullType;
    
    DataPullTypeEnum(String dataPullType) {
        this.dataPullType = dataPullType;
    }
}
