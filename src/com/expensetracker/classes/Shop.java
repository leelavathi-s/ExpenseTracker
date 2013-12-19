package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Shop
{
 public String shopName;
 
 public Shop(String shopName)
 {
	 this.shopName = shopName;
 }

public String getShopName() {
	return shopName;
}

public void setShopName(String shopName) {
	this.shopName = shopName;
}
 
public static Vector<String> getAvailableShops()
{
	Vector<String> shopsList = new Vector<String>();
	Connection connection = ExpenseTrackerUtility.getConnection();
	if(connection!=null)
	{
		try 
		{
			Statement	stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("Select * from Shop");
			while (resultSet.next()) 
			{
				System.out.println("Entered loop");
				System.out.println(resultSet.getString(2));
				shopsList.add(resultSet.getString(2));
			
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	return shopsList;
}

public void addNewShop()throws SQLException
{
	System.out.println("Entered here in addnew shop");
	Connection connection = ExpenseTrackerUtility.getConnection();
	
	if(connection!=null)
	{
		try 
		{
			Statement	stmt = connection.createStatement();
			stmt.executeUpdate("Insert into shop (ShopName) values(" + "'" + shopName + "')");
			System.out.println("Query:::" + stmt.toString());
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

}
}

