package com.maintainapps.modules.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tomcat.util.buf.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maintainapps.modules.util.ParserVO;
import com.maintainapps.modules.util.ResponseVO;

@RestController
@RequestMapping


public class ParserService {


	@Value("${selenium.path}")
	private String selenium;

	
	@Autowired
	ResourceLoader resourceLoader;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAUSER')")
	// getBookVo
	@PostMapping("/parse")
	public ResponseEntity<Object> parseValue(@RequestBody(required = true) ParserVO request) {
		ParserVO response = new ParserVO();
		response.setUrl(request.getUrl());

		if (request.getUrl() != null && request.getUrl().length() > 0) {
			WebDriver driver = null;
			Document document = null;
			try {

				if (request.getJsmode() == 1) {
					System.out.println("Connecting to ......" + selenium);
					DesiredCapabilities capability = DesiredCapabilities.chrome();
					driver = new RemoteWebDriver(new URL(selenium), capability);
					driver.get(request.getUrl());
					document = Jsoup.parse(driver.getPageSource());
				} else {
					document = Jsoup.connect(request.getUrl()).get();
				}

				if (!isEmpty(request.getTitle()))
					response.setTitle(document.select(request.getTitle()).text());

				if (!isEmpty(request.getCategory())) {
					ArrayList<String> temp = new ArrayList<String>();
					for (Element cat : document.select(request.getCategory())) {
						temp.add(cat.text());

					}
					response.setCategory(StringUtils.join(temp));
				}

				Collection<String> img = new ArrayList<String>();
				Collection<String> img_main = new ArrayList<String>();
				Collection<String> description = new ArrayList<String>();
				Collection<String> attributes = new ArrayList<String>();
				Collection<String> attributekeys = new ArrayList<String>();

				if (request.getThumbnails().length > 0) {
					Elements images = document.select(request.getThumbnails()[0]);

					for (Element page : images) {
						String turl = getThumbnailImageUrl(page, request, document);

						img.add(turl);

						if (request.getMainimage_mode() == 0 && !request.getImagepattern().isEmpty()) {

							img_main.add(transformUrl(turl, request.getImagepattern(), request.getHeightwidthpattern()));
							

						}

					}

					String[] stockArr = new String[img.size()];
					response.setThumbnails(img.toArray(stockArr));
					if (request.getMainimage_mode() == 0)
					{
						String[] stockArr1 = new String[img_main.size()];
						response.setMainimages(img_main.toArray(stockArr1));
					}

				}

				if (request.getMainimage_mode() == 1) {
					Elements images = document.select(request.getMainimages()[0]);

					for (Element page : images) {
						String turl = getImageUrl(page, request, document,request.getMainimage_imagepattern());

						img_main.add(turl);

					}

					String[] stockArr1 = new String[img_main.size()];
					response.setMainimages(img_main.toArray(stockArr1));
				}

				if (!isEmpty(request.getCurrentprice()))
					response.setCurrentprice(document.select(request.getCurrentprice()).text());

				if (!isEmpty(request.getOldprice()))
					response.setOldprice(document.select(request.getOldprice()).text());

				if (!isEmpty(request.getDescription())) {
					Elements images = document.select(request.getDescription());

					for (Element page : images) {

						description.add(page.text());
					}

				}
				response.setDescription(StringUtil.join(description, "\n"));

				if (request.getAttributes().length > 0) {
					Elements images = document.select(request.getAttributes()[0]);

					for (Element page : images) {
						attributes.add(page.text());

					}

				}

				if (request.getAttributekeys().length > 0) {
					Elements images = document.select(request.getAttributekeys()[0]);

					for (Element page : images) {
						attributekeys.add(page.text());

					}

				}

				String[] stockArr2 = new String[attributes.size()];
				response.setAttributes(attributes.toArray(stockArr2));

				String[] stockArr3 = new String[attributekeys.size()];
				response.setAttributekeys(attributekeys.toArray(stockArr3));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseVO(HttpStatus.BAD_REQUEST.toString(), "Url Not Passed", 999));
			} finally {
				if (driver != null)
					driver.quit();
			}

		}

		System.out.println(response);

		return ResponseEntity.status(HttpStatus.OK).body(response);

		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
		// ResponseVo(HttpStatus.BAD_REQUEST.toString(),"Not Parsed",999));
	}
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAUSER')")
	@GetMapping("/hello")
	public ResponseEntity<Object> hello() {
		return ResponseEntity.status(HttpStatus.OK).body("Hello");
	}

	private String getBackroundUrl(String bg) {
		String url = "";
		if (bg.indexOf("background-image:url(") > 0)
			url = bg.substring(bg.indexOf("background-image:url(") + 21, bg.indexOf(")"));
		else
			url = bg.substring(bg.indexOf("background-image: url(") + 22, bg.indexOf(")"));
		url = url.replaceAll("\"", "");
		return url;
	}

	private static String transformUrl(String thumbnailUrl, String imagepattern, String pattern) {
		String replacedUrl = "";

		replacedUrl = thumbnailUrl.replaceAll(imagepattern, pattern);
		return replacedUrl;
	}

	private String getImageUrl(Element page, ParserVO request, Document document,String mode) throws MalformedURLException {
		
		
		if (mode.indexOf("style") >= 0) {
			if (!page.attr("style").isEmpty()) {
				return getBackroundUrl(page.attr("style"));

			}
		}
		else if (mode.indexOf("src") >= 0) {
			if (!page.attr("src").isEmpty()) {
				if (request.getImagerelation() == 0) {
					URL url = new URL(document.baseUri());
					return url.getProtocol() + "://" + url.getHost() + "/" + page.attr("src");

				} else {
					return page.attr("src");

				}
			}
		}
		
		else {
			
				if (request.getImagerelation() == 0) {
					URL url = new URL(document.baseUri());
					return url.getProtocol() + "://" + url.getHost() + "/" + page.attr(mode);

				} else {
					return page.attr(mode);

				}
			}
		
		

		return "";
	}

	private String getThumbnailImageUrl(Element page, ParserVO request, Document document)
			throws MalformedURLException {
		
		String mode= request.getThumbnails_imagepattern();
		
		if (mode.equals("style")) {
			if (!page.attr("style").isEmpty()) {
				return getBackroundUrl(page.attr("style"));

			}
		}
		else if (mode.equals("src")) {
			if (!page.attr("src").isEmpty()) {
				if (request.getImagerelation() == 0) {
					URL url = new URL(document.baseUri());
					return url.getProtocol() + "://" + url.getHost() + "/" + page.attr("src");

				} else {
					return page.attr("src");

				}
			}
		}
		
		else {
			
				if (request.getImagerelation() == 0) {
					URL url = new URL(document.baseUri());
					return url.getProtocol() + "://" + url.getHost() + "/" + page.attr(mode);

				} else {
					return page.attr(mode);

				}
			}
		
		

		return "";
	}

	

	private boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

}
