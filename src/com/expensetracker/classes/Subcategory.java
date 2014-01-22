package com.expensetracker.classes;

import java.sql.Connection;
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
		Statement stmt = null;
		Vector<Subcategory> subCategoriesList = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet = null;
		if (connection != null) {
			try {
				subCategoriesList = new Vector<Subcategory>();
				stmt = connection.createStatement();
				if (obj instanceof Category) 
				{
					Category categoryObj = obj != null ? (Category) obj : null;
					if (categoryObj != null)
					{
						resultSet = stmt
								.executeQuery("Select * from subCategory where categoryId = "
										+ categoryObj.getCategoryId()
										+ "  order by subcategoryName asc");
					}

				}
				else if(obj instanceof Product)
				{
					Product productObj = obj!=null?(Product)obj:null;
					
					if (productObj != null)
					{
						resultSet = stmt
								.executeQuery("select sub.subcategoryid,sub.subcategoryname from subcategory sub join product where sub.SubCategoryId = product.SubCategoryId"
										+" and product.productid = "
										+ productObj.getProductId());
					}
				}	
				else
				{
					resultSet = stmt
							.executeQuery("Select * from subCategory order by subcategoryName asc");
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

	public void updateSubCategory(Subcategory subCategory) throws SQLException {
		Statement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();

		if (connection != null) {
			try {
				stmt = connection.createStatement();
				stmt.executeUpdate("update subcategory set subcategoryName ="
						+ "'" + subCategoryName + "' where subcategoryId = "
						+ subCategory.getSubCategoryId());
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
	}

	public Subcategory addNewSubCategory(Object obj) throws SQLException {
		Connection connection = ExpenseTrackerUtility.getConnection();
		Subcategory subcategory = null;
		Statement stmt = null;
		Integer categoryId = null;
		if (connection != null) {
			try {
				stmt = connection.createStatement();
				Category category = obj != null ? (Category) obj : null;
				if (category != null) {
					categoryId = category.getCategoryId();
				}

				stmt.executeUpdate("Insert into subcategory (SubCategoryName,categoryId) values("
						+ "'" + subCategoryName + "'," + categoryId + ")");

				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT LAST_INSERT_ID()");
				subcategory = new Subcategory(subCategoryName);
				while (resultSet.next()) {
					subcategory.setSubCategoryId(resultSet.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return subcategory;

	}

	public void removeSubCategory(Subcategory subCategory) throws SQLException {
		Statement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();

		if (connection != null) {
			try {
				stmt = connection.createStatement();
				stmt.executeUpdate("delete from subcategory where subcategoryId = "
						+ subCategory.getSubCategoryId());
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}

	}

	@Override
	public String toString() {

		return subCategoryName;
	}

}