package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Product {
	
	private String productName;
	
	private int productId;
	
	
	 
	 public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Product(String productName)
	 {
		 this.productName = productName;
	 }

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	 
	public static Vector<Product> getAvailableProducts(Object obj)
	{
		Vector<Product> brandList = new Vector<Product>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet =  null;
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				Category categoryObj =obj!=null?(Category)obj:null;
				if(categoryObj!=null)
				{
					 resultSet = stmt.executeQuery("SELECT *FROM product where categoryId=" +categoryObj.getCategoryId() +" group by productName");
				}	
				else
				{
					resultSet = stmt.executeQuery("SELECT *FROM product");
				}
				while (resultSet!=null && resultSet.next()) 
				{
					Product product = new Product();
					product.setProductId(resultSet.getInt(1));
					product.setProductName(resultSet.getString(2));
					brandList.add(product);
				
				}
			} catch (SQLException e)
			{
				
				e.printStackTrace();
			}
		}


		return brandList;
	}

	public void addNewProduct(Object obj)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Integer categoryId=null;
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();

							Category category= obj!=null?(Category) obj:null;
							if(category!=null)
							{
								categoryId = category.getCategoryId();
							}
							stmt.executeUpdate("Insert into product (productName,categoryId) values(" + "'" + productName + "'," + categoryId  + ")");
							connection.commit();
				
				
				
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return productName;
	}
	
	}



