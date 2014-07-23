package com.expensetracker.swing.pages.panel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.expensetracker.classes.ReportRequest;
import com.expensetracker.swing.pages.frame.CategoryReportFrame;
import com.expensetracker.swing.pages.frame.MonthlyFrame;
import com.expensetracker.swing.pages.frame.ProductReportFrame;
import com.expensetracker.swing.pages.frame.SubcategoryReportFrame;
import com.expensetracker.swing.pages.frame.WeeklyFrame;
import com.expensetracker.swing.pages.frame.YearlyFrame;


public class GenerateReportsPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3562613062566187449L;
	JFrame jFrame;
	WeeklyFrame weeklyFrame;
	MonthlyFrame monthlyFrame;
	YearlyFrame yearlyFrame;
	CategoryReportFrame categoryReportFrame;
	ProductReportFrame productReportFrame;
	SubcategoryReportFrame subcategoryReportFrame;
	
	ReportRequest reportRequest = new ReportRequest();

	GenerateReportsPanel generateReportsPanel = null;
	
	public GenerateReportsPanel()
	{
		
	}
	public GenerateReportsPanel(JFrame jFrame)
	{
		this.jFrame = jFrame;
	}
	public void buildGUI()
	{
		if (generateReportsPanel != null)
		{	
			return;
		}
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

		JRadioButton categoryWiseReport = new JRadioButton("Category");
		categoryWiseReport.setMnemonic(KeyEvent.VK_C);
		categoryWiseReport.add(Box.createRigidArea(new Dimension(100,30)));

		JRadioButton productWiseReport = new JRadioButton("Product");
		productWiseReport.setMnemonic(KeyEvent.VK_P);
		productWiseReport.add(Box.createRigidArea(new Dimension(100,30)));
		
		JRadioButton subCategoryWiseReport = new JRadioButton("Sub-Category");
		subCategoryWiseReport.setMnemonic(KeyEvent.VK_S);
		subCategoryWiseReport.add(Box.createRigidArea(new Dimension(100,30)));


		weeklyReport.setActionCommand("Weekly Report");
		weeklyReport.addActionListener(this);
		
		monthlyReport.setActionCommand("Monthly Report");
		monthlyReport.addActionListener(this);
		
		yearlyReport.setActionCommand("Yearly Report");
		yearlyReport.addActionListener(this);
		
		categoryWiseReport.setActionCommand("Category wise");
		categoryWiseReport.addActionListener(this);
		
		productWiseReport.setActionCommand("Product wise");
		productWiseReport.addActionListener(this);
		
		subCategoryWiseReport.setActionCommand("Sub Category wise");
		subCategoryWiseReport.addActionListener(this);

		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(weeklyReport);
		buttonGroup.add(monthlyReport);
		buttonGroup.add(yearlyReport);
		buttonGroup.add(categoryWiseReport);
		buttonGroup.add(productWiseReport);
		buttonGroup.add(subCategoryWiseReport);


		
		weeklyReport.setAlignmentX(Component.LEFT_ALIGNMENT);
		monthlyReport.setAlignmentX(Component.LEFT_ALIGNMENT);
		//jLabelForYearInput.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(weeklyReport);
		this.add(monthlyReport);
		this.add(yearlyReport);
		this.add(categoryWiseReport);
		this.add(productWiseReport);
		this.add(subCategoryWiseReport);


		
		weeklyFrame = new WeeklyFrame(reportRequest,this);
				
	}
	

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if("Weekly Report".equals(e.getActionCommand()))
		{
			
			weeklyFrame.setVisible(true);
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(yearlyFrame!=null)
			{
				yearlyFrame.setVisible(false);
			}if(categoryReportFrame!=null)
			{
				categoryReportFrame.setVisible(false);
			}
			if(productReportFrame!=null)
			{
				productReportFrame.setVisible(false);
			}
			if(subcategoryReportFrame!=null)
			{
				subcategoryReportFrame.setVisible(false);
			}

		}	
		else if("Monthly Report".equals(e.getActionCommand()))
		{
			monthlyFrame = new MonthlyFrame(reportRequest,this);
			weeklyFrame.setVisible(false);
			monthlyFrame.setVisible(true);
			if(yearlyFrame!=null)
			{	
				yearlyFrame.setVisible(false);
			}	
			if(categoryReportFrame!=null)
			{
				categoryReportFrame.setVisible(false);
			}
			if(productReportFrame!=null)
			{
				productReportFrame.setVisible(false);
			}
			if(subcategoryReportFrame!=null)
			{
				subcategoryReportFrame.setVisible(false);
			}

		}
		else if("Yearly Report".equals(e.getActionCommand()))
		{
			yearlyFrame = new YearlyFrame(this);
			weeklyFrame.setVisible(false);
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(categoryReportFrame!=null)
			{
				categoryReportFrame.setVisible(false);
			}
			if(productReportFrame!=null)
			{
				productReportFrame.setVisible(false);
			}
			if(subcategoryReportFrame!=null)
			{
				subcategoryReportFrame.setVisible(false);
			}
			yearlyFrame.setVisible(true);
		}
		
		else if("Category wise".equals(e.getActionCommand()))
		{
			categoryReportFrame = new CategoryReportFrame(this,jFrame);
			categoryReportFrame.setVisible(true);
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(yearlyFrame!=null)
			{
				yearlyFrame.setVisible(false);
			}
			if(productReportFrame!=null)
			{
				productReportFrame.setVisible(false);
			}
			if(subcategoryReportFrame!=null)
			{
				subcategoryReportFrame.setVisible(false);
			}
			weeklyFrame.setVisible(false);
		}
		else if("Product wise".equals(e.getActionCommand()))
		{
			productReportFrame = new ProductReportFrame(this);
			productReportFrame.setVisible(true);
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(yearlyFrame!=null)
			{
				yearlyFrame.setVisible(false);
			}
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(categoryReportFrame!=null)
			{
				categoryReportFrame.setVisible(false);
			}
			if(subcategoryReportFrame!=null)
			{
				subcategoryReportFrame.setVisible(false);
			}
			
			weeklyFrame.setVisible(false);
		}
		else if("Sub Category wise".equals(e.getActionCommand()))
		{
			subcategoryReportFrame = new SubcategoryReportFrame(this);
			subcategoryReportFrame.setVisible(true);
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(yearlyFrame!=null)
			{
				yearlyFrame.setVisible(false);
			}
			if(monthlyFrame!=null)
			{
				monthlyFrame.setVisible(false);
			}
			if(categoryReportFrame!=null)
			{
				categoryReportFrame.setVisible(false);
			}
			if(productReportFrame!=null)
			{
				productReportFrame.setVisible(false);
			}	
			weeklyFrame.setVisible(false);
		}
	
	
	
	}
		
		 
}