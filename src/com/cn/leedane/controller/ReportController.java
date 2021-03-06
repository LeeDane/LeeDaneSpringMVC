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

import com.cn.leedane.model.ReportBean;
import com.cn.leedane.service.ReportService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/report")
public class ReportController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());

	//举报service
	@Autowired
	private ReportService<ReportBean> reportService;

	/**
	 * 添加举报
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		message.put("isSuccess", resIsSuccess);
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(reportService.addReport(json, user, request));
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
	 * 取消举报
	 * @return
	 */
	@RequestMapping("/cancel")
	public String cancel(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.put("isSuccess", reportService.cancel(json, user, request));
			printWriter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		long end = System.currentTimeMillis();
		System.out.println("取消举报总计耗时：" +(end - start) +"毫秒");
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 获取举报列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			List<Map<String, Object>> result= reportService.getLimit(json, user, request);
			System.out.println("获得举报的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
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
