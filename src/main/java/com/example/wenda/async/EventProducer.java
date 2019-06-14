package com.example.wenda.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.util.JedisAdapter;

import com.example.wenda.util.RedisKeyUtil;

@Service
public class EventProducer {
	@Autowired
	JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel eventModel) {
        try {
        	
//        	BlockingQueue<EventModel> q = new ArrayBlockingQueue<EventModel>(10);
        	
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            
            jedisAdapter.lpush(key, json);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
