package model;

public class CatalogBean
{
	String name;
	String description;
	int id;
	public CatalogBean(String name, String description, int id)
	{
		super();
		this.name = name;
		this.description = description;
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	@Override
	public String toString()
	{
		return "CatalogBean [name=" + name + ", description=" + description + ", id=" + id + "]";
	}
	
}