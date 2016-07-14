package com.cn.leedane.mapper;

import com.cn.leedane.model.ZanBean;

/**
 * 赞的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:21:59
 * Version 1.0
 */
public interface ZanMapper extends BaseMapper<ZanBean>{
	
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
