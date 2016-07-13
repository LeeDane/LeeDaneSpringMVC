package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.ZanBean;

/**
 * 赞的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:21:59
 * Version 1.0
 */
public interface ZanMapper extends BaseMapper<ZanBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public ZanBean findById(int id);
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	/**
	 * 根据实体表的名称和ID获取其创建人ID
	 * @param commentId
	 * @return
	 */
	public int getObjectCreateUserId(String tableName, int tableId);
	
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableId
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
	
	/**
	 * 判断记录在数据中是否存在
	 * @param tableName
	 * @param tableId
	 * @return
	 */
	public boolean recordExists(String tableName, int tableId);
}
