package com.cn.leedane.mapper;

import com.cn.leedane.model.UploadBean;

/**
 * 断点上传mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:19:15
 * Version 1.0
 */
public interface UploadMapper extends BaseMapper<UploadBean>{
	
	/**
	 * 基础更新SQL的方法
	 * @param sql
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean updateSQL(String sql, Object ... obj);
	
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableUuid
	 * @param order
	 * @param serialNumber
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, String tableUuid, int order, int serialNumber, int userId);
}
