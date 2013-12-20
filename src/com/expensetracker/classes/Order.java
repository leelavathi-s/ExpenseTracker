package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Order
{

	String purchaseDate;
	
	int categoryId;
	
	int productId;

	int quantity;
	
	double price;
	
	int shopId;
	
	int brandId;
	
	public Order(String purchaseDate, String category,String productName, int quantity,
			double price, String shopName, String brandName)
	{
		this.purchaseDate = purchaseDate;
		//this.productName = productName;
	//	this.category = category;
		this.quantity = quantity;
		this.price = price;
		//this.shopName = shopName;
		//this.brandName = brandName;
	}
	
	public Order() {
		// TODO Auto-generated constructor stub
	}

	
	public int getCategoryId() {
		return categoryId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getProductId() {
		return productId;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}



	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
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



	


	


	public void saveOrderDetails(Order orderDetails)throws SQLException
	{
		System.out.println("Method invoked");
		
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				
				stmt.executeUpdate("Insert into purchaseorder (quantity,productID,price,shopId,brandId,categoryId,orderDate) values("
						+ orderDetails.getQuantity()
						+ ","
						+ orderDetails.getProductId()
						+ ","
						+ orderDetails.getPrice()
						+ ","
						+ orderDetails.getShopId()
						+ ","
						+ orderDetails.getBrandId()
						+ ","
						+ orderDetails.getCategoryId()
						+ ","
						+ orderDetails.getPurchaseDate() + ")");

			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
				
			}
		}	
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Date of Purchase::").append(getPurchaseDate())
		.append("Cateogry Id::")
		.append(getCategoryId())
		.append("  Product ID::")
		.append(getProductId())
		.append("  Quantity::")
		.append(getQuantity())
		.append("  Price::")
		.append(getPrice())
		.append("  Shop Id::")
		.append(getShopId())
		.append("  Brand Id::")
		.append(getBrandId());			
		return super.toString();
	}



}
