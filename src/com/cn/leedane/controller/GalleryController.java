package com.cn.leedane.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.GalleryBean;
import com.cn.leedane.service.GalleryService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/gallery")
public class GalleryController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private GalleryService<GalleryBean> galleryService;
	
	
	/**
	 * 添加网络链接到图库
	 * @return
	 */
	
	@RequestMapping("/addLink")
	public String addLink(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(galleryService.addLink(json, user, request));
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 获取图库的照片列表
	 * @return
	 */
	@RequestMapping("/getGalleryPaging")
	public String getGalleryPaging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			List<Map<String, Object>> result= galleryService.getGalleryByLimit(json, user, request);
			System.out.println("获得图库的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter();
		return null;
	}
	

	/**
	 * 移出图库
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			message.putAll(galleryService.delete(json, user, request));
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
}
