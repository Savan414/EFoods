package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class ItemDAO
{
	private static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	private static ItemDAO instance = null;
	
	private ItemDAO() throws Exception
	{
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
	}
	
	static synchronized ItemDAO getInstance() throws Exception
	{
		if(instance == null)
			instance = new ItemDAO();
		return instance;
	}
	
	private class GeneratePreparedStatement
	{
		private Connection conn;
		PreparedStatement ps;
				
		GeneratePreparedStatement(String query) throws Exception
		{	
			conn = DriverManager.getConnection(DB_URL);
			ps = conn.prepareStatement("SET SCHEMA ROUMANI");
			ps.executeUpdate();
			ps.close();
			ps = conn.prepareStatement(query);
		}
		
		PreparedStatement getPs()
		{
			return ps;
		}
		
		void closeConnection() throws Exception
		{
			if (!ps.isClosed()) this.ps.close();
			if(!conn.isClosed()) this.conn.close();
		}
		
		
	}
	List<ItemBean> getItems(String catalog, String orderBy)throws Exception
	{
		GeneratePreparedStatement psGenerator = null;
		ResultSet rs = null;
		try
		{
			//building sql
			String sql = "SELECT * FROM ITEM"; 			
			sql = sql + " WHERE CATID=" + Integer.parseInt(catalog);			
			sql = orderBy == null? sql : sql + " ORDER BY " + orderBy;
			System.out.println("sql=" + sql);
			psGenerator = new GeneratePreparedStatement(sql);
			PreparedStatement ps = psGenerator.getPs();			
			rs = ps.executeQuery();
			List<ItemBean> items = new ArrayList<ItemBean>();
			while(rs.next())
			{
				//ItemBean(String name, String number, double price, int catalog, String unit)
				String name = rs.getString("NAME");
				String number = rs.getString("NUMBER");
				double price = rs.getDouble("PRICE");
				int catalogID = rs.getInt("CATID");
				String unit = rs.getString("UNIT");
				
				items.add(new ItemBean(name, number, price, catalogID, unit));
			}
			if (items.size() < 1)
				throw new Exception("No Items Found");
			return items;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if(rs != null && !rs.isClosed()) rs.close();
			if(psGenerator != null ) psGenerator.closeConnection();
		}
	}
	
	
	ItemBean getItem(String number) throws Exception
	{
		GeneratePreparedStatement psGenerator = null;
		ResultSet rs = null;
		try
		{
			String sql = "SELECT * FROM ITEM WHERE NUMBER='" + number + "'";
						
//			ps.setString(1, number);
//			System.out.println(ps.toString());
//			rs =ps.executeQuery();
			
			psGenerator = new GeneratePreparedStatement(sql);
			PreparedStatement ps = psGenerator.getPs();			
			rs = ps.executeQuery();
			if (rs.getRow() < 0)
				throw new Exception ("No item with number " + number);
			ItemBean result = null;
			if(rs.next())
			{
				String name = rs.getString("NAME");
				String itemNumber = rs.getString("NUMBER");
				double price = rs.getDouble("PRICE");
				int catalogID = rs.getInt("CATID");
				String unit = rs.getString("UNIT");
				
				result = new ItemBean(name, itemNumber, price, catalogID, unit);
			}
			return result;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if(rs != null && !rs.isClosed()) rs.close();
			if(psGenerator != null ) psGenerator.closeConnection();
		}
	}

	List<CatalogBean> getCatalogs() throws Exception
	{
		GeneratePreparedStatement psGenerator = null;
		ResultSet rs = null;
		try
		{
			
			String sql = "SELECT * FROM CATEGORY";

			psGenerator = new GeneratePreparedStatement(sql);
			PreparedStatement ps = psGenerator.getPs();			
			rs =ps.executeQuery();
			List<CatalogBean> result = new ArrayList<CatalogBean>();
			while(rs.next())
			{
				String name = rs.getString("NAME");
				String description = rs.getString("DESCRIPTION");
				int id = rs.getInt("ID");
				result.add(new CatalogBean(name, description, id));
			}
			return result;						
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if(rs != null && !rs.isClosed()) rs.close();
			if(psGenerator != null ) psGenerator.closeConnection();
		}
	}

}































