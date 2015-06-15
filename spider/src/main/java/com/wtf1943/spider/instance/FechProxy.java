package com.wtf1943.spider.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.wtf1943.spider.utils.DBUtils;

public class FechProxy implements PageProcessor{
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	
    public Site getSite() {
		return site;
	}

	public void process(Page page) {
		// 部分二：定义如何抽取页面信息，并保存下来
		List<String> links = new ArrayList<String>();
		Selectable context = page.getHtml().xpath("//ul[@class='newslist_line']");
		if(context.match()){
			links = context.links().all();
		}
		if(links.size() > 0){
			page.addTargetRequests(links);
		}else{
			page.putField("childPage", page.getHtml().xpath("//div[@class='cont_font']/p").toString());       
		}
	}
	

	public static void main(String[] args) {
		List<String> links = new ArrayList<String>();
		links.add("http://www.youdaili.net/Daili/http/");
		Spider spider = Spider.create(new FechProxy());
		spider.addPipeline(new ProxyPipeline());
		spider.addUrl("http://www.youdaili.net/Daili/http/").thread(1).run();
    }
	
	static class ProxyPipeline implements Pipeline{
		DBUtils DB = DBUtils.getInstanse();

		public void process(ResultItems resultItems, Task task) {
			String context = resultItems.get("childPage");
			if(context == null){
				return;
			}
			System.out.println(context);
			Pattern p = Pattern.compile("((\\d{1,3}\\.\\d{1,3}.\\d{1,3}\\.\\d{1,3})(:{1}\\d{1,4}))");
			Matcher m = p.matcher(context);
			while(m.find()) {  
			  String temp = m.group();
			  String[] result = temp.split(":");
			  String ip = result[0];
			  String port = result[1];
			  BasicDBObject obj = new BasicDBObject();
			  obj.put("ip", ip);
			  obj.put("port", port);
			  DBCollection proxy = DB.getCollection("proxy", "proxy");
			  if(proxy.find(obj).hasNext()){
				  continue;
			  }
			  DB.insert("proxy", "proxy",obj);
			} 
			
		}
		
	}
}
