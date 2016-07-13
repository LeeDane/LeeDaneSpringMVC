package com.cn.leedane.mapper;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.cn.leedane.model.TemporaryBase64Bean;

/**
 * base64临时上传的文件mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:18:17
 * Version 1.0
 */
public interface TemporaryBase64Mapper extends BaseMapper<TemporaryBase64Bean>{
	
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	

	/**
	 * 批量删除(根据uuid)
	 * @param table(数据库中的表名称)
	 * @param uuids
	 * @return
	 */
	public boolean deleteBatchSql(String table, String[] uuids) throws DataAccessException;
	
	/**
	 * 通过uuid删除记录（删除某条心情下的全部上传记录）
	 * @param uuid
	 * @param tableName（最终所属的表名）
	 * @return
	 */
	public boolean deleteByUuid(String uuid, String tableName) throws DataAccessException;
	
	/**
	 * 通过uuid和order删除记录（删除某条心情下的指定order的记录）
	 * @param uuid
	 * @param order
	 * @param tableName（最终所属的表名）
	 * @return
	 * @throws DataAccessException
	 */
	public boolean deleteByUuidAndOrder(String uuid, int order, String tableName) throws DataAccessException;
}
