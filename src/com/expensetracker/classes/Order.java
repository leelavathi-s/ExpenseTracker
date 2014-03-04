package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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

	double quantity;
	
	double price;
	
	Shop shop;
	
	Brand brand;
	
	String categoryName;
	
	String brandName;
	
	String shopName;
	
	String productName;
	
	Subcategory subcategory;
	
	String subcategoryName;
	
	String commentTxt;
	
	public static String mySqlFormat= "YYYY-MM-dd";

	
	
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

	public String getCommentTxt() {
		return commentTxt;
	}

	public void setCommentTxt(String commentTxt) {
		this.commentTxt = commentTxt;
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

	public double getQuantity() {
		return quantity;
	}



	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}

	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	public void saveOrderDetails(Order orderDetails)throws SQLException
	{
		
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try(PreparedStatement stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Order.saveOrder"))) 
			{
				Integer brandId = null;
				if(orderDetails.getBrand()!=null)
				{
					brandId = orderDetails.getBrand().getBrandId();
					stmt.setInt(5, brandId);

				}
				else
				{
					stmt.setNull(5, Types.NULL);
				}
				
				Integer shopId = null;
				if(orderDetails.getShop()!=null)
				{
					shopId = orderDetails.getShop().getShopId();
					stmt.setInt(4, shopId);
				}
				else
				{
					stmt.setNull(4, Types.NULL);

				}
				
				stmt.setDouble(1, orderDetails.getQuantity());
				stmt.setInt(2, orderDetails.getProduct().getProductId());
				stmt.setDouble(3, orderDetails.getPrice());
				stmt.setInt(6, orderDetails.getCategory().getCategoryId());
				stmt.setInt(7, orderDetails.getSubcategory().getSubCategoryId());
				stmt.setString(8, orderDetails.getCommentTxt());
				stmt.setString(9, ExpenseTrackerUtility.formatDate(orderDetails.getPurchaseDate(),mySqlFormat));
				stmt.executeUpdate();

			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
				
			}
		}	
	}
	
		
	public static ArrayList<Order> retrievePurchasesForProduct(int productId) throws SQLException
	{	
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		PreparedStatement stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Order.retrievePurchasesForProduct"));
		stmt.setInt(1, productId);
		final ResultSet resultSet = stmt.executeQuery();
		
		ArrayList<Order> orderArrayList = new ArrayList<Order>();
		
		while (resultSet.next())
		{
			orderArrayList.add(new Order() 
			{{ 
				purchaseDate = resultSet.getDate(1);
				brandName = resultSet.getString(2);
				shopName = resultSet.getString(3);
				price = resultSet.getDouble(4);
				quantity = resultSet.getDouble(5);
			}});
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