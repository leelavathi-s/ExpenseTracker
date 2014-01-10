package com.expensetracker.utility;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ExpenseTrackerUtility 

{
	public static void showComponenets(boolean showFlag,Component component)
	{
		if(component!=null)
		{
			component.setVisible(showFlag);
		}
	}
	public static void showFailureMessage(Component component,String message,SQLException exception)
	{
		if(exception!=null)
		{	
			JOptionPane.showMessageDialog(component,message	+ exception.getMessage() + ".\nFix the error and try again", "Error",
				JOptionPane.ERROR_MESSAGE);
		}	
		else
		{
			JOptionPane.showMessageDialog(component,message, "Error",
					JOptionPane.ERROR_MESSAGE);

		}

	}
	public static void releaseResources(Connection connection,Statement statement) throws SQLException
  {

		if (connection != null) 
		{
			connection.close();

		}

		if (statement != null)
		{
			statement.close();
		}

	}
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
	public static int getNumberOfWeeksInCurrentMonth()
	{
		Calendar cal= getCurrentDate();	
        return cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

	}
	public static int getMonth(String month)
	{
		switch(month)
		{
		case "January":
			return 0;
		case "Febraury":
			return 1;
		case "March":
			return 2;
		case "April":
			return 3;
		case "May":
			return 4;
		case "June":
			return 5;
		case "July":
			return 6;
		case "August":
			return 7;
		case "September":
			return 8;
		case "October":
			return 9;
		case "November":
			return 10;
		case "December":
			return 11;
			
		}
		return 0;
	}
	public static int getNumberOfWeeksInMonth(String month,String year)
	{
		Calendar cal = Calendar.getInstance();
		   
	    cal.set(Calendar.YEAR, new Integer(year));
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.MONTH, getMonth(month));

		return cal.getActualMaximum(Calendar.WEEK_OF_MONTH); 
	}
	public static Calendar getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
   
	    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        return cal;

	}
	public static Calendar getDateForSelectedMonthAndYr(String month,String year)
	{

		Calendar cal = Calendar.getInstance();
   
	    cal.set(Calendar.YEAR, new Integer(year));
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.MONTH, getMonth(month));
        return cal;

		
	}
	public static String formatDate(Date date,String pattern)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	public static void initColumnSizes(JTable table) 
    {
    	
		for (int i = 0; i < table.getColumnCount(); i++)
		{
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
					.getColumnModel();
			TableColumn col = colModel.getColumn(i);
			int width;

			TableCellRenderer renderer = col.getHeaderRenderer();
			if (renderer == null)
			{
				renderer = table.getTableHeader().getDefaultRenderer();
			}
			Component comp = renderer.getTableCellRendererComponent(table,
					col.getHeaderValue(), false, false, 0, 0);
			width = comp.getPreferredSize().width;

			for (int r = 0; r < table.getRowCount(); r++) 
			{
				renderer = table.getCellRenderer(r, i);
				comp = renderer.getTableCellRendererComponent(table,
						table.getValueAt(r, i), false, false, r, i);
				int currentWidth = comp.getPreferredSize().width;
				width = Math.max(width, currentWidth);
			}

			width += 2 * 2;
			col.setPreferredWidth(width);
			col.setWidth(width);
		}

	}
	public static int getParentTableColumnWidth(JTable jTable,int columnIndex)
	{
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTable
					.getColumnModel();
			TableColumn col = colModel.getColumn(columnIndex);
			return col.getWidth();
		
	}	
	public static void setChildTableColumnWidth(JTable jTable,int columnIndex,int columnWidth)
	{
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTable
					.getColumnModel();
			TableColumn col = colModel.getColumn(columnIndex);
			TableCellRenderer renderer = jTable.getCellRenderer(0, columnIndex);
			Component comp = renderer.getTableCellRendererComponent(jTable,
					jTable.getValueAt(0, columnIndex), false, false, 0, columnIndex);
			int currentWidth = comp.getPreferredSize().width;
			int width = Math.max(columnWidth, currentWidth);
		
			 col.setWidth(width);
			 col.setPreferredWidth(width);
		
	}
	public static String formatAmountWithTwoDecimalPlaces(double amount)
	{
		return new DecimalFormat("#0.00").format(amount);
	}

	public static Date convertStringToDate(String dateString,String pattern)
	{
		Date date = null;
		try 
		{
			date = new SimpleDateFormat(pattern, Locale.ENGLISH).parse(dateString);
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}
	}
