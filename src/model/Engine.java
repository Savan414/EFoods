package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;




public class Engine
{
	static final double HST_RATE = 0.13;
	static final double MIN_SHIPPING_WAIVER = 70;
	static final double SHIPPING_COST = 5;
	static Engine engine = null;
	
	private ItemDAO itemDAO;
	private String xmlPOFolderPath;
	private String xmlPOProcessedFolderPath;
	private int count = 0;
	
	private Engine() throws Exception
	{
		itemDAO = ItemDAO.getInstance();
		initializeFilePath();
		initializeCount();
	}
	public synchronized static Engine getInstance() throws Exception
	{
		try
		{
			if (engine == null)
				engine = new Engine();
			return engine;	
		}
		catch (Exception e)
		{
			throw new Exception ("Issue at instatiating engine", e);
		}
	}
	public List<ItemBean> getItems(String catalog) throws Exception
	{
		try
		{
			return itemDAO.getItems(catalog, null);			
		}
		catch (Exception e)
		{
			throw new Exception ("Fail to get items", e);
		}
	}
	public List<ItemBean> getItems(String catalog, String orderBy) throws Exception
	{
		try
		{
			return itemDAO.getItems(catalog, orderBy);			
		}
		catch (Exception e)
		{
			throw new Exception ("Fail to get items", e);
		}
	}
	
	public List<ItemBean> searchItem(String number) throws Exception
	{
		List<ItemBean> result = new ArrayList<ItemBean>();
		ItemBean item = getItem(number);
		if (item == null)
		{
			throw new Exception("There is no item with number " + number);
		}
		result.add(item);
		//TODO
		return result;
	}
	public ItemBean getItem(String number) throws Exception
	{
		try
		{
			return itemDAO.getItem(number);
		}
		catch (Exception e)
		{
			throw new Exception("Fail to find item of number " + number);
		}
	}
	
	public List<CatalogBean> getCatalogs() throws Exception
	{
		try
		{
			return itemDAO.getCatalogs();			
		}
		catch (Exception e)
		{
			throw new Exception ("Fail to get catalogs",e);
		}
	}
	
	//return the 'existing item' in the stact if found, else return null;
	private ItemBean isExistingItem(List<ItemBean> items, String number)
	{
		ItemBean item = null;
		for(int i = 0; i < items.size() && item == null; i++)
		{
			if (items.get(i).getNumber().equals(number))
				item = items.get(i);
		}
		return item;
	}
	
	private void updateOrderTotalPrice(OrderBean order)
	{
		List<ItemBean> items = order.getItems();
		double total = 0;
		for(ItemBean item: items)
		{
			total = total + (item.getPrice() * item.getQuantity());
		}
		order.setTotal(total);
		order.setHST(total*HST_RATE);

		//set shipping cost
		if (total == 0 || total > MIN_SHIPPING_WAIVER )
			order.setShipping(0);
		else
			order.setShipping(SHIPPING_COST);
		
		order.setGrandTotal(order.getTotal() + order.getHST() + order.getShipping());
	}
	
	public OrderBean orderAddItem(OrderBean order, String number, String qty) throws Exception
	{
		int quantity;
		try
		{
			quantity = Integer.parseInt(qty);
		}
		catch(Exception e)
		{
			throw new Exception("Invalid quantity entries " + qty);
		}
		
		try
		{
			ItemBean item = isExistingItem(order.getItems(), number); 
			if(item != null)
			{
				item.setQuantity(item.getQuantity() + quantity);
			}
			else
			{
				item = this.getItem(number);
				item.setQuantity(quantity);
				order.getItems().add(item);
			}
			updateOrderTotalPrice(order);
			return order;
	
		}
		catch (Exception e)
		{
			throw new Exception("Fail to add "+ qty +" of item number " + number + " into order");
		}
	}
	
