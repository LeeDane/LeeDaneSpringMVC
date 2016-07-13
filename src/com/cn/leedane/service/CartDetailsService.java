package com.cn.leedane.service;

import com.cn.leedane.model.IDBean;

/**
 * 购物车清单的Service类
 * @author LeeDane
 * 2016年7月12日 上午11:30:44
 * Version 1.0
 */
public interface CartDetailsService<T extends IDBean>{

	public void addCartDetails();
}
