package com.expensetracker.swing.pages.panel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.expensetracker.classes.Order;
import com.expensetracker.classes.Product;
import com.expensetracker.utility.ExpenseTrackerUtility;


public class ComparePricePanel extends JPanel implements ItemListener
{
	JFrame jFrame = null;
	JLabel produtLabel = null;
	JComboBox<Product> jproductComboBox = null;
	JTable comparisonTable = null;
	JTextField quantityField;
	
	public ComparePricePanel(JFrame jFrame) 
	{
		this.jFrame = jFrame;
	}
	
	public void buildGUI()
	{
		this.removeAll();
		setLayout(new GridBagLayout());
		
		produtLabel = new JLabel("Select a Product");
		addToPanel(this,produtLabel,0,0,0.2,0);
		try {
			 jproductComboBox = new JComboBox<Product>(Product.getAvailableProducts(null));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jproductComboBox.addItemListener(this);
		addToPanel(this,jproductComboBox,1,0,0.8,0);
		
		JLabel quantityLabel = new JLabel ("Enter Quantity");
		addToPanel(this, quantityLabel, 0, 1, 0.2, 0);
		
		quantityField = new JTextField();
		addToPanel(this, quantityField, 1, 1, 0.8, 0);
		
		comparisonTable = new JTable();
		comparisonTable.setAutoCreateRowSorter(true);
		addToPanel(this, new JScrollPane(comparisonTable), 0, 2, 1.0, 2);
	}
	
	public class ComparisonTableModel implements TableModel
	{
		private String []columnNames = { "Date", "Brand", "Shop", "Price", "Quantity", "CQ Price" };
		private ArrayList<Order> orderList;
		public ComparisonTableModel(ArrayList<Order> orderList)
		{
			this.orderList = orderList;
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			if(getValueAt(0, columnIndex)!=null)
			{
			return getValueAt(0, columnIndex).getClass();
			}
			else
			{
				return float.class;
			}
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
					return order.getBrandName();
				case 2:
					return order.getShopName();
				case 3:
					return order.getPrice();
				case 4:
					return order.getQuantity();
				case 5:
					String currentQuantityString = quantityField.getText();
					if (currentQuantityString.equals(""))
						return Double.NaN;
					double currentQuantity = Float.parseFloat(quantityField.getText());
					return currentQuantity * (order.getPrice() / order.getQuantity());
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

		@Override
		public void addTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private static void addToPanel(JPanel container, Component item, int x,
			int y, double weightx,int gridWidth) 
	{
		GridBagConstraints temp = new GridBagConstraints();
		temp.gridx = x;
		temp.gridy = y;
		temp.weightx = weightx;
		temp.fill = GridBagConstraints.HORIZONTAL;
		temp.insets = new Insets(10, 10, 10, 10);
		if(gridWidth!=0)
		{
			temp.gridwidth = gridWidth;

		}

		container.add(item, temp);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		try {
			Product selectedProduct = (Product) jproductComboBox
					.getSelectedItem();
			ArrayList<Order> orders = Order
					.retrievePurchasesForProduct(selectedProduct.getProductId());
			if (orders.size() > 0)
				comparisonTable.setModel(new ComparisonTableModel(orders));
			else
			{
				comparisonTable.setModel(new DefaultTableModel());;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
