package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Order
{

	Date purchaseDate;
	
	Category category;
	
	Product product;

	int quantity;
	
	double price;
	
	Shop shop;
	
	Brand brand;
	
	String categoryName;
	
	String brandName;
	
	String shopName;
	
	String productName;
	
	
	public static String mySqlFormat= "YYYY-MM-dd";

	
	public Order(String purchaseDate, String category,String productName, int quantity,
			double price, String shopName, String brandName)
	{
		//this.purchaseDate = purchaseDate;
		//this.productName = productName;
	//	this.category = category;
		this.quantity = quantity;
		this.price = price;
		//this.shopName = shopName;
		//this.brandName = brandName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getShopName() {
		return shopName;
	}

	public String getProductName() {
		return productName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Order() {
		// TODO Auto-generated constructor stub
	}

	
	public Category getCategory() {
		return category;
	}

	public Product getProduct() {
		return product;
	}

	public Shop getShop() {
		return shop;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	
	public Date getPurchaseDate() {
		return purchaseDate;
	}



	public void setPurchaseDate(Date purchaseDate) {
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
		
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				
				stmt.executeUpdate("Insert into purchaseorder (quantity,productID,price,shopId,brandId,categoryId,orderDate) values("
						+ orderDetails.getQuantity()
						+ ","
						+ orderDetails.getProduct().getProductId()
						+ ","
						+ orderDetails.getPrice()
						+ ","
						+ (orderDetails.getShop()!=null?(orderDetails.getShop().getShopId()):null)
						+ ","
						+ (orderDetails.getBrand()!=null?(orderDetails.getBrand().getBrandId()):null)
						+ ","
						+ orderDetails.getCategory().getCategoryId()
						+ ",'"
						+ ExpenseTrackerUtility.formatDate(orderDetails.getPurchaseDate(),mySqlFormat) + "')");

			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
				
			}
		}	
	}
	
	public static List<Order> retrieveData(String basedOn)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		ArrayList<Order> orderArrayList = null;
		ResultSet resultSet = null;

		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				if("Weekly".equals(basedOn))
				{
						resultSet = stmt
						.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName"
								+ " from purchaseorder po,category cat,shop,product pro,brand"
								+ " where po.CategoryId = cat.CategoryId and po.ShopId =  shop.ShopId and pro.ProductId=po.ProductId and brand.brandid=po.brandid"
								);
				}
				orderArrayList = new ArrayList<Order>();
				while(resultSet.next())
				{
					Order order = new Order();
					order.setPurchaseDate(resultSet.getDate(1));
					order.setPrice(resultSet.getDouble(2));
					order.setQuantity(resultSet.getInt(3));
					order.setCategoryName(resultSet.getString(4));
					order.setShopName(resultSet.getString(5));
					order.setBrandName(resultSet.getString(6));
					order.setProductName(resultSet.getString(7));
					orderArrayList.add(order);
					
				}	
					
					
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
				
			}
		}	
		return orderArrayList;
	}
	
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Date of Purchase::").append(getPurchaseDate())
		.append("Cateogry Name::")
		.append(getCategoryName())
		.append("  Product Name::")
		.append(getProductName())
		.append("  Quantity::")
		.append(getQuantity())
		.append("  Price::")
		.append(getPrice())
		.append("  Shop Name::")
		.append(getShopName())
		.append("  Brand Name::")
		.append(getBrandName());			
		return super.toString();
	}



}
