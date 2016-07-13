package com.cn.leedane.mapper;

import java.util.List;
import java.util.Map;

import com.cn.leedane.model.FriendBean;

/**
 * 好友关系mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:11:33
 * Version 1.0
 */
public interface FriendMapper extends BaseMapper<FriendBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public FriendBean findById(int id);
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	/**
	 * 根据用户id删除指定好友信息
	 * @param uid
	 * @param friends
	 * @return
	 */
	public boolean deleteFriends(int uid, int ... friends);

	/**
	 * 判断两人是否是朋友的关系（判断两人的正式好友记录）
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFriend(int id, int to_user_id);
	
	/**
	 * 判断两人是否是朋友的关系（包括一方申请还没有同意）
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFriendRecord(int id, int to_user_id);
	
}
