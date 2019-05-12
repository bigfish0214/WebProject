package com.example.wenda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.User;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	public User getUser(int id) {
		return userDAO.selectByid(id);
	}
}
