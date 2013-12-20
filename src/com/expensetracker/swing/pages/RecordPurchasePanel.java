package com.expensetracker.swing.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import com.expensetracker.classes.Brand;
import com.expensetracker.classes.Category;
import com.expensetracker.classes.Order;
import com.expensetracker.classes.Product;
import com.expensetracker.classes.Shop;
import com.expensetracker.utility.ExpenseTrackerUtility;

/**
 * 
 */

/**
 * @author leela
 * 
 */
public class RecordPurchasePanel extends JPanel implements ItemListener,ActionListener
{

	
	JFormattedTextField dtPurchaseField;
	JComboBox<Category> categoryField;
	JComboBox<Product> productNameField;
	JFormattedTextField qtyField;
	JFormattedTextField priceField;
	JComboBox<Shop> purchasedFromField;
	JComboBox<Brand> brandNameField;
	JPanel jPanel;
	JFrame jFrame;
	
	JButton saveJButton = new JButton("Save");

	public RecordPurchasePanel(JFrame jFrame)
	{
		this.jFrame = jFrame;
	}
	
	public JPanel buildStoreDataGUI() 
	{
		 jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());

		JLabel dtPurchaseJLabel = new JLabel("Date of Purchase");
		addToPanel(jPanel, dtPurchaseJLabel, 0, 0, 0.1);

	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    dtPurchaseField = new JFormattedTextField(format);
	   // dtPurchaseField.setValue("yyyy-MM-dd");
	    dtPurchaseField.setForeground(Color.gray);
		//dtPurchaseField.setColumns(5);
		
