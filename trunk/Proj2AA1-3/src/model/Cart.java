package model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import catalog.jaxb.Products;

import beans.CartItemBean;

public class Cart 
{
	private Map<BigInteger, CartItemBean> contents;
	private static float HST = 13;
	private static float GST = 5;
	private static float PST = 8;
	private static float shippingCost = 5;
	private static float shippingCostThreshold = 100;
	
	public Cart()
	{
		contents = new TreeMap<BigInteger, CartItemBean>();
	}
	
	public Map<BigInteger, CartItemBean> getContents()
	{
		return contents;
	}
	
	public List<CartItemBean> getProducts()
	{
		List<CartItemBean> result = new ArrayList<CartItemBean>();
		Set<BigInteger> keys = this.contents.keySet();
		Iterator<BigInteger> it = keys.iterator();
		while (it.hasNext())
		{
			result.add(this.contents.get(it.next()));
		}
		return result;
	}
	
	/**
	 * 
	 * @pre no null objects
	 */
	public void add(Products prod, Integer qty)
	{
		BigInteger pid = prod.getProductID();
		if (contents.containsKey(pid))
		{
			CartItemBean current = contents.get(pid);
			current.setQuantity(current.getQuantity() + qty);
		} else
		{
			contents.put(pid, new CartItemBean(prod, qty));
		}
		
		//if the qty becomes negative remove it from the cart
		if (contents.get(pid).getQuantity() < 1)
		{
			contents.remove(pid);
		}
	}
	
	public void remove(BigInteger pid, Integer qty)
	{
		if (contents.containsKey(pid))
		{
			CartItemBean current = contents.get(pid);
			current.setQuantity(current.getQuantity() - qty);
			
			//if the qty becomes negative remove it from the cart
			if (contents.get(pid).getQuantity() < 1)
			{
				contents.remove(pid);
			}
		} // do nothing if the product is not in the cart
		
	}
	
	public boolean getIsEmpty()
	{
		return this.contents.isEmpty();
	}
	
	public BigDecimal getTotalNoTax()
	{
		Collection<CartItemBean> items = this.contents.values();
		BigDecimal result = new BigDecimal(0);
		Iterator<CartItemBean> it = items.iterator();
		while (it.hasNext())
		{
			CartItemBean current = it.next();
			result = result.add(current.getUnitPrice().multiply(new BigDecimal(current.getQuantity())));
		}
		return result;
	}
	
	public BigDecimal getHST()
	{
		return new BigDecimal(Cart.HST);
	}
	
	public BigDecimal getGST()
	{
		return new BigDecimal(Cart.GST);
	}
	
	public BigDecimal getPST()
	{
		return new BigDecimal(Cart.PST);
	}
	
	public BigDecimal getGSTValue()
	{
		return this.getTotalNoTax().multiply(this.getGST().divide(new BigDecimal(100)));
	}
	
	public BigDecimal getPSTValue()
	{
		return this.getTotalNoTax().multiply(this.getPST().divide(new BigDecimal(100)));
	}
	
	public BigDecimal getTax()
	{
		return this.getTotalNoTax().multiply(this.getHST().divide(new BigDecimal(100)));
	}
	
	public BigDecimal getTotalWithTax()
	{
		return this.getTotalNoTax().add(this.getTax());
	}
	
	public boolean getIsWaived()
	{
		return (this.getTotalNoTax().floatValue() > 100);
	}
	
	public BigDecimal getGrandTotal()
	{
		BigDecimal result = this.getTotalWithTax();
		if (!this.getIsWaived())
		{
			result.add(new BigDecimal(Cart.shippingCost));
		}
		return result;
	}
	
	public BigDecimal getShippingCost()
	{
		return this.getIsWaived() ? new BigDecimal(0) : new BigDecimal(Cart.shippingCost);
	}

}
