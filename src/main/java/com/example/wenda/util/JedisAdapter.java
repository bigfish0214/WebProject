package com.example.wenda.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.controller.CommentController;
import com.example.wenda.model.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

@Service
public class JedisAdapter implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
	
	private JedisPool pool;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		pool = new JedisPool("redis://localhost:6379/10");
	}
	
	public long sadd(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sadd(key, value);
		} catch(Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}
	
	public long srem(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.srem(key, value);
		} catch(Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}
	
	public long scard(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.scard(key);
		} catch(Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}
	
	public boolean sismember(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sismember(key, value);
		} catch(Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return false;
	}
	
	
	public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
	
    public Jedis getJedis() {
        return pool.getResource();
    }
    
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            tx.discard();
        } finally {
            if (tx != null) {
                
                tx.close();
                
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    
    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    
	public static void print(int index,Object obj) {
		System.out.println(String.format("%d, %s", index, obj.toString()));
	}
	
	public static void main(String[] args){
		Jedis jedis = new Jedis("redis://localhost:6379/9");
		jedis.flushDB();
		
		//get set
		jedis.set("hello", "world");
		print(1, jedis.get("hello"));
		
		jedis.rename("hello", "newhello");
		print(1, jedis.get("newhello"));
		
		jedis.setex("hello2", 15, "world");
		
		
		jedis.set("pv", "100");
		jedis.incr("pv");
		jedis.incrBy("pv", 5);
		jedis.decrBy("pv", 3);
		print(2, jedis.get("pv"));
		
		
		print(3,jedis.keys("*"));
		
		
		String listName = "list";
		jedis.del(listName);
		for(int i=0; i<10; ++i) {
			jedis.lpush(listName, "a" + String.valueOf(i));
		}
		
		print(4, jedis.lrange(listName, 2, 6));
		print(5, jedis.llen(listName));
		print(6, jedis.lpop(listName));
		print(7, jedis.llen(listName));
		print(7, jedis.lindex(listName, 3));
		print(8, jedis.linsert(listName, ListPosition.AFTER, "a4", "xx"));
		print(8, jedis.linsert(listName, ListPosition.BEFORE, "a4", "xx"));
		print(8, jedis.lrange(listName, 0, 12));
		
		
		String userKey = "userxx";
		jedis.hset(userKey, "name", "jim");
		jedis.hset(userKey, "age", "12");
		jedis.hset(userKey, "phone", "18660690829");
		print(12, jedis.hget(userKey, "name"));
		print(13, jedis.hgetAll(userKey));
		
		jedis.hdel(userKey, "phone");
		print(13, jedis.hgetAll(userKey));
		
		print(13, jedis.hexists(userKey, "age"));
		print(13, jedis.hexists(userKey, "email"));
		print(13, jedis.hkeys(userKey));
		print(13, jedis.hvals(userKey));
		
		jedis.hsetnx(userKey, "school", "zju");
		jedis.hsetnx(userKey, "name", "yxy");
		print(13, jedis.hgetAll(userKey));
		
		//set
		String likeKey1 = "commentLike1";
		String likeKey2 = "commentLike2";
		for(int i=0; i<10; i++) {
			jedis.sadd(likeKey1, String.valueOf(i));
			jedis.sadd(likeKey2, String.valueOf(i * i));
		}
		print(14, jedis.smembers(likeKey1));
		print(14, jedis.smembers(likeKey2));
		print(14, jedis.sunion(likeKey1, likeKey2));
		print(14, jedis.sdiff(likeKey1, likeKey2));
		print(14, jedis.sinter(likeKey1, likeKey2));
		print(14, jedis.sismember(likeKey1, "16"));
		print(14, jedis.sismember(likeKey2, "16"));
		jedis.srem(likeKey1, "5");
		print(14, jedis.smembers(likeKey1));
		jedis.smove(likeKey2, likeKey1, "25");
		print(14, jedis.smembers(likeKey1));
		print(14, jedis.scard(likeKey1));
		
		String rankKey = "rankKey";
		jedis.zadd(rankKey, 15, "jim");
		jedis.zadd(rankKey, 60, "Ben");
		jedis.zadd(rankKey, 90, "Lee");
		jedis.zadd(rankKey, 75, "Lucy");
		jedis.zadd(rankKey, 80, "Mei");
		print(30, jedis.zcard(rankKey));
		print(31, jedis.zcount(rankKey, 61, 100));
		print(32, jedis.zscore(rankKey, "Lucy"));
		jedis.zincrby(rankKey, 2, "Lucy");
		print(32, jedis.zscore(rankKey, "Lucy"));
		jedis.zincrby(rankKey, 2, "Luc");
		print(32, jedis.zscore(rankKey, "Luc"));
		print(30, jedis.zrange(rankKey, 0, 100));
		print(36, jedis.zrange(rankKey, 1, 3));
		print(36, jedis.zrevrange(rankKey, 1, 3));
		
		for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
			print(36, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
		}
		
		print(38, jedis.zrank(rankKey, "Ben"));
		print(38, jedis.zrevrank(rankKey, "Ben"));
		
		String setKey = "zset";
		jedis.zadd(setKey, 1, "a");
		jedis.zadd(setKey, 1, "b");
		jedis.zadd(setKey, 1, "c");
		jedis.zadd(setKey, 1, "d");
		jedis.zadd(setKey, 1, "e");
		
		print(40, jedis.zlexcount(setKey, "-", "+"));
		print(40, jedis.zlexcount(setKey, "(b", "[d"));
		print(40, jedis.zlexcount(setKey, "[b", "[d"));
		
		jedis.zrem(setKey, "b");
		print(43, jedis.zrange(setKey, 0, 10));
		jedis.zremrangeByLex(setKey, "(c", "+");
		print(44, jedis.zrange(setKey, 0, 2));
		
//		JedisPool pool = new JedisPool("redis://localhost:6379/9");
//		for(int i=0;i<100;i++) {
//			Jedis j = pool.getResource();
//			print(45, j.get("pv"));
//			j.close();
//		}
		
		User user = new User(); 
		user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        jedis.set("user1", JSONObject.toJSONString(user));
        
        String value = jedis.get("user1");
        User user2 = JSONObject.parseObject(value, User.class);
        print(47, user2);
	}

	
}
