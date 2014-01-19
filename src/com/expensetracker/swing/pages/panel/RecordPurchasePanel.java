package com.expensetracker.swing.pages.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.Character.Subset;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.omg.PortableInterceptor.SUCCESSFUL;

import com.expensetracker.classes.Brand;
import com.expensetracker.classes.Category;
import com.expensetracker.classes.Order;
import com.expensetracker.classes.Product;
import com.expensetracker.classes.Shop;
import com.expensetracker.classes.Subcategory;
import com.expensetracker.swing.pages.AddNewDialog;
import com.expensetracker.utility.ExpenseTrackerUtility;

/**
 * 
 */

/**
 * @author leela
 * 
 */
public class RecordPurchasePanel extends JPanel implements ItemListener,
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6144956813978134963L;
	JFormattedTextField dtPurchaseField;
	JTextField categoryField;
	JTextField subCategoryField;
	JComboBox<Product> productNameField;
	JFormattedTextField qtyField;
	JFormattedTextField priceField;
	JComboBox<Shop> purchasedFromField;
	JComboBox<Brand> brandNameField;
	JPanel jPanel;
	JFrame jFrame;
	JLabel shopNameJLabel;
	JLabel brandNameJLabel;
	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;

	JButton saveJButton;

	public RecordPurchasePanel(JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public void buildStoreDataGUI() {
		this.removeAll();
		
		
		jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());

		JLabel dtPurchaseJLabel = new JLabel("Date of Purchase");
		addToPanel(jPanel, dtPurchaseJLabel, 0, 0, 0.1);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		dtPurchaseField = new JFormattedTextField(format);
		// dtPurchaseField.setValue("yyyy-MM-dd");
		dtPurchaseField.setForeground(Color.gray);
		// dtPurchaseField.setColumns(5);

		// dtPurchaseField.requestFocusInWindow();
		/*
		 * try
		 * 
		 * { MaskFormatter dateMask = new MaskFormatter("####/##/##");
		 * dateMask.install(dtPurchaseField); } catch (ParseException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 */

		addToPanel(jPanel, dtPurchaseField, 1, 0, 0.8);

		JLabel itemJLabel = new JLabel("Product Name");
		addToPanel(jPanel, itemJLabel, 0, 1, 0.1);

		try {
				productNameField = new JComboBox<Product>(
						Product.getAvailableProducts(null));
			}
		catch (SQLException e)
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		addToPanel(jPanel, productNameField, 1, 1, 0.8);
		productNameField.addItemListener(this);
		productNameField.setName("Product Name");
		
		label2 = new JLabel("<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label2, 2, 1, 0.1);
		label2.addMouseListener(new LabelClickHandler("Product Name"));


		brandNameJLabel = new JLabel("Brand Name");
		addToPanel(jPanel, brandNameJLabel, 0, 2, 0.1);

		if (productNameField.getSelectedItem() != null) {
			try {
				brandNameField = new JComboBox<Brand>(
						Brand.getAvailableBrands(productNameField
								.getSelectedItem()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			brandNameField = new JComboBox<>();
		}
		addToPanel(jPanel, brandNameField, 1, 2, 0.8);

		label3 = new JLabel("<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label3, 2, 2, 0.1);
		label3.addMouseListener(new LabelClickHandler("Brand Name"));


		
		JLabel categoryJLabel = new JLabel("Category");
		addToPanel(jPanel, categoryJLabel, 0, 3, 0.1);

		categoryField = new JTextField();
		
		categoryField.setName("Category");
		categoryField.addActionListener(this);
		categoryField.setActionCommand("Category");

		label1 = new JLabel("<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label1, 2, 3, 0.1);
		label1.addMouseListener(new LabelClickHandler("Category Name"));

		JLabel subCategoryJLabel = new JLabel("Sub - Category");
		addToPanel(jPanel, subCategoryJLabel, 0, 4, 0.1);

		subCategoryField= new JTextField();
		subCategoryField.addActionListener(this);
		
		subCategoryField.setName("SubCategory");
		try {
			for (Subcategory subcategory : Subcategory.getAvailableSubCategories(productNameField.getSelectedItem())) {
				subCategoryField.setText(subcategory.getSubCategoryName());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addToPanel(jPanel, subCategoryField, 1, 4, 0.8);

		try
		{
			for (Category category : Category.getAvailableCategories(subCategoryField.getText())) 
			{
				categoryField.setText(category.getCategoryName());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addToPanel(jPanel, categoryField, 1, 3, 0.8);
		
		
		shopNameJLabel = new JLabel("Shop Name");
		addToPanel(jPanel, shopNameJLabel, 0, 5, 0.1);

		purchasedFromField = new JComboBox<Shop>(Shop.getAvailableShops());
		addToPanel(jPanel, purchasedFromField, 1, 5, 0.8);

		label4 = new JLabel("<html><a href=\" #\"> Not Available?</a></html>");
		addToPanel(jPanel, label4, 2, 5, 0.1);
		label4.addMouseListener(new LabelClickHandler("Shop Name"));

		JLabel qtyJLabel = new JLabel("Quantity");
		addToPanel(jPanel, qtyJLabel, 0, 6, 0.1);

		qtyField = new JFormattedTextField(NumberFormat.getNumberInstance());
		// qtyField.setValue(new Integer(quantity));
		// qtyField.setColumns(5);
		// qtyField.addPropertyChangeListener("value", this);
		addToPanel(jPanel, qtyField, 1, 6, 0.8);

		JLabel priceJLabel = new JLabel("Price");
		addToPanel(jPanel, priceJLabel, 0, 7, 0.1);

		NumberFormat amountDisplayFormat = NumberFormat.getCurrencyInstance();
		amountDisplayFormat.setMinimumFractionDigits(0);
		NumberFormat amountEditFormat = NumberFormat.getNumberInstance();

		priceField = new JFormattedTextField(new DefaultFormatterFactory(
				new NumberFormatter(amountDisplayFormat), new NumberFormatter(
						amountDisplayFormat), new NumberFormatter(
						amountEditFormat)));
		addToPanel(jPanel, priceField, 1, 7, 0.8);

		saveJButton = new JButton("Save");
		saveJButton.setDefaultCapable(true);
		saveJButton.setActionCommand("Save");
		saveJButton.addActionListener(this);

		JButton clearJButton = new JButton("Clear");
		buttonJPanel = new JPanel();

		buttonJPanel.add(saveJButton);
		buttonJPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonJPanel.add(clearJButton);
		buttonJPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		clearJButton.addActionListener(this);
		clearJButton.setActionCommand("Clear");
		// JPanel wrapper = new JPanel();
		this.setLayout(new BorderLayout());

		this.add(BorderLayout.CENTER, jPanel);
		this.add(BorderLayout.PAGE_END, buttonJPanel);

	}

	JPanel buttonJPanel;

	public void setDefaultFocusAndDefaultButton() {
		if (dtPurchaseField != null) {
			dtPurchaseField.requestFocus();
		}
		if (buttonJPanel != null) {
			if (saveJButton != null) {
				if (buttonJPanel.getRootPane() != null) {
					JRootPane jRootPane = buttonJPanel.getRootPane();
					if (jRootPane != null) {
						jRootPane.setDefaultButton(saveJButton);

					}
				}

			}
		}
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
	public void actionPerformed(ActionEvent e) {
		if ("Save".equals(e.getActionCommand())) {
			boolean successFlag = false;
			try {
				boolean chkStatsus = checkMandatoryFields();
				if (chkStatsus) {
					saveDetails();
					successFlag = true;
				} else {
					successFlag = false;

				}
			} catch (SQLException sqlException) {
				// TODO Auto-generated catch block
				successFlag = false;
				sqlException.printStackTrace();
				JOptionPane.showMessageDialog(jPanel,
						"Insertion failed because of an error. \nError Message  "
								+ sqlException.getMessage()
								+ ".\nFix the error and try again", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (successFlag) {
				int n = JOptionPane.showConfirmDialog(jPanel,
						"Record successfully added!Want to add more?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (1 == n) {
					jFrame.dispose();
				} else {
					clearFields();
				}
			}

		} else if ("Clear".equals(e.getActionCommand())) {
			clearFields();
		} 
		}

	public boolean checkMandatoryFields() {
		if (dtPurchaseField.getValue() == null) {
			JOptionPane.showMessageDialog(jPanel, "Enter purchase date",
					"Date missing", JOptionPane.ERROR_MESSAGE);
			return false;

		}
		if (brandNameField.isVisible()) {
			Brand brand = (Brand) (brandNameField.getSelectedItem());
			if (brand == null) {
				JOptionPane.showMessageDialog(jPanel, "Select or add a Brand",
						"Brand missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}
		String category =  categoryField.getText();
		if (category == null) {
			JOptionPane.showMessageDialog(jPanel, "Select or add a Category",
					"Category missing", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Product product = (Product) (productNameField.getSelectedItem());
		if (product == null) {
			JOptionPane.showMessageDialog(jPanel,
					"Select or add a Product to proceed", "Product missing",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (purchasedFromField.isVisible()) {
			Shop shop = (Shop) (purchasedFromField.getSelectedItem());
			if (shop == null) {
				JOptionPane.showMessageDialog(jPanel,
						"Select or add a Shop to proceed", "Shop missing",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if (qtyField.getValue() == null) {
			Number number = (Number) qtyField.getValue();
			if (number == null) {

				JOptionPane.showMessageDialog(jPanel, "Enter quantity",
						"Quantity missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if (priceField.getValue() == null) {
			Number numberPrice = (Number) priceField.getValue();
			if (numberPrice == null) {
				JOptionPane.showMessageDialog(jPanel, "Enter Price",
						"Price missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	protected String showAddNewItemUI(String selectedLink,
			Category selectedCategory, Product selectedProduct) {
		AddNewDialog dialog = new AddNewDialog();
		dialog.show(selectedLink, selectedCategory, selectedProduct);

		return dialog.getLastAddedItem();
	}

	public void saveDetails() throws SQLException {
		Order order = new Order();
		order.setPurchaseDate((Date) dtPurchaseField.getValue());

		if (brandNameField.isVisible()) {
			Brand brand = (Brand) (brandNameField.getSelectedItem());
			order.setBrand(brand);

		}

		Category category = new Category(categoryField.getText());
		category.setCategoryId(0);
		order.setCategory(category);

		Product product = (Product) (productNameField.getSelectedItem());
		order.setProduct(product);

		if (purchasedFromField.isVisible()) {
			Shop shop = (Shop) (purchasedFromField.getSelectedItem());
			order.setShop(shop);
		}

		Number number = (Number) qtyField.getValue();
		order.setQuantity(number.intValue());
		Number priceNumber = (Number) priceField.getValue();
		double price = priceNumber.doubleValue();
		order.setPrice(price);

		order.saveOrderDetails(order);
	}

	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		JComboBox comboBox = (JComboBox) itemEvent.getSource();
		if ("Product Name".equals(comboBox.getName()))
		{
			Product selectedProduct = (Product) comboBox.getSelectedItem();
			DefaultComboBoxModel<Brand> defaultComboBoxModel = new DefaultComboBoxModel<>();
			try 
			{
				for (Brand brand : Brand.getAvailableBrands(selectedProduct))
					defaultComboBoxModel.addElement(brand);
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			brandNameField.setModel(defaultComboBoxModel);
			
			try {
				for (Subcategory subcategory : Subcategory
						.getAvailableSubCategories(selectedProduct))
				{	
					subCategoryField.setText(subcategory.getSubCategoryName());

				}	
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			try {
				for (Category category : Category
						.getAvailableCategories(subCategoryField.getText()))
				{	
					categoryField.setText(category.getCategoryName());

				}	
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}
		
		}

	private void reloadComboBoxes(Object obj)
	{
		if(obj  instanceof Subcategory)
		{
			Subcategory selectedSubCategory = (Subcategory)obj;
			
			
			DefaultComboBoxModel<Product> defaultComboBoxModel = new DefaultComboBoxModel<>();
			try 
			{
				for (Product product : Product
						.getAvailableProducts(selectedSubCategory))
				{	
					defaultComboBoxModel.addElement(product);
				}	
			} 
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			productNameField.setModel(defaultComboBoxModel);

			DefaultComboBoxModel<Brand> defaultComboBoxModelForBrand = new DefaultComboBoxModel<>();

			if (productNameField.getSelectedItem() == null)
			{
				brandNameField.setModel(defaultComboBoxModelForBrand);
			}
			else 
			{
				try 
				{
					for (Brand brand : Brand
							.getAvailableBrands(productNameField
									.getSelectedItem()))
					{	
						defaultComboBoxModelForBrand.addElement(brand);
					}	
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				brandNameField.setModel(defaultComboBoxModelForBrand);

			}

		}

	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.dispose();
	}

	class LabelClickHandler implements MouseListener {
		String selectedLink;
		Product product = null;
		Category category = null;

		public LabelClickHandler(String selectedLinkStr) {
			selectedLink = selectedLinkStr;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {/*
			product = (Product) productNameField.getSelectedItem();
			category = (Category) categoryField.getSelectedItem();
			String lastItem = showAddNewItemUI(selectedLink, category, product);

			if ("Category Name".equals(selectedLink)) {
				categoryField.removeAllItems();

				try {
					for (Category category : Category.getAvailableCategories(null))
						categoryField.addItem(category);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!ExpenseTrackerUtility.isNullOrEmpty(lastItem)) {
					categoryField.setSelectedItem(lastItem);
				}
			} else if ("Product Name".equals(selectedLink)) {
				productNameField.removeAllItems();

				if (subCategoryField.getSelectedItem() != null) {
					try {
						for (Product product : Product
								.getAvailableProducts(subCategoryField
										.getSelectedItem()))
							productNameField.addItem(product);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					try {
						for (Product product : Product
								.getAvailableProducts(null))
							productNameField.addItem(product);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (!ExpenseTrackerUtility.isNullOrEmpty(lastItem)) {
					productNameField.setSelectedItem(lastItem);
				}

			} else if ("Brand Name".equals(selectedLink)) {
				brandNameField.removeAllItems();

				if (productNameField.getSelectedItem() != null) {
					try {
						for (Brand brand : Brand
								.getAvailableBrands(productNameField
										.getSelectedItem()))
							brandNameField.addItem(brand);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						for (Brand brand : Brand.getAvailableBrands(null))
							brandNameField.addItem(brand);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!ExpenseTrackerUtility.isNullOrEmpty(lastItem)) {
					brandNameField.setSelectedItem(lastItem);
				}

			} else if ("Shop Name".equals(selectedLink)) {
				purchasedFromField.removeAllItems();

				for (Shop shop : Shop.getAvailableShops())
					purchasedFromField.addItem(shop);

				if (!ExpenseTrackerUtility.isNullOrEmpty(lastItem)) {
					purchasedFromField.setSelectedItem(lastItem);
				}

			}

		*/}

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

	public void clearFields() {
		priceField.setText("");
		priceField.setValue(null);
		qtyField.setText("");
		dtPurchaseField.setText("");
	}
}
