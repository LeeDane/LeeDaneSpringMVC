package com.cn.leedane.mapper;

import com.cn.leedane.model.AttentionBean;

/**
 * 关注的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:03:44
 * Version 1.0
 */
public interface AttentionMapper extends BaseMapper<AttentionBean>{
	
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
