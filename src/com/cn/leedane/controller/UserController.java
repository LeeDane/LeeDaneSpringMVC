package com.cn.leedane.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.enums.LoginType;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.WechatHandler;
import com.cn.leedane.model.FriendBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.MD5Util;
import com.cn.leedane.utils.SpringUtil;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.wechat.bean.WeixinCacheBean;
import com.cn.leedane.wechat.util.WeixinUtil;
import com.opensymphony.xwork2.ActionContext;

@Controller
@RequestMapping("/leedane/user")
public class UserController extends BaseController{

	@Autowired
	private UserService<UserBean> userService;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private WechatHandler wechatHandler;
	
	//好友信息
	@Autowired
	private FriendService<FriendBean> friendService; 
	
	// 操作日志
	@Autowired
	protected OperateLogService<OperateLogBean> operateLogService;
	
	@Autowired
	private SystemCache systemCache;
	
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			//执行密码等信息的验证
			user = userService.loginUser(
					String.valueOf(json.get("account")),String.valueOf(json.get("password")));

			if (user != null) {
			
				//校验权限和角色
				if(checkRole(user) && checkPemission(user)){
					//登录成功后加载权限和角色信息缓存中
					
					if(user != null){
						if(user.getStatus() == 4 ){
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户已经注销.value);
							message.put("responseCode", EnumUtil.ResponseCode.用户已经注销.value);
						}else if(user.getStatus() == 0){
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先验证邮箱.value);
							message.put("responseCode", EnumUtil.ResponseCode.请先验证邮箱.value);
						}
						else {
							
							this.resIsSuccess = true;
							//判断是android平台的登录
							if(json.has("login_mothod") && LoginType.LOGIN_TYPE_ANDROID.getValue().equals(json.get("login_mothod"))){
								
								String noLoginCode = JsonUtil.getStringValue(json, "no_login_code");
								//检查免登陆码是否存在
								//不存在
								if(StringUtil.isNull(noLoginCode)){
									noLoginCode = StringUtil.getNoLoginCode(user.getAccount());
									//upDateNoLoginCode(user, noLoginCode);	
									//更新免登陆验证码
									user.setNoLoginCode(noLoginCode);
									userService.update(user);
								}else{
									//存在的话检查验证码的有效性，失效的验证码就更新验证码
								}
							}else{
								// 登录成功后将必要信息加载到session
								putInSessionAfterLoginSuccess();
							}
							
							message.put("userinfo", userHandler.getUserInfo(user, true));
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.恭喜您登录成功.value);
							message.put("responseCode", EnumUtil.ResponseCode.恭喜您登录成功.value);
						}
					}else{
						resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value);
						message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
					}
				}else{	
					resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value);
					message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				}
				
				// 保存用户登录日志信息
				String subject = user != null ? user.getAccount()+"登录系统": "账号"+json.get("account")+"登录系统失败";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "账号登录", resIsSuccess? 1 : 0, 0);
			}else{
				resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value);
				message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
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
	
	/**
	 * 根据用户id获取当个用户的id
	 * @return
	 */
	@RequestMapping("/searchUserByUserId")
	public String searchUserByUserId(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			//获取查找的用户id
			int searchUserId = JsonUtil.getIntValue(json, "searchUserId");
			//执行密码等信息的验证
			UserBean searchUser = userService.findById(searchUserId);

			if (searchUser != null) {			
				
				// 保存操作日志信息
				String subject = user.getAccount() + "查看" + searchUser.getAccount() + "个人基本信息";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "searchUserByUserId", 1, 0);
				message.put("userinfo", userHandler.getUserInfo(searchUser, false));
				message.put("isSuccess", true);
				printWriter();
				return null;
			}else{
				resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
				message.put("responseCode", EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
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
	 * 登录成功后系统的缓存数据
	 */
	private void putInSessionAfterLoginSuccess() {
		//先把session全部清空
		/*session.clear();
		//加载全部的好友信息ID和备注信息进入session中
		List<Map<String, Object>> friends = friendService.getFromToFriends(user.getId());
		session.put(ConstantsUtil.MY_FRIENDS, friends);
		session.put(ConstantsUtil.USER_ACCOUNT_SESSION, user.getAccount());
		session.put(ConstantsUtil.USER_SESSION, user);*/
	}
	
	/**
	 * 注册用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/registerUser")
	public String registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		//判断是否有在线的用户，那就先取消该用户的session
		/*if(ActionContext.getContext().getSession().get(ConstantsUtil.USER_SESSION) != null) {
			removeMultSession(ConstantsUtil.USER_SESSION);
		}*/
		try{	
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			user = new UserBean();
			user.setAccount(JsonUtil.getStringValue(json, "account"));
			user.setEmail(JsonUtil.getStringValue(json, "email"));
			user.setPassword(MD5Util.compute(JsonUtil.getStringValue(json, "password")));
			Date registerTime = new Date();
			user.setRegisterTime(registerTime);
			user.setRegisterCode(StringUtil.produceRegisterCode(DateUtil.DateToString(registerTime, "YYYYMMDDHHmmss"),
					JsonUtil.getStringValue(json, "account")));
			message = userService.saveUser(user);
			systemCache = (SystemCache) SpringUtil.getBean("systemCache");
			String adminId = (String) systemCache.getCache("admin-id");
			int aid = 1;
			if(!StringUtil.isNull(adminId)){
				aid = Integer.parseInt(adminId);
			}
			UserBean opearteUser = userService.findById(aid);
			//保存操作日志
			this.operateLogService.saveOperateLog(opearteUser, request, null, user.getAccount()+"注册成功", "register", 1, 0);
			printWriter();
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
	}
	
	/**
	 * 完成注册
	 * @return
	 */
	@RequestMapping("/completeRegister")
	public String completeRegister(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			if (this.userService.updateCheckRegisterCode(JsonUtil.getStringValue(json, "registerCode")))
				return "completeRegisterSuccess";
			else
				return "completeRegisterFailure";
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter();
		return null;
		
	}
	
	/**
	 * 再次发送邮箱验证信息
	 */
	@RequestMapping("/againSendRegisterEmail")
	public String againSendRegisterEmail(HttpServletRequest request, HttpServletResponse response) {
		try {	
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			//根据账号和密码找到该用户(密码需要再进行MD5加密)
			user = userService.loginUser(JsonUtil.getStringValue(json, "account"), JsonUtil.getStringValue(json, "password"));
			this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"请求发送邮箱", "againSendRegisterEmail", resIsSuccess? 1 : 0, 0);
			if(user != null && user.getStatus() == 2){
				//生成注册码
				String newRegisterCode = StringUtil.produceRegisterCode(DateUtil.DateToString(new Date(),"YYYYMMDDHHmmss"),
						user.getAccount());
				user.setRegisterCode(newRegisterCode);
				boolean isUpdate = userService.update(user);
					if(isUpdate)
						//发送邮件
						userService.sendEmail(user);
					message.put("isSuccess", true);
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件已发送成功.value));
					message.put("responseCode", EnumUtil.ResponseCode.邮件已发送成功.value);
					printWriter();
					return null;
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.不是未注册状态邮箱不能发注册码.value));
				message.put("responseCode", EnumUtil.ResponseCode.不是未注册状态邮箱不能发注册码.value);
				printWriter();
				return null;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value));
		message.put("responseCode", EnumUtil.ResponseCode.邮件发送失败.value);
		printWriter();
		return null;
	}
	
	/**
	 * 找回密码
	 * @return
	 */
	@RequestMapping("/findPassword")
	public String findPassword(HttpServletRequest request, HttpServletResponse response){
		try {	
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			//获得找回密码的类型(0:邮箱,1:手机)
			if(JsonUtil.getIntValue(json, "type") == 0){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.暂时不支持手机找回密码功能.value));
				message.put("responseCode", EnumUtil.ResponseCode.暂时不支持手机找回密码功能.value);
			}else if(JsonUtil.getIntValue(json, "type") == 1){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.暂时不支持邮箱找回密码功能.value));
				message.put("responseCode", EnumUtil.ResponseCode.暂时不支持邮箱找回密码功能.value);
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.未知的找回密码类型.value));
				message.put("responseCode", EnumUtil.ResponseCode.未知的找回密码类型.value);
			}
			
			printWriter();
			return null;
			//this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"寻找密码", "findPassword", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value));
		message.put("responseCode", EnumUtil.ResponseCode.邮件发送失败.value);
		printWriter();
		return null;
	}
	
	/**
	 * 退出系统
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		
		//判断是否有在线的用户，那就先取消该用户的session
		if(session.get(ConstantsUtil.USER_SESSION) != null) {
			user = (UserBean) ActionContext.getContext().getSession().get("user");
			try {
				this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"退出系统", "logout", resIsSuccess? 1 : 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.clear();
			//removeSession(ConstantsUtil.USER_SESSION);
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.注销成功.value));
		message.put("responseCode", EnumUtil.ResponseCode.注销成功.value);
		printWriter();
		return null;
	}
	
	/**
	 * 根据用户的id获取用户的base64位图像信息
	 * {"uid":2, "size":"30x30"} "order":0默认是0, tablename:"t_user"
	 * @return
	 */
	@RequestMapping("/getHeadBase64StrById")
	public String getHeadBase64StrById(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			resmessage = userService.getHeadBase64StrById(json, user, request);
			resIsSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		printWriter();
		return null;
	}
	
	/**
	 * 获取用户的头像路径
	 * @return
	 */
	@RequestMapping("/getHeadPath")
	public String getHeadPath(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			String picSize = JsonUtil.getStringValue(json, "picSize", "30x30");
			message.put("message", userHandler.getUserPicPath(user.getId(), picSize));
			message.put("isSuccess", true);
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
	 * 用户上传个人的头像
	 * {"base64":"hhdjshuffnfbnfds"}
	 * @return
	 */
	@RequestMapping("/uploadHeadBase64Str")
	public String uploadHeadBase64Str(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			resIsSuccess = userService.uploadHeadBase64StrById(json, user, request);
			message.put("isSuccess", resIsSuccess);
			operateLogService.saveOperateLog(user, request, null, user.getAccount()+"上传头像" + StringUtil.getSuccessOrNoStr(resIsSuccess), "uploadHeadBase64Str", ConstantsUtil.STATUS_NORMAL, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter();
		return null;
	}
	
	/**
	 * 取得所有用户
	 * @return
	 */
	@RequestMapping("/getAllUsers")
	public String getAllUsers(HttpServletRequest request, HttpServletResponse response){
		String page = request.getParameter("page");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String sort = "";
		sort = request.getParameter("sort");
		String adminId = (String) systemCache.getCache("admin-id");
		int aid = 1;
		if(!StringUtil.isNull(adminId)){
			aid = Integer.parseInt(adminId);
		}
		UserBean user = userService.findById(aid);
		
		if(user == null){
			this.resmessage = "请先登录";
			message.put("isSuccess", false);
			message.put("resmessage", resmessage);
			printWriter();
			return null;
		}
		
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		
		//要是有一个为空，说明只加载
		if(StringUtil.isNull(page) || StringUtil.isNull(start) || StringUtil.isNull(limit)){
			
		}else{
			//int p = Integer.parseInt(page);
			int s = Integer.parseInt(start);
			int l = Integer.parseInt(limit);
			if(!StringUtil.isNull(sort)){
				JSONArray ja = JSONArray.fromObject(sort);
				if(ja != null){
					sort = "order by " + ja.getJSONObject(0).getString("property") + " " + ja.getJSONObject(0).getString("direction") + " ";
				}
			}
			sort = sort == null || sort.equals("") ? " " : sort + " ";
			int total = userService.total(DataTableType.用户.value, "id", " where status=1 ");
			ls = userService.find4MoreUser(sort + "limit ?,?", s, l);
			buildGetAllUserResp(ls,total);
		}
		
		try {
			this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"查看所有用户", "getAllUsers", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的年龄
	 * @return
	 */
	@RequestMapping("/statisticsUserAge")
	public String statisticsUserAge(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserAge();
				
		message.put("xaxis", "统计所有用户的年龄"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //年龄段人数最多的数字
		message.put("data", ls);
		
		try {
			this.operateLogService.saveOperateLog(user, request, null, "统计所有用户的年龄", "getAllUsers", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的年龄段
	 * @return
	 */
	@RequestMapping("/statisticsUserAgeRang")
	public String statisticsUserAgeRang(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserAgeRang();
				
		message.put("xaxis", "统计所有用户的年龄"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的注册时间的年份
	 * @return
	 */
	@RequestMapping("/statisticsUserRegisterByYear")
	public String statisticsUserRegisterByYear(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByYear();	
		message.put("xaxis", "统计所有用户的注册时间的年份"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的注册时间的月份
	 * @return
	 */
	@RequestMapping("/statisticsUserRegisterByMonth")
	public String statisticsUserRegisterByMonth(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByMonth();
				
		message.put("xaxis", "统计所有用户的注册时间的月份"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的最近一个月的注册人数
	 * @return
	 */
	@RequestMapping("/statisticsUserRegisterByNearMonth")
	public String statisticsUserRegisterByNearMonth(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByNearMonth();
				
		message.put("xaxis", "统计所有用户的最近一个月的注册人数"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		printWriter();
		return null;
	}
	
	/**
	 * 统计所有用户的最近一周的注册人数
	 * @return
	 */
	@RequestMapping("/statisticsUserRegisterByNearWeek")
	public String statisticsUserRegisterByNearWeek(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByNearWeek();
				
		message.put("xaxis", "统计所有用户的最近一周的注册人数"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		printWriter();
		return null;
	}
	
	/**
	 * 构建获得全部用户返回响应的数据
	 */
	private void buildGetAllUserResp(List<Map<String, Object>> ls, int total) {
		message.put("rows", ls);
		message.put("total", total);
		
	}
	
	/**
	 * 获得y轴中人数最多的数量
	 * @param ls
	 * @return
	 */
	private int getMaximum(List<Map<String, Object>> ls) {
		int maximum = 0;
		if(ls == null || ls.size() == 0)
			return maximum;
		for(Map<String, Object> m: ls){
			int i = StringUtil.changeObjectToInt(m.get("yaxis"));
			if(i > maximum){
				maximum = i;
			}
		}
		
		return maximum;
	}
	
	/**
	 * 获取手机注册的验证码
	 * @return
	 */
	@RequestMapping("/getPhoneRegisterCode")
	public String getPhoneRegisterCode(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.getPhoneRegisterCode(json, request));
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
	 * 获取手机登录的验证码
	 * @return
	 */
	@RequestMapping("/getPhoneLoginCode")
	public String getPhoneLoginCode(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.getPhoneLoginCode(json, request));
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
	 * 通过手机注册
	 * @return
	 */
	@RequestMapping("/registerByPhone")
	public String registerByPhone(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			user = userService.registerByPhone(json, request);
			if(user == null){
				resmessage = "用户不存在或者参数不正确";
			}else{
				if(user.getStatus() == 4 ){
					resmessage = "用户已经被注销,有疑问请联系客服";
				}else if(user.getStatus() == 0){
					resmessage = "请先登录邮箱完成注册...";
				}else{
					message.put("userinfo", userHandler.getUserInfo(user, true));
					resmessage = "登录成功，正在为您跳转...";
					this.resIsSuccess = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		printWriter();
		return null;
	}
	
	/**
	 * 通过手机注册(为了测试需要提供的接口)
	 * @return
	 */
	@RequestMapping("/registerByPhoneNoValidate")
	public String registerByPhoneNoValidate(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			
			message.putAll(userService.registerByPhoneNoValidate(json, request));
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
	 * 通过手机登录
	 * @return
	 */
	@RequestMapping("/loginByPhone")
	public String loginByPhone(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			user = userService.loginByPhone(json, request);
			if(user == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户不存在或请求参数不对.value));
				message.put("responseCode", EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
			}else{
				if(user.getStatus() == 4 ){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户已经注销.value));
					message.put("responseCode", EnumUtil.ResponseCode.用户已经注销.value);
				}else if(user.getStatus() == 0){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先验证邮箱.value));
					message.put("responseCode", EnumUtil.ResponseCode.请先验证邮箱.value);
				}else{
					message.put("userinfo", userHandler.getUserInfo(user, true));
					message.put("isSuccess", true);
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.恭喜您登录成功.value));
					message.put("responseCode", EnumUtil.ResponseCode.恭喜您登录成功.value);
					
				}
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
	
	/**
	 * 检查账号是否已经存在
	 * @return
	 */
	@RequestMapping("/checkAccount")
	public String checkAccount(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.checkAccount(json, request, user));
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
	 * 绑定微信账号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/bingWechat")
	public String bingWechat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			String FromUserName = JsonUtil.getStringValue(json, "FromUserName");
			String account = JsonUtil.getStringValue(json, "account");
			String password = JsonUtil.getStringValue(json, "password");
			if(StringUtil.isNull(FromUserName) || StringUtil.isNull(account) || StringUtil.isNull(password)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
				message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
				printWriter();
				return null;
			}
			
			//执行绑定
			user = userService.bindByWeChat(FromUserName, account, password);
			if (user != null) {
				WeixinCacheBean cacheBean = new WeixinCacheBean();
				String currentType = JsonUtil.getStringValue(json, "currentType", WeixinUtil.MODEL_MAIN_MENU);
				cacheBean.setBindLogin(true);
				cacheBean.setCurrentType(currentType);
				cacheBean.setLastBlogId(0);
				
				wechatHandler.addCache(FromUserName, cacheBean);
				
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作成功.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作成功.value);
				message.put("isSuccess", true);
				
				// 保存用户绑定日志信息
				String subject = user.getAccount()+"绑定账号微信账号"+ FromUserName +"成功";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "bingWechat", 1, 0);
				printWriter();
				return null;
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value));
				message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
				printWriter();
				return null;
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
	 * 获取用户的基本数据(评论数，转发数，积分)
	 * @return
	 */
	@RequestMapping("/getUserInfoData")
	public String getUserInfoData(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.getUserInfoData(json, user, request));
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
	 * 更新用户的基本信息
	 * @return
	 */
	@RequestMapping("/updateUserBase")
	public String updateUserBase(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.updateUserBase(json, user, request));
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
	 * 更新登录密码
	 * @return
	 */
	@RequestMapping("/updatePassword")
	public String updatePassword(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
			}
			message.putAll(userService.updatePassword(json, user, request));
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
	
	/*@RequestMapping("/aaa")
	public String getAllUser(HttpServletRequest request, HttpServletResponse response){
		try {
			if(!checkParams(request, response)){
				printWriter();
				return null;
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
	}*/
	
	/**
	 * 检查用户的权限
	 * @param user
	 * @return
	 */
	private boolean checkPemission(UserBean user) {
		return true;
	}
	
	/**
	 * 检查用户角色
	 * @param user
	 * @return
	 */
	private boolean checkRole(UserBean user) {
		return true;
	}
}
