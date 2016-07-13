package com.cn.leedane.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService<UserBean> userService;
	
	/**
	 * 获取所有用户列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUser")
	public String getAllUser(HttpServletRequest request){
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String paramsString = request.getParameter("params");
		System.out.println("paramsString:"+paramsString);
		UserBean find = userService.findById(1);
		return "/allUser";
	}
	
	/**
	 * 跳转到添加用户界面
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/toAddUser")
	public String toAddUser(HttpServletRequest request){
		
		return "/addUser";
	}
	*//**
	 * 添加用户并重定向
	 * @param user
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/addUser")
	public String addUser(UserBean user,HttpServletRequest request){
		userService.save(user);
		return "redirect:/user/getAllUser";
	}
	
	*//**
	 *编辑用户
	 * @param user
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/updateUser")
	public String updateUser(UserBean user,HttpServletRequest request){
		
		
		if(userService.update(user)){
			user = userService.findById(user.getId());
			request.setAttribute("user", user);
			return "redirect:/user/getAllUser";
		}else{
			return "/error";
		}
	}
	*//**
	 * 根据id查询单个用户
	 * @param id
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/getUser")
	public String getUser(int id,HttpServletRequest request){
		
		request.setAttribute("user", userService.findById(id));
		return "/editUser";
	}
	*//**
	 * 删除用户
	 * @param id
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("/delUser")
	public void delUser(int id,HttpServletRequest request,HttpServletResponse response){
		String result = "{\"result\":\"error\"}";
		
		if(userService.delete(id)){
			result = "{\"result\":\"success\"}";
		}
		
		response.setContentType("application/json");
		
		try {
			PrintWriter out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
}
