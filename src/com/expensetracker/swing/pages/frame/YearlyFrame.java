package com.expensetracker.swing.pages.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

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
import com.expensetracker.classes.ReportType;
import com.expensetracker.swing.pages.panel.YearlyReportPanel;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class YearlyFrame extends JFrame implements MouseListener
{
	JScrollPane jScrollPaneForYearlyReport;
	JTable footerForYearlyReport;
	JTable jTableForYearlyreport;
	List<Report> dataListForYearlyReport;
	JComboBox<String> jComboBoxForYearInput;
	JPanel jPanelForYearInput;
	JLabel jLabelForYearInput;
	JPanel jPanel;

	public  YearlyFrame(JPanel jPanel)
	{
		this.jPanel = jPanel;
		retrieveAndBuildTabelForYearlyReport(jPanel);


	}
public void retrieveAndBuildTabelForYearlyReport(JPanel jPanel)
{
	
	try
	
	{
		dataListForYearlyReport = Report.retrievePriceAmountForYearlyReport();
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
	jTableForYearlyreport = new JTable(new MyTableModelForYearlyReport());
	jTableForYearlyreport
			.setPreferredScrollableViewportSize(new Dimension(450, 70));
	jTableForYearlyreport.setFillsViewportHeight(true);
	jTableForYearlyreport.setAutoCreateRowSorter(true);
	jTableForYearlyreport.setName(ReportType.YEARLY.toString());

	jTableForYearlyreport.addMouseListener(this);

	ExpenseTrackerUtility.initColumnSizes(jTableForYearlyreport);

	for (int c = 0; c < jTableForYearlyreport.getColumnCount() - 1; c++) 
	{
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForYearlyreport
				.getColumnModel();
		TableColumn col = colModel.getColumn(c);
		col.setCellRenderer(new YearColumnRender());

	}
	jScrollPaneForYearlyReport = new JScrollPane(jTableForYearlyreport);
	jScrollPaneForYearlyReport.setVisible(true);
	jPanel.add(jScrollPaneForYearlyReport);

	double totalAmountSpentPerMonth = 0.0;

	for (Report report : dataListForYearlyReport) 
	{
		totalAmountSpentPerMonth += report
				.getTotalPricePerYearForYearlyReport();
	}

	footerForYearlyReport = new JTable(1,
			jTableForYearlyreport.getColumnCount());
	footerForYearlyReport.setValueAt("Total", 0, 0);
	footerForYearlyReport.setValueAt(
			new DecimalFormat("#0.00").format(totalAmountSpentPerMonth), 0,
			1);
	footerForYearlyReport.setVisible(true);

	for (int columnIndex = 0; columnIndex < jTableForYearlyreport
			.getColumnCount(); columnIndex++)
	{
		ExpenseTrackerUtility.setChildTableColumnWidth(
				footerForYearlyReport, columnIndex, ExpenseTrackerUtility
						.getParentTableColumnWidth(jTableForYearlyreport,
								columnIndex));
	}
	jPanel.add(footerForYearlyReport);
}
	public class MyTableModelForYearlyReport implements TableModel
			{
			
			private String[] columnNames={"Year","Amount spent(per year)"};
			
			
			
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
				if(dataListForYearlyReport!=null && !dataListForYearlyReport.isEmpty())
				{	
					return dataListForYearlyReport.size();
				}	
				else
				{	
					return 0;
				}	
			}
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				Report report = (Report)dataListForYearlyReport.get(rowIndex);
				switch(columnIndex)	
				{
					case 0:
						return report.getYearForYearlyReport();
					case 1:
						return report.getTotalPricePerYearForYearlyReport();
						 
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

	

	
	public class YearColumnRender extends JLabel implements TableCellRenderer
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
		createFrame(selectedRowString,jTable.getName());			
	
		
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
	
	public void createFrame(String selectedRowString,String name)
	{
		
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Yearly Report");
		
		YearlyReportPanel panel = new YearlyReportPanel(selectedRowString,jFrame);
		jFrame.getContentPane().add(BorderLayout.CENTER,panel);
		jFrame.setVisible(true);
		jFrame.pack();
	
	
	}
	public void setVisible(boolean showFlag)
	{
		
		ExpenseTrackerUtility.showComponenets(showFlag, jScrollPaneForYearlyReport);
		ExpenseTrackerUtility.showComponenets(showFlag, footerForYearlyReport);
		ExpenseTrackerUtility.showComponenets(showFlag, jPanelForYearInput);


	}


}