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

	public Product addNewProduct(Object obj)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Integer categoryId=null;
		Product product = null;
		if(connection!=null)
		{
			try 
 {
				Statement stmt = connection.createStatement();

				Category category = obj != null ? (Category) obj : null;
				if (category != null) 
				{
					categoryId = category.getCategoryId();
				}
				stmt.executeUpdate("Insert into product (productName,categoryId) values("
						+ "'" + productName + "'," + categoryId + ")");
				connection.commit();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT LAST_INSERT_ID()");
				product = new Product(productName);
				while (resultSet.next())
				{	
					product.setProductId(resultSet.getInt(1));
				}			
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
		}
		return product;

	}
	public void removeProduct(Product product)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				stmt.executeUpdate("delete from product where productId = " + product.getProductId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}

	}

	public void updateProduct(Product product)throws SQLException
	{

		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				stmt.executeUpdate("update product set productName =" +"'" + productName + "' where productId = " + product.getProductId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
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



