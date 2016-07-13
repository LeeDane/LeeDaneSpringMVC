package com.cn.leedane.mapper;

import java.util.List;

import com.cn.leedane.model.AttentionBean;
import com.cn.leedane.model.CartBean;

/**
 * 购物车的mapper类
 * @author LeeDane
 * 2016年7月12日 上午11:05:51
 * Version 1.0
 */
public interface CartMapper extends BaseMapper<CartBean>{
	
	/**
	 * 基础根据id找到一个实体对象
	 * 不存在延迟加载问题，不采用lazy机制，在内部缓存中进行数据查找，
	 * 如果没有发现数据則将越过二级缓存，直接调用SQL查询数据库。
	 * 如果没有数据就返回null，返回的是真正的实体类
	 * @param id
	 * @return
	 */
	public CartBean findById(int id);
	
	/**
	 * 执行HQL获得实体
	 * @param beanName  实体名称如：xxxBean
	 * @param where where语句，参数需直接填写在字符串中
	 * @return
	 */
	public List<CartBean> executeHQL(String beanName, String where);
}
