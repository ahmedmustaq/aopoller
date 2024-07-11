package com.maintainapps.modules.services;


import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXParseException;

import com.maintainapps.module.schema.AttributeType;
import com.maintainapps.module.schema.Config;
import com.maintainapps.module.schema.Config.Documents;
import com.maintainapps.module.schema.Config.Fields;
import com.maintainapps.module.schema.ConstantType;
import com.maintainapps.module.schema.DocumentType;
import com.maintainapps.module.schema.ExprType;
import com.maintainapps.module.schema.ExtractToType;
import com.maintainapps.module.schema.FieldType;
import com.maintainapps.module.schema.ReplaceType;
import com.maintainapps.module.schema.ResolveType;
import com.maintainapps.module.schema.TextType;
import com.maintainapps.modules.util.ParserVO;

@RestController
@RequestMapping(path = "/plugin")

public class GeneratePluginService {
	
	private final String oldprice = "oldprice";
	private final String currentprice = "currentprice";
	private final String mainimages = "mainimages";
	private final String thumbnailimages = "thumbnailimages";

	private final String category = "category";
	private final String availability = "availability";
	private final String parse_title = "parse_title";
	private final String description = "desc";
	private final String attributekeys = "attributekeys";
	private final String attributes = "attributes";
	private final String ratings = "ratings";
	private final String storeid = "storeid";

	@Value("${pluginxsdpath}")
	private  String pluginpath;
	
	private Schema schema;
	
