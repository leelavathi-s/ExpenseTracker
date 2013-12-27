package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Report
{
	private Date weeklyReportStartDt = null;
	
	private Date weeklyReportEndDt=null;
	
	private ReportType reportType;

	public static String mySqlFormat= "YYYY-MM-dd";

	public Date getWeeklyReportStartDt()
	{/*
		if(this.weeklyReportEndDt==null)
		{
			this.weeklyReportStartDt = ExpenseTrackerUtility.getCurrentDate().getTime();
			return weeklyReportStartDt;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(weeklyReportEndDt);
			cal.add(Calendar.DATE,1);
    		
			this.weeklyReportStartDt =cal.getTime();

			return weeklyReportStartDt;
		}		
	*/
		return this.weeklyReportStartDt;
	}

	public Date getWeeklyReportEndDt() 
	{/*
		Calendar cal = Calendar.getInstance();
		cal.setTime(weeklyReportStartDt);

		weeklyReportEndDt = cal.getTime();
		Calendar tempCal = (Calendar) cal.clone();
		cal.add(Calendar.DAY_OF_MONTH, 6);
		if (cal.get(Calendar.MONTH) != tempCal.get(Calendar.MONTH)) 
		{
			cal.set(Calendar.DATE, tempCal.getActualMaximum(Calendar.DATE));
			cal.set(Calendar.MONTH, tempCal.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, tempCal.get(Calendar.YEAR));
		}
		weeklyReportEndDt = cal.getTime();
		return weeklyReportEndDt;
	*/
		return this.weeklyReportEndDt;
		}

	public ReportType getReportType() {
		return reportType;
	}

	public void setWeeklyReportStartDt(Date weeklyReportStartDt) {
		this.weeklyReportStartDt = weeklyReportStartDt;
	}

	public void setWeeklyReportEndDt(Date weeklyReportEndDt) {
		this.weeklyReportEndDt = weeklyReportEndDt;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
	
	
	public  static List<Double> retrievePriceAmountForWeeklyReport()throws SQLException
	{
		int maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInCurrentMonth(); 

		ResultSet resultSet = null;
		List<Double> priceList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				priceList = new ArrayList<Double>();
				Calendar cal=ExpenseTrackerUtility.getCurrentDate();
				Date currentDt=cal.getTime();
				
				for (int i = 0; i < maxWeeknumber; i++) 
				{

					Date startDt=currentDt;
					Calendar tempCal =(Calendar) cal.clone();
		        	cal.add(Calendar.DAY_OF_MONTH, 6);
		        	if(cal.get(Calendar.MONTH)!=tempCal.get(Calendar.MONTH))
		        	{
		        		cal.set(Calendar.DATE, tempCal.getActualMaximum(Calendar.DATE));
		        		cal.set(Calendar.MONTH,tempCal.get(Calendar.MONTH));
		        		cal.set(Calendar.YEAR,tempCal.get(Calendar.YEAR));
		        	}
		        	Date endDate=cal.getTime();

					resultSet = stmt
							.executeQuery("Select sum(price)  price "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid"
									+ " and orderDate between "
									+ "'"
									+ ExpenseTrackerUtility.formatDate(startDt,
											mySqlFormat)
									+ "' and "
									+ "'"
									+ ExpenseTrackerUtility.formatDate(endDate,
											mySqlFormat) + "'");
					
	        		cal.add(Calendar.DATE,1);

					endDate=cal.getTime();

					currentDt = endDate;

					while (resultSet.next()) 
					{
					
						priceList.add(resultSet.getDouble(1));
					}	

				}
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;

			}
		}
		return priceList;

	}
	public static List<Order> retrieveDataForWeeklyReport(String selectedRowString)throws SQLException
	{

		Calendar cal=ExpenseTrackerUtility.getCurrentDate();
		ResultSet resultSet = null;
		List<Order> orderArrayList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				orderArrayList = new ArrayList<Order>();
				if(selectedRowString!=null)
				{
				StringTokenizer stringTokenizer = new StringTokenizer(
						(String) selectedRowString, " ");
				ArrayList<String> arrayList = new ArrayList<String>();
				while (stringTokenizer.hasMoreElements())
				{
					arrayList.add((String) stringTokenizer.nextElement());

				}

					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid"
									+ " and orderDate between "
									+ "'"
									+ ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(2).replace('(', ' ').trim(), "dd-MMM-yyyy")),
											mySqlFormat)
									+ "' and "
									+ "'"
									+ ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(4).replace(')', ' ').trim(), "dd-MMM-yyyy")),
											mySqlFormat) + "'");
					while(resultSet.next())
					{
						Order order = new Order();
						order.setPurchaseDate(resultSet.getDate(1));
						order.setPrice(resultSet.getDouble(2));
						order.setQuantity(resultSet.getInt(3));
						order.setCategoryName(resultSet.getString(4));
						order.setShopName(resultSet.getString(5));
						order.setBrandName(resultSet.getString(6));
						order.setProductName(resultSet.getString(7));
						orderArrayList.add(order);
						
					}		

				}
			
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;

			}
		}
		return orderArrayList;

	}
	

}
