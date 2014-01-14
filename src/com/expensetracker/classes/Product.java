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
	 
	public static Vector<Product> getAvailableProducts(Object obj)throws SQLException
	{
		Vector<Product> brandList = new Vector<Product>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet =  null;
		Statement	stmt =  null;
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				Subcategory subcategory =obj!=null?(Subcategory)obj:null;
				if(subcategory!=null)
				{
					 resultSet = stmt.executeQuery("SELECT *FROM product where subcategoryId=" +subcategory.getSubCategoryId() +" group by productName order by productname");
				}	
				else
				{
					resultSet = stmt.executeQuery("SELECT *FROM product order by productname");
				}
				while (resultSet!=null && resultSet.next()) 
				{
					Product product = new Product();
					product.setProductId(resultSet.getInt(1));
					product.setProductName(resultSet.getString(3));
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
		Statement stmt = null;
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();

				Subcategory category = obj != null ? (Subcategory) obj : null;
				if (category != null) 
				{
					categoryId = category.getCategoryId();
				}
				stmt.executeUpdate("Insert into product (productName,subcategoryId) values("
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
		Statement	stmt = null;
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				stmt.executeUpdate("delete from product where productId = " + product.getProductId());
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
		
		Statement	stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				stmt.executeUpdate("update product set productName =" +"'" + productName + "' where productId = " + product.getProductId());
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
	public String toString() {
		// TODO Auto-generated method stub
		return productName;
	}
	
	}



