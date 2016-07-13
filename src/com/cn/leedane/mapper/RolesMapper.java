package com.cn.leedane.mapper;

import com.cn.leedane.model.RolesBean;

/**
 * 角色mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:16:38
 * Version 1.0
 */
public interface RolesMapper extends BaseMapper<RolesBean>{
	
	/**
	 * 根据id找到一个实体对象
	 * 存在延迟加载的问题，充分利用hibernate的内部缓存和二级缓存中的现有数据，
	 * 也许别人把数据库中的数据修改了，load如果在缓存中找到了数据，则不会再访问数据库，
	 * 而get则会返回最新数据。
	 * 如果没有得到数据，将抛出异常，返回的将是实体的代理类
	 * @param id
	 * @return
	 */
	public RolesBean loadById(int id);
	
}
