package com.cn.leedane.mapper;

import com.cn.leedane.model.ChatBgUserBean;

/**
 * 聊天背景与用户关系相关mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:06:56
 * Version 1.0
 */
public interface ChatBgUserMapper extends BaseMapper<ChatBgUserBean>{
	
	/**
	 * 判断记录在数据中是否存在
	 * @param tableName
	 * @param tableId
	 * @return
	 */
	public boolean recordExists(String tableName, int tableId);
	/**
	 * 判断是否下载过
	 * @param userId
	 * @param chatBgTableId
	 * @return
	 */
	public boolean exists(int userId, int chatBgTableId);
}
