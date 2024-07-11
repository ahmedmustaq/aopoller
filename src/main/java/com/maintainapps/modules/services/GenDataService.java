package com.maintainapps.modules.services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.maintainapps.module.schema.Config;
import com.maintainapps.module.schema.DocumentType;
import com.maintainapps.module.schema.ExtractToType;
import com.maintainapps.modules.util.AvailOffersVO;
import com.maintainapps.modules.util.HostVO;
import com.maintainapps.modules.util.WordpressVO;
import com.maintainapps.modules.util.WordpressVO.Meta;
import com.maintainapps.modules.util.WordpressVO.Tax;
import com.opencsv.CSVWriter;

@RestController
@RequestMapping(path = "/gendata")
public class GenDataService {

	String dateformat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	@Value("${solrhost}")
	private String apiHost;
	
	@Value("${pluginpath}")
	private  String pluginpath;

	private static Config config = null;
	
	
	
	@PostConstruct
	public void init() {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			config = (Config) jaxbUnmarshaller.unmarshal(new File(pluginpath));
			System.out.println("Config file loaded with " + config.getDocuments().getDocument().size() + " parsers.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAAGGREGATOR')")
	@GetMapping(value = { "/host" })
	@Produces("application/json")
	public ResponseEntity<Object> host() throws SolrServerException, IOException {

		final SolrClient client = getSolrClient();

		final Map<String, String> queryParamMap = new HashMap<String, String>();
		queryParamMap.put("q", "parse_title:[* TO *]");
		queryParamMap.put("fl", "id");
		queryParamMap.put("group", "true");
		queryParamMap.put("group.ngroups", "true");
		queryParamMap.put("group.field", "host");
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("mycore", queryParams);
		final GroupResponse groupResponse = response.getGroupResponse();
		ArrayList<HostVO> responsehost = new ArrayList<HostVO>();
		for (GroupCommand groupCommand : groupResponse.getValues()) {
			for (Group group : groupCommand.getValues()) { //This loop traverses through each group
				responsehost.add(new HostVO(group.getGroupValue(),group.getResult().getNumFound()));
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(responsehost);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DATAAGGREGATOR')")
	@GetMapping(value = { "/search" })

	public void search(@RequestParam Map<String, String> queryMap, HttpServletResponse responseHttp)
			throws SolrServerException, IOException {

		String timestamp = "";
		final SolrClient client = getSolrClient();
		SolrQuery query = new SolrQuery("*:*");

		query.setFields("id", "tstamp", "attributekeys", "attributes", "parse_title", "host", "oldprice",
				"currentprice", "desc", "category", "url", "thumbnailimages", "mainimages");
		if (queryMap.get("fromdate") != null) {
			String s = new SimpleDateFormat(dateformat).format(Long.parseLong(queryMap.get("fromdate")));
			String s1 = new SimpleDateFormat(dateformat).format(Long.parseLong(queryMap.get("todate")));
			timestamp = "tstamp:[" + s + " TO " + s1 + "]";
			query.addFilterQuery(timestamp);
		}

		if (queryMap.get("host") != null) {
			query.addFilterQuery("host:" + queryMap.get("host"));

		}
		
		if (queryMap.get("noofrecs") != null) {
			query.setRows(Integer.parseInt(queryMap.get("noofrecs")));

		}
		
		if (queryMap.get("start") != null) {
			query.setStart(Integer.parseInt(queryMap.get("start")));

		}
		
		
		query.addFilterQuery("parse_title:[* TO *]");

		
		
		
		responseHttp.setContentType("text/csv;charset=UTF-8");

		CSVWriter writer = new CSVWriter(responseHttp.getWriter());

		System.out.println(query);
		final QueryResponse response = client.query("mycore", query);

		final SolrDocumentList documents = response.getResults();
		
		if(queryMap.get("formatExport").equals("wordpress"))
		{
			populateWordpressProduct(writer, documents, queryMap);
		}
		else if(queryMap.get("formatExport").equals("availoffers"))
		{
			populateAvailOfferPost(writer, documents, queryMap);
		}
	

	}
	
	private void populateWordpressProduct(CSVWriter writer,SolrDocumentList documents,Map<String, String> queryMap) throws IOException
	{
		WordpressVO parent = new WordpressVO();
		Tax tax = parent.new Tax();
		Meta meta = parent.new Meta();
		HashMap<String, String> attributeList = new HashMap<String, String>();
		for (SolrDocument solrDocument : documents) {
			if (solrDocument.containsKey("attributekeys")) {
				Collection<String> keylist = sanitize(solrDocument.getFieldValues("attributekeys"));
				
				
				if (keylist != null) {
					for (String obj : keylist) {
						attributeList.put(obj, "");
					}
				}
			}

		}

		parent.setAttribute(attributeList);
		parent.setTax(tax);
		parent.setMeta(meta);
		writer.writeNext(WordpressVO.createHeader(parent));

		for (SolrDocument solrDocument : documents) {
			WordpressVO record = new WordpressVO();
			attributeList.replaceAll((k, v) -> "");
			tax = parent.new Tax();
			meta = parent.new Meta();

			if (solrDocument.containsKey("attributekeys")) {
				Object[] keylist = sanitize(solrDocument.getFieldValues("attributekeys")).toArray();
				Collection<Object> attr = solrDocument.getFieldValues("attributes");
				int j = 0;
				try {
					if (keylist != null) {
						for (Object obj : attr) {
							String value = (String) obj;
							String key = keylist[j].toString();
							String repvalue = "";
							if(value.indexOf(":") > 0)
							{	
								repvalue = value.replaceAll(key, "");
								repvalue = repvalue.replaceAll(":", "");
							}
							else
							{
								repvalue = value.replaceAll(key, "");
							}
							attributeList.put(key, sanitizeAttribute(repvalue));
							j++;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// ignore possible
				}
			}

			record.setPost_title(getValue(solrDocument, "parse_title"));
			record.setRegular_price(getCurrency(solrDocument, "oldprice"));
			record.setSale_price(getCurrency(solrDocument, "currentprice"));
			record.setPost_excerpt(getValue(solrDocument, "desc"));
			if(queryMap.get("category") != null && queryMap.get("category").length() > 0)
			{
				tax.setProduct_cat(queryMap.get("category"));
			}
			else {
				tax.setProduct_cat(getCategory(solrDocument, "category"));
			}
			meta.setSource_url(getValue(solrDocument, "url"));
			record.setImages(getImageValue(solrDocument, "thumbnailimages", "mainimages", queryMap.get("pattern")));

			record.setTax(tax);
			record.setMeta(meta);
			record.setAttribute(attributeList);
			String[] recordStr = WordpressVO.createRecord(record);
			//System.out.println(String.join(",", recordStr));
			writer.writeNext(recordStr);

		}

		writer.close();
	}
	
	
	private void populateAvailOfferPost(CSVWriter writer,SolrDocumentList documents,Map<String, String> queryMap) throws IOException
	{
		AvailOffersVO header = new AvailOffersVO();

		writer.writeNext(AvailOffersVO.createHeader(header));

		for (SolrDocument solrDocument : documents) {
			AvailOffersVO record = new AvailOffersVO();
		

			record.setPost_title(getValue(solrDocument, "parse_title"));
			record.setRehub_offer_name(getValue(solrDocument, "parse_title"));
			record.setRehub_offer_product_price_old(getCurrency(solrDocument, "oldprice"));
			record.setRehub_offer_product_price(getCurrency(solrDocument, "currentprice"));
			record.setPost_excerpt(getValue(solrDocument, "desc"));
			record.setRehub_offer_product_desc(getValue(solrDocument, "desc"));
			if(solrDocument.containsKey("attributes"))
			{
				Object[] keylist = solrDocument.getFieldValues("attributes").toArray();
				record.setTextattributes(String.join("\n",Arrays.copyOf(keylist, keylist.length, String[].class)));
			}
			if(queryMap.get("category") != null && queryMap.get("category").length() > 0)
			{
				record.setPost_category(queryMap.get("category"));
			}
			else {
				record.setPost_category(getCategory(solrDocument, "category"));
			}
			//meta.setSource_url(getValue(solrDocument, "url"));
			record.setRehub_offer_product_url(getValue(solrDocument, "url"));
			record.setImages(getImageValue(solrDocument, "thumbnailimages", "mainimages", queryMap.get("pattern")));
			
			
			String[] recordStr = AvailOffersVO.createRecord(record);
			//System.out.println(String.join(",", recordStr));
			writer.writeNext(recordStr);

		}

		writer.close();
	}

	private Collection<String> sanitize(Collection<Object> keylist) {
		
		ArrayList<String> keys = new ArrayList<String>();
		for (Object object : keylist) {
			String formatted = (String) object;
			if(formatted.indexOf(":")>0)
			{
				String str = formatted.substring(0,formatted.indexOf(":"));
				keys.add(sanitizeAttribute(str));
			}
			else	
			{
				keys.add(sanitizeAttribute(formatted));
			}
			
		}
		return keys;
	}

	private HttpSolrClient getSolrClient() {
		return new HttpSolrClient.Builder(apiHost).withConnectionTimeout(10000).withSocketTimeout(60000).build();
	}

	private String getValue(SolrDocument doc, String key) {
		if (doc.containsKey(key)) {
			String str;
			try {
				str = new String(doc.getFieldValue(key).toString().getBytes(),"UTF-8");
				return str;
			} catch (UnsupportedEncodingException e) {
				
			}
			
		}
		return "";
	}

	private String getCurrency(SolrDocument doc, String key) {
		if (doc.containsKey(key)) {
			
			String str = (String) doc.getFieldValue(key);
			str = str.replaceAll("[^\\d.]", "");
			return str;
		}

		return "";
	}

	private String getCategory(SolrDocument doc, String key) {
		ArrayList<String> categories = new ArrayList<String>();
		if (doc.containsKey(key)) {
			Collection<Object> cats = doc.getFieldValues(key);

			for (Object object : cats) {

				String catString = (String) object;
				categories.add(catString);

			}

		}

		return String.join(" > ", categories);
	}

	private String getImageValue(SolrDocument doc, String thumbkey, String mainkey, String customImagePattern) {
		ArrayList<String> images = new ArrayList<String>();
		if (doc.containsKey(thumbkey)) {
			String pattern = getImageRegularExpression(doc.getFieldValue("host").toString());
			if (pattern.length() > 0) {
				Collection<Object> imagescol = doc.getFieldValues(thumbkey);

				for (Object object : imagescol) {
					String imgUrl = (String) object;
					if (customImagePattern != null)
						imgUrl = imgUrl.replaceAll(pattern, customImagePattern);
					imgUrl = sanitize(imgUrl);

					images.add(imgUrl);
				}
			} else {
				Collection<Object> imagescol = doc.getFieldValues(mainkey);

				for (Object object : imagescol) {
					String imgUrl = (String) object;
					imgUrl = sanitize(imgUrl);
					images.add(imgUrl);
				}
			}
		}

		return String.join("|", images);

	}

	public static String sanitize(String text) {

		if (text != null && text.length() > 0) {
			UrlDetector parser = new UrlDetector(text, UrlDetectorOptions.Default);
			List<Url> found = parser.detect();
			String imgUrl = found.get(0).getFullUrl();
			if(imgUrl.indexOf(")")>0)
			{
				imgUrl = imgUrl.substring(0,imgUrl.lastIndexOf(")"));
				text = imgUrl;
			}
			
		}
		return text;
	}
	
	public String sanitizeAttribute(String str)
	{
		
		//str = str.replaceAll("^[a-zA-Z0-9]*$", "");
	    // remove all non-ASCII characters
		str = str.replaceAll("[^\\x00-\\x7F]", "");


		str = str.trim();
		
		return str;
	}

	private String getImageRegularExpression(String host) {

		List<DocumentType> docs = config.getDocuments().getDocument();
		for (DocumentType documentType : docs) {
			if (documentType.getUrl().indexOf(host) > 0) {
				List<Object> extracts = documentType.getFragmentOrExtractTo();
				for (Object extract : extracts) {
					ExtractToType e = (ExtractToType) extract;
					if (e.getResolve() != null && e.getResolve().getReplace() != null) {
						return e.getResolve().getReplace().getPattern();
					}
				}
			}
		}

		return "";
	}

}
