package com.maintainapps.modules.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import com.maintainapps.modules.util.StoreHippoVO.SEO;

public class WordpressVO {

	private String post_title = "";

	private String ID = "";

	private String post_excerpt = "";

	private String post_status = "";

	private String post_date = "";

	private String sku = "";

	private String parent_sku = "";

	private String stock = "";

	private String regular_price = "";

	private String sale_price = "";

	private String weight = "";

	private String length = "";

	private String width = "";

	private String height = "";

	private String visibility = "";

	private String stock_status = "";

	private String images = "";

	private String product_page_url = "";
	
	
	private Meta meta;

	private HashMap<String, String> attribute = new HashMap<String, String>();

	private Tax tax;

	/**
	 * @author Mustaq
	 *
	 */

	public class Meta {
		private String source_url = "";

		public String getSource_url() {
			return source_url;
		}

		public void setSource_url(String source_url) {
			this.source_url = source_url;
		}

	}

	public class Tax {
		private String product_cat = "";
		private String product_tag = "";

		public String getProduct_cat() {
			return product_cat;
		}

		public void setProduct_cat(String product_cat) {
			this.product_cat = product_cat;
		}

		public String getProduct_tag() {
			return product_tag;
		}

		public void setProduct_tag(String product_tag) {
			this.product_tag = product_tag;
		}

	}

	public static String[] createHeader(Object obj) {
		ArrayList<String> header = new ArrayList<String>();
		try {

			Field[] fields = obj.getClass().getDeclaredFields();
			Field attribute = obj.getClass().getDeclaredField("attribute");
			Field meta = obj.getClass().getDeclaredField("meta");
			Field tax = obj.getClass().getDeclaredField("tax");
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					if (!(f.getType().isAssignableFrom(HashMap.class) || f.getType().isAssignableFrom(Tax.class)
							|| f.getType().isAssignableFrom(Meta.class))) {
						header.add(f.getName());
					}
				}
			}

			HashMap<String, String> attr = (HashMap<String, String>) attribute.get(obj);

			for (String key : attr.keySet()) {
				header.add("attributes." + key);
			}

			Object object2 = tax.get(obj);
			Field[] taxes = object2.getClass().getDeclaredFields();
			for (Field f : taxes) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					String h = f.getName();
					// h = h.replace("_", " ");
					if (h.indexOf("$") == -1)
						header.add("tax." + h);
				}
			}
			Object object3 = meta.get(obj);
			Field[] metas = object3.getClass().getDeclaredFields();
			for (Field f : metas) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					String h = f.getName();
					// h = h.replace("_", " ");
					if (h.indexOf("$") == -1)
						header.add("meta." + h);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return header.toArray(new String[header.size()]);
	}

	public static void main(String[] args) {
		WordpressVO vo = new WordpressVO();
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("Color", "Blue");
		
		Tax tax = vo.new Tax();
		tax.setProduct_cat("Blouse");
		Meta m = vo.new Meta();
		m.setSource_url("https://amazon.com");
		
		vo.setAttribute(attributes);
		vo.setMeta(m);
		vo.setTax(tax);

		System.out.println(String.join(",", createHeader(vo)));
		System.out.println(String.join(",",createRecord(vo)));
	}

	public static String[] createRecord(Object obj) {
		ArrayList<String> valueRecord = new ArrayList<String>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Field attribute = obj.getClass().getDeclaredField("attribute");
			Field tax = obj.getClass().getDeclaredField("tax");
			Field meta = obj.getClass().getDeclaredField("meta");
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

			
			HashMap<String, String> attr = (HashMap<String, String>) attribute.get(obj);

			for (String key : attr.keySet()) {
				valueRecord.add("" + attr.get(key));
			}
			
			

			Object object2 = tax.get(obj);
			Field[] taxes = object2.getClass().getDeclaredFields();
			for (Field f : taxes) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					try {
						String value = (String) f.get(object2);
						if (value == null)
							value = "";
						valueRecord.add("" + value);
					} catch (Exception e) {

					}
				}
			}
			
			Object object3 = meta.get(obj);
			Field[] metas = object3.getClass().getDeclaredFields();
			for (Field f : metas) {
				if (!Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					try {
						String value = (String) f.get(object3);
						if (value == null)
							value = "";
						valueRecord.add("" + value);
					} catch (Exception e) {

					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return valueRecord.toArray(new String[valueRecord.size()]);
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

	public String getPost_status() {
		return post_status;
	}

	public void setPost_status(String post_status) {
		this.post_status = post_status;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getParent_sku() {
		return parent_sku;
	}

	public void setParent_sku(String parent_sku) {
		this.parent_sku = parent_sku;
	}

	
	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getRegular_price() {
		return regular_price;
	}

	public void setRegular_price(String regular_price) {
		this.regular_price = regular_price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getStock_status() {
		return stock_status;
	}

	public void setStock_status(String stock_status) {
		this.stock_status = stock_status;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	
	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public HashMap<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(HashMap<String, String> attribute) {
		this.attribute = attribute;
	}

	public String getProduct_page_url() {
		return product_page_url;
	}

	public void setProduct_page_url(String product_page_url) {
		this.product_page_url = product_page_url;
	}
	
	

}
