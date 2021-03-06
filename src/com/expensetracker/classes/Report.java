package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
	private String subCategoryName;

	private double totalPricePerSubCategory;

	
	private double totalPricePerProduct;
	private double totalPricePerMonthForMonthlyReport;
	private double totalPricePerYearForYearlyReport;

	private double totalPriceForExtrasSelf;
	private double totalPriceForExtrasOthers;
	
	

	public double getTotalPriceForExtrasSelf() {
		return totalPriceForExtrasSelf;
	}

	public void setTotalPriceForExtrasSelf(double totalPriceForExtrasSelf) {
		this.totalPriceForExtrasSelf = totalPriceForExtrasSelf;
	}

	public double getTotalPriceForExtrasOthers() {
		return totalPriceForExtrasOthers;
	}

	public void setTotalPriceForExtrasOthers(double totalPriceForExtrasOthers) {
		this.totalPriceForExtrasOthers = totalPriceForExtrasOthers;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public double getTotalPricePerSubCategory() {
		return totalPricePerSubCategory;
	}

	public void setTotalPricePerSubCategory(double totalPricePerSubCategory) {
		this.totalPricePerSubCategory = totalPricePerSubCategory;
	}

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
		Calendar todaysDate  = ExpenseTrackerUtility.getCurrentDate();

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
		PreparedStatement stmt = null;
		StringBuffer additionalQueryString = null;
		if(connection!=null)
		{
			try 
			{
				priceList = new ArrayList<Double>();
				Date currentDt=cal.getTime();
				
				for (int i = 0; i < maxWeeknumber; i++) 
				{

					Date startDt=currentDt;
					Calendar tempCal =(Calendar) cal.clone();
		        	cal.add(Calendar.DAY_OF_MONTH, 6);
		        	if(reportRequest.getProduct()==null && reportRequest.getCategory()==null && reportRequest.getSubcategory()==null)
			        {
			        	if(cal.get(Calendar.MONTH)!=todaysDate.get(Calendar.MONTH))
			        	{
			        		break;
			        	}
			        }
		        	if(cal.get(Calendar.MONTH)!=tempCal.get(Calendar.MONTH))
		        	{
		        		cal.set(Calendar.DATE, tempCal.getActualMaximum(Calendar.DATE));
		        		cal.set(Calendar.MONTH,tempCal.get(Calendar.MONTH));
		        		cal.set(Calendar.YEAR,tempCal.get(Calendar.YEAR));
		        	}
		        	Date endDate=cal.getTime();
		        	additionalQueryString = new StringBuffer();
		        	String  thirdParameter = null;
		        	Properties properties = null;
		        	if(reportRequest.getCategory() != null)
		        	{
		        		additionalQueryString.append(" and cat.categoryName = ? ");
		        		thirdParameter = reportRequest.getCategory();
			        	 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceAmountForWeeklyReport", additionalQueryString.toString());

		        	}	
		        	if(reportRequest.getProduct()!=null)
		        	{
		        		additionalQueryString.append(" and pro.productname =? ");
		        		thirdParameter = reportRequest.getProduct();
		        		 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceAmountForWeeklyReport", additionalQueryString.toString());

		        	}
		        	if(reportRequest.getSubcategory()!=null)
		        	{
		        		additionalQueryString.append(" and sub.subcategoryname =? ");
		        		thirdParameter = reportRequest.getSubcategory();
		        		 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceAmountForWeeklyReport", additionalQueryString.toString());

		        	}

		        	stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceAmountForWeeklyReport",properties));

		        	stmt.setString(1, ExpenseTrackerUtility.formatDate(startDt,mySqlFormat));
		        	stmt.setString(2, ExpenseTrackerUtility.formatDate(endDate,mySqlFormat));
		        	if(thirdParameter!=null)
		        	{
		        		stmt.setString(3, thirdParameter);
		        	}
					resultSet = stmt.executeQuery();
					
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return priceList;

	}
	public  static List<Report> retrievePriceAmountForYearlyReport()throws SQLException
	{
		ResultSet resultSet = null;
		List<Report> priceList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		Statement stmt = null;
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				priceList = new ArrayList<Report>();
				resultSet = stmt.executeQuery(ExpenseTrackerUtility.getQuery("Report.retrievePriceAmountForYearlyReport"));
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);

			}
		}
		return priceList;

	}

	public static List<Order> retrieveDataForWeeklyReport(String selectedRowString,ReportRequest reportRequest)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		PreparedStatement stmt = null;
		if(connection!=null)
		{
			try 
			{
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
				String thirdParameter = null;
				Properties properties = null;
				StringBuffer additionalQueryString = new StringBuffer();
 
				if(reportRequest.getCategory()!=null)
				{
	        		additionalQueryString.append(" and cat.categoryName = ? ");
	        		thirdParameter = reportRequest.getCategory();
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForWeeklyReport", additionalQueryString.toString());
					
				}
				if(reportRequest.getSubcategory()!=null)
				{
	        		thirdParameter = reportRequest.getSubcategory();
	        		additionalQueryString.append(" and sub.subcategoryName  = ? ");
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForWeeklyReport", additionalQueryString.toString());					
				}
				if(reportRequest.getProduct()!=null)
				{
	        		thirdParameter = reportRequest.getProduct();
					additionalQueryString.append(" and pro.productName = ? ");
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForWeeklyReport", additionalQueryString.toString());					
				}
				
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveDataForWeeklyReport",properties));
				
	        	stmt.setString(1, ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(2).replace('(', ' ').trim(), "dd-MMM-yyyy")),
						mySqlFormat));
	        	stmt.setString(2, ExpenseTrackerUtility.formatDate((ExpenseTrackerUtility.convertStringToDate(arrayList.get(4).replace(')', ' ').trim(), "dd-MMM-yyyy")),
						mySqlFormat));
	        	if(thirdParameter!=null)
	        	{
	        		stmt.setString(3, thirdParameter);
	        	}
	        	
				resultSet = stmt.executeQuery();
				
					while(resultSet.next())
					{
						Order order = new Order();
						order.setPurchaseDate(resultSet.getDate(1));
						order.setPrice(resultSet.getDouble(2));
						order.setQuantity(resultSet.getDouble(3));
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return orderArrayList;

	}
	public static List<Order> retrieveDataForExtrasReport(ReportRequest reportRequest)throws SQLException
	{
		
		PreparedStatement preparedStatement = null;
		List<Order> orderArrayList = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				preparedStatement = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveDataForExtras"));
				preparedStatement.setString(1, reportRequest.getSubcategory());
				preparedStatement.setString(2, reportRequest.getYear());
				preparedStatement.setString(3, reportRequest.getMonth());
				
				ResultSet resultSet = preparedStatement.executeQuery();
				
				orderArrayList = new ArrayList<Order>();
				while(resultSet.next())
				{
					Order order = new Order();
					order.setPurchaseDate(resultSet.getDate(1));
					order.setPrice(resultSet.getDouble(2));
					order.setQuantity(resultSet.getDouble(3));
					order.setCategoryName(resultSet.getString(4));
					order.setShopName(resultSet.getString(5));
					order.setBrandName(resultSet.getString(6));
					order.setProductName(resultSet.getString(7));
					order.setSubcategoryName(resultSet.getString(8));
					order.setCommentTxt(resultSet.getString(9));
					orderArrayList.add(order);
					
				}		
				
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					throw e;

				}
				finally
				{
					ExpenseTrackerUtility.releaseResources(connection, preparedStatement);
				}
			}
			return orderArrayList;

		}

	public static List<Order> retrieveDataForMonthlyReport(ReportRequest reportRequest)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;

		Connection connection = ExpenseTrackerUtility.getConnection();
		PreparedStatement stmt = null;
		String thirdParameter = null;
		StringBuffer additionalQueryString=null;
		Properties properties = null;
		if(connection!=null)
		{
			try 
			{
				orderArrayList = new ArrayList<Order>();
        		additionalQueryString = new StringBuffer();

	        	if(reportRequest.getCategory() != null)
	        	{
	        		additionalQueryString.append(" and cat.categoryName = ? ");
	        		thirdParameter = reportRequest.getCategory();
		        	 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForMonthlyReport", additionalQueryString.toString());

	        	}	
	        	if(reportRequest.getProduct()!=null)
	        	{
	        		additionalQueryString.append(" and pro.productname =? ");
	        		thirdParameter = reportRequest.getProduct();
	        		 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForMonthlyReport", additionalQueryString.toString());

	        	}
	        	if(reportRequest.getSubcategory()!=null)
	        	{
	        		additionalQueryString.append(" and sub.subcategoryname =? ");
	        		thirdParameter = reportRequest.getSubcategory();
	        		 properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForMonthlyReport", additionalQueryString.toString());

	        	}
	        	
					
					stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveDataForMonthlyReport",properties));
					stmt.setString(1, reportRequest.getMonth());
					stmt.setString(2, reportRequest.getYear());
					
					if(thirdParameter!=null)
					{
						stmt.setString(3, thirdParameter);
					}
					resultSet = stmt.executeQuery();
					while(resultSet.next())
					{
						Order order = new Order();
						order.setPurchaseDate(resultSet.getDate(1));
						order.setPrice(resultSet.getDouble(2));
						order.setQuantity(resultSet.getDouble(3));
						order.setCategoryName(resultSet.getString(4));
						order.setShopName(resultSet.getString(5));
						order.setBrandName(resultSet.getString(6));
						order.setProductName(resultSet.getString(7));
						orderArrayList.add(order);
						
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
		return orderArrayList;

	}
	public static List<Order> retrieveDataForYearlyReport(String year,String category,String product)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		PreparedStatement stmt = null;
		if(connection!=null)
		{
			try 
			{
				
				String secondParameter = null;
				Properties properties = null;
				StringBuffer additionalQueryString = new StringBuffer();
 
				if(category!=null)
				{
	        		additionalQueryString.append(" and cat.categoryName = ? ");
	        		secondParameter = category;
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForYearlyReport", additionalQueryString.toString());
					
				}
				if(product!=null)
				{
					secondParameter = product;
	        		additionalQueryString.append(" and pro.productName = ? ");
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForYearlyReport", additionalQueryString.toString());					
				}
				
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveDataForYearlyReport",properties));
				stmt.setString(1, year);
				
				if(secondParameter!=null)
				{
					stmt.setString(2, secondParameter);
				}	
				orderArrayList = new ArrayList<Order>();
				
				resultSet = stmt.executeQuery();
					
				while(resultSet.next())
				{
					Order order = new Order();
					order.setPurchaseDate(resultSet.getDate(1));
					order.setPrice(resultSet.getDouble(2));
					order.setQuantity(resultSet.getDouble(3));
					order.setCategoryName(resultSet.getString(4));
					order.setShopName(resultSet.getString(5));
					order.setBrandName(resultSet.getString(6));
					order.setProductName(resultSet.getString(7));
					orderArrayList.add(order);
					
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
		return orderArrayList;

	}
	public static List<Order> retrieveDataForCategoryYearlyReport(ReportRequest reportRequest)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		PreparedStatement stmt = null;
		Properties properties=null;
		String thirdParameter = null;
		StringBuffer additionalQueryString = new StringBuffer();
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				
				if(reportRequest.getMonth()!=null)
				{
	        		additionalQueryString.append(" and monthname(orderdate) = ? ");
	        		thirdParameter = reportRequest.getMonth();
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrieveDataForCategoryYearlyReport", additionalQueryString.toString());

				}
				
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveDataForCategoryYearlyReport",properties));
				stmt.setString(1, reportRequest.getYear());
				stmt.setString(2, reportRequest.getCategory());
				
				if(thirdParameter!=null)
				{
					stmt.setString(3, reportRequest.getMonth());
				}
				
				orderArrayList = new ArrayList<Order>();
				if(reportRequest.getYear()!=null)
				{
				
					resultSet = stmt.executeQuery();
					while(resultSet.next())
					{
						Order order = new Order();
						order.setPurchaseDate(resultSet.getDate(1));
						order.setPrice(resultSet.getDouble(2));
						order.setQuantity(resultSet.getDouble(3));
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return orderArrayList;

	}

	public static List<Order> retrieveDataForProductYearlyReport(ReportRequest reportRequest)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		Statement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				orderArrayList = new ArrayList<Order>();
				if(reportRequest.getYear()!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName "
									+ "from purchaseorder po left join shop "
									+ "on po.ShopId =  shop.ShopId "
									+ "left join brand on brand.brandid=po.brandid "
									+ "join category cat join product pro "
									+ "where po.CategoryId = cat.CategoryId "
									+"and pro.ProductId=po.ProductId "
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
						order.setQuantity(resultSet.getDouble(3));
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return orderArrayList;

	}
	public static List<Order> retrieveDataForSubCategoryYearlyReport(ReportRequest reportRequest)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		Statement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.createStatement();
				orderArrayList = new ArrayList<Order>();
				if(reportRequest.getYear()!=null)
				{
				
					resultSet = stmt
							.executeQuery("Select orderDate,price,quantity,categoryName,shopName,brandName,productName,subcategoryname "
									+ "from purchaseorder po left join shop "
									+ "on po.ShopId =  shop.ShopId "
									+ "left join brand on brand.brandid=po.brandid "
									+ "join category cat join product pro join subcategory sub"
									+ " where po.CategoryId = cat.CategoryId "
									+" and pro.ProductId=po.ProductId "
									+" and po.subcategoryid = sub.subcategoryid "
									+ " and year(orderdate) = '"
									+ reportRequest.getYear()
									+" ' and sub.subcategoryname = '"
									+reportRequest.getSubcategory()
									+"'");
					while(resultSet.next())
					{
						Order order = new Order();
						order.setPurchaseDate(resultSet.getDate(1));
						order.setPrice(resultSet.getDouble(2));
						order.setQuantity(resultSet.getDouble(3));
						order.setCategoryName(resultSet.getString(4));
						order.setShopName(resultSet.getString(5));
						order.setBrandName(resultSet.getString(6));
						order.setProductName(resultSet.getString(7));
						order.setSubcategoryName(resultSet.getString(8));

						orderArrayList.add(order);
						
					}		

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
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveAvailableYears"));
				yearList = new Vector<String>();

				resultSet = stmt.executeQuery();
								
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return yearList;	
	}
	
	
	public static Vector<String> retrieveAvailableMonths() throws SQLException
	{

		ResultSet resultSet = null;
		Vector<String> monthList = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveAvailableMonths"));
				monthList = new Vector<String>();

				resultSet = stmt.executeQuery();
								
				while (resultSet.next()) 
				{
					monthList.add(resultSet.getString(1));
					
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
		return monthList;	
	}
	
	public static List<Report> retrievePriceForCategoryWiseReport(ReportRequest reportRequest)throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceForCategoryWiseReport"));
				
				stmt.setString(1, reportRequest.getMonth());
				reportList = new ArrayList<Report>();
				resultSet = stmt.executeQuery();
								
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return reportList;

	
	}
	public static List<Order> retrieveLastPurchaseInfo(int productId)throws SQLException
	{

		ResultSet resultSet = null;
		List<Order> orderArrayList = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrieveLastPurchaseInfo"));
				stmt.setInt(1, productId);
				stmt.setInt(2, productId);

				orderArrayList = new ArrayList<Order>();
				resultSet = stmt.executeQuery();
								
				while (resultSet.next()) 
				{
					Order order = new Order();
					order.setPurchaseDate(resultSet.getDate(1));
					order.setPrice(resultSet.getDouble(2));
					order.setQuantity(resultSet.getDouble(3));
					order.setCategoryName(resultSet.getString(4));
					order.setShopName(resultSet.getString(5));
					order.setBrandName(resultSet.getString(6));
					order.setProductName(resultSet.getString(7));
					order.setSubcategoryName(resultSet.getString(8));
					order.setCommentTxt(resultSet.getString(9));
					orderArrayList.add(order);
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
		return orderArrayList;

	
	}
	
	
	public static List<Report> retrievePriceForProductWiseReport()throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceForProductWiseReport"));
				reportList = new ArrayList<Report>();
				resultSet = stmt.executeQuery();
								
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
			finally
			{
				ExpenseTrackerUtility.releaseResources(connection, stmt);
			}
		}
		return reportList;

	
	}

	public static List<Report> retrievePriceForSubCategoryWiseReport()throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceForSubCategoryWiseReport"));
				reportList = new ArrayList<Report>();
				resultSet = stmt.executeQuery();
								
				while (resultSet.next()) 
				{
					report = new Report();
					report.setSubCategoryName(resultSet.getString(2));
					report.setTotalPricePerSubCategory(resultSet.getDouble(1));
					report.setYearForYearlyReport(resultSet.getString(3));
					reportList.add(report);
					
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
		return reportList;

	
	}

	public static List<Report> retrievePriceForExtras(ReportRequest reportRequest) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		List<Report> priceList = null; 
		if(connection!=null)
		{
			try 
			{
				
				if(reportRequest.getYear()!=null)
				{
					String selectedYrStr = (String)reportRequest.getYear();
		
					stmt= connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceForExtrasMonthlyReport"));
					stmt.setString(1, selectedYrStr);
					stmt.setString(2, selectedYrStr);
					
					ResultSet resultSet = stmt.executeQuery();
					
					priceList = new ArrayList<Report>();
					while(resultSet.next())
					{
						Report report = new Report();
						report.setMonthForMonthlyReport(resultSet.getString(2));
						report.setCategoryName(resultSet.getString(3));
						report.setSubCategoryName(resultSet.getString(4));
						report.setTotalPricePerMonthForMonthlyReport(resultSet.getDouble(1));
						priceList.add(report);
					}
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
		return priceList;

	}
	public static List<Report> retrievePriceForMonthlyReport(ReportRequest reportRequest)throws SQLException
	{


		Report report = null;
		ResultSet resultSet = null;
		List<Report> reportList = null;
		Properties properties = null;
		PreparedStatement stmt = null;
		Connection connection = ExpenseTrackerUtility.getConnection();
		if(connection!=null)
		{
			try 
			{
				reportList = new ArrayList<Report>();
				if(reportRequest.getYear()!=null)
				{
					String selectedYrStr = (String)reportRequest.getYear();
					StringBuffer additionalQueryString = new StringBuffer();
					String thirdParameter = null;
					
					if(reportRequest.getCategory()!=null)
					{
		        		additionalQueryString.append(" inner join category cat on po.categoryid=cat.categoryid and cat.categoryname= ?");
		        		thirdParameter = reportRequest.getCategory();
			        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceForMonthlyReport", additionalQueryString.toString());
						
					}

					if(reportRequest.getProduct()!=null)
					{
		        		additionalQueryString.append(" inner join product pro on po.productid=pro.productid and pro.productname= ?");
		        		thirdParameter = reportRequest.getProduct();
			        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceForMonthlyReport", additionalQueryString.toString());
						
					}
					if(reportRequest.getSubcategory()!=null)
					{
		        		additionalQueryString.append(" inner join subcategory sub on sub.subcategoryid=po.subcategoryid and sub.subcategoryname= ?");
		        		thirdParameter = reportRequest.getSubcategory();
			        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceForMonthlyReport", additionalQueryString.toString());
						
					}
					
					additionalQueryString.append(" GROUP BY m.name");
					additionalQueryString.append(" ORDER BY m.id");
					
		        	properties = ExpenseTrackerUtility.getAndSetProperty("Report.retrievePriceForMonthlyReport", additionalQueryString.toString());

					stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Report.retrievePriceForMonthlyReport",properties));

					stmt.setString(1, selectedYrStr);
					stmt.setString(2, selectedYrStr);
					
					if(thirdParameter!=null)
					{
						stmt.setString(3, thirdParameter);
					}
					resultSet = stmt.executeQuery();
					List<Report> priceListForExtras = retrievePriceForExtras(reportRequest);
			
					while (resultSet.next()) 
					{
						report = new Report();
						report.setMonthForMonthlyReport(resultSet.getString(2));
						report.setTotalPricePerMonthForMonthlyReport(new Double(ExpenseTrackerUtility.formatAmountWithTwoDecimalPlaces(resultSet.getDouble(1))));
						for(Report extrasInfoReport:priceListForExtras)
						{
							// Setting price information month-wise
							if(report.getMonthForMonthlyReport().equals(extrasInfoReport.getMonthForMonthlyReport()))
							{
								if("Self".equals(extrasInfoReport.getSubCategoryName()))
								{
									report.setTotalPriceForExtrasSelf(extrasInfoReport.getTotalPricePerMonthForMonthlyReport());

								}	
								else if("Others".equals(extrasInfoReport.getSubCategoryName()))
								{
									report.setTotalPriceForExtrasOthers(extrasInfoReport.getTotalPricePerMonthForMonthlyReport());
								}	
							}
						}	
						reportList.add(report);
						
					}

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
		
		List<Report> priceListForExtras = retrievePriceForExtras(reportRequest);
		return reportList;

	
	}
}