package com.example.wenda.service;

import org.springframework.stereotype.Service;

@Service
public class SettingService {
	public String getUserId(int userId) {
		return "hello " + String.valueOf(userId);
	}
}
