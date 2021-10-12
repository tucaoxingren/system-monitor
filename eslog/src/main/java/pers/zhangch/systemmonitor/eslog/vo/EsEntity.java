package pers.zhangch.systemmonitor.eslog.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Zhang ChangHao
 * @time 2021/9/26 16:26
 */
@Getter
@Setter
public class EsEntity<T> {
    
    private String id;
    private T data;
    private Date date;
    
    private EsEntity() {
    
    }
    
    /**
     *
     * @param data 索引数据
     */
    public EsEntity(T data) {
        this.data = data;
        this.id = id;
        this.date = new Date();
    }
    
    /**
     *
     * @param id 主键
     * @param data 索引数据
     */
    public EsEntity(String id, T data) {
        this.data = data;
        this.id = id;
        this.date = new Date();
    }
    
    /**
     *
     * @param id 主键
     * @param data 索引数据
     * @param date 索引查询时间
     */
    public EsEntity(String id, T data, Date date) {
        this.data = data;
        this.id = id;
        this.date = date;
    }
}
