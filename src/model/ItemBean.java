package model;

public class ItemBean
{
	private String name;
	private String number;
	private double price;
	private int quantity;
	private double extended;
	private int catalog;
	private String unit;
	
	public ItemBean() {
		
	}
	
	
	public ItemBean(String name, String number, double price, int catalog, String unit, int quantity, double extended)
	{
		super();
		this.name = name;
		this.number = number;
		this.price = price;
		this.quantity = quantity;
		this.extended = extended;
		this.catalog = catalog;
		this.unit = unit;	
	}
	
	public ItemBean(String name, String number, double price, int catalog, String unit)
	{
		this(name, number, price, catalog, unit, 0, 0.0);
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number.toUpperCase();
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public double getExtended()
	{
		return extended;
	}

	public void setExtended(double extended)
	{
		this.extended = extended;
	}

	public int getCatalog()
	{
		return catalog;
	}

	public void setCatalog(int catalog)
	{
		this.catalog = catalog;
	}

	@Override
	public String toString()
	{
		return "ItemBean [name=" + name + ", number=" + number + ", price=" + price + ", quantity=" + quantity
				+ ", extended=" + extended + ", catalog=" + catalog + ", unit=" + unit + "]";
	}
	
	
}