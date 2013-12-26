package com.expensetracker.utility;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
	public static int getNumberOfWeeksInCurrentMonth()
	{
		Calendar cal= getCurrentDate();	
		int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return maxWeeknumber;

	}
	
	public static Calendar getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
   
	    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
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
