package com.wtf1943.spider.utils;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class DBUtils {

	public void connect(){
		String myUserName = "admin";  
		String myPassword = "admin";  
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		  
		// 1.数据库列表  
		for (String s : mongoClient.getDatabaseNames()) {  
		    System.out.println("DatabaseName=" + s);  
		}  
		// 2.链接student数据库  
		DB db = mongoClient.getDB("student");  
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);  
		  
		// 4.集合列表  
		Set<String> colls = db.getCollectionNames();  
		for (String s : colls) {  
		    System.out.println("CollectionName=" + s);  
		}  
		  
		// 5.获取摸个集合对象  
		DBCollection coll = db.getCollection("user");  
	}
	public static void main(String[] args) {
		new DBUtils().connect();
	}
}
