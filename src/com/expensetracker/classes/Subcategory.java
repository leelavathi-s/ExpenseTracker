package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Subcategory {

	private int subCategoryId;

	private String subCategoryName;

	public Subcategory(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public Subcategory() {
	}

	public int getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public static Vector<Subcategory> getAvailableSubCategories(Object obj)
			throws SQLException {
		PreparedStatement stmt = null;
		Vector<Subcategory> subCategoriesList = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet = null;
		if (connection != null) {
			try {
				subCategoriesList = new Vector<Subcategory>();
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.getAvailableSubCategoriesByCategory"));
				if (obj instanceof Category) 
				{
					Category categoryObj = obj != null ? (Category) obj : null;
					if (categoryObj != null)
					{
						stmt.setInt(1, categoryObj.getCategoryId());
						resultSet = stmt.executeQuery();
					}

				}
				else if(obj instanceof Product)
				{
					Product productObj = obj!=null?(Product)obj:null;
					
					if (productObj != null)
					{
						stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.getAvailableSubCategoriesByProduct"));
						stmt.setInt(1, productObj.getProductId());
						resultSet = stmt.executeQuery();
					}
				}	
				else
				{
					stmt=connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.getAvailableSubCategories"));
					resultSet = stmt.executeQuery();
				}
				while (resultSet.next()) {
					Subcategory subcategory = new Subcategory();
					subcategory.setSubCategoryId(resultSet.getInt(1));
					subcategory.setSubCategoryName(resultSet.getString(2));
					subCategoriesList.add(subcategory);

				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return subCategoriesList;
	}

	public void updateSubCategory(Subcategory subCategory) throws SQLException 
	{
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();

		if (connection != null) {
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.updateSubCategory"));
				stmt.setString(1, subCategoryName);
				stmt.setInt(2, subCategory.getSubCategoryId());
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

	public Subcategory addNewSubCategory(Object obj) throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Subcategory subcategory = null;
		PreparedStatement stmt = null;
		Integer categoryId = null;
		if (connection != null)
		{
			try
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.addSubCategory"));
				Category category = obj != null ? (Category) obj : null;
				if (category != null)
				{
					categoryId = category.getCategoryId();
				}

				stmt.setString(1, subCategoryName);
				stmt.setInt(2, categoryId);
				stmt.executeUpdate();

				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT LAST_INSERT_ID()");
				subcategory = new Subcategory(subCategoryName);
				while (resultSet.next())
				{
					subcategory.setSubCategoryId(resultSet.getInt(1));
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
		return subcategory;

	}

	public void removeSubCategory(Subcategory subCategory) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();

		if (connection != null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("SubCategory.deleteSubCategory"));
				stmt.setInt(1, subCategory.getSubCategoryId());
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
	public String toString() {

		return subCategoryName;
	}

}