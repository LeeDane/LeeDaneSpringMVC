package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.CollectionBean;

/**
 * 收藏夹的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:08:06
 * Version 1.0
 */
public interface CollectionMapper extends BaseMapper<CollectionBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public CollectionBean findById(int id);
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableId
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
