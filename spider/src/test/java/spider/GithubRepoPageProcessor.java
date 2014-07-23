package spider;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
    	String sell_counter =page.getHtml().toString();
    	sell_counter = sell_counter.substring(sell_counter.indexOf("\"apiItemInfo\":\"")+15, sell_counter.indexOf("coupon:{\"couponApi"));
    	sell_counter = sell_counter.replace("\",", "");
    	//部分三：从页面发现后续的url地址来抓取
        //page.addTargetRequests(requests);
    	 List<String> links = getHttpLinks(page.getHtml().toString());
    	 System.out.println(links.size());
    	
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