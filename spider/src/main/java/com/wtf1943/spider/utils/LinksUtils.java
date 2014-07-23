package com.wtf1943.spider.utils;

import java.util.ArrayList;
import java.util.List;

public class LinksUtils {
	 
	/**获取字符串中的链接
	 * @param str
	 * @return
	 */
	public static List<String> getHttpLinks(String str){
	    	List<String> links = new ArrayList<String>();
	    	String[] tempList = str.split("\"");
	    	for(int i=0;i<tempList.length;i++){
	    		if(tempList[i].startsWith("http://")){
	    			links.add(tempList[i]);
	    		}
	    	}
	    	return links;
	    }
}
