package com.expensetracker.swing.pages;
/**
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
/**
 * @author leela
 *
 */
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

public class HomePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[])
	{
		HomePage homePage = new HomePage();
		homePage.buildGUI();
	}

	public void buildGUI() 
	{

		JFrame jFrame = new JFrame("Expense Tracker");
		jFrame.setLocation(300, 300);

		RecordPurchasePanel recordPurchasePane = new RecordPurchasePanel(jFrame);
		ComparePricePanel comparePricePanel = new ComparePricePanel();
		GenerateReportsPanel generateReportsPanel = new GenerateReportsPanel();

		Border blackline = BorderFactory.createLineBorder(Color.black);
		comparePricePanel.setBorder(BorderFactory.createTitledBorder(blackline,
				"Compare"));
		JButton comPriceButton = new JButton("Compare Price");
		comparePricePanel.add(comPriceButton);

		generateReportsPanel.setBorder(BorderFactory.createTitledBorder(
				blackline, "Reports"));
		JButton genReportButton = new JButton("Generate Report");
		generateReportsPanel.add(genReportButton);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Record Purchase", null,
				recordPurchasePane.buildStoreDataGUI(), "Record Data");
		tabbedPane.setMnemonicAt(0,KeyEvent.VK_1);
		
		tabbedPane.addTab("Compare Price", null, comparePricePanel, null);
		tabbedPane.setMnemonicAt(1,KeyEvent.VK_2);

		tabbedPane.addTab("Generate Reports", null, generateReportsPanel, null);
		tabbedPane.setMnemonicAt(2,KeyEvent.VK_3);

		tabbedPane.setSelectedIndex(0);

		jFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
		
		recordPurchasePane.setDefaultFocusAndDefaultButton();

	}

}
