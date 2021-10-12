package pers.zhangch.systemmonitor.monitor.vo;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 监控数据格式
 * @author Zhang ChangHao
 * @time 2021/10/11 16:56
 */
@Setter
@Getter
public class MonitorDataVO implements Serializable {
    
    private static final long serialVersionUID = 9122665969103336326L;
    
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = -1;
    
    /**
     * 成功标记
     */
    private int code = 0;
    /**
     * 错误类型
     */
    private String errorType;
    /**
     * 错误原因
     */
    private String errorMsg;
    /**
     * 返回数据
     */
    private JSONObject data;
    
    /**
     * 拆分数组节点路径 传输到es的json数据最好不要包含数组 如果包含了数组可以传入数组节点路径 将数组分割后逐个上传到es中
     */
    private String spiltArrayPath;
    
    public boolean isSuccess() {
        return CODE_SUCCESS == this.code;
    }
}
