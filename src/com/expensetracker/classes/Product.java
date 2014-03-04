package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	 
	public static Vector<Product> getAvailableProducts(Object obj)throws SQLException
	{
		Vector<Product> brandList = new Vector<Product>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet =  null;
		PreparedStatement	stmt =  null;
		if(connection!=null)
		{
			try 
			{
				Subcategory subcategory =obj!=null?(Subcategory)obj:null;
				if(subcategory!=null)
				{
					stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Product.getAvailableProductsBySubCategory"));
					stmt.setInt(1, subcategory.getSubCategoryId());
				}	
				else
				{
					stmt=connection.prepareStatement(ExpenseTrackerUtility.getQuery("Product.getAvailableProducts"));
				}
				resultSet = stmt.executeQuery();

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
				throw e;
			}
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}

		}


		return brandList;
	}

	public Product addNewProduct(Object obj)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Integer categoryId=null;
		Product product = null;
		PreparedStatement stmt = null;
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Product.adddProduct"));

				Subcategory category = obj != null ? (Subcategory) obj : null;
				if (category != null) 
				{
					categoryId = category.getSubCategoryId();
				}
				stmt.setString(1, productName);
				stmt.setInt(2, categoryId);
				stmt.executeUpdate();
				
				
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT LAST_INSERT_ID()");
				product = new Product(productName);
				while (resultSet.next())
				{	
					product.setProductId(resultSet.getInt(1));
				}			
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return product;

	}
	public void removeProduct(Product product)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		PreparedStatement	stmt = null;
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Product.deleteProduct"));
				stmt.setInt(1, product.getProductId());
				stmt.executeUpdate();
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}

	}

	public void updateProduct(Product product)throws SQLException
	{
		
		PreparedStatement	stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Product.updateProduct"));
				stmt.setString(1, product.getProductName());
				stmt.setInt(2, product.getProductId());
				stmt.executeUpdate();
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}

		}

	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (obj instanceof Product == false)
			return false;
		
		return this.productId == ((Product)obj).productId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return productName;
	}
	
	}