	@PostConstruct
	public void init() {
		try {
			String schemaLang = "http://www.w3.org/2001/XMLSchema";
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
			schema = factory.newSchema(new File(pluginpath));
			System.out.println("Schema loaded...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping(value="/generate",consumes = {MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_XML_VALUE},headers = "Accept=application/xml")
	public ResponseEntity<Object> generate(@RequestBody(required = true) ParserVO request) {
		
		Config config = new Config();
		
		String hosturl_str = "";
		
		Documents documents = new Documents();
		Fields fields = new Fields();
		
		DocumentType doc = new DocumentType();
		doc.setEngine("css");
		try {
			URL url = new URL(request.getUrl());
			hosturl_str = url.getProtocol() + "://" + url.getHost();
			doc.setUrl("^"+hosturl_str);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		if(!isEmpty(request.getTitle()))
		fields.getField().add(addField(doc, parse_title, request.getTitle(), false, true, false,""));
		if(!isEmpty(request.getOldprice()))
		fields.getField().add(addField(doc, oldprice, request.getOldprice(), false, true, false,""));
		if(!isEmpty(request.getCurrentprice()))
		fields.getField().add(addField(doc, currentprice, request.getCurrentprice(), false, true, false,""));
		if(!isEmpty(request.getAvailability()))
		fields.getField().add(addField(doc, availability, request.getAvailability(), false, true, false,""));
		if(!isEmpty(request.getRatings()))
		fields.getField().add(addField(doc, ratings, request.getRatings(), false, true, false,""));
		if(!isEmpty(request.getStoreid()))
		fields.getField().add(addField(doc, storeid, request.getStoreid(), false, true, false,""));
		if(!isEmpty(request.getThumbnails()[0]))
		fields.getField().add(addImageField(doc, thumbnailimages, request.getThumbnails()[0], request.getThumbnails_imagepattern()));

		
		
		if(request.getMainimage_mode() == 0)
		{
			fields.getField().add(addMainImageField(doc, mainimages,request.getThumbnails()[0] ,request.getThumbnails_imagepattern(),request.getImagepattern(),request.getHeightwidthpattern()));
		}
		else {
			fields.getField().add(addImageField(doc, mainimages,request.getMainimages()[0] ,request.getMainimage_imagepattern()));
		}
			
		
		
		
		if(!isEmpty(request.getCategory()))
			fields.getField().add(addField(doc, category, request.getCategory(), true, true, false,""));
		
		if(!isEmpty(request.getDescription()))
			fields.getField().add(addField(doc, description, request.getDescription(), false, true, false,""));
		
		if(!isEmpty(request.getAttributekeys()[0]))
			fields.getField().add(addField(doc, attributekeys, request.getAttributekeys()[0], true, true, false,""));
		
		if(!isEmpty(request.getAttributes()[0]))
			fields.getField().add(addField(doc, attributes, request.getAttributes()[0], true, true, false,""));
		
		

		
		documents.getDocument().add(doc);
		
		config.setDocuments(documents);
		config.setFields(fields);
		
		String response = validate(config);
		if(response.indexOf("Exception") >= 0)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		else
		return ResponseEntity.status(HttpStatus.OK).body(response);	
		
		
		
		
		
	}			
	
	 private  String validate(Config config) 
    {
		 String xmlContentActual = "";
         try {
			//Create JAXB Context
			    JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
			     
			    //Create Marshaller
			    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			   
 
			    //Required formatting??
			    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			   
 
			    //Print XML String to Console
			    StringWriter sw = new StringWriter();
			     
			    //Write XML to StringWriter
			    jaxbMarshaller.marshal(config, sw);
			     
			    //Non verified xml
			    xmlContentActual = sw.toString();
			    
			    jaxbMarshaller.setSchema(schema);
			    
			    //Write XML to StringWriter
			    jaxbMarshaller.marshal(config, sw);
			     
			    //Verify XML Content
			    String xmlContent = sw.toString();
			    return xmlContent;
		}	 catch (MarshalException e) {
			return e.getCause().toString() + "\n" + xmlContentActual;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        return "";
       
    }
	
	
	private FieldType addField(DocumentType doc, String fieldname, String fieldvalue,boolean multi, boolean fieldtext, boolean fieldattribute,String attributevalue)
	{
		FieldType field = new FieldType();
		field.setName(fieldname);
		field.setMulti(multi);
		
		ExtractToType extract = new ExtractToType();
		extract.setField(field);
		if(fieldtext)
		{
			TextType text = new TextType();
			ExprType expression = new ExprType();
			expression.setValue(fieldvalue);
			text.setExpr(expression);
			extract.setText(text);
		}
		
		if(fieldattribute)
		{
			AttributeType attr = new AttributeType();
			ExprType expression = new ExprType();
			expression.setValue(fieldvalue);
			attr.setExpr(expression);
			attr.setName(attributevalue);
			extract.setAttribute(attr);
		}
		
		doc.getFragmentOrExtractTo().add(extract);
		
		return field;
	}
	
	private FieldType addConstantField(DocumentType doc, String fieldname, String fieldvalue)
	{
		FieldType field = new FieldType();
		field.setName(fieldname);
		field.setMulti(false);
		
		ExtractToType extract = new ExtractToType();
		extract.setField(field);
		
		ConstantType text = new ConstantType();
		text.setValue(fieldvalue);
		extract.setConstant(text);
		
		
		doc.getFragmentOrExtractTo().add(extract);
		
		return field;
	}
	
	private FieldType addImageField(DocumentType doc, String fieldname, String fieldvalue,String attributetext)
	{
		FieldType field = new FieldType();
		field.setName(fieldname);
		field.setMulti(true);
		
		ExtractToType extract = new ExtractToType();
		extract.setField(field);
		
		
		ResolveType resolve= new ResolveType();
		AttributeType attr = new AttributeType();
		ExprType expression = new ExprType();
		expression.setValue(fieldvalue);
		attr.setExpr(expression);
		attr.setName(attributetext);
		resolve.setAttribute(attr);	
		extract.setResolve(resolve);
		
		
		doc.getFragmentOrExtractTo().add(extract);
		
		return field;
	}
	
	
	private FieldType addMainImageField(DocumentType doc, String fieldname, String fieldvalue,String attributetext,String imagepattern,String replacepattern)
	{
		FieldType field = new FieldType();
		field.setName(fieldname);
		field.setMulti(true);
		
		ExtractToType extract = new ExtractToType();
		extract.setField(field);
		
		
		ResolveType resolve= new ResolveType();
		ReplaceType replace= new ReplaceType();
		replace.setPattern(imagepattern);
		replace.setSubstitution(replacepattern);
		AttributeType attr = new AttributeType();
		ExprType expression = new ExprType();
		expression.setValue(fieldvalue);
		attr.setExpr(expression);
		attr.setName(attributetext);
		resolve.setReplace(replace);	
		replace.setAttribute(attr);
		extract.setResolve(resolve);
		
		
		doc.getFragmentOrExtractTo().add(extract);
		
		return field;
	}
	

	private boolean isEmpty(String str) {
		return str == null || str.isEmpty() || str.equals("NA");
	}
	
	private boolean isEmpty(String[] str) {
		return str == null || str.length == 0;
	}


}