	    //dtPurchaseField.requestFocusInWindow();
		/*try
		
		{
			MaskFormatter dateMask = new MaskFormatter("####/##/##");
			dateMask.install(dtPurchaseField);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        

		
		addToPanel(jPanel, dtPurchaseField, 1, 0, 0.8);

		JLabel categoryJLabel = new JLabel("Category");
		addToPanel(jPanel, categoryJLabel, 0, 1, 0.1);

		categoryField = new JComboBox(Category.getAvailableCategories());
		addToPanel(jPanel, categoryField, 1, 1, 0.8);
		categoryField.setName("Category");
		categoryField.setEditable(true);
		categoryField.addItemListener(this);

		JLabel label1 = new JLabel(
				"<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label1, 2, 1, 0.1);
		label1.addMouseListener(new LabelClickHandler("Category Name"));

		JLabel itemJLabel = new JLabel("Product Name");
		addToPanel(jPanel, itemJLabel, 0, 2, 0.1);

		if(categoryField.getSelectedItem()!=null)
		{
			productNameField = new JComboBox(Product.getAvailableProducts(categoryField.getSelectedItem()));
		}
		else
		{
			productNameField= new JComboBox<>();
		}
		addToPanel(jPanel, productNameField, 1, 2, 0.8);
		productNameField.addItemListener(this);
		productNameField.setName("Product Name");

		JLabel label2 = new JLabel(
				"<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label2, 2, 2, 0.1);
		label2.addMouseListener(new LabelClickHandler("Product Name"));
		
		JLabel brandNameJLabel = new JLabel("Brand Name");
		addToPanel(jPanel, brandNameJLabel, 0, 3, 0.1);

		if(productNameField.getSelectedItem()!=null)
		{	
			brandNameField = new JComboBox(Brand.getAvailableBrands(productNameField.getSelectedItem()));
		}
		else
		{
			brandNameField = new JComboBox<>();
		}
		addToPanel(jPanel, brandNameField, 1, 3, 0.8);

		JLabel label4 = new JLabel(
				"<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label4, 2, 3, 0.1);
		label4.addMouseListener(new LabelClickHandler("Brand Name"));

		JLabel shopNameJLabel = new JLabel("Shop Name");
		addToPanel(jPanel, shopNameJLabel, 0, 4, 0.1);

		purchasedFromField = new JComboBox(Shop.getAvailableShops());
		addToPanel(jPanel, purchasedFromField, 1, 4, 0.8);

		JLabel label3 = new JLabel(
				"<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label3, 2, 4, 0.1);
		label3.addMouseListener(new LabelClickHandler("Shop Name"));

		JLabel qtyJLabel = new JLabel("Quantity");
		addToPanel(jPanel, qtyJLabel, 0, 5, 0.1);

		qtyField = new JFormattedTextField(
				NumberFormat.getNumberInstance());
	//	qtyField.setValue(new Integer(quantity));
	//	qtyField.setColumns(5);
		// qtyField.addPropertyChangeListener("value", this);
		addToPanel(jPanel, qtyField, 1, 5, 0.8);

		JLabel priceJLabel = new JLabel("Price");
		addToPanel(jPanel, priceJLabel, 0, 6, 0.1);

		NumberFormat amountDisplayFormat = NumberFormat.getCurrencyInstance();
		amountDisplayFormat.setMinimumFractionDigits(0);
		NumberFormat amountEditFormat = NumberFormat.getNumberInstance();

	   priceField = new JFormattedTextField(
				new DefaultFormatterFactory(new NumberFormatter(
						amountDisplayFormat), new NumberFormatter(
						amountDisplayFormat), new NumberFormatter(
						amountEditFormat)));
		//priceField.setValue(price);
	//	priceField.setColumns(5);
		// priceField.addPropertyChangeListener("value", this);
		addToPanel(jPanel, priceField, 1, 6, 0.8);

		
		
		
		
		saveJButton.setDefaultCapable(true);
		saveJButton.addActionListener(this);
		
		JButton clearJButton = new JButton("Clear");
		
		buttonJPanel.add(saveJButton);
		buttonJPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonJPanel.add(clearJButton);
		buttonJPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		

		//clearJButton.addActionListener(this);		
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());

		wrapper.add(BorderLayout.CENTER, jPanel);
		wrapper.add(BorderLayout.PAGE_END, buttonJPanel);
		
		return wrapper;
	}
	
	JPanel buttonJPanel = new JPanel();
	
	
	public void setDefaultFocusAndDefaultButton()
	{
		dtPurchaseField.requestFocus();
		buttonJPanel.getRootPane().setDefaultButton(saveJButton);
		
	}

	private static void addToPanel(JPanel container, Component item, int x,
			int y, double weightx) {
		GridBagConstraints temp = new GridBagConstraints();
		temp.gridx = x;
		temp.gridy = y;
		temp.weightx = weightx;
		temp.fill = GridBagConstraints.HORIZONTAL;
		temp.insets = new Insets(10, 10, 10, 10);

		container.add(item, temp);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		boolean successFlag = false;
		try
		{
			boolean chkStatsus = checkMandatoryFields();
			if(chkStatsus)
			{	
				saveDetails();
				successFlag = true;
			}
			else
			{
				successFlag = false;
				
			}
		} 
		catch (SQLException sqlException)
		{
			// TODO Auto-generated catch block
			successFlag = false;
			sqlException.printStackTrace();
			JOptionPane.showMessageDialog(jPanel,
					"Insertion failed because of an error. \nError Message  "
							+ sqlException.getMessage() + ".\nFix the error and try again", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if(successFlag)
		{
			int n = JOptionPane.showConfirmDialog(jPanel,
					"Record successfully added!Want to add more?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			 if(1 == n) 
			 { 
				 jFrame.dispose();
			 }else
			 {
				 clearFields();
			 }
		}	
	}
	public boolean checkMandatoryFields()
	{
		if(dtPurchaseField.getValue()==null)
		{
			JOptionPane.showMessageDialog(jPanel, "Enter purchase date", "Date missing", JOptionPane.ERROR_MESSAGE);
			return false;
	
		}
		Brand brand = (Brand)(brandNameField.getSelectedItem());
		if(brand==null)
		{
			JOptionPane.showMessageDialog(jPanel, "Select or add a Brand", "Brand missing", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	
		Category category = (Category)(categoryField.getSelectedItem());
		if(category==null)
		{
			JOptionPane.showMessageDialog(jPanel, "Select or add a Category", "Category missing", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	
		Product product = (Product)(productNameField.getSelectedItem());
		if (product == null)
		{
			JOptionPane.showMessageDialog(jPanel, "Select or add a Product to proceed", "Product missing", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Shop shop = (Shop)(purchasedFromField.getSelectedItem());
		if (shop == null)
		{
			JOptionPane.showMessageDialog(jPanel, "Select or add a Shop to proceed", "Shop missing", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	
		if(qtyField.getValue()==null)
		{
			Number number = (Number)qtyField.getValue();
			if(number==null)
			{

				JOptionPane.showMessageDialog(jPanel, "Enter quantity", "Quantity missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if(priceField.getValue()==null)
		{
			Number numberPrice = (Number)priceField.getValue();
			if(numberPrice==null)
			{
				JOptionPane.showMessageDialog(jPanel, "Enter Price", "Price missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	
		return true;
	}
	
	
	protected String showAddNewItemUI(String selectedLink)
	{
		AddNewDialog dialog = new AddNewDialog();
		dialog.show(selectedLink);
		
		return dialog.getLastAddedItem();
	}

	
	public void saveDetails()throws SQLException
	{
			Order order = new Order();
			order.setPurchaseDate((Date)dtPurchaseField.getValue());

			Brand brand = (Brand)(brandNameField.getSelectedItem());
			order.setBrandId(brand.getBrandId());
			
		
			Category category = (Category)(categoryField.getSelectedItem());
			order.setCategoryId(category.getCategoryId());
			
			
		
			Product product = (Product)(productNameField.getSelectedItem());
			order.setProductId(product.getProductId());
			
			Shop shop = (Shop)(purchasedFromField.getSelectedItem());
			order.setShopId(shop.getShopId());
			
			Number number = (Number)qtyField.getValue();
			order.setQuantity(number.intValue());
			Number priceNumber = (Number)priceField.getValue();
			double price = priceNumber.doubleValue();
			order.setPrice(price);
			
			order.saveOrderDetails(order);
	}

	@Override
	public void itemStateChanged(ItemEvent itemEvent)
	{
		JComboBox comboBox = (JComboBox)itemEvent.getSource();
		if("Product Name".equals(comboBox.getName()))
		{
			Product selectedProduct = (Product)comboBox.getSelectedItem();		
			DefaultComboBoxModel<Brand> defaultComboBoxModel = new DefaultComboBoxModel<>();
			for(Brand brand:Brand.getAvailableBrands(selectedProduct))
				defaultComboBoxModel.addElement(brand);
	
			brandNameField.setModel(defaultComboBoxModel);
		
		}
		else 
		{
			if("Category".equals(comboBox.getName()))
			{
				Category selectedCategory = (Category)comboBox.getSelectedItem();
				DefaultComboBoxModel<Product> defaultComboBoxModel = new DefaultComboBoxModel<>();
				for(Product product:Product.getAvailableProducts(selectedCategory))
					defaultComboBoxModel.addElement(product);
		
				productNameField.setModel(defaultComboBoxModel);
				
				DefaultComboBoxModel<Brand> defaultComboBoxModelForBrand = new DefaultComboBoxModel<>();

				if(productNameField.getSelectedItem()==null)
				{
					brandNameField.setModel(defaultComboBoxModelForBrand);
				}
				else
				{
					for(Brand brand:Brand.getAvailableBrands(productNameField.getSelectedItem()))
						defaultComboBoxModelForBrand.addElement(brand);
					
					brandNameField.setModel(defaultComboBoxModelForBrand);

			
				}
					
			}
		}
	}
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.dispose();
    }

	/*@Override
	 public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Entering actionPerformed method");
		String dtPurchase = StringFunctions.isNullOrEmpty(dtPurchaseField
				.getValue()) ? null : (String) dtPurchaseField.getValue();
		String category = StringFunctions.isNullOrEmpty(categoryField
				.getSelectedItem()) ? null : (String) categoryField
				.getSelectedItem();
		String productName = StringFunctions.isNullOrEmpty(productNameField
				.getSelectedItem()) ? null : (String) productNameField
				.getSelectedItem();
		int qty = (Integer) qtyField.getValue();
		double price = (Double) priceField.getValue();
		String shopName = StringFunctions.isNullOrEmpty(purchasedFromField
				.getSelectedItem()) ? null : (String) purchasedFromField
				.getSelectedItem();
		String brandName = StringFunctions.isNullOrEmpty(brandNameField
				.getSelectedItem()) ? null : (String) brandNameField
				.getSelectedItem();
		Order order = new Order(dtPurchase, category, productName, qty, price,
				shopName, brandName);
		System.out.println("Created order object:::" + order.toString());
		order.saveOrderDetails(order);

	}
*/

