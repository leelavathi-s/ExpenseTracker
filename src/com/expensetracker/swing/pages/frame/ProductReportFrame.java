package com.expensetracker.swing.pages.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import com.expensetracker.swing.pages.panel.CategoryReportPanel;
import com.expensetracker.swing.pages.panel.ProductReportPanel;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class ProductReportFrame  extends JFrame implements MouseListener
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5404084022659253012L;
	/**
	 * 
	 */
	JScrollPane jScrollPaneForProductWiseReport;
	JTable footerForProductWiseReport;
	JTable jTableForProductWiseReport;
	List<Report> dataListForProductWiseReport;
	JPanel jPanel;

	public ProductReportFrame(JPanel jPanel) 
	{
		this.jPanel = jPanel;
		retrieveAndBuildTabelForProductWiseReport(jPanel);

	}

	public void retrieveAndBuildTabelForProductWiseReport(JPanel jPanel) 
	{
		try

		{
			dataListForProductWiseReport = Report
					.retrievePriceForProductWiseReport();

		} catch (SQLException sqlException) {
			JOptionPane.showMessageDialog(this,
					"Retrieval failed because of an error. \nError Message  "
							+ sqlException.getMessage()
							+ ".\nFix the error and try again", "Error",
					JOptionPane.ERROR_MESSAGE);

			sqlException.printStackTrace();
		}
		jTableForProductWiseReport = new JTable(
				new MyTableModelForCategoryWiseReport());
		jTableForProductWiseReport
				.setPreferredScrollableViewportSize(new Dimension(450, 70));
		jTableForProductWiseReport.setFillsViewportHeight(true);
		jTableForProductWiseReport.setAutoCreateRowSorter(true);
		jTableForProductWiseReport.setName(ReportType.CATEGORY.toString());

		jTableForProductWiseReport.addMouseListener(this);

		ExpenseTrackerUtility.initColumnSizes(jTableForProductWiseReport);

		DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableForProductWiseReport
				.getColumnModel();
		TableColumn col = colModel.getColumn(0);
		col.setCellRenderer(new CategoryWiseRenderer());
		jScrollPaneForProductWiseReport = new JScrollPane(
				jTableForProductWiseReport);
		jScrollPaneForProductWiseReport.setVisible(true);
		jPanel.add(jScrollPaneForProductWiseReport);

		double totalAmountSpentPerMonth = 0.0;

		for (Report report : dataListForProductWiseReport)
		{
			totalAmountSpentPerMonth += report.getTotalPricePerProduct();
		}

		footerForProductWiseReport = new JTable(1,
				jTableForProductWiseReport.getColumnCount());
		footerForProductWiseReport.setValueAt("Total", 0, 1);
		footerForProductWiseReport.setValueAt(
				new DecimalFormat("#0.00").format(totalAmountSpentPerMonth), 0,
				2);
		footerForProductWiseReport.setVisible(true);

		for (int columnIndex = 0; columnIndex < jTableForProductWiseReport
				.getColumnCount(); columnIndex++) 
		{
			ExpenseTrackerUtility.setChildTableColumnWidth(
					footerForProductWiseReport, columnIndex,
					ExpenseTrackerUtility.getParentTableColumnWidth(
							jTableForProductWiseReport, columnIndex));
		}
		jPanel.add(footerForProductWiseReport);
	}

	public class MyTableModelForCategoryWiseReport implements TableModel 
	{

		private String[] columnNames = { "Product", "Year",
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
			if (dataListForProductWiseReport != null
					&& !dataListForProductWiseReport.isEmpty()) 
			{
				return dataListForProductWiseReport.size();
			} 
			else 
			{
				return 0;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Report report = (Report) dataListForProductWiseReport
					.get(rowIndex);
			switch (columnIndex)
			{
			case 0:
				return report.getProductName();
			case 1:
				return report.getYearForYearlyReport();
			case 2:
				return report.getTotalPricePerProduct();

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
	public void mouseClicked(MouseEvent e) {

		JTable jTable = (JTable) e.getSource();
		Report selectedRowString = (Report) dataListForProductWiseReport
				.get(jTable.getSelectedRow());
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setProduct(selectedRowString.getProductName());
		reportRequest.setYear(selectedRowString.getYearForYearlyReport());
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
		jFrame.setTitle("Product wise Report");

		ProductReportPanel panel = new ProductReportPanel(reportRequest,
				jFrame);
		jFrame.getContentPane().add(BorderLayout.CENTER, panel);
		jFrame.setVisible(true);
		jFrame.pack();

	}

	public void setVisible(boolean showFlag) 
	{

		ExpenseTrackerUtility.showComponenets(showFlag,
				jScrollPaneForProductWiseReport);
		ExpenseTrackerUtility.showComponenets(showFlag,
				footerForProductWiseReport);

	}


}