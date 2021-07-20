package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.*;


@XmlRootElement(name = "product")
public class OrderBean
{
//	CustomerBean customer
	private List<ItemBean> items;
	private double total;
	private double shipping;
	private double HST;
	private double grandTotal;
	private String id;
	private int orderNumber;
	private Date submitted;
	private String account;
	
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public OrderBean(List<ItemBean> items)
	{
		super();
		this.items = items;
	}
	
	public OrderBean()
	{
		super();
		this.items = null;
	}
	public List<ItemBean> getItems()
	{
		return items;
	}
	public void setItems(List<ItemBean> items)
	{
		this.items = items;
	}
	public double getTotal()
	{
		return total;
	}
	public void setTotal(double total)
	{
		this.total = total;
	}
	public double getShipping()
	{
		return shipping;
	}
	public void setShipping(double shipping)
	{
		this.shipping = shipping;
	}
	public double getHST()
	{
		return HST;
	}
	public void setHST(double hST)
	{
		HST = hST;
	}
	public double getGrandTotal()
	{
		return grandTotal;
	}
	public void setGrandTotal(double grandTotal)
	{
		this.grandTotal = grandTotal;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public Date getSubmitted()
	{
		return submitted;
	}
	public void setSubmitted(Date submitted)
	{
		this.submitted = submitted;
	}
	@Override
	public String toString()
	{
		return "OrderBean [items=" + items + ", total=" + total + ", shipping=" + shipping + ", HST=" + HST
				+ ", grandTotal=" + grandTotal + ", id=" + id + ", submitted=" + submitted + "]";
	} 
	
	public boolean isEmpty() {
		if (items == null)
			return true;
			
		return items.size() == 0;
	}
	public void clear() {

		items = new ArrayList<>();
		total = 0;
		shipping = 0;
		HST = 0;
		grandTotal = 0;
		submitted = null;
		
	}
	
	

}