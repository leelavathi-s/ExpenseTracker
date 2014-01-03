package com.expensetracker.swing.pages;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;

import javax.jws.Oneway;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Order;
import com.expensetracker.classes.Report;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class MonthlyReportPanel extends JPanel implements ItemListener
{
	String selectedMonth = null;
	List<Order> orderList = null;
	String selectedYear = null;
GenerateReportsPanel generateReportsPanel = null;
	public MonthlyReportPanel(String selectedRowString,String selectedYear,GenerateReportsPanel generateReportsPanel)
	{
		this.selectedMonth = selectedRowString;
		this.selectedYear = selectedYear;
		this.generateReportsPanel = generateReportsPanel;

		buildGUI();
	}
	public MonthlyReportPanel()
	{
		buildGUI();	
		
	}
	
	public void buildGUI()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
		this.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
					.createTitledBorder("Report of " + selectedMonth + "-" + selectedYear),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)));
			
		try 
		{
			orderList = Report.retrieveDataForMonthlyReport(selectedMonth,selectedYear);
		} 
		catch (SQLException sqlException)
		{
			JOptionPane.showMessageDialog(this,
					"Retrieval failed because of an error. \nError Message  "
							+ sqlException.getMessage()
							+ ".\nFix the error and try again", "Error",
					JOptionPane.ERROR_MESSAGE);

			sqlException.printStackTrace();
		}
		if(!orderList.isEmpty() && orderList.size()>0)
		{
			JTable jTable = new JTable(new MyTableModel());
			jTable.setPreferredScrollableViewportSize(new Dimension(450, 70));
			jTable.setFillsViewportHeight(true);
			jTable.setAutoCreateRowSorter(true);
			
			ExpenseTrackerUtility.initColumnSizes(jTable);

			JScrollPane jScrollPane = new JScrollPane(jTable);
			
			this.add(jScrollPane);
			double totalAmountSpent =0.0;
			for(Order order:orderList)
			{	
				totalAmountSpent+= order.getPrice();
			}
			 JTable footer = new JTable(1,jTable.getColumnCount());
			    footer.setValueAt("Total", 0, 5);
			    footer.setValueAt(ExpenseTrackerUtility.formatAmountWithTwoDecimalPlaces(totalAmountSpent), 0, 6);
			    
			    for(int columnIndex=0;columnIndex<jTable.getColumnCount();columnIndex++)
			    {	
			    	ExpenseTrackerUtility.setChildTableColumnWidth(footer, columnIndex, ExpenseTrackerUtility.getParentTableColumnWidth(jTable, columnIndex));
			    }	
			    this.add(footer);
			    
			    JCheckBox jCheckBox = new JCheckBox("Weekly Split-up");
			    jCheckBox.setSelected(false);
			    jCheckBox.addItemListener(this);
			    jCheckBox.setName("Weekly");
			    this.add(jCheckBox);

		}
		else
		{
			JLabel jLabel = new JLabel("No records found for the selected week.");
			this.add(jLabel);
		}

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

	
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
	    if(e.getStateChange() == ItemEvent.SELECTED)
		{
			//GenerateReportsPanel generateReportsPanel = new GenerateReportsPanel();
			generateReportsPanel.retrieveAndBuildTableDataForWeeklyReport(selectedMonth, selectedYear,this);
		}
	    if(e.getStateChange() == ItemEvent.DESELECTED)
	  	{
	    	JFrame jFrame = generateReportsPanel.jFrame;
	    	//JTable jTable = generateReportsPanel.footerForWeeklyReport;
	    	//Component[]  components = this.getComponents();
	    	for(Component components:this.getComponents())
	    	{
	    		if("jScrollPaneForWeeklyReport".equals(components.getName()) || "footerForWeeklyReport".equals(components.getName()))
	    		{
	    			components.setVisible(false);

	    		}	
	    	}
	    	jFrame.pack();
 	  	}
	    
			
	}

}