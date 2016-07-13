package com.cn.leedane.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cn.leedane.model.UserBean;
import com.cn.leedane.utils.EnumUtil;

public class BaseController {
	
	protected UserBean user;
	protected JSONObject json;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> message = new HashMap<String, Object>();
	protected Map<String,Object> session;
	//返回结果中包含的是否成功
	protected boolean resIsSuccess;
	//返回结果中包含的提示信息
	protected String resmessage;
	//返回的编码
	protected int responseCode;
	
	/**
	 * 通过原先servlet方式输出json对象。
	 * 目的：解决复杂的文本中含有特殊的字符导致struts2的json
	 * 		解析失败，给客户端返回500的bug
	 */
	protected void printWriter(){
		JSONObject jsonObject = JSONObject.fromObject(message);
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.append(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();
		}
		
	}

	
	/**
	 * 校验请求参数
	 * @param request
	 * @return
	 */
	protected boolean checkParams(HttpServletRequest request0, HttpServletResponse response0){
		boolean result = false;
		message.put("isSuccess", resIsSuccess);
		this.request = request0;
		this.response = response0;
		try{
			user = (UserBean) request.getAttribute("user");
			json = JSONObject.fromObject(request.getAttribute("params"));
		}catch(Exception e){
			e.printStackTrace();
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
		}
		return result;
	}
}
