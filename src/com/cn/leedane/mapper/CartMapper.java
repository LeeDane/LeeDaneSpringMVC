package com.cn.leedane.mapper;

import java.util.List;

import com.cn.leedane.model.CartBean;

/**
 * 购物车的mapper类
 * @author LeeDane
 * 2016年7月12日 上午11:05:51
 * Version 1.0
 */
public interface CartMapper extends BaseMapper<CartBean>{
	
	/**
	 * 执行HQL获得实体
	 * @param beanName  实体名称如：xxxBean
	 * @param where where语句，参数需直接填写在字符串中
	 * @return
	 */
	public List<CartBean> executeHQL(String beanName, String where);
}
