package com.example.wenda;


import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import com.example.wenda.service.FollowService;
import com.example.wenda.model.EntityType;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
	QuestionDAO questionDAO;
	@Autowired
	FollowService followService;
	
	@Test
	public void initDatabase() {
		Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            
            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }

            
            user.setPassword("xx");
            userDAO.updatePassword(user);
            
            
            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            
            questionDAO.addQuestion(question);
            
        }
        
	}
}
