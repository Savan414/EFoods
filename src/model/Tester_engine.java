package model;

import java.util.ArrayList;
import java.util.List;

public class Tester_engine
{
	public static void main (String[] args) throws Exception
	{
		List<ItemBean> items = new ArrayList<ItemBean>();
		OrderBean order = new OrderBean(items);
		Engine engine = Engine.getInstance();
		ItemDAO itemDao = ItemDAO.getInstance();
		System.out.println(engine.searchItem("1409S413"));		
	}
}
