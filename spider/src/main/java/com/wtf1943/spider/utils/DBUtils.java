package com.wtf1943.spider.utils;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class DBUtils {
	private MongoClient mongoClient = null;
	
	private DBUtils(){
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	} 
	
	public DB getDB(String name){
		DB db = mongoClient.getDB(name);
		return db;
	}
	
	public DBCollection getCollection(DB db,String collectionName){
		DBCollection coll = db.getCollection(collectionName);
		return coll;
	}
	public void connect(){
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
