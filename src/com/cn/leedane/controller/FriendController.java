package com.cn.leedane.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.FriendBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/friend")
public class FriendController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private FriendService<FriendBean> friendService;
	
	//用户信息
	@Autowired
	private UserService<UserBean> userService;

	// 操作日志
	@Autowired
	protected OperateLogService<OperateLogBean> operateLogService;
	
	/**
	 * 解除好友关系
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.deleteFriends(json, user, request));
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
	 * 发起增加好友
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"id":1, "to_user_id": 2, "add_introduce":"你好,我是XX"}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.addFriend(json, user, request));
			printWriter();
			return null;
		} catch (Exception e) {
			//resmessage = "抱歉，添加好友执行出现异常！请核实提交的信息后重试或者联系管理员";
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 同意增加好友
	 * @return
	 */
	@RequestMapping("/agreeFriend")
	public String agreeFriend(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"relation_id":100}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.addAgree(json, user, request));
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
	 * 判读两人是否是好友
	 * @return
	 */
	@RequestMapping("/isFriend")
	public String isFriend(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			if(json.has("to_user_id")) {
				int to_user_id = json.getInt("to_user_id");			
				UserBean toUser = userService.findById(to_user_id);
				if(toUser!= null){
					message.put("isSuccess", friendService.isFriend(user.getId(), to_user_id));
					
					// 保存操作日志信息
					String subject = user.getAccount()+"判断跟"+toUser.getAccount()+"是否是朋友";
					this.operateLogService.saveOperateLog(user, request, new Date(), subject, "isFriend", 1, 0);
					printWriter();
					return null;
				}	
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 获取已经跟我成为好友关系的分页列表
	 * @return
	 */
	@RequestMapping("/friendsPaging")
	public String friendsPaging(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.friendsAlreadyPaging(json, user, request));
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
	 * 获取还未跟我成为好友关系的用户(我发起对方未答应或者对方发起我还未答应的)
	 * @return
	 */
	@RequestMapping("/friendsNotyetPaging")
	public String friendsNotyetPaging(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.friendsNotyetPaging(json, user, request));
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
	 * 获取全部已经跟我成为好友关系列表
	 * @return
	 */
	@RequestMapping("/friends")
	public String friends(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.friends(json, user, request));
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
	 * 获取我发送的好友请求列表
	 * @return
	 */
	@RequestMapping("/requestPaging")
	public String requestPaging(HttpServletRequest request, HttpServletResponse response) {
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.requestPaging(json, user, request));
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
	 * 获取等待我同意的好友关系列表
	 * @return
	 */
	@RequestMapping("/responsePaging")
	public String responsePaging(HttpServletRequest request, HttpServletResponse response) {
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.responsePaging(json, user, request));
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
	 * 用户本地联系人跟服务器上的好友进行匹配后返回
	 * @return
	 */
	@RequestMapping("/matchContact")
	public String matchContact(HttpServletRequest request, HttpServletResponse response) {
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(friendService.matchContact(json, user, request));
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
