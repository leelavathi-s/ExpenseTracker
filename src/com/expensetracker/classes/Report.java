package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Report
{
	private Date weeklyReportStartDt = null;
	
	private Date weeklyReportEndDt=null;
	
	private ReportType reportType;

	public static String mySqlFormat= "yyyy-MM-dd";
	
	private String monthForMonthlyReport;
	
	private double totalPricePerMonthForMonthlyReport;

	public Date getWeeklyReportStartDt()
	{
		return this.weeklyReportStartDt;
	}

	public Date getWeeklyReportEndDt() 
	{		
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
	
	
	public  static List<Double> retrievePriceAmountForWeeklyReport(String monthSelected,String yrSelected)throws SQLException
	{
		int maxWeeknumber = 0;
		Calendar cal=null;

		if(monthSelected==null && yrSelected==null)
		{	
			maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInCurrentMonth(); 
			cal = ExpenseTrackerUtility.getCurrentDate();
		}
		else
		{
			maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInMonth(monthSelected, yrSelected);
			cal= ExpenseTrackerUtility.getDateForSelectedMonthAndYr(monthSelected, yrSelected);
		}
		ResultSet resultSet = null;
		List<Double> priceList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				priceList = new ArrayList<Double>();
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
					
						priceList.add(new Double(new DecimalFormat("#0.00").format(resultSet.getDouble(1))));
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
	public static List<Order> retrieveDataForMonthlyReport(String month,String year)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				orderArrayList = new ArrayList<Order>();
				if(month!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid "
									+"and monthname(orderdate)='" 
									+ month + "'"
									+ " and year(orderdate) = "
									+ year);
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

	public String getMonthForMonthlyReport() {
		return monthForMonthlyReport;
	}

	public void setMonthForMonthlyReport(String monthForMonthlyReport) {
		this.monthForMonthlyReport = monthForMonthlyReport;
	}

	public double getTotalPricePerMonthForMonthlyReport() {
		return totalPricePerMonthForMonthlyReport;
	}

	public void setTotalPricePerMonthForMonthlyReport(
			double totalPricePerMonthForMonthlyReport) {
		this.totalPricePerMonthForMonthlyReport = totalPricePerMonthForMonthlyReport;
	}

	public static Vector<String> retrieveAvailableYears() throws SQLException
	{

		ResultSet resultSet = null;
		Vector<String> yearList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				yearList = new Vector<String>();

				resultSet = stmt
						.executeQuery("select distinct year(orderdate) from purchaseorder");
								
				while (resultSet.next()) 
				{
					yearList.add(resultSet.getString(1));
					
				}

				
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;

			}
		}
		return yearList;

	
	
	}
	public static List<Report> retrievePriceForMonthlyReport(Object selectedYear)throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				reportList = new ArrayList<Report>();
				if(selectedYear!=null)
				{
					String selectedYrStr = (String)selectedYear;
				resultSet = stmt
						.executeQuery(" SELECT sum(price) AS price, m.name FROM MONTHS m" 
          +" LEFT JOIN purchaseorder po ON MONTH(STR_TO_DATE(CONCAT(m.name,'" + selectedYrStr + "'),'%M %Y')) = MONTH(po.orderDate) AND YEAR(po.orderDate) = '" + selectedYrStr +"'"
          + " GROUP BY m.name"
          + " ORDER BY m.id"
          );
								
				while (resultSet.next()) 
				{
					report = new Report();
					report.setMonthForMonthlyReport(resultSet.getString(2));
					report.setTotalPricePerMonthForMonthlyReport(new Double(ExpenseTrackerUtility.formatAmountWithTwoDecimalPlaces(resultSet.getDouble(1))));
					reportList.add(report);
					
				}

				}
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;

			}
		}
		return reportList;

	
	}
}