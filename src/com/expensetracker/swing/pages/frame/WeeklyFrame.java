package com.expensetracker.swing.pages.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.expensetracker.swing.pages.panel.WeeklyReportPanel;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class WeeklyFrame extends JFrame implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4521385530553068627L;
	List<Double> priceListForWeeklyReport;
	JTable jTableForWeeklyreport;
	JScrollPane jScrollPaneForWeeklyReport;
	JTable footerForWeeklyReport;
	String categoryName;
	ReportRequest reportRequest;
	
	public WeeklyFrame(ReportRequest reportRequest,JPanel panel)
	{
			this.reportRequest = reportRequest;
			double totalAmountSpentPerWeek=0;
			Report report = null;
			int maxWeeknumber = 0;
			Calendar cal = null;
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
	 		try 
			{
	 			priceListForWeeklyReport = Report.retrievePriceAmountForWeeklyReport(reportRequest);
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
			Object[][] rowData = new Object[maxWeeknumber][2];
			Date currentDt=cal.getTime();
			for(int i=0;i<maxWeeknumber;i++)
			{
				
				for(int j=0;j<2;j++)
				{
					if(j!=1)
					{	
						report = new Report();
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
			        	report.setWeeklyReportStartDt(startDt);
			        	report.setWeeklyReportEndDt(endDate);
						
			        	rowData[i][j] = report;
							
		        		cal.add(Calendar.DATE,1);
		        		
		        		endDate=cal.getTime();

						currentDt = endDate;

					
					}
					else
					{	
						rowData[i][j]=priceListForWeeklyReport.get(i);
						totalAmountSpentPerWeek+=(Double)rowData[i][j];
					}	
				}
			}
			buildTableForWeeklyreport(rowData,totalAmountSpentPerWeek,panel);

	}
	public void buildTableForWeeklyreport(Object[][] rowData,double totalAmountSpentPerWeek,JPanel panel)
	{
		jTableForWeeklyreport = new JTable(new MyTableModelForWeeklyReport(rowData));
		jTableForWeeklyreport.setPreferredScrollableViewportSize(new Dimension(450, 40));
		jTableForWeeklyreport.setFillsViewportHeight(true);
		jTableForWeeklyreport.setAutoCreateRowSorter(true);
		jTableForWeeklyreport.setName(ReportType.WEEKLY.toString());
		
		jTableForWeeklyreport.addMouseListener(this);
		//jTableForWeeklyreport.setName("jTableForWeeklyreport");

		ExpenseTrackerUtility.initColumnSizes(jTableForWeeklyreport);

		for (int c = 0; c < jTableForWeeklyreport.getColumnCount()-1; c++)
		{
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForWeeklyreport
					.getColumnModel();
			TableColumn col = colModel.getColumn(c);
			col.setCellRenderer(new WeekColumnRender());
			
		}
		 jScrollPaneForWeeklyReport = new JScrollPane(jTableForWeeklyreport);
		 jScrollPaneForWeeklyReport.setName("jScrollPaneForWeeklyReport");
		 jScrollPaneForWeeklyReport.setVisible(true);
		
	    panel.add(jScrollPaneForWeeklyReport);

	    footerForWeeklyReport = new JTable(1,jTableForWeeklyreport.getColumnCount());
	    footerForWeeklyReport.setValueAt("Total", 0, 0);
	    footerForWeeklyReport.setValueAt(ExpenseTrackerUtility.formatAmountWithTwoDecimalPlaces(totalAmountSpentPerWeek), 0, 1);
	    footerForWeeklyReport.setName("footerForWeeklyReport");
	    footerForWeeklyReport.setVisible(true);
	    for(int columnIndex=0;columnIndex<jTableForWeeklyreport.getColumnCount();columnIndex++)
	    {	
	    	ExpenseTrackerUtility.setChildTableColumnWidth(footerForWeeklyReport, columnIndex, ExpenseTrackerUtility.getParentTableColumnWidth(jTableForWeeklyreport, columnIndex));
	    }	
	    panel.add(footerForWeeklyReport);
	    if(panel instanceof MonthlyReportPanel)
	    {
	    	pack();
	    }
	   
	}
	public void setVisible(boolean showFlag)
	{
		
		ExpenseTrackerUtility.showComponenets(showFlag, jScrollPaneForWeeklyReport);
		ExpenseTrackerUtility.showComponenets(showFlag, footerForWeeklyReport);

	}
	public class WeekColumnRender extends JLabel implements TableCellRenderer
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
			 MyTableModelForWeeklyReport model = (MyTableModelForWeeklyReport)table.getModel();
			 final String format = "dd-MMM-yyyy";
			 String start = ExpenseTrackerUtility.formatDate(model.getWeekStartDate(row), format);
			 String end = ExpenseTrackerUtility.formatDate(model.getWeekEndDate(row), format);
				setText("<html><a href=\" #\"> Week "  + (row + 1)
						+ "</a>&nbsp;&nbsp (" + start + " - " + end  + ") </html> ");
			
			return this;
		}

		   	
    }

	public class MyTableModelForWeeklyReport implements TableModel
	{
		Object[][] rowData;	
		public    MyTableModelForWeeklyReport(Object[][] rowData)
		{
			this.rowData=rowData;
		}
				
		private String[] columnNames={"Week","Amount spent(per week)"};
		
		public Date getWeekStartDate (int row)
		{	
			return ((Report)rowData[row][0]).getWeeklyReportStartDt();
		}
		
		public Date getWeekEndDate (int row)
		{	
			return ((Report)rowData[row][0]).getWeeklyReportEndDt();
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// TODO Auto-generated method stub
			return getValueAt(0, columnIndex).getClass();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			// TODO Auto-generated method stub
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return rowData.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) 
		{
			if(columnIndex!=1)
			{
				Report report = (Report) rowData[rowIndex][columnIndex];
				String veiwString = "Week "
						+ (rowIndex + 1)
						+ " (" +ExpenseTrackerUtility.formatDate(
								report.getWeeklyReportStartDt(), "dd-MMM-yyyy")
						+ " - "
						+ ExpenseTrackerUtility.formatDate(
								report.getWeeklyReportEndDt(), "dd-MMM-yyyy")
						+ ")";

				return veiwString;
			}
			else
			{
				return rowData[rowIndex][columnIndex];
			}
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
		
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		JTable jTable = (JTable)e.getSource();
		String selectedRowString = (String)jTable.getValueAt(jTable.getSelectedRow(),jTable.getSelectedColumn());
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
		jFrame.setTitle("Weekly Report");

		WeeklyReportPanel panel = new WeeklyReportPanel(selectedRowString,reportRequest);
		jFrame.getContentPane().add(BorderLayout.CENTER, panel.buildGUI());

		jFrame.pack();
		// jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);

	}
}