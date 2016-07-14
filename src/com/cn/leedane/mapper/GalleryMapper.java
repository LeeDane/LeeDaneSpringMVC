package com.cn.leedane.mapper;

import com.cn.leedane.model.GalleryBean;
import com.cn.leedane.model.UserBean;
/**
 * 图库mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:12:12
 * Version 1.0
 */
public interface GalleryMapper extends BaseMapper<GalleryBean>{
	
	/**
	 * 检查该图片是否已经加入图库
	 * @param user
	 * @param path
	 * @return
	 */
	public boolean isExist(UserBean user, String path);
}
