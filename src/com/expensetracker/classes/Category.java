package com.expensetracker.classes;

import java.sql.Connection;
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
	
	public static Vector<Category> getAvailableCategories()
	{
		Vector<Category> categoriesList = new Vector<Category>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery("Select * from Category");
				while (resultSet.next()) 
				{
					Category category= new Category();
					category.setCategoryId(resultSet.getInt(1));
					category.setCategoryName(resultSet.getString(2));
					categoriesList.add(category);
				
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public void addNewCategory()throws SQLException
	{
		System.out.println("Entered here in addnew category");
		Connection connection = ExpenseTrackerUtility.getConnection();
		
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				stmt.executeUpdate("Insert into category (CategoryName) values(" + "'" + categoryName + "')");
				System.out.println("Query:::" + stmt.toString());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}

	}
}
