package com.example.wenda.controller;


import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.example.wenda.model.Question;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.model.HostHolder;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.FollowService;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.User;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private QuestionService questionService;
	@Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
	
	@RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
	public String userIndex(Model model, @PathVariable("userId") int userId) {
		model.addAttribute("vos", getQuestions(userId, 0, 10));
		User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
		return "index";
	}
	
	@RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("vos", getQuestions(0, 0, 10));
		return "index";
	}
	
	public List<ViewObject> getQuestions(int userId, int offset, int limit){
		List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos = new ArrayList<ViewObject>();
		for(Question question : questionList) {
			ViewObject vo = new ViewObject();
			vo.set("question", question);
			vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
			vo.set("user", userService.getUser(question.getUserId()));
			vos.add(vo);
		}
		return vos;
	}
}
