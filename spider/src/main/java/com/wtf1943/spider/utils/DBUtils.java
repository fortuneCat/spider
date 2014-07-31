package com.wtf1943.spider.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DBUtils {
	private MongoClient mongoClient = null;

	private DBUtils() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static DBUtils getInstanse() {
		return new DBUtils();
	}

	private DB getDB(String name) {
		DB db = mongoClient.getDB(name);
		return db;
	}

	public void addDB(String dbname) {

	}

	private DBCollection getCollection(String name, String collectionName) {
		DB db = getDB(name);
		DBCollection coll = db.getCollection(collectionName);
		return coll;
	}

	/**
	 * 根据数据库名 集合名插入数据
	 * 
	 * @param dbname
	 * @param collectionName
	 * @param object
	 */
	public void insert(String dbname, String collectionName, DBObject object) {
		DBCollection coll = getCollection(dbname, collectionName);
		coll.insert(object);
	}

	public void insert(String dbname, String collectionName, String json) {

	}

	public static void main(String[] args) {
		DBCollection coll = DBUtils.getInstanse().getCollection("tao_bao",
				"sell_counter");
		DBCursor cursorDocJSON = coll.find();
		while (cursorDocJSON.hasNext()) {
			System.out.println(cursorDocJSON.next());
		}

	}
}
