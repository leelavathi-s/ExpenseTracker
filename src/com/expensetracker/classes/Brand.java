package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Brand
{
	public String brandName;
	
	public int brandId;
	 
	 public Brand(String brandName)
	 {
		 this.brandName = brandName;
	 }

	public Brand() {
		// TODO Auto-generated constructor stub
	}

	public String getBrandName() {
		return brandName;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	 
	public static Vector<Brand> getAvailableBrands(Object obj)
	{
		Vector<Brand> brandList = new Vector<Brand>();
		Connection connection = ExpenseTrackerUtility.getConnection();
		ResultSet resultSet = null;
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				Product product = obj!=null?(Product)obj:null;
				if(product!=null)
				{
				 resultSet = stmt.executeQuery("Select * from Brand where productId=" + product.getProductId());
				}
				else
				{
					resultSet = stmt.executeQuery("Select * from Brand");
				}
				while (resultSet.next()) 
				{
					Brand brand = new Brand();
					brand.setBrandId(resultSet.getInt(1));
					brand.setBrandName(resultSet.getString(2));
					
					brandList.add(brand);
				
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return brandList;
	}

	public void addNewBrand(Object object)throws SQLException
	{
		Connection connection = ExpenseTrackerUtility.getConnection();
		Integer productId = null;
		
		if(connection!=null)
		{
			try 
			{
				Statement	stmt = connection.createStatement();
				Product product= object!=null?(Product) object:null;
				if(product!=null)
				{
					productId = product.getProductId();
				}
				stmt.executeUpdate("Insert into brand (brandName,productId) values(" + "'" + brandName + "'," + productId+")");
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw e;				
			}
		}

	}

	@Override
	public String toString()
	{
		
		return brandName;
	}
	
	
	}

