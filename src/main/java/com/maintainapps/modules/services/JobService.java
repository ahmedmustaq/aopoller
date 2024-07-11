package com.maintainapps.modules.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.maintainapps.modules.util.CommandVO;
import com.maintainapps.modules.util.UrlVO;

@RestController
@RequestMapping(path = "/crawler")

public class JobService {
	@Autowired
	RestTemplate restTemplate;

	@Value("${nutchhost}")
	private String apiHost;
	
	@Value("${selenium.path}")
	private String selenium;
	
	@Value("${server.servlet.context-path}")
	private String context;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAAGGREGATOR')")
	// getBookVo
	@GetMapping(value = { "/config/{config}", "/config" })
	@Produces("application/json")
	public ResponseEntity<Object> config(HttpServletRequest request, @PathVariable(required = false) String config) {
		String url = apiHost + request.getRequestURI().replace("/crawler", "");
		Object userStr = restTemplate.getForEntity(url, Object.class).getBody();
		return ResponseEntity.status(HttpStatus.OK).body(userStr);
	}

	@PostMapping("/childurls")
	public ResponseEntity<Object> childurl(@RequestBody(required = true) UrlVO data,@QueryParam(value = "html") boolean html) {
		ArrayList<String> urls = new ArrayList<>();
		Document doc;
		WebDriver driver = null;
		for (String url : data.getUrl()) {
			try {
				
				if (data.getJsmode() == 1) {
					System.out.println("Connecting to ......" + selenium);
					
					DesiredCapabilities capability = DesiredCapabilities.chrome();
					driver = new RemoteWebDriver(new URL(selenium), capability);
					driver.get(url);
					if(data.getWaitsec() > 0)
					{
						WebDriverWait wait = new WebDriverWait(driver, data.getWaitsec());
						if(data.getVisibilitySelector() != null && data.getVisibilitySelector().length() > 0)
						{
							WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(data.getVisibilitySelector())));
							
						}
					}
					
					//System.out.println(driver.getPageSource());
					
					doc = Jsoup.parse(driver.findElement(By.tagName("body")).getAttribute("innerHTML"));
				} else {
					doc = Jsoup.connect(url).get();
				}
				
				
			

				Elements imports = doc.select("a[href]");

				for (Element element : imports) {
					String link = element.absUrl("href").trim();
					if(link.length() == 0)
					{
						link = element.attr("href").trim();
					}
					if (link.matches(data.getPattern())) {
						
						if(data.getRemovequery() == 1)
						{
							link = getUrlWithoutParameters(link);
						}
						if(data.getRemovepath() > 0)
						{
							link = getUrlWithoutPath(link,data.getRemovepath());
						}
						urls.add(link);	
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (driver != null)
					driver.quit();
			}

		}

		String[] stockArr = new String[urls.size()];
		String[] unique = Arrays.stream(urls.toArray(stockArr)).distinct().toArray(String[]::new);
		data.setChildurls(unique);

		if(!html)
		{
			Document responseDoc = Jsoup.parse("<html></html>");
			responseDoc.body().addClass("body-styles-cls");
			responseDoc.body().appendElement("div");
			for(int i =0;i<unique.length-1;i++)
			{
				Element a = new Element("a");
				a.text(i++ + "links");
				a.attr("href", unique[i]);
				responseDoc.body().appendChild(a);
			
			}
			
			//System.out.println(responseDoc.toString());
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
	
	
	private String getUrlWithoutParameters(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    return new URI(uri.getScheme(),
	                   uri.getAuthority(),
	                   uri.getPath(),
	                   null, // Ignore the query part of the input url
	                   null).toString();
	}
	
	
	private String getUrlWithoutPath(String url,int pathparam) throws URISyntaxException {
	    	
		String splits[] = url.split("/");
		
		List<String> strList = new ArrayList(Arrays.asList(splits));
		if(pathparam < splits.length)
		{
			strList.remove(pathparam);
		}
		String joinedString = String.join("/", strList);
		return joinedString;
	}

	@GetMapping("/job")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAAGGREGATOR')")
	public ResponseEntity<Object> listJobs(HttpServletRequest request, @QueryParam(value = "crawlId") String id) {

		String url = apiHost + request.getRequestURI().replace(context+ "/crawler", "");
		Map<String, String> params = new HashMap<String, String>();
		// params.put("id", "1");
		if (id != null) {
			params.put("crawlId", id);
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(restTemplate.getForEntity(url, Object.class, params).getBody());
	}

	@GetMapping(value = { "/generic/{command}", "/generic/{command}/{anyurl}" })
	public ResponseEntity<Object> generic(HttpServletRequest request) {

		String url = apiHost + request.getRequestURI().replace(context+"/crawler/generic", "");
		Map<String, String> params = new HashMap<String, String>();
		return ResponseEntity.status(HttpStatus.OK)
				.body(restTemplate.getForEntity(url, Object.class, params).getBody());
	}

	@GetMapping("/admin/stop")
	public ResponseEntity<Object> stop(HttpServletRequest request,@QueryParam("force") String force) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String url = apiHost + request.getRequestURI().replace(context+"/crawler", "");
		params.put("force", force);
		String response = restTemplate.getForEntity(url, String.class, params).getBody();
		CommandVO command = new CommandVO();
		command.setMessage(response);
		System.out.println(response);
		return ResponseEntity.status(HttpStatus.OK).body(command);
	}

	@PostMapping("/seed/create")

	public ResponseEntity<Object> setSeed(HttpServletRequest request, @RequestBody(required = true) CommandVO data)
			throws Exception {

		String url = apiHost + request.getRequestURI().replace(context+"/crawler", "");

		String seedfile = postToServer(url, data);

		data.setSeedfile(seedfile);

		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

	@PostMapping("/job/create")

	public ResponseEntity<Object> inject(HttpServletRequest request, @RequestBody(required = true) CommandVO data)
			throws Exception {

		String url = apiHost + request.getRequestURI().replace(context+"/crawler", "");

		String response = postToServer(url, data);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private String postToServer(String url, Object data) throws IOException {
		String response = "Failure";
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);

			String json = new Gson().toJson(data);
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			// httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			httpPost.setEntity(entity);
			response = EntityUtils.toString(client.execute(httpPost).getEntity(), "UTF-8");
			System.out.println(">>>>>" + response + ">>>>>");
		} catch (Exception e) {

		} finally {
			client.close();
		}

		return response;

	}
	
	
	

}
