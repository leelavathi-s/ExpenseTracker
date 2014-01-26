package com.expensetracker.swing.pages.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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
import com.expensetracker.swing.pages.panel.MonthlyReportPanel;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class MonthlyFrame extends JFrame implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7950539357761926081L;
	JScrollPane jScrollPaneForMonthlyReport;
	JTable footerForMonthlyReport;
	JTable jTableForMonthlyreport;
	List<Report> dataListForMonthlyReport;
	JComboBox<String> jComboBoxForYearInput;
	JPanel jPanelForYearInput;
	JLabel jLabelForYearInput;
	JPanel jPanel;
	String categoryName;
	String year;
	ReportRequest reportReq;

	public  MonthlyFrame(ReportRequest reportRequest,JPanel jPanel)
	{
		this.jPanel = jPanel;
		this.reportReq = reportRequest;
		jPanelForYearInput = new JPanel();
		jPanelForYearInput.setLayout(new BoxLayout(jPanelForYearInput,
				BoxLayout.LINE_AXIS));

		jLabelForYearInput = new JLabel("Select an year");
		try 
		{
			jComboBoxForYearInput = new JComboBox<String>(
					Report.retrieveAvailableYears());
			jComboBoxForYearInput.addActionListener(new YearInputHandler());
			jComboBoxForYearInput.setPreferredSize(new Dimension(50, 20));
			jComboBoxForYearInput.setMaximumSize(new Dimension(150, 20));

		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		jPanelForYearInput.add(jLabelForYearInput);
		jPanelForYearInput.add(Box.createRigidArea(new Dimension(10, 50)));

		jPanelForYearInput.add(jComboBoxForYearInput);
		jPanelForYearInput.add(Box.createRigidArea(new Dimension(0, 50)));
		
		//If year in report request object is null, the call for Monthly frame display is from Monthly radio button select in Generate reports tab.
		if (reportRequest.getYear() == null) 
		{
			jPanelForYearInput.setVisible(true);

			jLabelForYearInput.setAlignmentX(Component.LEFT_ALIGNMENT);

			jPanel.add(jPanelForYearInput);
		} 
		else
		{
			jPanelForYearInput.setVisible(false);

		}
		retrieveAndBuildTabelForMonthlyReport(false, jPanel);

	}
public void retrieveAndBuildTabelForMonthlyReport(boolean yrOnchg,JPanel jPanel)
{
	if(yrOnchg)
	{
		jPanel.remove(jScrollPaneForMonthlyReport);
		jPanel.remove(footerForMonthlyReport);
		//Forcing to take value of year field from 'Select an yr' drop down rather from report request
		reportReq.setYear(null);
	}
	try
	
	{
		if(reportReq.getYear()==null)
		{
			reportReq.setYear((String)jComboBoxForYearInput.getSelectedItem());
			
		dataListForMonthlyReport = Report
				.retrievePriceForMonthlyReport(reportReq);
		}
		else
		{
			dataListForMonthlyReport = Report
					.retrievePriceForMonthlyReport(reportReq);
			
		}
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
	jTableForMonthlyreport = new JTable(new MyTableModelForMonthlyReport());
	jTableForMonthlyreport
			.setPreferredScrollableViewportSize(new Dimension(450, 70));
	jTableForMonthlyreport.setFillsViewportHeight(true);
	jTableForMonthlyreport.setAutoCreateRowSorter(true);
	jTableForMonthlyreport.setName(ReportType.MONTHLY.toString());

	jTableForMonthlyreport.addMouseListener(this);

	ExpenseTrackerUtility.initColumnSizes(jTableForMonthlyreport);

	for (int c = 0; c < jTableForMonthlyreport.getColumnCount() - 1; c++) 
	{
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForMonthlyreport
				.getColumnModel();
		TableColumn col = colModel.getColumn(c);
		col.setCellRenderer(new MonthColumnRender());

	}
	jScrollPaneForMonthlyReport = new JScrollPane(jTableForMonthlyreport);
	jScrollPaneForMonthlyReport.setVisible(true);
	jPanel.add(jScrollPaneForMonthlyReport);

	double totalAmountSpentPerMonth = 0.0;

	for (Report report : dataListForMonthlyReport) 
	{
		totalAmountSpentPerMonth += report
				.getTotalPricePerMonthForMonthlyReport();
	}

	footerForMonthlyReport = new JTable(1,
			jTableForMonthlyreport.getColumnCount());
	footerForMonthlyReport.setValueAt("Total", 0, 0);
	footerForMonthlyReport.setValueAt(
			new DecimalFormat("#0.00").format(totalAmountSpentPerMonth), 0,
			1);
	footerForMonthlyReport.setVisible(true);

	for (int columnIndex = 0; columnIndex < jTableForMonthlyreport
			.getColumnCount(); columnIndex++)
	{
		ExpenseTrackerUtility.setChildTableColumnWidth(
				footerForMonthlyReport, columnIndex, ExpenseTrackerUtility
						.getParentTableColumnWidth(jTableForMonthlyreport,
								columnIndex));
	}
	jPanel.add(footerForMonthlyReport);
}
	public class MyTableModelForMonthlyReport implements TableModel
 {

		private String[] columnNames = { "Month", "Amount spent(per month)" };

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
		public int getRowCount()
		{
			if (dataListForMonthlyReport != null
					&& !dataListForMonthlyReport.isEmpty()) 
			{
				return dataListForMonthlyReport.size();
			} 
			else 
			{
				return 0;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Report report = (Report) dataListForMonthlyReport.get(rowIndex);
			switch (columnIndex) 
			{
			case 0:
				return report.getMonthForMonthlyReport();
			case 1:
				return report.getTotalPricePerMonthForMonthlyReport();

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

	public class YearInputHandler implements ActionListener
	{
	
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			retrieveAndBuildTabelForMonthlyReport(true,jPanel);	
		}
		
	}

	
	public class MonthColumnRender extends JLabel implements TableCellRenderer
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
				setText("<html><a href=\" #\"> " + value
						+ "</a></html> ");
			
			return this;
		}

		   	
    }


	@Override
	public void mouseClicked(MouseEvent e)
	{

		JTable jTable = (JTable)e.getSource();
		String selectedRowString = (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
		createFrame(selectedRowString,jTable.getName(),reportReq.getYear()!=null?year:jComboBoxForYearInput.getSelectedItem());			
	
		
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
	
	public void createFrame(String selectedRowString,String name,Object yrSelected)
	{
		
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Monthly Report");
		reportReq.setMonth(selectedRowString);
		MonthlyReportPanel panel = new MonthlyReportPanel(reportReq,jFrame);
		jFrame.getContentPane().add(BorderLayout.CENTER,panel);
		jFrame.setVisible(true);
		jFrame.pack();
	
	
	}
	public void setVisible(boolean showFlag)
	{
		
		ExpenseTrackerUtility.showComponenets(showFlag, jScrollPaneForMonthlyReport);
		ExpenseTrackerUtility.showComponenets(showFlag, footerForMonthlyReport);
		ExpenseTrackerUtility.showComponenets(showFlag, jPanelForYearInput);


	}


}