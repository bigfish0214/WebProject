package com.example.wenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.model.Question;

@Service
public class QuestionService {
	@Autowired
	private QuestionDAO questionDAO;
	
	public List<Question> getLatestQuestions(int userId,int offset,int limit){
		return questionDAO.selectLatestQuestions(userId, offset, limit);
	}
	
	public Question getQuestionByid(int id) {
		return questionDAO.selectQuestionByid(id);
	}
}
