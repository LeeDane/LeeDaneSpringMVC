package com.cn.leedane.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.TimeLineBean;
import com.cn.leedane.service.CircleOfFriendService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/circleOfFriend")
public class CircleOfFriendController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	//朋友圈service
	@Autowired
	private CircleOfFriendService<TimeLineBean> circleOfFriendService;
	
	/**
	 * 获取朋友圈列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(circleOfFriendService.getLimit(json, user, request));
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