	private void orderDelItems(OrderBean order, List<String> toDelete)
	{
		List<ItemBean> items = order.getItems();
		for (String e: toDelete)
		{
			ItemBean item = isExistingItem(items, e);
			if(item !=null)
				items.remove(isExistingItem(order.getItems(), e));
		}
	}
	private void orderUpdateQty (OrderBean order, Map<String, String> itemsQty) throws Exception
	{
		List<ItemBean> items = order.getItems();
		Set<String> toUpdate = itemsQty.keySet();
		for(String itemNo: toUpdate)
		{
			ItemBean item = isExistingItem(items, itemNo);
			if (item!=null)
			{
				int quantity;
				try
				{
					quantity = Integer.parseInt(itemsQty.get(itemNo));
					if (quantity < 0 )
						throw new Exception ();
				}
				catch (Exception e)
				{
					throw new Exception ("Invalid quantity entries!");
				}
				if (quantity == 0)
					items.remove(item);
				item.setQuantity(quantity);
			}
		}
	}
	
	public OrderBean updateOrder(OrderBean order, Map<String, String> itemsQty, List<String> toDelete) throws Exception
	{
		orderDelItems(order, toDelete);
		orderUpdateQty(order, itemsQty);
		updateOrderTotalPrice(order);
		return order;
	}
	
	 public  void jaxbObjectToXML(OrderBean order, String id, String path) throws Exception
	 {
	            //Create JAXB Context
	            JAXBContext jaxbContext = JAXBContext.newInstance(OrderBean.class);
	             
	            //Create Marshaller
	            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	            //Required formatting??
	            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	 
	           //Store XML to File
	            File filePath = new File(path + String.format("%s.xml", id));
	            if (!filePath.exists()) {
	                	filePath.createNewFile();
	            }
            	
           	    //Writes XML file to file-system
	            jaxbMarshaller.marshal(order, filePath);
	  }
	 
	 
	public OrderBean convertFromXMLFileToObject(File file,String user) {
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(OrderBean.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            OrderBean order = (OrderBean) jaxbUnmarshaller.unmarshal(file);
            
           if (order.getAccount() != null && order.getAccount().equals(user))
        	   return order;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        
        return null;
		
	}
	public ArrayList<String> getXMLLinks(String user, String path) {
		ArrayList<String> names = new ArrayList<>();
	    File[] files = new File(path).listFiles();
	    
	    for (File file : files) {
	        if (!file.isDirectory() && file.getName().contains(".xml")) {
	        	
	        	 OrderBean order = convertFromXMLFileToObject(file, user);
	        	 if (order != null) 
	        	 {
	        		 String[] f = file.getName().split(".xml");
	        		 names.add(f[0]);
	        	 }
	        }
	    }
		return names;
	}
	
	
	public String getXmlPOProcessedFolderPath() {
		return xmlPOProcessedFolderPath;
	}
	
	public String getXmlFolderPath() {
		return xmlPOFolderPath;
	}
	
	public int increment() {
		count++;
		return count;
	}
	
	
	
	private void initializeFilePath() {
		
		if (xmlPOFolderPath == null) {
	        xmlPOFolderPath = System.getProperty("user.dir") + "/appData/PO/";
	        File filePath = new File(xmlPOFolderPath);
	        //Create folder if they don't exist
	        if (!filePath.exists()) {
	            try{
	            	if (filePath.getParentFile().exists()) 
	            		filePath.getParentFile().mkdirs();
	            	
	            	filePath.mkdirs();
	            }
	            
	            catch(SecurityException se){
	                //handle it
	            }           
	        }
		}
		
		if (xmlPOProcessedFolderPath == null) {
			xmlPOProcessedFolderPath = System.getProperty("user.dir") + "/appData/PO_processed/";
			File filePath = new File(xmlPOProcessedFolderPath);
			if (!filePath.exists()) 
					filePath.mkdirs();
			
		}
   }
	
	private void initializeCount() {	
		
	    File[] files = new File(xmlPOFolderPath).listFiles();
	    
	    for (File file : files) {
	        if (!file.isDirectory() && file.getName().contains(".xml")) {
	        		 String[] f = file.getName().split(".xml");
	        		 int num = Integer.parseInt(f[0].split("_")[1]);
	        		 if (num > count)
	        			 count = num;
	        }
	    }
	    
	    files = new File(xmlPOProcessedFolderPath).listFiles();
	    for (File file : files) {
	        if (!file.isDirectory() && file.getName().contains(".xml")) {
	        		 String[] f = file.getName().split(".xml");
	        		 int num = Integer.parseInt(f[0].split("_")[1]);
	        		 if (num > count)
	        			 count = num;
	        }
	    }
	}
	
}

































