package com.cn.leedane.mapper;

import com.cn.leedane.model.CompanyBean;

/**
 * 公司的mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:09:23
 * Version 1.0
 */
public interface CompanyMapper extends BaseMapper<CompanyBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public CompanyBean findById(int id);
}
