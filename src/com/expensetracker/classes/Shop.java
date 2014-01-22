package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Shop
{
 private String shopName;
 
 private int shopId;
 
 public Shop(String shopName)
 {
	 this.shopName = shopName;
 }

public Shop() {
	// TODO Auto-generated constructor stub
}

public String getShopName() {
	return shopName;
}

public void setShopName(String shopName) {
	this.shopName = shopName;
}
 
public int getShopId() {
	return shopId;
}

public void setShopId(int shopId) {
	this.shopId = shopId;
}

public static Vector<Shop> getAvailableShops()
{
	Vector<Shop> shopsList = new Vector<Shop>();
	Connection connection = ExpenseTrackerUtility.getConnection();
	if(connection!=null)
	{
		try 
		{
			Statement	stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("Select * from Shop order by shopname asc");
			while (resultSet.next()) 
			{
				Shop shop = new Shop();
				shop.setShopId(resultSet.getInt(1));
				shop.setShopName(resultSet.getString(2));
				
				shopsList.add(shop);
			
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	return shopsList;
}

public Shop addNewShop()throws SQLException
{
	Connection connection = ExpenseTrackerUtility.getConnection();
	Shop shop = null;
	if(connection!=null)
	{
		try 
		{
			Statement	stmt = connection.createStatement();
			stmt.executeUpdate("Insert into shop (ShopName) values(" + "'" + shopName + "')");
			
			ResultSet resultSet = stmt
					.executeQuery("SELECT LAST_INSERT_ID()");
			 shop = new Shop(shopName);
			while (resultSet.next())
			{
				shop.setShopId(resultSet.getInt(1));
			}			

			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	return shop;

}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	
	if (obj == null)
		return false;
	
	if (obj instanceof Shop == false)
		return false;
	
	return this.shopId == ((Shop)obj).shopId;
}

@Override
public String toString() {
	// TODO Auto-generated method stub
	return shopName;
}

}
