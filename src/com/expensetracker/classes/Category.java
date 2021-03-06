package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Category 
{

	public String categoryName;
	
	public int categoryId;

	public Category(String categoryName)
	{
		this.categoryName = categoryName;
	}
	public Category() {
		// TODO Auto-generated constructor stub
	}
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public static Vector<Category> getAvailableCategories(Object obj)throws SQLException
	{
		Vector<Category> categoriesList = new Vector<Category>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		if(connection!=null)
		{
	
			try
			{
				
				if (obj instanceof Subcategory) 
				{
					Subcategory subCategoryObj = obj != null ? (Subcategory) obj : null;
					if (obj != null)
					{
						stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Category.getAvailableCategoriesBySubCategory"));
						stmt.setInt(1, subCategoryObj.getSubCategoryId());
					}

				}
				else
				{	
					 stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Category.getAvailableCategories"));
				} 
				resultSet = stmt.executeQuery();
				while (resultSet.next()) 
				{
					Category category= new Category();
					category.setCategoryId(resultSet.getInt(1));
					category.setCategoryName(resultSet.getString(2));
					categoriesList.add(category);
				
				}
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
		}


		return categoriesList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return categoryName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public Category addNewCategory()throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Category category=null;
		PreparedStatement	stmt = null;
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Category.addCategory"));
				stmt.setString(1, categoryName);
				stmt.executeUpdate();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT LAST_INSERT_ID()");
				category = new Category(categoryName);
				while (resultSet.next())
				{	
					category.setCategoryId(resultSet.getInt(1));
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
		return category;

	}
	public void removeCategory(Category category)throws SQLException
	{
		PreparedStatement	stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Category.deleteCategory"));
				stmt.setInt(1, category.getCategoryId());
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
	public void updateCategory(Category category)throws SQLException
	{
		PreparedStatement	stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Category.updateCategory"));
				stmt.setString(1, categoryName);
				stmt.setInt(2, category.getCategoryId());
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

}