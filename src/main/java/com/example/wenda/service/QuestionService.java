package com.example.wenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.model.Question;

@Service
public class QuestionService {
	@Autowired
	private QuestionDAO questionDAO;
	@Autowired
	private SensitiveService sensitiveService;
	
	public Question getById(int id) {
        return questionDAO.getById(id);
    }
	
	public int addQuestion(Question question) {
		
		//html过滤
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		//敏感词过滤
		question.setContent(sensitiveService.filter(question.getContent()));
		question.setTitle(sensitiveService.filter(question.getTitle()));
		
		return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
	}
	
	public List<Question> getLatestQuestions(int userId,int offset,int limit){
		return questionDAO.selectLatestQuestions(userId, offset, limit);
	}
	
	public Question getQuestionByid(int id) {
		return questionDAO.selectQuestionByid(id);
	}
	
	public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }
	
	public Question selectById(int id) {
		return questionDAO.selectByid(id);
	}
}
