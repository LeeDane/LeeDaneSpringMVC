package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.UploadBean;

/**
 * 断点上传mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:19:15
 * Version 1.0
 */
public interface UploadMapper extends BaseMapper<UploadBean>{
	
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
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
