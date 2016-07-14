package com.cn.leedane.mapper;

import com.cn.leedane.model.CollectionBean;

/**
 * 收藏夹的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:08:06
 * Version 1.0
 */
public interface CollectionMapper extends BaseMapper<CollectionBean>{
	
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableId
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