    class LabelClickHandler implements MouseListener 
    {
    	String selectedLink;

    	public LabelClickHandler(String selectedLinkStr)
    	{
    		selectedLink = selectedLinkStr;
    	}

    	@Override
    	public void mouseClicked(MouseEvent arg0)
    	{
    		System.out.println("Entered here in mouseclicked");
    		System.out.println("" + arg0.getID());
    		String lastItem = showAddNewItemUI(selectedLink);

    		System.out.println("selected link:::" + selectedLink);
    		if("Category Name".equals(selectedLink))
    		{
    			categoryField.removeAllItems();

    			for (Category category : Category.getAvailableCategories())
    				categoryField.addItem(category);

    			if(!ExpenseTrackerUtility.isNullOrEmpty(lastItem))
    			{
    				categoryField.setSelectedItem(lastItem);
    			}
    		}
    		else if("Product Name".equals(selectedLink))
    		{
    			productNameField.removeAllItems();

    			if(categoryField.getSelectedItem()!=null)
    			{
    				for(Product product:Product.getAvailableProducts(categoryField.getSelectedItem()))
    					productNameField.addItem(product);

    			}
    			else
    			{
    				for(Product product:Product.getAvailableProducts(null))
    					productNameField.addItem(product);

    			}

    			if(!ExpenseTrackerUtility.isNullOrEmpty(lastItem))
    			{
    				productNameField.setSelectedItem(lastItem);
    			}

    		}
    		else if("Brand Name".equals(selectedLink))
    		{
    			brandNameField.removeAllItems();

    			if(productNameField.getSelectedItem()!=null)
    			{
    				for (Brand brand : Brand.getAvailableBrands(productNameField
    						.getSelectedItem()))
    					brandNameField.addItem(brand);
    			}	
    			else
    			{
    				for (Brand brand : Brand.getAvailableBrands(null))
    					brandNameField.addItem(brand);
    			}
    			if(!ExpenseTrackerUtility.isNullOrEmpty(lastItem))
    			{
    				brandNameField.setSelectedItem(lastItem);
    			}

    		}
    		else if("Shop Name".equals(selectedLink))
    		{
    			purchasedFromField.removeAllItems();

    			for (Shop shop : Shop.getAvailableShops())
    				purchasedFromField.addItem(shop);

    			if(!ExpenseTrackerUtility.isNullOrEmpty(lastItem))
    			{
    				purchasedFromField.setSelectedItem(lastItem);
    			}

    		}

    	}


    	@Override
    	public void mouseEntered(MouseEvent arg0) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void mouseExited(MouseEvent arg0) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void mousePressed(MouseEvent arg0) {

    	}

    	@Override
    	public void mouseReleased(MouseEvent arg0) {
    		// TODO Auto-generated method stub

    	}
    }

    public void clearFields()
    {
    	priceField.setText("");
    	qtyField.setText("");
    	dtPurchaseField.setText("");
    }
	}
