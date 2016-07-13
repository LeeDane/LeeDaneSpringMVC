package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.FanBean;

/**
 * 粉丝mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:10:24
 * Version 1.0
 */
public interface FanMapper extends BaseMapper<FanBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public FanBean findById(int id);
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	/**
	 * 根据用户id删除批量取消粉丝
	 * @param uid
	 * @param fanId
	 * @return
	 */
	public boolean cancel(int uid, int ... fanId);

	/**
	 * 判断两人是否是互粉关系
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFanEachOther(int id, int to_user_id);
	
}
