package com.expensetracker.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExpenseTrackerUtility 

{
	
	public static boolean isNullOrEmpty(String str)
	{
		if(str==null || str.equals(""))
		{
			return true;
		}
		return false;
	}
	
	public static Connection getConnection()
	{
		Connection connection = null;
		try 
		{
			connection = DriverManager
					.getConnection(
							"jdbc:mysql://localhost:3306/expensetracker",
							"root", "123");
			
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

}
