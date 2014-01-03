package com.expensetracker.swing.pages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Order;
import com.expensetracker.classes.Report;
import com.expensetracker.classes.ReportType;
import com.expensetracker.utility.ExpenseTrackerUtility;


public class GenerateReportsPanel extends JPanel implements ActionListener, MouseListener
{

	List<Order> orderList = null;
	List<Double> priceListForWeeklyReport = null;
	List<Report> dataListForMonthlyReport = null;

	JTable jTableForWeeklyreport;
	JTable footerForWeeklyReport;
	JScrollPane jScrollPaneForWeeklyReport;

	JTable jTableForMonthlyreport;
	JTable footerForMonthlyReport;
	JScrollPane jScrollPaneForMonthlyReport;
	JLabel jLabelForYearInput;
	JComboBox<String> jComboBoxForYearInput;
	JPanel jPanelForYearInput;
	JFrame jFrame;


	GenerateReportsPanel generateReportsPanel = null;
	public void buildGUI()
	{
		generateReportsPanel = new GenerateReportsPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory
				.createTitledBorder("Select an option for report generation"),
		BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
		
		Border lineBorder = BorderFactory.createLineBorder(Color.red);
		JRadioButton weeklyReport = new JRadioButton("Weekly");
		weeklyReport.setMnemonic(KeyEvent.VK_W);
		weeklyReport.setBorder(lineBorder);
		weeklyReport.setSelected(true);
		weeklyReport.add(Box.createRigidArea(new Dimension(100,30)));
		
		
		JRadioButton monthlyReport = new JRadioButton("Monthly");
		monthlyReport.setMnemonic(KeyEvent.VK_M);
		//monthlyReport.setBorder(lineBorder);
		monthlyReport.add(Box.createRigidArea(new Dimension(100,30)));

		
		JRadioButton yearlyReport = new JRadioButton("Yearly");
		yearlyReport.setMnemonic(KeyEvent.VK_Y);
	//	yearlyReport.setBorder(lineBorder);
		yearlyReport.add(Box.createRigidArea(new Dimension(100,30)));

		weeklyReport.setActionCommand("Weekly Report");
		weeklyReport.addActionListener(this);
		
		monthlyReport.setActionCommand("Monthly Report");
		monthlyReport.addActionListener(this);
		
		yearlyReport.setActionCommand("Yearly Report");
		yearlyReport.addActionListener(this);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(weeklyReport);
		buttonGroup.add(monthlyReport);
		buttonGroup.add(yearlyReport);
		
		weeklyReport.setAlignmentX(Component.LEFT_ALIGNMENT);
		monthlyReport.setAlignmentX(Component.LEFT_ALIGNMENT);
		//jLabelForYearInput.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(weeklyReport);
		this.add(monthlyReport);
		this.add(yearlyReport);
		
		retrieveAndBuildTableDataForWeeklyReport(null,null,this);
		
				
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

	public void createFrame(String selectedRowString,String name,Object yrSelected)
	{
		jFrame = new JFrame();
		if(ReportType.WEEKLY.toString().equals(name))
		{	
			jFrame.setTitle("Weekly Report");
		
			WeeklyReportPanel panel = new WeeklyReportPanel(selectedRowString);
			jFrame.getContentPane().add(BorderLayout.CENTER,
				panel.buildGUI());
		}
		else if(ReportType.MONTHLY.toString().equals(name))
		{
			jFrame.setTitle("Monthly Report");
			
			MonthlyReportPanel panel = new MonthlyReportPanel(selectedRowString,(String)yrSelected,this);
			jFrame.getContentPane().add(BorderLayout.CENTER,panel);
		
		}	
		jFrame.pack();
		//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
			
	
	}
	public void retrieveAndBuildTableDataForWeeklyReport(String monthSelected,String yrSelected,JPanel panel)
	{
			double totalAmountSpentPerWeek=0;
			Report report = new Report();
			int maxWeeknumber = 0;
			Calendar cal = null;
			if(monthSelected==null && yrSelected==null)
			{
				maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInCurrentMonth(); 
				cal = ExpenseTrackerUtility.getCurrentDate();

			} 
			else
			{
				maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInMonth(monthSelected, yrSelected); 
				cal= ExpenseTrackerUtility.getDateForSelectedMonthAndYr(monthSelected, yrSelected);

			}
	 		try 
			{
	 			priceListForWeeklyReport = Report.retrievePriceAmountForWeeklyReport(monthSelected,yrSelected);
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
	    	jFrame.pack();
	    }
	  //  jFrame.pack();
	   
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
	public void actionPerformed(ActionEvent e)
	{
		if("Weekly Report".equals(e.getActionCommand()))
		{
			retrieveAndBuildTableDataForWeeklyReport(null, null, this);
			
			ExpenseTrackerUtility.showComponenets(false, footerForMonthlyReport);
			ExpenseTrackerUtility.showComponenets(false, jScrollPaneForMonthlyReport);
			ExpenseTrackerUtility.showComponenets(false, jPanelForYearInput);

		}	
		else if("Monthly Report".equals(e.getActionCommand()))
		{
			
			 jPanelForYearInput = new JPanel();
			 jPanelForYearInput.setLayout(new BoxLayout(jPanelForYearInput, BoxLayout.LINE_AXIS));
			
			 jLabelForYearInput = new JLabel("Select an year");
			 try 
			 {
				jComboBoxForYearInput = new JComboBox<String>(Report.retrieveAvailableYears());
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
			jPanelForYearInput.setVisible(true);

			jLabelForYearInput.setAlignmentX(Component.LEFT_ALIGNMENT);

			this.add(jPanelForYearInput);
			
			retrieveAndBuildTableForMonthlyReport(false);
			
			ExpenseTrackerUtility.showComponenets(false, footerForWeeklyReport);
			ExpenseTrackerUtility.showComponenets(false, jScrollPaneForWeeklyReport);

		}
		else if("Yearly Report".equals(e.getActionCommand()))
		{
			ExpenseTrackerUtility.showComponenets(false, footerForWeeklyReport);
			ExpenseTrackerUtility.showComponenets(false, jScrollPaneForWeeklyReport);

			ExpenseTrackerUtility.showComponenets(false, footerForMonthlyReport);
			ExpenseTrackerUtility.showComponenets(false, jScrollPaneForMonthlyReport);
			ExpenseTrackerUtility.showComponenets(false, jPanelForYearInput);

			

		}	
	
	}
		public class YearInputHandler implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				retrieveAndBuildTableForMonthlyReport(true);

			}
			
		}
	
		public void retrieveAndBuildTableForMonthlyReport(boolean yrOnChg)
		{
			if(yrOnChg)
			{	
				this.remove(jScrollPaneForMonthlyReport);
				this.remove(footerForMonthlyReport);
			}	


				try 
				{
		 			dataListForMonthlyReport = Report.retrievePriceForMonthlyReport(jComboBoxForYearInput.getSelectedItem());
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
				jTableForMonthlyreport.setPreferredScrollableViewportSize(new Dimension(450, 70));
				jTableForMonthlyreport.setFillsViewportHeight(true);
				jTableForMonthlyreport.setAutoCreateRowSorter(true);
				jTableForMonthlyreport.setName(ReportType.MONTHLY.toString());
				
				
				jTableForMonthlyreport.addMouseListener(this);
				

				ExpenseTrackerUtility.initColumnSizes(jTableForMonthlyreport);

				for (int c = 0; c < jTableForMonthlyreport.getColumnCount()-1; c++)
				{
					DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForMonthlyreport
							.getColumnModel();
					TableColumn col = colModel.getColumn(c);
					col.setCellRenderer(new MonthColumnRender());
					
				}
				jScrollPaneForMonthlyReport = new JScrollPane(jTableForMonthlyreport);
				jScrollPaneForMonthlyReport.setVisible(true);
				this.add(jScrollPaneForMonthlyReport);
				
				double totalAmountSpentPerMonth=0.0;
				
				for(Report report:dataListForMonthlyReport)
				{
					totalAmountSpentPerMonth+=report.getTotalPricePerMonthForMonthlyReport();
				}
				
			    footerForMonthlyReport = new JTable(1,jTableForMonthlyreport.getColumnCount());
			    footerForMonthlyReport.setValueAt("Total", 0, 0);
			    footerForMonthlyReport.setValueAt(new DecimalFormat("#0.00").format(totalAmountSpentPerMonth), 0, 1);
			    footerForMonthlyReport.setVisible(true);
			    
			    for(int columnIndex=0;columnIndex<jTableForMonthlyreport.getColumnCount();columnIndex++)
			    {	
			    	ExpenseTrackerUtility.setChildTableColumnWidth(
					footerForMonthlyReport, columnIndex, ExpenseTrackerUtility
							.getParentTableColumnWidth(jTableForMonthlyreport,
									columnIndex));
			    }	
					
					this.add(footerForMonthlyReport);
					

				
			}

public class MyTableModelForMonthlyReport implements TableModel
{
	
	private String[] columnNames={"Month","Amount spent(per month)"};
	

	
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
		if(dataListForMonthlyReport!=null && !dataListForMonthlyReport.isEmpty())
			return dataListForMonthlyReport.size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Report report = (Report)dataListForMonthlyReport.get(rowIndex);
		switch(columnIndex)	
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

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			JTable jTable = (JTable)e.getSource();
			String selectedRowString = (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
			createFrame(selectedRowString,jTable.getName(),jComboBoxForYearInput.getSelectedItem());			
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

		 
}
