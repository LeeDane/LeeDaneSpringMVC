package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.SignInBean;

/**
 * 签到mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:17:46
 * Version 1.0
 */
public interface SignInMapper extends BaseMapper<SignInBean>{
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	/**
	 * 用户指定时间是否已经签到
	 * @param userId 用户ID
	 * @param dateTime 指定的日期
	 * @return
	 */
	public boolean isSign(int userId, String dateTime);
	
	/**
	 * 用户历史上是否有签到记录
	 * @param userId 用户ID
	 * @return
	 */
	public boolean hasHistorySign(int userId);
	
	/**
	 * 保存(签到),当天已经签到的直接返回false
	 * @param signInBean
	 * @return
	 */
	public boolean save(SignInBean signInBean, int userId);
	
	/**
	 * 获取数据库中最新的记录
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getNewestRecore(int userId);
	
	/**
	 * 获取昨天的签到记录
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> getYesTodayRecore(int uid);
	
	/**
	 * 获取用户当前的积分
	 * @param uid
	 * @return
	 */
	public int getScore(int uid);
}
