package com.maintainapps.modules.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import com.maintainapps.modules.util.WordpressVO.Meta;
import com.maintainapps.modules.util.WordpressVO.Tax;

public class AvailOffersVO {
	
	private String rehub_offer_disclaimer = "";
	
	private String rehub_offer_name = "";
	
	private String rehub_offer_btn_text = "Avail Offers";
	
	private String rehub_offer_product_desc = "";
	
	private String rehub_offer_product_price = "";
	
	private String rehub_offer_product_price_old = "";
	
	private String rehub_offer_product_thumb = "";
	
	private String rehub_offer_product_url = "";
	
	private String textattributes;
	
	private String post_title = "";

	private String ID = "";

	private String post_excerpt = "";
	
	private String post_category = "";
	
	private String images = "";

	public String getRehub_offer_disclaimer() {
		return rehub_offer_disclaimer;
	}

	public void setRehub_offer_disclaimer(String rehub_offer_disclaimer) {
		this.rehub_offer_disclaimer = rehub_offer_disclaimer;
	}

	public String getRehub_offer_name() {
		return rehub_offer_name;
	}

	public void setRehub_offer_name(String rehub_offer_name) {
		this.rehub_offer_name = rehub_offer_name;
	}

	public String getRehub_offer_btn_text() {
		return rehub_offer_btn_text;
	}

	public void setRehub_offer_btn_text(String rehub_offer_btn_text) {
		this.rehub_offer_btn_text = rehub_offer_btn_text;
	}

	public String getRehub_offer_product_desc() {
		return rehub_offer_product_desc;
	}

	public void setRehub_offer_product_desc(String rehub_offer_product_desc) {
		this.rehub_offer_product_desc = rehub_offer_product_desc;
	}

	public String getRehub_offer_product_price() {
		return rehub_offer_product_price;
	}

	public void setRehub_offer_product_price(String rehub_offer_product_price) {
		this.rehub_offer_product_price = rehub_offer_product_price;
	}

	public String getRehub_offer_product_price_old() {
		return rehub_offer_product_price_old;
	}

	public void setRehub_offer_product_price_old(String rehub_offer_product_price_old) {
		this.rehub_offer_product_price_old = rehub_offer_product_price_old;
	}

	public String getRehub_offer_product_thumb() {
		return rehub_offer_product_thumb;
	}

	public void setRehub_offer_product_thumb(String rehub_offer_product_thumb) {
		this.rehub_offer_product_thumb = rehub_offer_product_thumb;
	}

	public String getRehub_offer_product_url() {
		return rehub_offer_product_url;
	}

	public void setRehub_offer_product_url(String rehub_offer_product_url) {
		this.rehub_offer_product_url = rehub_offer_product_url;
	}

	public String getTextattributes() {
		return textattributes;
	}

	public void setTextattributes(String textattributes) {
		this.textattributes = textattributes;
	}

	
	
	public String getPost_title() {
		return post_title;
	}

	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPost_excerpt() {
		return post_excerpt;
	}

	public void setPost_excerpt(String post_excerpt) {
		this.post_excerpt = post_excerpt;
	}

	public static String[] createHeader(Object obj) {
		ArrayList<String> header = new ArrayList<String>();
		try {

			Field[] fields = obj.getClass().getDeclaredFields();
			
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					if (!(f.getType().isAssignableFrom(HashMap.class) || f.getType().isAssignableFrom(Tax.class)
							|| f.getType().isAssignableFrom(Meta.class))) {
						header.add(f.getName());
					}
				}
			}
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return header.toArray(new String[header.size()]);
	}

	

	public static String[] createRecord(Object obj) {
		ArrayList<String> valueRecord = new ArrayList<String>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					if (!(f.getType().isAssignableFrom(HashMap.class) || f.getType().isAssignableFrom(Tax.class)
							|| f.getType().isAssignableFrom(Meta.class))) {
						Object value = f.get(obj);
						if (value == null)
							value = "";
						valueRecord.add("" + value);

					}
				}
			}

	
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return valueRecord.toArray(new String[valueRecord.size()]);
	}

	public String getPost_category() {
		return post_category;
	}

	public void setPost_category(String post_category) {
		this.post_category = post_category;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	
	

}
