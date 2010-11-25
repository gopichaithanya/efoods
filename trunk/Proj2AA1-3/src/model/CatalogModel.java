package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import beans.CartItemBean;

import PO.jaxb.*;
import catalog.jaxb.Categories;
import catalog.jaxb.Dataroot;
import catalog.jaxb.Products;

// Created by Swati and Rachel

public class CatalogModel 
{
    private static Map<BigInteger, Categories> categories;
    private static Map<BigInteger, Products> products;
    private static Map<BigInteger, List<Products>> categoryIDtoProducts;
    private static ArrayList<CustomerType> accounts;
    private static Map<String, String> passwords;
    private static Map<String, Integer> orderAmounts;

    public CatalogModel(String catalogFile, String accountsFile) throws JAXBException,
        FileNotFoundException, IOException 
    {

        JAXBContext jc = JAXBContext.newInstance("catalog.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Dataroot data = (Dataroot) unmarshaller.unmarshal(new File(catalogFile));

        categories = new TreeMap<BigInteger, Categories>();
        products = new TreeMap<BigInteger, Products>();
        categoryIDtoProducts = new TreeMap<BigInteger, List<Products>>();
        accounts = new ArrayList<CustomerType>();
        passwords = new TreeMap<String, String>();
        orderAmounts = new TreeMap<String, Integer>();
    
        this.loadAccounts(accountsFile);
        this.loadCategoriesMaps(data.getCategories());
        this.loadProductsMaps(data.getProducts());
    }

    private void loadCategoriesMaps(List<Categories> categories) 
    {
        Iterator<Categories> iterator = categories.iterator();
        while (iterator.hasNext()) 
        {
            Categories category = iterator.next();
            CatalogModel.categories.put(category.getCategoryID(), category);
        }
    }
    
    private void loadAccounts(String Accountspath) throws FileNotFoundException
    {
    	File accounts = new File(Accountspath);
    	Scanner input = new Scanner(accounts);
    	ObjectFactory of = new ObjectFactory();
    	while (input.hasNext())
    	{
    		input.useDelimiter("\t");
    		CustomerType current = of.createCustomerType();
        	AddressType addr = of.createAddressType();
    		current.setAccount(input.next());
    		//System.out.println(current.toString());
    		input.skip("\t"); 
    		
    		current.setName(input.next());
    		System.out.println(current.toString());
    		input.skip("\t"); 
    		
    		addr.getStreet().add(input.next()); //get the Street
    		input.skip("\t"); 
    		
    		addr.setCity(input.next()); // get the city
    		input.skip("\t");
    		
    		addr.setProvince(input.next()); // get the Province
    		input.skip("\t"); 
    		
    		addr.setPostal(input.next()); // get the Province
    		input.skip("\t");
    		
    		current.setAddress(addr); //set the address
    		
    		input.useDelimiter("\n");
    		CatalogModel.passwords.put(current.getAccount(), input.next());
    		CatalogModel.orderAmounts.put(current.getAccount(), 0);
    		input.skip("\n");
    		
    		CatalogModel.accounts.add(current);
    		System.out.println(current.toString());
    	}
    	for (int i = 0; i < CatalogModel.accounts.size(); i++)
    	{
    		System.out.println(CatalogModel.accounts.get(i).toString());
    	}
    }

    private void loadProductsMaps(List<Products> products) 
    {
        Iterator<Products> iterator = products.iterator();
        while (iterator.hasNext()) 
        {
            Products product = iterator.next();
            // ignore discontinued items
            if (!product.isDiscontinued()) 
            {
                CatalogModel.products.put(product.getProductID(), product);
                BigInteger categoryID = product.getCategoryID();
                List<Products> temp;
                if (CatalogModel.categoryIDtoProducts.containsKey(categoryID)) 
                {
                    temp = CatalogModel.categoryIDtoProducts.get(categoryID);
                } else 
                {
                    temp = new ArrayList<Products>();
                }
                temp.add(product);
                CatalogModel.categoryIDtoProducts.put(categoryID, temp);
                temp = null;
            }
        }
    }

    public Map<BigInteger, Categories> getCategories() 
    {
        return categories;
    }

    public Map<BigInteger, Products> getProducts() 
    {
        return products;
    }

    public List<Products> getProductsByCategoryID(BigInteger categoryID) 
    {
        return categoryIDtoProducts.get(categoryID);
    }

    // Convenience method 
    public List<Products> getProductsByCategoryID(String categoryID) 
    {
        return getProductsByCategoryID(new BigInteger(categoryID));
    }

    public List<Products> getProducts(String search) 
    {
        List<Products> topResults = new ArrayList<Products>(); // best matches
        List<Products> lowResults = new ArrayList<Products>(); // not so great
        Iterator<Products> keys = getProducts().values().iterator();
        boolean isNumeric = search.trim().matches("[0-9]+");
        String sortBy = "name";
        if (isNumeric)
            sortBy = "id";

        if (search.length() > 0) 
        {
            String keyword = search.toLowerCase().trim();
            String name, id;
            String[] names;
            Products product;

            while (keys.hasNext()) 
            {
                product = keys.next();
                name = product.getProductName().toLowerCase();
                names = name.split(" ");
                id = product.getProductID().toString();

                if (name.startsWith(keyword)) 
                {
                    topResults.add(product);
                } else if (id.startsWith(keyword)) 
                {
                    topResults.add(product);
                } else 
                {
                    /*
                     * This is a bug in the code. If there is a product such as:
                     * "What Car Car" and the user is searching for 'Car', the
                     * item will get added to the list two times. This won't
                     * happen if the product is "Car Car Car" because it doesn't
                     * get to this "else" statement.
                     */
                    for (int i = 0; i < names.length; i++) 
                    {
                        if (names[i].startsWith(keyword))
                            lowResults.add(product);
                    }
                }
            }

            //Collections.sort(topResults, ProductsComparator.getComparator(sortBy, ProductsComparator.ASCENDING));
            //Collections.sort(lowResults, ProductsComparator.getComparator(sortBy, ProductsComparator.ASCENDING));
            topResults.addAll(topResults.size(), lowResults);
        }

        return topResults;
    }

    public List<Products> getRelatedProducts(BigInteger productID, int numRelatedProducts) 
    {
        BigInteger categoryID = this.getProducts().get(productID).getCategoryID();
        List<Products> products = (ArrayList<Products>) this.getProductsByCategoryID(categoryID);
        int numProducts = products.size();
        // good to use a map because then we won't get duplicates :)`
        Map<BigInteger, Products> relatedProducts = new TreeMap<BigInteger, Products>();

        if (numRelatedProducts <= numProducts) 
        {
            Random generator = new Random();

            // Keep looping until we've filled our array to desired size
            while (relatedProducts.size() < numRelatedProducts) 
            {
                int num = generator.nextInt(numProducts);
                BigInteger key = products.get(num).getProductID();

                if (!productID.equals(key)) 
                {
                    relatedProducts.put(key, products.get(num));
                }
            }
        }

        return new ArrayList<Products>(relatedProducts.values());
    }

    public List<Products> getRandomProducts(int numRandomProducts) 
    {
        ArrayList<Products> products = new ArrayList<Products>(this.getProducts().values());
        ArrayList<BigInteger> keys = new ArrayList<BigInteger>(this.getProducts().keySet());
        int numProducts = products.size();
        // good to use a map because then we won't get duplicates :)`
        Map<Integer, Products> randomProducts = new TreeMap<Integer, Products>();

        if (numRandomProducts <= numProducts) 
        {
            Random generator = new Random();

            // Keep looping until we've filled our array to desired size
            while (randomProducts.size() < numRandomProducts) 
            {
                int num = generator.nextInt(numProducts);
                BigInteger key = keys.get(num);
                randomProducts.put(num, this.getProducts().get(key));
            }
        }
        return new ArrayList<Products>(randomProducts.values());
    }
    
    public class ProductComparator implements Comparator<Products>
    {
    	
    	String sortBy;
    	int sortOrder;
    	
    	ProductComparator(String sortBy, int sortOrder)
    	{
    		this.sortBy = sortBy;
    		this.sortOrder = sortOrder;
    		
    	}
		@Override
		public int compare(Products o1, Products o2) {
			// TODO take care of sortOrder
			if(sortBy.equals("name"))
			{
				return o1.getProductName().compareTo(o2.getProductName());
				
			}else if (sortBy.equals("id"))
			{
				return o1.getProductID().compareTo(o2.getProductID());
			}
			System.err.println("ProductComparator performing wrong comparision");
			return 0;
		}	
    }
    
    public String toString()
    {
    	return "Categories are:\n" + this.printCategories() + "\nProducts are:\n" + this.printProducts();
    }
    
    private String printCategories()
    {
    	String result = "";
    	Iterator<Categories> it = this.getCategories().values().iterator();
    	while (it.hasNext())
    	{
    		Categories current = it.next();
    		result = result + "id: " + current.getCategoryID() + "| name: " + current.getCategoryName() + "\n";
    	}
    	return result;
    }
    
    private String printProducts()
    {
    	String result = "";
    	Iterator<Products> it = this.getProducts().values().iterator();
    	while (it.hasNext())
    	{
    		Products current = it.next();
    		result = result + "id: " + current.getProductID() + "| name: " + current.getProductName() + "\n";
    	}
    	return result;
    }
    
    public CustomerType getAccount(String id)
    {    	
    	CustomerType current = null;
    	ArrayList<CustomerType> accts = new ArrayList<CustomerType>(CatalogModel.accounts);
    	for(CustomerType acct : accts)
    	{
    		if ( acct.getAccount().equals(new String(id))){
    			current =  acct;
    		}
    	}
    	return current;
    }
    
    public List<CustomerType> getAccounts()
    {
    	return CatalogModel.accounts;
    }
    
    public boolean validate(String id, String pw)
    {
    	/*Boolean result = false;
    	for (int i = 0; !result && i < CatalogModel.accounts.size(); i++)
    	{
    		CustomerType current = CatalogModel.accounts.get(i);
    		if (id.equals(current.getAccount()))
    		{
    			result = pw.equals(current.getPassword());
    		}
    	}
    	return result;*/
    	return CatalogModel.passwords.containsKey(id) && CatalogModel.passwords.get(id).equals(pw);
    }
    
    public String makePO(Cart cart, CustomerType customer, String path)
    {
    	ObjectFactory of = new ObjectFactory();
    	OrderType order = of.createOrderType();
    	ItemsType items = of.createItemsType();
    	Collection<CartItemBean> cartItems = cart.getContents().values();
    	for (CartItemBean cartItm : cartItems)
    	{
    		ItemType item = of.createItemType();
    		item.setName(cartItm.getProductName());
    		item.setNumber(cartItm.getProductID());
    		item.setPrice(cartItm.getUnitPrice());
    		item.setQuantity(new BigInteger(String.valueOf(cartItm.getQuantity())));
    		item.setExtended(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
    		
    		items.getItem().add(item);
    	}
    	order.setItems(items);
    	order.setCustomer(customer);
    	order.setGrandTotal(cart.getGrandTotal());
    	order.setGST(cart.getGSTValue());
    	order.setPST(cart.getPSTValue());
    	order.setShipping(cart.getShippingCost());
    	order.setTotal(cart.getTotalNoTax());
    	try 
    	{
			order.setSubmitted(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) 
		{
			e.printStackTrace();
		}
		
		CatalogModel.orderAmounts.put(customer.getAccount(),CatalogModel.orderAmounts.get(customer.getAccount()) + 1); //increment the number of orders for this cutomer
		order.setId(new BigInteger(String.valueOf(CatalogModel.orderAmounts.get(customer.getAccount()))));
		
		JAXBElement<OrderType> finalOrder = of.createOrder(order);
		
		String orderID = CatalogModel.orderAmounts.get(customer.getAccount()) < 9 ? "0" + String.valueOf(CatalogModel.orderAmounts.get(customer.getAccount())) : String.valueOf(CatalogModel.orderAmounts.get(customer.getAccount()));
		String fileName = "/po" + customer.getAccount() + orderID + ".xml";
		File xmlFile = new File(path + fileName);
		
		try 
		{
			JAXBContext jc = JAXBContext.newInstance("PO.jaxb");
			Marshaller m = jc.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(finalOrder, xmlFile);
		} catch (JAXBException e) 
		{
			e.printStackTrace();
		}
		System.out.println(xmlFile.getAbsolutePath());
		return fileName; //returns the filename with a / in the beginning
    }
}
