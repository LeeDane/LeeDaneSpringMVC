package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.ScoreBean;

/**
 * 积分的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:17:12
 * Version 1.0
 */
public interface ScoreMapper extends BaseMapper<ScoreBean>{
	
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	/**
	 * 获取当前用户的总积分
	 * @param userId
	 * @return
	 */
	public int getTotalScore(int userId);
}
