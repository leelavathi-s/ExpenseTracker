package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Order
{

	String purchaseDate;
	
	String category;
	
	String productName;

	int quantity;
	
	double price;
	
	String shopName;
	
	String brandName;
	
	public Order(String purchaseDate, String category,String productName, int quantity,
			double price, String shopName, String brandName)
	{
		this.purchaseDate = purchaseDate;
		this.productName = productName;
		this.category = category;
		this.quantity = quantity;
		this.price = price;
		this.shopName = shopName;
		this.brandName = brandName;
	}
	
	public String getPurchaseDate() {
		return purchaseDate;
	}



	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getShopName() {
		return shopName;
	}



	public void setShopName(String shopName) {
		this.shopName = shopName;
	}



	public String getBrandName() {
		return brandName;
	}



	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}



	public void saveOrderDetails(Order orderDetails)
	{
		System.out.println("Method invoked");
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expensetracker", "root", "123");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Catch 1");
			e.printStackTrace();
		}
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Catch 2");
			e.printStackTrace();
		}
		try {
			 stmt.executeUpdate("Insert into order values()");
			
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Catch 3");
			e.printStackTrace();
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Catch 4");
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Catch 5");
			e.printStackTrace();
		}

	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Date of Purchase::").append(getPurchaseDate())
		.append("Cateogry::")
		.append(getCategory())
		.append("  ProductName::")
		.append(getProductName())
		.append("  Quantity::")
		.append(getQuantity())
		.append("  Price::")
		.append(getPrice())
		.append("  ShopName::")
		.append(getShopName())
		.append("  BrandName::")
		.append(getBrandName());			
		return super.toString();
	}



}
