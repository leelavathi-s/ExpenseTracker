package com.expensetracker.swing.pages;

import java.awt.BorderLayout;

import javax.swing.JDialog;

public class AddNewDialog {

	String lastAddedItem;
	
	public String getLastAddedItem()
	{
		return lastAddedItem;
	}
	
	public void show(String selectedLink)
	{
		JDialog jFrameForAddNewItem = new JDialog();
		jFrameForAddNewItem.setTitle("Add new");
		jFrameForAddNewItem.setModal(true);
		
		AddNewPanel panel = new AddNewPanel(jFrameForAddNewItem);
		jFrameForAddNewItem.getContentPane().add(BorderLayout.CENTER,
				panel.buildGUI(selectedLink));
		jFrameForAddNewItem.pack();
		jFrameForAddNewItem.setVisible(true);
		lastAddedItem = panel.getLastAddedItem();
	}
}
