package com.cn.leedane.mapper;

import com.cn.leedane.model.CommentBean;

/**
 * 评论的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:08:44
 * Version 1.0
 */
public interface CommentMapper extends BaseMapper<CommentBean>{
	/**
	 * 根据实体表的名称和ID获取其创建人ID
	 * @param commentId
	 * @return
	 */
	public int getObjectCreateUserId(String tableName, int tableId);
	
	/**
	 * 获取总数
	 * @param tableName  表名
	 * @param where where后面语句，参数需直接填写在字符串中
	 * @return
	 */
	public int getTotal(String tableName, String where);
	
	/**
	 * 基础更新SQL的方法
	 * @param sql
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean updateSQL(String sql, Object ... obj);
	
	/**
	 * 获取当前用户的总评论数
	 * @param userId
	 * @return
	 */
	public int getTotalComments(int userId);
}
