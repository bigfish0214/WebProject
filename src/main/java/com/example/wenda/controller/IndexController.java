package com.example.wenda.controller;


import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wenda.model.User;
import com.example.wenda.service.SettingService;

//@Controller
public class IndexController {
	@Autowired
	private SettingService settingService;
	
	@RequestMapping(path= {"/","/index"},method = RequestMethod.GET)
	@ResponseBody
	public String index(HttpSession httpSession) {
		return "Hello " + httpSession.getAttribute("msg") + " " + settingService.getUserId(2);
	}
	@RequestMapping(path= {"/profile/{group}/{userId}"})
	@ResponseBody
	public String profile(@PathVariable("userId") int userId,
			@PathVariable(value = "group") String group,
			@RequestParam(name = "type",defaultValue = "1") int type,
			@RequestParam(name = "key",required = false) String key) {
		return String.format("Profile Page of %s %d k:%s t:%d",group,userId,key,type);
	}
	
	@RequestMapping({"/hello"})
	public String hello(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
		List<String> colors = Arrays.asList(new String[] {"Red","Blue","Green"});
//		List<String> colors = Arrays.asList(new String[] {});
		
		Map<String,String> m = new HashMap<>();
		for(int i=1;i<=4;i++) {
			m.put(String.valueOf(i), String.valueOf(i*i));
		}
		
        model.addAttribute("name", name);
        model.addAttribute("colors", colors);
        model.addAttribute("m", m);
        model.addAttribute("user", new User("zhangsan"));
        return "hello";
    }
	
	
	@RequestMapping(path = {"/request"}, method = RequestMethod.GET)
	@ResponseBody
	public String template(Model model,HttpServletRequest request,
			HttpServletResponse response,
			HttpSession httpSession,
			@CookieValue("JSESSIONID") String cookie) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			sb.append(name + ":" + request.getHeader(name) + "<br>");
		}
		sb.append(request.getMethod() + "<br>");
		sb.append(request.getQueryString() + "<br>");
		sb.append(request.getPathInfo() + "<br>");
		sb.append(request.getRequestURI() + "<br>");
		sb.append(request.getRequestURL() + "<br>");
		response.addHeader("name", "lee");
		response.addCookie(new Cookie("username","jojo"));
        return sb.toString();
    }
	
	
	@RequestMapping(path= {"/redirect/{code}"})
	public RedirectView redirect(@PathVariable("code") int code,HttpSession httpSession) {
		httpSession.setAttribute("msg", "jump from redirect");
		RedirectView red = new RedirectView("/",true);
		if(code==301) {
			red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		}
		return red;
				
	}
	@RequestMapping(path="/admin")
	@ResponseBody
	public String admin(@RequestParam(value = "key",defaultValue = "admin2") String key) throws IllegalAccessException {
		if("admin".equals(key)) {
			return "hello admin";
		}
		throw new IllegalAccessException("参数不对");
	}
	
	@ExceptionHandler
	@ResponseBody
	public String error(Exception e) {
		return "error:" + e.getMessage();
	}
	
}
