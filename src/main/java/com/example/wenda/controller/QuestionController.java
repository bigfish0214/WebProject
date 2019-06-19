package com.example.wenda.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.wenda.model.Comment;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.LikeService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import com.example.wenda.service.FollowService;
import com.example.wenda.model.User;

@Controller
public class QuestionController {
	//使用指定的类XXX初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类。
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	
	
	@Autowired
	private QuestionService questionService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LikeService likeService;
	@Autowired
    FollowService followService;
	
	
	@RequestMapping(value = "/question/add", method = {RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam(value = "title") String title,
			@RequestParam("content") String content) {
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			question.setCommentCount(0);
			if(hostHolder.getUser() == null) {
				//question.setUserId(WendaUtil.ANONYMOUS_USERID);
				return WendaUtil.getJSONString(999);
			}else {
				question.setUserId(hostHolder.getUser().getId());
			}
			
			if(questionService.addQuestion(question) > 0) {
				return WendaUtil.getJSONString(0);
			}
			
		}catch(Exception e){
			logger.error("增加题目失败" + e.getMessage());
		}
		return WendaUtil.getJSONString(1, "失败");
	}
	
	@RequestMapping(value = "/question/{qid}")
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.selectById(qid);
		model.addAttribute("question", question);
		
		List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> vos = new ArrayList<ViewObject>();
		for(Comment comment : commentList) {
			ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if(hostHolder.getUser() == null) {
            	vo.set("liked", 0);
            } else {
            	vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT, comment.getId()));
            }
            
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
		}
		model.addAttribute("comments", vos);
		
		
		List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

		
		return "detail";
	}
	
}
