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

import com.expensetracker.swing.pages.panel.ComparePricePanel;
import com.expensetracker.swing.pages.panel.GenerateReportsPanel;
import com.expensetracker.swing.pages.panel.InformationPanel;
import com.expensetracker.swing.pages.panel.RecordPurchasePanel;

public class HomePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GenerateReportsPanel generateReportsPanel = null;
	
	JTabbedPane tabbedPane = null;
	RecordPurchasePanel recordPurchasePane = null;
	ComparePricePanel comparePricePanel = null;
	
	JFrame jFrame = null;
	public static void main(String args[])
	{
		HomePage homePage = new HomePage();
		homePage.buildGUI();
	}

	public void buildGUI() 
	{

		jFrame = new JFrame("Expense Tracker");
		jFrame.setLocation(300, 300);

		recordPurchasePane = new RecordPurchasePanel(jFrame);
		 comparePricePanel = new ComparePricePanel(jFrame);
		generateReportsPanel = new GenerateReportsPanel();
		InformationPanel informationPanel =  new InformationPanel(jFrame);

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Record Purchase", null,recordPurchasePane, "Record Data");
		tabbedPane.setMnemonicAt(0,KeyEvent.VK_1);
		
		tabbedPane.addTab("Compare Price", null, comparePricePanel, null);
		tabbedPane.setMnemonicAt(1,KeyEvent.VK_2);

		tabbedPane.addTab("Generate Reports", null, generateReportsPanel, null);
		tabbedPane.setMnemonicAt(2,KeyEvent.VK_3);
		
		tabbedPane.addTab("Information", null, informationPanel, null);
		tabbedPane.setMnemonicAt(1,KeyEvent.VK_4);


		tabbedPane.setSelectedIndex(3);
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
	        	  generateReportsPanel.buildGUI();
        	  
	          }
	          if("Record Purchase".equals(sourceTabbedPane.getTitleAt(index)))
	          {
	        	  recordPurchasePane.buildStoreDataGUI();
	        	  
	          }
	          if("Compare Price".equals(sourceTabbedPane.getTitleAt(index)))
	          {
	        	  comparePricePanel.buildGUI();
	        	  
	          }
        	  jFrame.pack();

		}
		
	}

}
