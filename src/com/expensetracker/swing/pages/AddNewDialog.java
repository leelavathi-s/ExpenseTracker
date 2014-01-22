package com.expensetracker.swing.pages;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import com.expensetracker.classes.Category;
import com.expensetracker.classes.Product;
import com.expensetracker.swing.pages.panel.AddNewPanel;

public class AddNewDialog {

	Object lastAddedItem;
	
	public Object getLastAddedItem()
	{
		return lastAddedItem;
	}
	
	public void show(String selectedLink,Category selectedCategory,Product selectedProduct)
	{
		JDialog jFrameForAddNewItem = new JDialog();
		jFrameForAddNewItem.setTitle("Add new");
		jFrameForAddNewItem.setModal(true);
		
		AddNewPanel panel = new AddNewPanel(jFrameForAddNewItem);
		jFrameForAddNewItem.getContentPane().add(BorderLayout.CENTER,
				panel.buildGUI(selectedLink,selectedCategory,selectedProduct));
		jFrameForAddNewItem.pack();
		jFrameForAddNewItem.setVisible(true);
		lastAddedItem = panel.getLastAddedItem();
	}
}
