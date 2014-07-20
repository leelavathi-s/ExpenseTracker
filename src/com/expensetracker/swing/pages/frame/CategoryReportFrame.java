package com.expensetracker.swing.pages.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Report;
import com.expensetracker.classes.ReportRequest;
import com.expensetracker.classes.ReportType;
import com.expensetracker.swing.pages.panel.CategoryReportPanel;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class CategoryReportFrame extends JFrame implements MouseListener,ItemListener
 {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7099256350099169101L;
	JScrollPane jScrollPaneForCategoryWiseReport;
	JTable footerForCategoryWiseReport;
	JTable jTableForCategoryWiseReport;
	List<Report> dataListForCategoryWiseReport;
	JPanel jPanel;
	JCheckBox jCheckBoxForMonthSelection;
	List<HashMap<String, JScrollPane>> newScrollPaneList = new ArrayList();
	List<HashMap<String, JTable>> newTableList = new ArrayList();
	String currentMonth= null;
	String previousMonth=null;
	static int count=0;
	List<JCheckBox> checkBoxList = null;
	JFrame jFrame;
	JLabel jLabelForMonth = null;
	JPanel jPanel2 = null;

	public CategoryReportFrame(JPanel jPanel,JFrame parentfFrame) 
	{
		this.jPanel = jPanel;
		this.jFrame = parentfFrame;
		currentMonth=ExpenseTrackerUtility.getMonthName(ExpenseTrackerUtility.getCurrentDate().get(Calendar.MONTH));
		checkBoxList = new ArrayList<JCheckBox>();
		
		Vector<String> monthList = new Vector<String>();
		try 
		{
			monthList = Report.retrieveAvailableMonths();
		} 
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 jPanel2 = new JPanel(new GridLayout(1,monthList.size()));
		
		
		JLabel jLabelForMonth = new JLabel("Select month");
		jPanel2.add(jLabelForMonth);
		for(String month:monthList)
		{
			jCheckBoxForMonthSelection = new JCheckBox(month);
			jCheckBoxForMonthSelection.addItemListener(this);

			jPanel2.add(jCheckBoxForMonthSelection);
			checkBoxList.add(jCheckBoxForMonthSelection);
			if(currentMonth.equals(jCheckBoxForMonthSelection.getText()))
			{
				jCheckBoxForMonthSelection.setSelected(true);				
				jCheckBoxForMonthSelection.addItemListener(this);

			}

		}
		jPanel.add(jPanel2);

	}

	public void retrieveAndBuildTabelForCategoryWiseReport(JPanel jPanel,String month) 
	{

		try

		{
			ReportRequest reportRequest = new ReportRequest();
			reportRequest.setMonth(month);
			dataListForCategoryWiseReport = Report
					.retrievePriceForCategoryWiseReport(reportRequest);

		} catch (SQLException sqlException) {
			JOptionPane.showMessageDialog(this,
					"Retrieval failed because of an error. \nError Message  "
							+ sqlException.getMessage()
							+ ".\nFix the error and try again", "Error",
					JOptionPane.ERROR_MESSAGE);

			sqlException.printStackTrace();
		}
		
		

		jTableForCategoryWiseReport = new JTable(
				new MyTableModelForCategoryWiseReport(dataListForCategoryWiseReport,month));
		jTableForCategoryWiseReport
				.setPreferredScrollableViewportSize(new Dimension(450, 70));
		jTableForCategoryWiseReport.setFillsViewportHeight(true);
		jTableForCategoryWiseReport.setAutoCreateRowSorter(true);
		jTableForCategoryWiseReport.setName(ReportType.CATEGORY.toString());

		jTableForCategoryWiseReport.addMouseListener(this);



		ExpenseTrackerUtility.initColumnSizes(jTableForCategoryWiseReport);

		DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForCategoryWiseReport
				.getColumnModel();
		TableColumn col = colModel.getColumn(0);
		col.setCellRenderer(new CategoryWiseRenderer());
		jScrollPaneForCategoryWiseReport = new JScrollPane(
				jTableForCategoryWiseReport);
		jScrollPaneForCategoryWiseReport.setVisible(true);
		
		jScrollPaneForCategoryWiseReport.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory
				.createTitledBorder("Report of Month -  " + month),
		BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		jPanel.add(jScrollPaneForCategoryWiseReport);

		double totalAmountSpentPerMonth = 0.0;

		for (Report report : dataListForCategoryWiseReport)
		{
			totalAmountSpentPerMonth += report.getTotalPricePerCategory();
		}

		footerForCategoryWiseReport = new JTable(1,
				jTableForCategoryWiseReport.getColumnCount());
		footerForCategoryWiseReport.setValueAt("Total", 0, 1);
		footerForCategoryWiseReport.setValueAt(
				new DecimalFormat("#0.00").format(totalAmountSpentPerMonth), 0,
				2);
		footerForCategoryWiseReport.setVisible(true);

		for (int columnIndex = 0; columnIndex < jTableForCategoryWiseReport
				.getColumnCount(); columnIndex++) 
		{
			ExpenseTrackerUtility.setChildTableColumnWidth(
					footerForCategoryWiseReport, columnIndex,
					ExpenseTrackerUtility.getParentTableColumnWidth(
							jTableForCategoryWiseReport, columnIndex));
		}
		jPanel.add(footerForCategoryWiseReport);
	}

	public class MyTableModelForCategoryWiseReport implements TableModel 
	{
		List<Report> dataList = null;
		String month= null;
		public MyTableModelForCategoryWiseReport(List<Report>  dataList,String month)
		{
			this.dataList = dataList;
			this.month = month;
		}

		private String[] columnNames = { "Category", "Year",
				"Amount spent(per year)" };

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
			if (dataList != null
					&& !dataList.isEmpty()) 
			{
				return dataList.size();
			} 
			else 
			{
				return 0;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Report report = (Report) dataList
					.get(rowIndex);
			switch (columnIndex)
			{
			case 0:
				return report.getCategoryName();
			case 1:
				return report.getYearForYearlyReport();
			case 2:
				return report.getTotalPricePerCategory();

			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	public class CategoryWiseRenderer extends JLabel implements
			TableCellRenderer
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2717301993761887887L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			setText("<html><a href=\" #\"> " + value + "</a></html> ");

			return this;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

		JTable jTable = (JTable) e.getSource();
		MyTableModelForCategoryWiseReport myTableModelForCategoryWiseReport = (MyTableModelForCategoryWiseReport)jTable.getModel();
		Report selectedRowString = (Report) myTableModelForCategoryWiseReport.dataList
				.get(jTable.getSelectedRow());
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setCategory(selectedRowString.getCategoryName());
		reportRequest.setYear(selectedRowString.getYearForYearlyReport());
		reportRequest.setMonth(myTableModelForCategoryWiseReport.month);
		createFrame(reportRequest, jTable.getName());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void createFrame(ReportRequest reportRequest, String name) 
	{

		JFrame jFrame = new JFrame();
		jFrame.setTitle("Category wise Report");

		CategoryReportPanel panel = new CategoryReportPanel(reportRequest,
				jFrame);
		jFrame.getContentPane().add(BorderLayout.CENTER, panel);
		jFrame.setVisible(true);
		jFrame.pack();

	}

	public void setVisible(boolean showFlag) 
	{

		ExpenseTrackerUtility.showComponenets(showFlag,
				jScrollPaneForCategoryWiseReport);
		ExpenseTrackerUtility.showComponenets(showFlag,
				footerForCategoryWiseReport);
		
		ExpenseTrackerUtility.showComponenets(showFlag, jLabelForMonth);
		for(JCheckBox jCheckBox:checkBoxList)
		{
			ExpenseTrackerUtility.showComponenets(showFlag, jCheckBox);
		}	

	}

	public void recordPreviousReferences(String month)
	{
		HashMap<String, JScrollPane> tempScrollPaneReference = null;
		HashMap<String, JTable> tempJTableReference = null;
		if(jScrollPaneForCategoryWiseReport!=null)
		{
		    tempScrollPaneReference = new HashMap<>();
			tempScrollPaneReference.put(month, jScrollPaneForCategoryWiseReport);
		}
		
		if(footerForCategoryWiseReport!=null)
		{
			tempJTableReference = new HashMap<>();
			tempJTableReference.put(month,footerForCategoryWiseReport);
		}	
		
		newScrollPaneList.add(tempScrollPaneReference);
		newTableList.add(tempJTableReference);
	}
	
	public void findReference(String month)
	{
		for(int i=0;i<newScrollPaneList.size();i++)
		{
			HashMap<String, JScrollPane> hashMap = newScrollPaneList.get(i);
			JScrollPane jScrollPane = null;
			if(hashMap!=null)
			{
				 jScrollPane = hashMap.get(month);
			}
			if(jScrollPane!=null)
			{	
				jScrollPane.setVisible(false);
				hashMap.remove(month);
			}	
				
		}
		for(int i=0;i<newTableList.size();i++)
		{
			JTable jTable = null;
			HashMap<String, JTable> hashMap = newTableList.get(i);
			if(hashMap!=null)
			{
				jTable= hashMap.get(month);
			}	
			if(jTable!=null)
			{
				jTable.setVisible(false);
				hashMap.remove(month);

			}	
				
		}

	}
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		JCheckBox jCheckBox = (JCheckBox)e.getSource();
	    if(e.getStateChange() == ItemEvent.SELECTED)
		{
	    	retrieveAndBuildTabelForCategoryWiseReport(jPanel,jCheckBox.getText());
	    	
	    	recordPreviousReferences(jCheckBox.getText());
		}
	    if(e.getStateChange() == ItemEvent.DESELECTED)
		{
	    	findReference(jCheckBox.getText());	    	
	    	jCheckBox.setSelected(false);
		}
	    jFrame.pack();
		
	}

}