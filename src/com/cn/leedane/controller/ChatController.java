package com.cn.leedane.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.ChatBean;
import com.cn.leedane.service.ChatService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/chat")
public class ChatController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private ChatService<ChatBean> chatService;
	
	/**
	 * 发送聊天信息
	 * @return
	 */
	@RequestMapping("/send")
	public String send(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatService.send(json, user, request));
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
	 * 删除聊天记录
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatService.deleteChat(json, user, request));
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
	 * 分页获取聊天历史列表(两个人的聊天)
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatService.getLimit(json, user, request));
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
	 * 更新聊天信息为已读状态
	 * @return
	 */
	@RequestMapping("/updateRead")
	public String updateRead(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatService.updateRead(json, user, request));
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
	 * 获取用户全部未读的信息
	 * @return
	 */
	@RequestMapping("/noReadList")
	public String noReadList(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(chatService.noReadList(json, user, request));
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
