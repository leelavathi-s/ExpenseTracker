package com.expensetracker.swing.pages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Order;
import com.expensetracker.utility.ExpenseTrackerUtility;


public class GenerateReportsPanel extends JPanel implements ActionListener, MouseListener
{

	List<Order> orderList = null;
	List<Double> priceListForWeeklyReport = null;

	GenerateReportsPanel generateReportsPanel = null;
	public GenerateReportsPanel buildGUI()
	{
 		 generateReportsPanel = new GenerateReportsPanel();
		generateReportsPanel.setLayout(new BoxLayout(generateReportsPanel, BoxLayout.PAGE_AXIS));
		
		Border titleBorder = BorderFactory.createLineBorder(Color.gray);
		generateReportsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory
				.createTitledBorder("Select an option for report generation"),
		BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
		
		Border lineBorder = BorderFactory.createLineBorder(Color.red);
		JRadioButton weeklyReport = new JRadioButton("Weekly");
		weeklyReport.setMnemonic(KeyEvent.VK_W);
		weeklyReport.setBorder(lineBorder);
		weeklyReport.setSelected(true);
		//weeklyReport.setPreferredSize(new Dimension(200,30));
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
		weeklyReport.setName("name");
		weeklyReport.addActionListener(this);
		
		monthlyReport.setActionCommand("Monthly Report");
		monthlyReport.addActionListener(this);
		
		yearlyReport.setActionCommand("Yearly Report");
		yearlyReport.addActionListener(this);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(weeklyReport);
		buttonGroup.add(monthlyReport);
		buttonGroup.add(yearlyReport);
		
		
		generateReportsPanel.add(weeklyReport);
		if(weeklyReport.isSelected())
		{
			
			retrieveAndBuildTableDataForWeeklyReport("Weekly");
			
		}
		generateReportsPanel.add(monthlyReport);
		generateReportsPanel.add(yearlyReport);
		
		
		
		return generateReportsPanel;
	}
	
    public class WeekColumnRender extends JLabel implements TableCellRenderer
    {
    	
    	@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) 
		{
        	ArrayList<String> arrayList = null;

			if(value!=null)
			{
				StringTokenizer stringTokenizer = new StringTokenizer(
						(String) value, "(");
				arrayList = new ArrayList<String>();
				while (stringTokenizer.hasMoreElements())
				{
					arrayList.add((String) stringTokenizer.nextElement());

				}
				setText("<html><a href=\" #\">" + arrayList.get(0)
						+ "</a>&nbsp;&nbsp (" + arrayList.get(1) + "</html> ");
				
				

			}
			
			
			return this;
		}

		   	
    }
	public void createFrame(String selectedRowString)
	{
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Weekly Report");
		
		WeeklyReportPanel panel = new WeeklyReportPanel(selectedRowString);
		jFrame.getContentPane().add(BorderLayout.CENTER,
				panel.buildGUI());
		jFrame.pack();
		//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
			
	
	}
	
	class WeekRange { Date start; Date end; }
	public void retrieveAndBuildTableDataForWeeklyReport(String userSelectedReportChoice)
	{
		if("Weekly".equals(userSelectedReportChoice))
		{

			int maxWeeknumber = ExpenseTrackerUtility.getNumberOfWeeksInCurrentMonth(); 
	 		try 
			{
	 			priceListForWeeklyReport = Order.retrievePriceAmountForWeeklyReport();
			} 
			catch (SQLException sqlException)
			{
				JOptionPane.showMessageDialog(generateReportsPanel,
						"Retrieval failed because of an error. \nError Message  "
								+ sqlException.getMessage()
								+ ".\nFix the error and try again", "Error",
						JOptionPane.ERROR_MESSAGE);

				sqlException.printStackTrace();
			}
			Object[][] rowData = new Object[maxWeeknumber][2];
			Calendar cal=ExpenseTrackerUtility.getCurrentDate();
			Date currentDt=cal.getTime();
		
			for(int i=0;i<maxWeeknumber;i++)
			{
				for(int j=0;j<2;j++)
				{
					if(j!=1)
					{	
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
			        	
						rowData[i][j] = "Week " + (i+1) + " (" + ExpenseTrackerUtility.formatDate(startDt,"dd-MMM-yyyy") + " - " + ExpenseTrackerUtility.formatDate(endDate,"dd-MMM-yyyy") + ")";
						rowData[i][j] = new WeekRange(startDt, endDate);
		        		cal.add(Calendar.DATE,1);
		        		
		        		endDate=cal.getTime();

						currentDt = endDate;

					
					}
					else
					{	
						rowData[i][j]=priceListForWeeklyReport.get(i);
					}	
				}
			}
		
			
			JTable jTable = new JTable(new MyTableModelForWeeklyReport(rowData));
			jTable.setPreferredScrollableViewportSize(new Dimension(350, 70));
			//jTable.setFillsViewportHeight(true);
			jTable.setAutoCreateRowSorter(true);
			
			jTable.addMouseListener(this);

			ExpenseTrackerUtility.initColumnSizes(jTable);

			for (int c = 0; c < jTable.getColumnCount()-1; c++)
			{
				DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTable
						.getColumnModel();
				TableColumn col = colModel.getColumn(c);
				col.setCellRenderer(new WeekColumnRender());
				
			}
			JScrollPane jScrollPane = new JScrollPane(jTable);

			generateReportsPanel.add(jScrollPane);
			
		}
		

	}

	public class MyTableModelForWeeklyReport implements TableModel
	{
		Object[][] rowData;	
		public MyTableModelForWeeklyReport(Object[][] rowData)
		{
			this.rowData=rowData;
		}
		private String[] columnNames={"Week Number","Total Price"};
		
		
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
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) // if it is the week number column
			{
			    WeekRange range = rowData[rowIndex][columnIndex];
			    return "Week " + range.WeekNumber;
			
			}
			// TODO Auto-generated method stub
			return rowData[rowIndex][columnIndex];
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
	public void actionPerformed(ActionEvent e)
	{
		System.out.println(((JRadioButton)e.getSource()).getName());
		if("Weekly Report".equals(e.getSource()))
		{
			
		}	
		else if("Monthly Report".equals(e.getActionCommand()))
		{
			
		}
		else if("Yearly Report".equals(e.getActionCommand()))
		{
			
		}	
	
	}
	
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			JTable jTable = (JTable)e.getSource();
			String selectedRowString = (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
			createFrame(selectedRowString);			
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
