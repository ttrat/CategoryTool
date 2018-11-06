package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.Provider;

public class ProviderWriter {
	
	private Document document = new Document();
	private SAXBuilder builder = new SAXBuilder();
	
	private long max;
	private boolean update = true;
	
	private final String PROVIDER_ELEMENT_NAME = "provider";
	
	
	private static final String DEFAULT_RECORD_PATH = System.getProperty("user.home")+File.separator+"default_record.xml";
	private static String PROVIDER_RECORD_PATH = "";
	
	public ProviderWriter() {
//		ClassLoader loader = Thread.currentThread().getContextClassLoader();
//		File file = new File(loader.getResource("Providers.xml").getFile());
		File file = new File ("resources/Providers.xml");
		PROVIDER_RECORD_PATH = file.getAbsolutePath();
		
		try {
			File input = new File(PROVIDER_RECORD_PATH);
			document = builder.build(input);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addProvider(Provider provider) throws IOException, JDOMException {
		
		max = findMax();
		max += 1;
		
//        File input = new File(PROVIDER_RECORD_PATH);
        
//        document = builder.build(input);
        
        Element element = new Element(PROVIDER_ELEMENT_NAME);
        element.setAttribute("id", Long.toString(max));
        element.setAttribute("name", provider.getName());
        element.setAttribute("recordFile", provider.getRecordFile());
        
        document.getRootElement().addContent(element);
        
        writeToFile(document);
    }
	
	public void removeProvider(long id) throws JDOMException, IOException{
		
		File input = new File(PROVIDER_RECORD_PATH);
		
		document = builder.build(input);
    	
    	List<Element> elements = document.getRootElement().getChildren(PROVIDER_ELEMENT_NAME);
    	
    	 for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
         {
	         Element element = iter.next();
	         if(element.getAttribute("id").getValue().equals(Long.toString(id))){
	        	 iter.remove();
	        	 document.getRootElement().removeContent(element);
	         }
         }
	}
	
	private static void writeToFile(Document document) throws IOException{
		OutputStream out = null;
        
        try{
        	out = new FileOutputStream(PROVIDER_RECORD_PATH);
        	
        	XMLOutputter serializer = new XMLOutputter();
        	serializer.setFormat(Format.getPrettyFormat());
        	serializer.output(document, out);
             
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	out.close();
        }
	}
	
	private long findMax(){
		long max = 0;
		long id;
		
		List<Element> elements = document.getRootElement().getChildren(PROVIDER_ELEMENT_NAME);
		
    	
   	 	for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
        {
	         Element element = iter.next();
	         
	         if(element.getAttribute("id") != null){
	        	 id = Long.parseLong(element.getAttribute("id").getValue());
	         }
	         else{
	        	 update = true;
	        	 id = -1L;
	         }
	         
	         if(id > max){
	        	 max = id;
	         }
        }
   	 	
   	 	return max;
	}
	
}
