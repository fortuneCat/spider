package spider;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.wtf1943.spider.utils.DBUtils;
import com.wtf1943.spider.utils.Strings;

/**这部分我们直接通过GithubRepoPageProcessor这个例子来介绍PageProcessor的编写方式。
 * 我将PageProcessor的定制分为三个部分，分别是爬虫的配置、页面元素的抽取和链接的发现。
 * @author daniel
 *
 */
public class GithubRepoPageProcessor implements PageProcessor {
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
    	 // 部分二：定义如何抽取页面信息，并保存下来
    	String context = page.getHtml().toString();
    	context = Strings.getStringNoBlank(context);
    	if(context.startsWith("$callback(")){
    		String jsonStr = context.substring(context.indexOf("(")+1, context.indexOf(")"));
    		DBObject object = (DBObject) JSON.parse(jsonStr);
    		System.out.println(getSite().toString());
    		object = (DBObject) JSON.parse(object.get("quantity").toString()) ;
    		//DBUtils.getInstanse().insert("tao_bao", "sell_counter", object);
    	}else{
    		String sell_counter = context.substring(context.indexOf("\"apiItemInfo\":\"")+15, context.indexOf("coupon:{\"couponApi"));
    		sell_counter = sell_counter.replace("\",", "");
    		//部分三：从页面发现后续的url地址来抓取
    		List<String> links = new ArrayList<String>();
    		links.add(sell_counter);
    		page.addTargetRequests(links);
    	}
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor()).addUrl("http://item.taobao.com/item.htm?id=40182665250").thread(5).run();
    }
    
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