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

import com.cn.leedane.model.ZanBean;
import com.cn.leedane.service.ZanService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/zan")
public class ZanController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	//赞service
	@Autowired
	private ZanService<ZanBean> zanService;

	/**
	 * 添加赞
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(zanService.addZan(json, user, request));
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
	 * 取消赞
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(zanService.deleteZan(json, user, request));
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
	 * 获取赞列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			List<Map<String, Object>> result= zanService.getLimit(json, user, request);
			System.out.println("获得赞的数量：" +result.size());
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
	 * 获取点赞用户列表
	 * @return
	 */
	@RequestMapping("/getAllZanUser")
	public String getAllZanUser(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(zanService.getAllZanUser(json, user, request));
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
