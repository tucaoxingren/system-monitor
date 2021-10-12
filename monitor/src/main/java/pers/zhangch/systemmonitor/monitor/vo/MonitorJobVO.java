package pers.zhangch.systemmonitor.monitor.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Validator;
import cn.hutool.cron.pattern.CronPattern;
import lombok.Getter;
import lombok.Setter;
import pers.zhangch.systemmonitor.monitor.autoconfigure.DataPullTypeEnum;
import pers.zhangch.systemmonitor.monitor.schedule.DefaultParentTask;

import java.util.Date;

/**
 * 监控定时任务属性
 * @author Zhang ChangHao
 * @time 2021/10/11 13:36
 */
@Getter
@Setter
public class MonitorJobVO {
    
    private String name;
    
    /**
     * 监控job定时执行cron表达式
     */
    private String cron;
    /**
     * 是否启用
     */
    private boolean enabled = true;
    /**
     * 描述
     */
    private String description = "";
    /**
     * 是否推送到ElasticSearch
     */
    private boolean pushToEs = true;
    /**
     * 监控数据来源
     */
    private DataPullTypeEnum dataPullType = DataPullTypeEnum.REST_FUL;
    /**
     * 监控数据来源为RESTful时 数据获取地址
     */
    private String dataRESTfulUrl;
    
    /**
     * 任务执行Class
     */
    private String executeTaskClass = DefaultParentTask.class.getName();
    
    /**
     * 检查当前任务配置是否正确
     * @return
     */
    public boolean check() {
        boolean result = this.enabled;
        if (result) {
            try {
                checks();
                result = true;
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }
    
    /**
     * 检查当前任务配置是否正确
     */
    public void checks() {
        if (this.enabled) {
            try {
                if (DataPullTypeEnum.REST_FUL.equals(this.dataPullType)) {
                    if (Validator.isEmpty(this.dataRESTfulUrl)) {
                        throw new ValidateException("数据来源类型为{}时 dataRESTfulUrl 不能为空", this.dataPullType.getDataPullType());
                    }
                }
                if (Validator.isEmpty(this.cron)) {
                    throw new ValidateException("Cron表达式不能为空");
                } else {
                    try {
                        new CronPattern(this.cron);
                    } catch (Exception e) {
                        throw new ValidateException("Cron表达式错误: {}", e.getMessage());
                    }
                }
            } catch (Exception e) {
                throw new ValidateException("监控任务：[{}]初始化失败 - {}", this.name, e.getMessage());
            }
        }
    }
    
    /**
     * 获取ES索引名称
     */
    public String getESIndexName() {
        return "system-monitor-" + DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
    }
}
