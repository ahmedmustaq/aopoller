package com.maintainapps.modules.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class StoreHippoVO {

	private String name;
	
	private String alias;
	
	private String description;
	
	private String price;
	
	private String collections;
	
	private String brand;
	
	private String sku;
	
	private String categories;
	
	private int publish;
	
	private String weight;
	
	private String isbn;
	
	private String seller = "admin@availoffers.com";
	
	private String feature1;
	
	private String feature2;
	
	private String feature3;
	
	private String feature4;
	
	private String feature5;
	
	private String image1;
	
	private Attribute attribute;
	 /**
	 * @author Mustaq
	 *
	 */
	public class Attribute{
		private String Authors = "";
		private String ISBN = "";
		private String No_of_pages = "";
		private String Published_Date = "";
		private String Publisher = "";
		private String Format = "Paperback";
		private String Language = "English";
		public String getAuthors() {
			return Authors;
		}
		public void setAuthors(String authors) {
			Authors = authors;
		}
		public String getISBN() {
			return ISBN;
		}
		public void setISBN(String iSBN) {
			ISBN = iSBN;
		}
		public String getNo_of_pages() {
			return No_of_pages;
		}
		public void setNo_of_pages(String no_of_pages) {
			No_of_pages = no_of_pages;
		}
		public String getPublished_Date() {
			return Published_Date;
		}
		public void setPublished_Date(String published_Date) {
			Published_Date = published_Date;
		}
		public String getPublisher() {
			return Publisher;
		}
		public void setPublisher(String publisher) {
			Publisher = publisher;
		}
		public String getFormat() {
			return Format;
		}
		public void setFormat(String format) {
			Format = format;
		}
		public String getLanguage() {
			return Language;
		}
		public void setLanguage(String language) {
			Language = language;
		}
		
		
	}
	
	private SEO seo;
	public class SEO{
		private String title = "";
		private String description = "";
		private String keywords = "";
		private String canonical_url = "";
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		public String getCanonical_url() {
			return canonical_url;
		}
		public void setCanonical_url(String canonical_url) {
			this.canonical_url = canonical_url;
		}
		
		
	}
	
	
	
	
	public static String[] createHeader(Object obj) {
		 ArrayList<String> header = new ArrayList<String>();
		try {
	   
	    Field[] fields = obj.getClass().getDeclaredFields();
	    Field attribute = obj.getClass().getDeclaredField("attribute");
	    Field seo = obj.getClass().getDeclaredField("seo");
	    for (Field f : fields) {
	      if (!Modifier.isStatic(f.getModifiers())) {
	        f.setAccessible(true);
	        if(!(f.getType().isAssignableFrom(Attribute.class) || f.getType().isAssignableFrom(SEO.class)))
	        {
		        header.add(f.getName());
	        }
	      }
	    }
	    
	    Object object1 = attribute.get(obj);
	    Field[] attributes = object1.getClass().getDeclaredFields();
	    for (Field f : attributes) {
		      if (!Modifier.isStatic(f.getModifiers())) {
		        f.setAccessible(true);
		        String h = f.getName();
		        h = h.replace("_", " ");
		        if(h.indexOf("$") == -1)
		        header.add("attributes."+h);
		      }
		    }
	    
		
		 Object object2 = seo.get(obj);
		 Field[] taxes = object2.getClass().getDeclaredFields();
		    for (Field f : taxes) {
			      if (!Modifier.isStatic(f.getModifiers())) {
			        f.setAccessible(true);
			        String h = f.getName();
			        //h = h.replace("_", " ");
			        if(h.indexOf("$") == -1)
			        header.add("seo."+h);
			      }
			    }
		    
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    return header.toArray(new String[header.size()]);
	  }


	public static void main(String[] args) {
		StoreHippoVO vo = new StoreHippoVO();
		Attribute a =  vo.new Attribute();
		a.setAuthors("Hai");
		SEO s =  vo.new SEO();
		s.setTitle("Hai");
		vo.setAttribute(a);
		vo.setSeo(s);
		System.out.println(String.join(",",createHeader(vo)));
		System.out.println(String.join(",",createRecord(vo)));
	}
	
	public static String[] createRecord(Object obj){
	    ArrayList<String> valueRecord = new ArrayList<String>();
	    try {
	    Field[] fields = obj.getClass().getDeclaredFields();
	    Field attribute = obj.getClass().getDeclaredField("attribute");
	    Field seo = obj.getClass().getDeclaredField("seo");
	    for (Field f : fields) {
	      if (!Modifier.isStatic(f.getModifiers())) {
	        f.setAccessible(true);
	        if(!(f.getType().isAssignableFrom(Attribute.class) || f.getType().isAssignableFrom(SEO.class)))
	        {
	        Object value = f.get(obj);
	        if(value == null)
	        value = "";
	        valueRecord.add("" + value);
	        
	        }
	      }
	    }
	    
	    Object object1 = attribute.get(obj);
	    Field[] attributes = object1.getClass().getDeclaredFields();
	    for (Field f : attributes) {
		      if (!Modifier.isStatic(f.getModifiers())) {
		        f.setAccessible(true);
		        try {
		        String value = (String) f.get(object1);
		        if(value == null)
			        value = "";
		        valueRecord.add("" + value);
		        }
		        catch(Exception e)
		        {
		        	
		        }
		      }
		    }
	    
	    Object object2 = seo.get(obj);
	    Field[] taxes = object2.getClass().getDeclaredFields();
	    for (Field f : taxes) {
		      if (!Modifier.isStatic(f.getModifiers())) {
		        f.setAccessible(true);
		        try {
			        String value = (String) f.get(object2);
			        if(value == null)
				        value = "";
			        valueRecord.add("" + value);
		        }
		        catch(Exception e)
		        {
		        	
		        }
		      }
		    }
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    return valueRecord.toArray(new String[valueRecord.size()]);
	  }

	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getAlias() {
		return alias;
	}




	public void setAlias(String alias) {
		this.alias = alias;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public String getPrice() {
		return price;
	}




	public void setPrice(String price) {
		this.price = price;
	}




	public String getCollections() {
		return collections;
	}




	public void setCollections(String collections) {
		this.collections = collections;
	}




	public String getBrand() {
		return brand;
	}




	public void setBrand(String brand) {
		this.brand = brand;
	}




	public String getSku() {
		return sku;
	}




	public void setSku(String sku) {
		this.sku = sku;
	}




	public String getCategories() {
		return categories;
	}




	public void setCategories(String categories) {
		this.categories = categories;
	}




	public int getPublish() {
		return publish;
	}




	public void setPublish(int publish) {
		this.publish = publish;
	}




	public String getWeight() {
		return weight;
	}




	public void setWeight(String weight) {
		this.weight = weight;
	}




	public String getIsbn() {
		return isbn;
	}




	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}




	public String getSeller() {
		return seller;
	}




	public void setSeller(String seller) {
		this.seller = seller;
	}




	public String getFeature1() {
		return feature1;
	}




	public void setFeature1(String feature1) {
		this.feature1 = feature1;
	}




	public String getFeature2() {
		return feature2;
	}




	public void setFeature2(String feature2) {
		this.feature2 = feature2;
	}




	public String getFeature3() {
		return feature3;
	}




	public void setFeature3(String feature3) {
		this.feature3 = feature3;
	}




	public String getFeature4() {
		return feature4;
	}




	public void setFeature4(String feature4) {
		this.feature4 = feature4;
	}




	public String getFeature5() {
		return feature5;
	}




	public void setFeature5(String feature5) {
		this.feature5 = feature5;
	}




	public String getImage1() {
		return image1;
	}




	public void setImage1(String image1) {
		this.image1 = image1;
	}




	public Attribute getAttribute() {
		return attribute;
	}




	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}


	public SEO getSeo() {
		return seo;
	}


	public void setSeo(SEO seo) {
		this.seo = seo;
	}
	
	
}
