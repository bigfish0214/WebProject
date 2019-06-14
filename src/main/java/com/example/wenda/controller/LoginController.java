package com.example.wenda.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.wenda.async.EventProducer;
import com.example.wenda.service.UserService;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;

@Controller
public class LoginController {
	//使用指定的类XXX初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类。
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	UserService userService;
	@Autowired
	EventProducer eventProducer;
	
	@RequestMapping(path = {"/reg"},method = RequestMethod.POST)
	public String reg(Model model,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "next", required = false) String next,
			@RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
			HttpServletResponse response) {
		try {
			Map<String,Object> map = userService.register(username, password);
			if(map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			}else {
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}
		}catch(Exception e) {
			logger.error("注册异常" + e.getMessage());
			return "login";
		}
		
	}
	
	
	@RequestMapping(path = {"/login"},method = RequestMethod.POST)
	public String login(Model model,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "next", required = false) String next,
			@RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
			HttpServletResponse response) {
		try {
			Map<String,Object> map = userService.login(username, password);
			if(map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                
				
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username", username).setExt("email", "lvhao999@163.com")
                        .setActorId((int)map.get("userId")));
                
                
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			}else {
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}
		}catch(Exception e) {
			logger.error("登录异常" + e.getMessage());
			return "login";
		}
		
	}
	
	
	@RequestMapping(path = {"/logout"},method = RequestMethod.GET)
	public String logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);
		return "redirect:/";
	}
	
	
	@RequestMapping(path = {"/reglogin"},method = RequestMethod.GET)
	public String reg(Model model,@RequestParam(value = "next", required = false) String next) {
		model.addAttribute("next", next);
		return "login";
	}
	
}
