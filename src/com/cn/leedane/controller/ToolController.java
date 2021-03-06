package com.cn.leedane.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.EmailUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.wechat.util.HttpRequestUtil;

@Controller
@RequestMapping("/leedane/tool")
public class ToolController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	@Autowired
	private UserService<UserBean> userService;
	
	/**
	 * 翻译
	 * @return
	 */
	@RequestMapping("/fanyi")
	public String fanyi(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			String content = JsonUtil.getStringValue(json, "content");
			String msg = HttpRequestUtil.sendAndRecieveFromYoudao(content);
			msg = StringUtil.getYoudaoFanyiContent(msg);
			message.put("isSuccess", true);
			message.put("message", msg);
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
	 * 发送邮件通知的接口
	 * @return
	 */
	@RequestMapping("/sendEmail")
	public String sendEmail(HttpServletRequest request, HttpServletResponse response){
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			String toUserId = JsonUtil.getStringValue(json, "to_user_id");//接收邮件的用户的Id，必须
			String content = JsonUtil.getStringValue(json, "content"); //邮件的内容，必须
			String object = JsonUtil.getStringValue(json, "object"); //邮件的标题，必须
			if(StringUtil.isNull(toUserId) || StringUtil.isNull(content) || StringUtil.isNull(object)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.参数不存在或为空.value));
				message.put("responseCode", EnumUtil.ResponseCode.参数不存在或为空.value);
				printWriter();
				return null;
			}
			
			UserBean toUser = userService.findById(StringUtil.changeObjectToInt(toUserId));
			if(toUser == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该用户不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.该用户不存在.value);
				printWriter();
				return null;
			}
			if(StringUtil.isNull(toUser.getEmail())){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value));
				message.put("responseCode", EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value);
				printWriter();
				return null;
			}
			
			//String content = "用户："+user.getAccount() +"已经添加您为好友，请您尽快处理，谢谢！";
			//String object = "LeeDane好友添加请求确认";
			Set<UserBean> set = new HashSet<UserBean>();		
			set.add(toUser);	
			
			EmailUtil emailUtil = EmailUtil.getInstance(null, set, content, object);
			try {
				emailUtil.sendMore();
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送成功.value));
			} catch (Exception e) {
				e.printStackTrace();
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value)+",失败原因是："+e.toString());
				message.put("responseCode", EnumUtil.ResponseCode.邮件发送失败.value);
			}
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
