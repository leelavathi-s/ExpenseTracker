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
	
	private String yearForYearlyReport;

	private String categoryName;
	
	private double totalPricePerCategory;
	
	private String productName;
	
	private double totalPricePerProduct;
	private double totalPricePerMonthForMonthlyReport;
	private double totalPricePerYearForYearlyReport;


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getTotalPricePerProduct() {
		return totalPricePerProduct;
	}

	public void setTotalPricePerProduct(double totalPricePerProduct) {
		this.totalPricePerProduct = totalPricePerProduct;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getTotalPricePerCategory() {
		return totalPricePerCategory;
	}

	public void setTotalPricePerCategory(double totalPricePerCategory) {
		this.totalPricePerCategory = totalPricePerCategory;
	}

	public double getTotalPricePerYearForYearlyReport() {
		return totalPricePerYearForYearlyReport;
	}

	public void setTotalPricePerYearForYearlyReport(
			double totalPricePerYearForYearlyReport) {
		this.totalPricePerYearForYearlyReport = totalPricePerYearForYearlyReport;
	}

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
	
	
	public String getYearForYearlyReport() {
		return yearForYearlyReport;
	}

	public void setYearForYearlyReport(String yearForYearlyReport) {
		this.yearForYearlyReport = yearForYearlyReport;
	}

	public  static List<Double> retrievePriceAmountForWeeklyReport(ReportRequest reportRequest)throws SQLException
	{
		int maxWeeknumber = 0;
		Calendar cal=null;

		if(reportRequest.getMonth()==null && reportRequest.getYear()==null)
		{	
			maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInCurrentMonth(); 
			cal = ExpenseTrackerUtility.getCurrentDate();
		}
		else
		{
			maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInMonth(reportRequest.getMonth(), reportRequest.getYear());
			cal= ExpenseTrackerUtility.getDateForSelectedMonthAndYr(reportRequest.getMonth(), reportRequest.getYear());
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
											mySqlFormat) + "'"
									+ (reportRequest.getCategory() != null ? " and cat.categoryName = '" + reportRequest.getCategory() +  "'" : "")
									+(reportRequest.getProduct() != null ? " and pro.productname = '" + reportRequest.getProduct() +  "'" : ""));
					
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
	public  static List<Report> retrievePriceAmountForYearlyReport()throws SQLException
	{
		ResultSet resultSet = null;
		List<Report> priceList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				priceList = new ArrayList<Report>();
				resultSet = stmt
							.executeQuery("select sum(price),YEAR(orderdate) from purchaseorder group by Year(orderdate)");
	     			while (resultSet.next()) 
					{
	     				Report report = new Report();
	     				report.setTotalPricePerYearForYearlyReport(resultSet.getDouble(1));
	     				report.setYearForYearlyReport(resultSet.getString(2));
						priceList.add(report);
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

	public static List<Order> retrieveDataForWeeklyReport(String selectedRowString,ReportRequest reportRequest)throws SQLException
	{

		Calendar cal=ExpenseTrackerUtility.getCurrentDate();
		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		StringBuffer queryStr=null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				queryStr = new StringBuffer();
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
				queryStr.append("Select orderDate,price,quantity,categoryName,shopName,brandName,productName ");
				queryStr.append("from purchaseorder po,category cat,shop,product pro,brand ");
				queryStr.append("where po.CategoryId = cat.CategoryId ");
				queryStr.append("and po.ShopId =  shop.ShopId ");
				queryStr.append("and pro.ProductId=po.ProductId ");
				queryStr.append("and brand.brandid=po.brandid ");
				queryStr.append(" and orderDate between ");
				queryStr.append("'");
				queryStr.append(ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(2).replace('(', ' ').trim(), "dd-MMM-yyyy")),
											mySqlFormat));
				queryStr.append("' and ");
				queryStr.append("'");
				queryStr.append(ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(4).replace(')', ' ').trim(), "dd-MMM-yyyy")),
											mySqlFormat) + "'");
				if(reportRequest.getCategory()!=null)
				{
					queryStr.append(" and cat.categoryName = '");
					queryStr.append(reportRequest.getCategory());
					queryStr.append("'");
					
				}
				if(reportRequest.getProduct()!=null)
				{
					queryStr.append(" and pro.productName = '");
					queryStr.append(reportRequest.getProduct());
					queryStr.append("'");
					
				}

					resultSet = stmt
							.executeQuery(queryStr.toString());
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
	public static List<Order> retrieveDataForMonthlyReport(ReportRequest reportRequest)throws SQLException
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
				if(reportRequest.getMonth()!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid "
									+"and monthname(orderdate)='" 
									+ reportRequest.getMonth() + "'"
									+ " and year(orderdate) = "
									+ reportRequest.getYear()
									+(reportRequest.getCategory()!=null? " and cat.categoryname ='" + reportRequest.getCategory() + "'":"")
									+(reportRequest.getProduct()!=null? " and pro.productname ='" + reportRequest.getProduct() + "'":""));
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
	public static List<Order> retrieveDataForYearlyReport(String year,String category,String product)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		StringBuffer queryStr=null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				orderArrayList = new ArrayList<Order>();
				if(year!=null)
				{
					queryStr = new StringBuffer();
					queryStr.append("Select orderDate,price,quantity,categoryName,shopName,brandName,productName ");
					queryStr.append("from purchaseorder po,category cat,shop,product pro,brand ");
					queryStr.append("where po.CategoryId = cat.CategoryId ");
					queryStr.append("and po.ShopId =  shop.ShopId ");
					queryStr.append("and pro.ProductId=po.ProductId ");
					queryStr.append("and brand.brandid=po.brandid ");
					queryStr.append(" and year(orderdate) = ");
					queryStr.append(year);
					if(category!=null)
					{
							queryStr.append(" and cat.categoryname ='");
							queryStr.append(category);
							queryStr.append("'");
					}
					resultSet = stmt
							.executeQuery(queryStr.toString());
						
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
	public static List<Order> retrieveDataForCategoryYearlyReport(ReportRequest reportRequest)throws SQLException
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
				if(reportRequest.getYear()!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid "
									+ " and year(orderdate) = "
									+ reportRequest.getYear()
									+" and cat.categoryName = '"
									+reportRequest.getCategory()
									+"'");
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

	public static List<Order> retrieveDataForProductYearlyReport(ReportRequest reportRequest)throws SQLException
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
				if(reportRequest.getYear()!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po,category cat,shop,product pro,brand "
									+ "where po.CategoryId = cat.CategoryId "
									+ "and po.ShopId =  shop.ShopId "
									+ "and pro.ProductId=po.ProductId "
									+ "and brand.brandid=po.brandid "
									+ " and year(orderdate) = "
									+ reportRequest.getYear()
									+" and pro.productName = '"
									+reportRequest.getProduct()
									+"'");
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
	public static List<Report> retrievePriceForCategoryWiseReport()throws SQLException
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
				resultSet = stmt
						.executeQuery("select sum(price),cat.categoryName,year(orderdate) from purchaseorder po,category cat where po.categoryId = cat.categoryId  group by po.categoryId,year(po.orderdate)");
								
				while (resultSet.next()) 
				{
					report = new Report();
					report.setCategoryName(resultSet.getString(2));
					report.setTotalPricePerCategory(resultSet.getDouble(1));
					report.setYearForYearlyReport(resultSet.getString(3));
					reportList.add(report);
					
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
	public static List<Report> retrievePriceForProductWiseReport()throws SQLException
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
				resultSet = stmt
						.executeQuery("select sum(price),prod.productname,year(orderdate) from purchaseorder po,product prod where po.productid = prod.productId  group by po.productId,year(po.orderdate)");
								
				while (resultSet.next()) 
				{
					report = new Report();
					report.setProductName(resultSet.getString(2));
					report.setTotalPricePerProduct(resultSet.getDouble(1));
					report.setYearForYearlyReport(resultSet.getString(3));
					reportList.add(report);
					
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



	public static List<Report> retrievePriceForMonthlyReport(ReportRequest reportRequest)throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;
		StringBuffer queryStr = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				Statement stmt = connection.createStatement();
				reportList = new ArrayList<Report>();
				queryStr = new StringBuffer();
				if(reportRequest.getYear()!=null)
				{
					String selectedYrStr = (String)reportRequest.getYear();
					queryStr.append(" SELECT sum(price) AS price, m.name FROM MONTHS m");
					queryStr.append(" LEFT JOIN purchaseorder po ON MONTH(STR_TO_DATE(CONCAT(m.name,'");
					queryStr.append(selectedYrStr);
					queryStr.append("'),'%M %Y')) = MONTH(po.orderDate) AND YEAR(po.orderDate) = '");
					queryStr.append(selectedYrStr);
					queryStr.append("'");
					if(reportRequest.getCategory()!=null)
					{
						queryStr.append("inner join category cat on po.categoryid=cat.categoryid and cat.categoryname='");
						queryStr.append(reportRequest.getCategory());
						queryStr.append("'");
					}
					if(reportRequest.getProduct()!=null)
					{
						queryStr.append("inner join product pro on po.productid=pro.productid and pro.productname='");
						queryStr.append(reportRequest.getProduct());
						queryStr.append("'");
					}
					queryStr.append(" GROUP BY m.name");
					queryStr.append(" ORDER BY m.id");
				resultSet = stmt
						.executeQuery(queryStr.toString());
								
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
