package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Patient;
import model.Provider;


public class ProviderReader {
	
	private static final String DEFAULT_RECORD_PATH = System.getProperty("user.home")+File.separator+"default_record.xml";
	private static String PROVIDER_RECORD_PATH = "";
	
	private List<Provider> providers = new ArrayList<Provider>();
	
	private SAXParser saxParser;
	
	private ProviderHandler providerHandler;
	
	public ProviderReader(){
		try{
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			saxParser = factory.newSAXParser();
			
//			ClassLoader loader = Thread.currentThread().getContextClassLoader();
//			File file = new File(loader.getResource("Providers.xml").getFile());
			File file = new File ("resources/Providers.xml");
			PROVIDER_RECORD_PATH = file.getAbsolutePath();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readProviderRecord() throws SAXException, IOException {
		
		this.providerHandler = new ProviderHandler();
		
//		File file = new File(recordPath);
//		if(!file.exists())
//		{
//			file.createNewFile();
//		}
		
		saxParser.parse(PROVIDER_RECORD_PATH, providerHandler);
		
		this.providers = providerHandler.getProviders();
	}
	
	public List<Provider> getProviders(){
		
		if(providers != null)
			return this.providers;
		else
			throw new NullPointerException("Provider records are null.");
		
	}
	
	private class ProviderHandler extends DefaultHandler{
		private List<Provider> providers = new ArrayList<Provider>();
		
		public List<Provider> getProviders() {
			return providers;
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			try{
				if (qName.equalsIgnoreCase("PROVIDER")){
					
					long id = Long.parseLong(attributes.getValue("id"));
					String name = attributes.getValue("name") == null ? "-1" : attributes.getValue("name");
					String recordFile = attributes.getValue("recordFile") == null ? DEFAULT_RECORD_PATH : attributes.getValue("recordFile");
					
					Provider provider = new Provider(id, name, recordFile);
					
					providers.add(provider);
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
//	public static void main(String[] args){
//		
//	}

}
