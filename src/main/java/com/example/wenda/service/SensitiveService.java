package com.example.wenda.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.example.wenda.controller.IndexController;

@Service
public class SensitiveService implements InitializingBean{
	//使用指定的类XXX初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类。
	private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
	
	private class TrieNode{
		//是不是关键词结尾
		private boolean end = false;
		//当前节点下所有子节点
		private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();
		
		public void addSubNode(Character key, TrieNode node) {
			subNodes.put(key, node);
		}
		
		TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}
		
		boolean isKeyWordEnd() {
			return end;
		}
		
		void setkeywordEnd(boolean end) {
			this.end = end;
		}
	}
	
	private TrieNode rootNode = new TrieNode();

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
			InputStreamReader read = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt;
			while((lineTxt = bufferedReader.readLine()) != null) {
				addWord(lineTxt.trim());
			}
			read.close();
			is.close();
		}catch(Exception e) {
			logger.error("读取敏感词文件失败" + e.getMessage());
		}
	}
	//增加关键词
	private void addWord(String lineTxt) {
		TrieNode tempNode = rootNode;
		for(int i=0;i<lineTxt.length();++i) {
			Character c = lineTxt.charAt(i);
			if(isSymbol(c)) {
				continue;
			}
			TrieNode node = tempNode.getSubNode(c);
			if(node==null) {
				node = new TrieNode();
				tempNode.addSubNode(c, node);
			}
			tempNode = node;
			if(i==lineTxt.length()-1) {
				tempNode.setkeywordEnd(true);
			}
		}
	}
	
	private boolean isSymbol(char c) {
		int ic = (int) c;
		//东亚文字
		return !(CharUtils.isAsciiAlphanumeric(c)) && (ic < 0x2E80 || ic > 0x9FFF);
	}
	
	public String filter(String text) {
		if(StringUtils.isBlank(text)) {
			return text;
		}
		
		StringBuilder result = new StringBuilder();
		
		
		String replacement = "***";
		TrieNode tempNode = rootNode;
		int begin = 0;
		int position = 0;
		
			
		while(position < text.length()) {
			char c = text.charAt(position);
			
			if(isSymbol(c)) {
				if(tempNode == rootNode) {
					result.append(c);
					++begin;
				}
				++position;
				
				if(begin < text.length() && position == text.length()) {
					result.append(text.charAt(begin));
					position = begin + 1;
					begin = position;
					tempNode = rootNode;
				}
				continue;
			}
			
			tempNode = tempNode.getSubNode(c);
			
			if(tempNode == null) {
				result.append(text.charAt(begin));
				position = begin + 1;
				begin = position;
				tempNode = rootNode;
			}else if(tempNode.isKeyWordEnd()){
				//发现敏感词
				result.append(replacement);
				++position;
				begin = position;
				tempNode = rootNode;
			}else {
				++position;
				if(position == text.length()) {
					result.append(text.charAt(begin));
					position = begin + 1;
					begin = position;
					tempNode = rootNode;
				}
			}
		}
			
		
		
		
		return result.toString();
	}

}
