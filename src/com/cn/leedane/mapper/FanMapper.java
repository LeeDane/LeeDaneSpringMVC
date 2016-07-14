package com.cn.leedane.mapper;

import com.cn.leedane.model.FanBean;

/**
 * 粉丝mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:10:24
 * Version 1.0
 */
public interface FanMapper extends BaseMapper<FanBean>{
	
	/**
	 * 根据用户id删除批量取消粉丝
	 * @param uid
	 * @param fanId
	 * @return
	 */
	public boolean cancel(int uid, int ... fanId);

	/**
	 * 判断两人是否是互粉关系
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFanEachOther(int id, int to_user_id);
	
}
