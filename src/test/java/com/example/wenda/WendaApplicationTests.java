package com.example.wenda;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.wenda.model.EntityType;
import com.example.wenda.service.FollowService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WendaApplicationTests {
	@Autowired
	FollowService followService;
	
	@Test
	public void contextLoads() {
		for (int j = 2; j < 12; ++j) {
            followService.follow(12, EntityType.ENTITY_USER, j);
        }
	}

}
