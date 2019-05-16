package com.example.wenda.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.User;
import com.nowcoder.util.WendaUtil;


@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	
	public User getUser(int id) {
		return userDAO.selectByid(id);
	}
	
	public Map<String, String> register(String username,String password){
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		User user = userDAO.selectByName(username);
		if(user!=null) {
			map.put("msg","用户名已经存在");
			return map;
		}
		user = new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setPassword(WendaUtil.MD5(password+user.getSalt()));
		userDAO.addUser(user);
		return map;
	}
	
	
	public Map<String, String> login(String username,String password){
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		User user = userDAO.selectByName(username);
		if(user == null) {
			map.put("msg","用户名不存在");
			return map;
		}
		if(!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
			map.put("msg","密码错误");
			return map;
		}
		
		
		return map;
	}
}
