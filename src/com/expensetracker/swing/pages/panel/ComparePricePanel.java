package com.expensetracker.swing.pages.panel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.expensetracker.classes.Product;


public class ComparePricePanel extends JPanel
{
	JFrame jFrame = null;
	JLabel produtLabel = null;
	JComboBox<Product> jproductComboBox = null;
	JLabel compareChoice;
	JRadioButton shopRadioButton;
	JRadioButton brandRadioButton;
	JLabel quantityLabel = null;
	JTextField 	qtyField = null;
	
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
		addToPanel(this,jproductComboBox,1,0,0.8,0);

		
		compareChoice = new JLabel("Select choice for comparison");
		addToPanel(this,compareChoice,0,1,0.2,0);

		shopRadioButton = new JRadioButton("Shop");
		addToPanel(this,shopRadioButton,1,1,0.4,0);

		brandRadioButton = new JRadioButton("Brand");
		addToPanel(this,brandRadioButton,2,1,0.4,0);

		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(shopRadioButton);
		buttonGroup.add(brandRadioButton);
		
		quantityLabel = new JLabel("Quantity");
		addToPanel(this,quantityLabel,0,2,0.2,0);

		qtyField = new JTextField();
		addToPanel(this,qtyField,1,2,0.8,0);

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

}
