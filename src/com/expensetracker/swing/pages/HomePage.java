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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HomePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GenerateReportsPanel generateReportsPanel = null;
	
	JTabbedPane tabbedPane = null;
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
		generateReportsPanel = new GenerateReportsPanel();

		Border blackline = BorderFactory.createLineBorder(Color.black);
		comparePricePanel.setBorder(BorderFactory.createTitledBorder(blackline,
				"Compare"));
		JButton comPriceButton = new JButton("Compare Price");
		comparePricePanel.add(comPriceButton);

	
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Record Purchase", null,
				recordPurchasePane.buildStoreDataGUI(), "Record Data");
		tabbedPane.setMnemonicAt(0,KeyEvent.VK_1);
		
		tabbedPane.addTab("Compare Price", null, comparePricePanel, null);
		tabbedPane.setMnemonicAt(1,KeyEvent.VK_2);

		tabbedPane.addTab("Generate Reports", null, generateReportsPanel.buildGUI(), null);
		tabbedPane.setMnemonicAt(2,KeyEvent.VK_3);

		tabbedPane.setSelectedIndex(0);
		tabbedPane.addChangeListener(new TabbedPaneChangeListener());
	    
		jFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
		
		recordPurchasePane.setDefaultFocusAndDefaultButton();
        // Just a comment
	}
	
	public class TabbedPaneChangeListener implements ChangeListener
	{

		@Override
		public void stateChanged(ChangeEvent e) {

	          JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
	          int index = sourceTabbedPane.getSelectedIndex();
	          if("Generate Reports".equals(sourceTabbedPane.getTitleAt(index)))
	          {
	        	//  generateReportsPanel.buildGUI();
	        	  
	        	  
	          }
	        			
		}
		
	}

}
