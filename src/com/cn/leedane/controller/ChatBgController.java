package com.cn.leedane.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.ChatBgBean;
import com.cn.leedane.service.ChatBgService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/chatBg")
public class ChatBgController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
private ChatBgService<ChatBgBean> chatBgService;
	
	@Resource
	public void setChatBgService(ChatBgService<ChatBgBean> chatBgService) {
		this.chatBgService = chatBgService;
	}
	
	/**
	 * 发布聊天背景历史
	 * @return
	 */
	@RequestMapping("/publish")
	public String publish(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatBgService.publish(json, user, request));
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
	 * 分页获取聊天背景列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			message.putAll(chatBgService.paging(json, user, request));
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
	 * 检验聊天背景
	 * @return
	 */
	@RequestMapping("/verifyChatBg")
	public String verifyChatBg(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			message.putAll(chatBgService.verifyChatBg(json, user, request));
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
