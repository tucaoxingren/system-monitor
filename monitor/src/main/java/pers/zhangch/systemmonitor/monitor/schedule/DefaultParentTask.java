package pers.zhangch.systemmonitor.monitor.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.lang.Validator;
import cn.hutool.cron.task.Task;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pers.zhangch.systemmonitor.eslog.utils.ESLogUtil;
import pers.zhangch.systemmonitor.eslog.vo.EsEntity;
import pers.zhangch.systemmonitor.monitor.autoconfigure.DataPullTypeEnum;
import pers.zhangch.systemmonitor.monitor.vo.MonitorDataVO;
import pers.zhangch.systemmonitor.monitor.vo.MonitorJobVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认定时任务处理
 * @author Zhang ChangHao
 * @time 2021/10/11 13:32
 */
@Slf4j
@Getter
@Setter
public class DefaultParentTask implements Task {
    
    private MonitorJobVO monitorJobVO;
    
    @Override
    public void execute() {
        log.debug(String.format("JOB[%s(%s)]开始执行, 执行周期Cron[%s], 当前时间%s",
                monitorJobVO.getName(),
                monitorJobVO.getDescription(),
                monitorJobVO.getCron(),
                DateUtil.now()));
        List<String> monitorDataList = new ArrayList<>();
        try {
            if (DataPullTypeEnum.REST_FUL.equals(monitorJobVO.getDataPullType())) {
                String result = HttpUtil.get(monitorJobVO.getDataRESTfulUrl());
                MonitorDataVO monitorDataVO = JSONUtil.toBean(result, MonitorDataVO.class);
                if (monitorDataVO.isSuccess()) {
                    if (Validator.isNotEmpty(monitorDataVO.getSpiltArrayPath())) {
                        String jsonPath = monitorDataVO.getSpiltArrayPath();
                        String monitorDataStr = monitorDataVO.getData().toJSONString(0);
                        JSONArray jsonArray = (JSONArray) JSONUtil.getByPath(monitorDataVO.getData(), jsonPath);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject monitorData = JSONUtil.parseObj(monitorDataStr);
                            JSONUtil.putByPath(monitorData, jsonPath, jsonObject);
                            monitorDataList.add(monitorData.toJSONString(0));
                        }
                    } else {
                        monitorDataList.add(monitorDataVO.getData().toJSONString(0));
                    }
                } else {
                    throw new StatefulException(monitorDataVO.getErrorType(), monitorDataVO.getErrorMsg());
                }
            }
        } catch (Exception e) {
            log.error("监控任务[{}({})]数据获取错误-错误信息：{}", monitorJobVO.getName(), monitorJobVO.getDescription(), e.getMessage());
        }
        
        if (monitorJobVO.isPushToEs()) {
            for (String monitorData : monitorDataList) {
                ESLogUtil.saveAsync(monitorJobVO.getESIndexName(), new EsEntity<>(monitorData));
            }
        }
    }
}
