package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import catalog.jaxb.Categories;
import catalog.jaxb.Dataroot;
import catalog.jaxb.Products;


public class CatalogModel 
{
    private static Map<BigInteger, Categories> categories;
    private static Map<BigInteger, Products> products;
    private static Map<BigInteger, List<Products>> categoryIDtoProducts;
    private static ArrayList<Account> accounts;

    public CatalogModel(String catalogFile) throws JAXBException,
        FileNotFoundException, IOException 
    {

        System.out.println(catalogFile);
    	JAXBContext jc = JAXBContext.newInstance("catalog.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Dataroot data = (Dataroot) unmarshaller.unmarshal(new File(catalogFile + "/Catalog.xml"));

        categories = new TreeMap<BigInteger, Categories>();
        products = new TreeMap<BigInteger, Products>();
        categoryIDtoProducts = new TreeMap<BigInteger, List<Products>>();
        accounts = new ArrayList<Account>();
        
       
        this.loadAccounts(catalogFile+"/WEB-INF/accounts.txt");
        this.loadCategoriesMaps(data.getCategories());
        this.loadProductsMaps(data.getProducts());
        
        this.extractPictures(data, catalogFile);
    }
    
    private void extractPictures(Dataroot data, String path) throws IOException
    {
    	//Dataroot data = data;
    	 // data; = (Dataroot) unmarshaller.unmarshal(new File("xml file"));
    	  
    	List<Categories> list = data.getCategories();
    	  Iterator<Categories> iterator = list.iterator();
    	  final int msOffset = 78;
    	  while (iterator.hasNext()) 
    	  {
    	    Categories item = iterator.next();
    	   // String imageName = item.getCategoryID().toString();
    	    String pathName = path + "img/img" + item.getCategoryID() + ".jpeg";
    	    System.out.println(pathName);
    	    File file = new File(pathName);
    	    byte[] image = item.getPicture();
    	    
    	    try{
    	    OutputStream out = new FileOutputStream(file);
    	    out.write(image, msOffset, image.length - msOffset);
    	    out.close();
    	    
    	    }catch (Exception e)
    	    {
    	    	e.printStackTrace();
    	    }
    	  }
    }
    private void loadAccounts(String Accountspath) throws FileNotFoundException
    {
    	File accounts = new File(Accountspath);
    	Scanner input = new Scanner(accounts);
    	input.useDelimiter("\t");
    	Account current = new Account();
    	while (input.hasNext())
    	{
    		current.setAccountID(input.next());
    		System.out.println(current.toString());
    		input.skip("\t"); 
    		current.setAccountName(input.next());
    		System.out.println(current.toString());
    		input.skip("\t"); 
    		
    		input.useDelimiter("\n");
    		String addPass = input.next();
    		input.skip("\n"); 
    		input.useDelimiter("\t");
    		
    		current.setAddress(addPass.substring(0, (addPass.length() - 4)));
    		System.out.println(current.toString());
    		current.setPassword(addPass.substring(addPass.length() - 4, addPass.length()));
    		
    		CatalogModel.accounts.add(current);
    		System.out.println(current.toString());
    		current = new Account();
    	}
    	for (int i = 0; i < CatalogModel.accounts.size(); i++)
    	{
    		System.out.println(CatalogModel.accounts.get(i).toString());
    	}
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
    public Account getAccount(String id, String pw){
    	
    	Account current = new Account();
    	ArrayList<Account> accts = new ArrayList<Account>(CatalogModel.accounts);
    	//for (int i = 0; i < CatalogModel.accounts.size(); i++)
    	for(Account acct : accts)
    	{
    		if ( acct.getAccountID().equals(new String(id))){
    			current = acct;
    		}
    	}
    	
    	return current;
    }
    public boolean validat(String id, String pw)
    {
    	Boolean result = false;
    	for (int i = 0; !result && i < CatalogModel.accounts.size(); i++)
    	{
    		Account current = CatalogModel.accounts.get(i);
    		if (id.equals(current.getAccountID()))
    		{
    			result = pw.equals(current.getPassword());
    		}
    	}
    	return result;
    }
}