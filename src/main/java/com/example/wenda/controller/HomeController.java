package com.example.wenda.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.example.wenda.aspect.LogAspect;
import com.example.wenda.model.Question;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
	public String userIndex(Model model, @PathVariable("userId") int userId) {
		model.addAttribute("vos", getQuestions(userId, 0, 10));
		return "index";
	}
	
	@RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("vos", getQuestions(0, 0, 10));
		return "index";
	}
	
	@RequestMapping(path = {"question/{questionId}"}, method = RequestMethod.GET)
	public String questionIndex(Model model, @PathVariable(value = "questionId") int questionId) {
		List<ViewObject> vos = new ArrayList<ViewObject>();
		Question question = questionService.getQuestionByid(questionId);
		ViewObject vo = new ViewObject();
		vo.set("question", question);
		vo.set("user", userService.getUser(question.getId()));
		vos.add(vo);
		model.addAttribute("vos", vos);
		return "index";
	}
	
	public List<ViewObject> getQuestions(int userId, int offset, int limit){
		List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos = new ArrayList<ViewObject>();
		for(Question question : questionList) {
			ViewObject vo = new ViewObject();
			vo.set("question", question);
			vo.set("user", userService.getUser(question.getId()));
			vos.add(vo);
		}
		return vos;
	}
}
