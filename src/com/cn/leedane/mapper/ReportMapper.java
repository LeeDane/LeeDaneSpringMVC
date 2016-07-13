package com.cn.leedane.mapper;

import com.cn.leedane.model.ReportBean;

/**
 * 举报的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:16:09
 * Version 1.0
 */
public interface ReportMapper extends BaseMapper<ReportBean>{
	
	
	/**
	 * 基础更新SQL的方法
	 * @param sql
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean updateSQL(String sql, Object ... obj);
	
	/**
	 * 判断记录在数据中是否存在
	 * @param tableName
	 * @param tableId
	 * @return
	 */
	public boolean recordExists(String tableName, int tableId);
	
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableIsd
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
