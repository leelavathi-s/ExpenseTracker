package com.expensetracker.swing.pages.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.expensetracker.classes.Brand;
import com.expensetracker.classes.Category;
import com.expensetracker.classes.Product;
import com.expensetracker.classes.Shop;
import com.expensetracker.classes.Subcategory;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class AddNewPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -581874303641622790L;
	JLabel jLabel;
	JTextField inputItemjTextField;
	JPanel addNewRightPane;
	JPanel wrapperPanel;
	GroupLayout groupLayout;
	JButton addJButton;
	JLabel categoryJLabel;
	JLabel productNameJLabel;
	JLabel subCategoryJLabel;
	JComboBox<Product> productNameField;
	JComboBox<Subcategory> subCategoryField;

	JComboBox<Category> categoryField;

	Object lastAddedItem;
	JRadioButton categoryRadioButton;
	JRadioButton subcategoryRadioButton;
	JRadioButton shopRadioButton;
	JRadioButton brandRadioButton;
	JRadioButton productNameRadioButton;
    JDialog parentDialog;
	
	public AddNewPanel(JDialog jFrameForAddNewItem) {
		parentDialog = jFrameForAddNewItem;
	}

	public Object getLastAddedItem() {
		return lastAddedItem;
	}

	public JPanel buildGUI(String selectedLink,Category selectedCategory,Product selectedProduct) 
	{
		wrapperPanel = new JPanel(new BorderLayout());

		JPanel addNewLeftPane = new JPanel();
		addNewLeftPane.setLayout(new BoxLayout(addNewLeftPane,
				BoxLayout.PAGE_AXIS));
		addNewLeftPane
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
								.createTitledBorder("Select the item which was not available"),
						BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		categoryRadioButton = new JRadioButton("Category Name");
		shopRadioButton = new JRadioButton("Shop Name");
		brandRadioButton = new JRadioButton("Brand Name");
		productNameRadioButton = new JRadioButton("Product Name");
		subcategoryRadioButton = new JRadioButton("Sub Category");

		categoryRadioButton.setActionCommand("Category Name");
		categoryRadioButton.setMnemonic(KeyEvent.VK_C);
		categoryRadioButton.addActionListener(this);

		shopRadioButton.setActionCommand("Shop Name");
		shopRadioButton.setMnemonic(KeyEvent.VK_S);
		shopRadioButton.addActionListener(this);

		brandRadioButton.setActionCommand("Brand Name");
		brandRadioButton.setMnemonic(KeyEvent.VK_B);
		brandRadioButton.addActionListener(this);

		productNameRadioButton.setActionCommand("Product Name");
		productNameRadioButton.setMnemonic(KeyEvent.VK_P);
		productNameRadioButton.addActionListener(this);
		
		subcategoryRadioButton.setActionCommand("Sub Category");
		subcategoryRadioButton.setMnemonic(KeyEvent.VK_S);
		subcategoryRadioButton.addActionListener(this);


		if ("Category Name".equals(selectedLink)) 
		{
			categoryRadioButton.setSelected(true);
		} 
		else if ("Product Name".equals(selectedLink)) 
		{
			productNameRadioButton.setSelected(true);
		}
		else if ("Brand Name".equals(selectedLink)) 
		{
			brandRadioButton.setSelected(true);
		} 
		else if ("Shop Name".equals(selectedLink)) 
		{
			shopRadioButton.setSelected(true);
		}
		if ("SubCategory".equals(selectedLink)) 
		{
			subcategoryRadioButton.setSelected(true);
		}
		ButtonGroup group = new ButtonGroup();
		group.add(categoryRadioButton);
		group.add(shopRadioButton);
		group.add(brandRadioButton);
		group.add(productNameRadioButton);
		group.add(subcategoryRadioButton);


		addNewLeftPane.add(categoryRadioButton);
		addNewLeftPane.add(Box.createRigidArea(new Dimension(0, 30)));

		addNewLeftPane.add(shopRadioButton);
		addNewLeftPane.add(Box.createRigidArea(new Dimension(0, 30)));

		addNewLeftPane.add(brandRadioButton);
		addNewLeftPane.add(Box.createRigidArea(new Dimension(0, 30)));

		addNewLeftPane.add(productNameRadioButton);
		addNewLeftPane.add(Box.createRigidArea(new Dimension(0, 30)));
		
		addNewLeftPane.add(subcategoryRadioButton);


		categoryRadioButton.setPreferredSize(new Dimension(200, 30));

		addNewRightPane = new JPanel();
		groupLayout = new GroupLayout(addNewRightPane);
		addNewRightPane.setLayout(groupLayout);
		addNewRightPane
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
								.createTitledBorder("Click on Add to insert the new item"),
						BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		jLabel = new JLabel("Enter the new ".concat(selectedLink));

		inputItemjTextField = new JTextField();
		inputItemjTextField.setMinimumSize(new Dimension(150, 20));
		inputItemjTextField.setMaximumSize(new Dimension(40, 5));
		inputItemjTextField.setPreferredSize(new Dimension(40, 5));

		addJButton = new JButton("Add");
		addJButton.addActionListener(this);

		showForAllSelections();

		if (productNameRadioButton.isSelected())
		{
			try 
			{
				showSubCategoryComboBox();
			}
			catch (SQLException e)
			{
				ExpenseTrackerUtility.showFailureMessage(this, "Not able to to load Sub category drop down", e);
			}
		}
		if (subcategoryRadioButton.isSelected())
		{
			try 
			{
				showCategoryComboxBox();
			}
			catch (SQLException e)
			{
				ExpenseTrackerUtility.showFailureMessage(this, "Not able to to load  category drop down", e);
			}
		}
		else if (brandRadioButton.isSelected()) 
		{
			try 
			{
				showProduct(selectedProduct);
			} 
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		addNewRightPane.add(jLabel);
		addNewRightPane.add(inputItemjTextField);

		wrapperPanel.add(BorderLayout.LINE_START, addNewLeftPane);
		wrapperPanel.add(BorderLayout.CENTER, addNewRightPane);
		return wrapperPanel;
	}

	public void showForAllSelections() 
	{
		groupLayout.setHorizontalGroup(groupLayout
				.createSequentialGroup()
				.addComponent(jLabel)
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(inputItemjTextField)
								.addComponent(addJButton)));
		groupLayout.setVerticalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(inputItemjTextField))
				.addComponent(addJButton));
		showComponents(false, "Hide All");

	}

	public void showComponents(boolean showFlag, String itemToShowOrHide) 
	{

		if ("Product Name".equals(itemToShowOrHide) && showFlag) 
		{
			ExpenseTrackerUtility.showComponenets(true, productNameField);
			ExpenseTrackerUtility.showComponenets(true, productNameJLabel);

			ExpenseTrackerUtility.showComponenets(false, categoryField);
			ExpenseTrackerUtility.showComponenets(false, categoryJLabel);
			
			ExpenseTrackerUtility.showComponenets(false, subCategoryJLabel);
			ExpenseTrackerUtility.showComponenets(false, subCategoryField);


		}
		else if ("Category".equals(itemToShowOrHide) && showFlag)
		{
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			
			ExpenseTrackerUtility.showComponenets(true, categoryField);
			ExpenseTrackerUtility.showComponenets(true, categoryJLabel);
			
			ExpenseTrackerUtility.showComponenets(false, subCategoryJLabel);
			ExpenseTrackerUtility.showComponenets(false, subCategoryField);


		}
		else if ("SubCategory".equals(itemToShowOrHide) && showFlag)
		{
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			
			ExpenseTrackerUtility.showComponenets(false, categoryField);
			ExpenseTrackerUtility.showComponenets(false, categoryJLabel);
			
			ExpenseTrackerUtility.showComponenets(true, subCategoryJLabel);
			ExpenseTrackerUtility.showComponenets(true, subCategoryField);
			

		} 

		else if ("Hide All".equals(itemToShowOrHide) && !showFlag) 
		{
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			ExpenseTrackerUtility.showComponenets(false, productNameField);
			
			ExpenseTrackerUtility.showComponenets(false, categoryField);
			ExpenseTrackerUtility.showComponenets(false, categoryJLabel);
			
			ExpenseTrackerUtility.showComponenets(false, subCategoryJLabel);
			ExpenseTrackerUtility.showComponenets(false, subCategoryField);


		}

	}

	public void showSubCategoryComboBox()throws SQLException
	{
		subCategoryJLabel = new JLabel("Sub-Category to be associated with");
		subCategoryField = new JComboBox(Subcategory.getAvailableSubCategories(null));
		
		showComponents(true, "SubCategory");
		groupLayout.setHorizontalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(subCategoryJLabel))
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(inputItemjTextField)
								.addComponent(subCategoryField)
								.addComponent(addJButton)));

		groupLayout.setVerticalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(inputItemjTextField))
				.addGroup(groupLayout.createParallelGroup())
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(subCategoryJLabel)
								.addComponent(subCategoryField))
				.addComponent(addJButton));

	}
	public void showCategoryComboxBox()throws SQLException
	{

		categoryJLabel = new JLabel("Category to be associated with");
		categoryField = new JComboBox(Category.getAvailableCategories(null));
		
		showComponents(true, "Category");
		groupLayout.setHorizontalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(categoryJLabel))
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(inputItemjTextField)
								.addComponent(categoryField)
								.addComponent(addJButton)));

		groupLayout.setVerticalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(inputItemjTextField))
				.addGroup(groupLayout.createParallelGroup())
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(categoryJLabel)
								.addComponent(categoryField))
				.addComponent(addJButton));

	}

	public void showProduct(Product selectedProduct) throws SQLException 
	{
		productNameJLabel = new JLabel("Product to be associated with");
		productNameField = new JComboBox(Product.getAvailableProducts(null));
		
		if(selectedProduct!=null)
		{
			productNameField.setSelectedItem(selectedProduct);
		}
		showComponents(true, "Product Name");

		groupLayout.setHorizontalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(productNameJLabel))
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(inputItemjTextField)
								.addComponent(productNameField)
								.addComponent(addJButton)));

		groupLayout.setVerticalGroup(groupLayout
				.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup().addComponent(jLabel)
								.addComponent(inputItemjTextField))
				.addGroup(groupLayout.createParallelGroup())
				.addGroup(
						groupLayout.createParallelGroup()
								.addComponent(productNameJLabel)
								.addComponent(productNameField))
				.addComponent(addJButton));

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String newllyAddedItem = inputItemjTextField.getText();
		inputItemjTextField.setText(null);
		boolean sucessFlag = true;
		if (!"Add".equals(e.getActionCommand()))
		{
			jLabel.setText("Enter the new ".concat(e.getActionCommand()));
			sucessFlag = false;
		} 
		else 
		{
			if ("Add".equals(e.getActionCommand())) 
			{
				if (newllyAddedItem == null || newllyAddedItem.equals("")) 
				{
					JOptionPane.showMessageDialog(addNewRightPane,
							"Please enter the item to be added", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if (categoryRadioButton.isSelected())
		{
			showForAllSelections();
			if (!(newllyAddedItem == null || newllyAddedItem.equals(""))) 
			{
				Category category = new Category(newllyAddedItem);
				try 
				{
					category = category.addNewCategory();
				} 
				catch (SQLException sqlException) 
				{
					handleDBFailure(sqlException);
					sucessFlag = false;
				}

				lastAddedItem = category;
			}
		} 
		else if (shopRadioButton.isSelected()) 
		{
			showForAllSelections();

			if (!(newllyAddedItem == null || newllyAddedItem.equals(""))) 
			
			{

				Shop shop = new Shop(newllyAddedItem);
				try
				{
					 shop = shop.addNewShop();
				} 
				catch (SQLException sqlException)
				{
					handleDBFailure(sqlException);
					sucessFlag = false;
				}

				lastAddedItem = shop;
			}

		} 
		else if (brandRadioButton.isSelected())
		{
			if (productNameField == null || !productNameField.isVisible())
			{
				try {
					showProduct(null);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if (!(newllyAddedItem == null || newllyAddedItem.equals("")))
			{
				Brand brand = new Brand(newllyAddedItem);
				try 
				{
					brand.addNewBrand(productNameField.getSelectedItem());
				}
				catch (SQLException sqlException)
				{
					handleDBFailure(sqlException);
					sucessFlag = false;
				}

				lastAddedItem = newllyAddedItem;
			}
		} 
		else if (subcategoryRadioButton.isSelected())
		{
			if (categoryField == null || !categoryField.isVisible())
			{
				try {
					showCategoryComboxBox();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if (!(newllyAddedItem == null || newllyAddedItem.equals("")))
			{
				Subcategory subcategory = new Subcategory(newllyAddedItem);
				try 
				{
					subcategory = subcategory.addNewSubCategory(categoryField.getSelectedItem());
				}
				catch (SQLException sqlException)
				{
					handleDBFailure(sqlException);
					sucessFlag = false;
				}

				lastAddedItem = subcategory;
			}
		} 

		else if (productNameRadioButton.isSelected())
		{
			if (subCategoryField == null || !subCategoryField.isVisible()) 
			{
				try 
				{
					showSubCategoryComboBox();
				} 
				catch (SQLException e1) 
				{
					handleDBFailure(e1);
					e1.printStackTrace();
				}
			}
			if (!(newllyAddedItem == null || newllyAddedItem.equals("")))
			{
				Product product = new Product(newllyAddedItem);
				try 
				{
					product = product.addNewProduct(subCategoryField.getSelectedItem());
				} 
				catch (SQLException sqlException) 
				
				{
					handleDBFailure(sqlException);
					sucessFlag = false;
				}
				lastAddedItem = product;
			}
		}
		if (sucessFlag) 
		{
			int n = JOptionPane.showConfirmDialog(addNewRightPane,
					"Record successfully added!Want to add more?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			 if(1 == n) 
			 { 
				 parentDialog.dispose();
			 }
		}
		
		
		 
	}
	public void handleDBFailure(SQLException  sqlException)
	{
		JOptionPane.showMessageDialog(addNewRightPane,
				"Insertion failed because of an error. \nError Message  "
						+ sqlException.getMessage() + ".\nFix the error and try again", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

}