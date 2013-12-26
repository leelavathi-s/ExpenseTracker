package com.expensetracker.swing.pages;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Order;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class WeeklyReportPanel  extends JPanel
{

	String selectedRowString = null;
	List<Order> orderList = null;

	WeeklyReportPanel  weeklyReportPanel=null;
	public WeeklyReportPanel(String selectedRowString)
	{
		this.selectedRowString = selectedRowString;
	}
	
	public WeeklyReportPanel() 
	{
		// TODO Auto-generated constructor stub
	}

	public WeeklyReportPanel buildGUI()
	{
		  weeklyReportPanel= new WeeklyReportPanel();
		weeklyReportPanel.setLayout(new BoxLayout(weeklyReportPanel, BoxLayout.PAGE_AXIS));
			
		weeklyReportPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
					.createTitledBorder("Week Report"),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)));
			
		try 
		{
			orderList = Order.retrieveDataForWeeklyReport(selectedRowString);
		} 
		catch (SQLException sqlException)
		{
			JOptionPane.showMessageDialog(weeklyReportPanel,
					"Retrieval failed because of an error. \nError Message  "
							+ sqlException.getMessage()
							+ ".\nFix the error and try again", "Error",
					JOptionPane.ERROR_MESSAGE);

			sqlException.printStackTrace();
		}
		if(!orderList.isEmpty() && orderList.size()>0)
		{
			JTable jTable = new JTable(new MyTableModel());
			jTable.setPreferredScrollableViewportSize(new Dimension(350, 70));
			jTable.setFillsViewportHeight(true);
			jTable.setAutoCreateRowSorter(true);
			
			ExpenseTrackerUtility.initColumnSizes(jTable);

			JScrollPane jScrollPane = new JScrollPane(jTable);
			
			weeklyReportPanel.add(jScrollPane);
		}
		else
		{
			JLabel jLabel = new JLabel("No records found for the selected week.");
			weeklyReportPanel.add(jLabel);
		}
		return weeklyReportPanel;

	}
	
	public class MyTableModel implements TableModel
	{
		
		private String[] columnNames={"Order Date","Category","Product","Brand","Shop Name","Quantity","Price"};
		

		
		@Override
		public void addTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return orderList.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Order order = (Order)orderList.get(rowIndex);
			switch(columnIndex)	
			{
				case 0:
					return order.getPurchaseDate();
				case 1:
					return order.getCategoryName();
				case 2:
					return order.getProductName();
				case 3:
					return order.getBrandName();
				case 4:
					return order.getShopName();
				case 5:
					return order.getQuantity();
				case 6:
					return order.getPrice();
					
				 
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
