
package com.expensetracker.swing.pages.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.expensetracker.classes.Brand;
import com.expensetracker.classes.Category;
import com.expensetracker.classes.Order;
import com.expensetracker.classes.Product;
import com.expensetracker.classes.Report;
import com.expensetracker.classes.Subcategory;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class InformationPanel extends JPanel implements ActionListener
 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3360181986159832554L;
	DefaultMutableTreeNode top;
	DefaultMutableTreeNode sampleTop;

	JTextField jTextField;
	JTree tree;
	DefaultTreeModel treeModel;
	int newNodeCount;
	String currentlySelectedNodeValue;
	int currentlySelectedNodeKey;
	JTree sampleTree;
	JRadioButton searchAddOrRemoveItemButton;
	JRadioButton getRecentPurchaseInfoButton;
	JFrame jFrame;
	MyCreateSearchAddOrRemoveItemTree myCreateSearchAddOrRemoveItemTree;
	MyGetInfoPanel myGetInfoPanel;

	public InformationPanel(JFrame jFrame) 
	{
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

		this.jFrame = jFrame;
		searchAddOrRemoveItemButton = new JRadioButton("Search Or Add Or Remove Items");
		searchAddOrRemoveItemButton.addActionListener(this);
		searchAddOrRemoveItemButton.setName("SAR");
		searchAddOrRemoveItemButton.setSelected(true);
		myCreateSearchAddOrRemoveItemTree = new MyCreateSearchAddOrRemoveItemTree();
		
		getRecentPurchaseInfoButton = new JRadioButton("Get recent purchase proudct info");
		getRecentPurchaseInfoButton.addActionListener(this);
		getRecentPurchaseInfoButton.setName("SAR");
		
		ButtonGroup group = new ButtonGroup();
		group.add(searchAddOrRemoveItemButton);
		group.add(getRecentPurchaseInfoButton);

		add(searchAddOrRemoveItemButton);
		add(getRecentPurchaseInfoButton);
		
		myCreateSearchAddOrRemoveItemTree.buildGUI();

		
		}
    private class MyRenderer extends DefaultTreeCellRenderer
    {
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) 
		{

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			
			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)value;
			setToolTipText(myCreateSearchAddOrRemoveItemTree.determineItemType(defaultMutableTreeNode));
			
			return this;
		}

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

	private void createNodes(DefaultMutableTreeNode top) throws SQLException
	{
		Vector<Category> categories = Category.getAvailableCategories(null);
		for (Category categoryObj : categories) 
		{
			DefaultMutableTreeNode categoryNode = addObject(null, categoryObj);

			Vector<Subcategory> subCatogries = Subcategory
					.getAvailableSubCategories(categoryObj);

			for (Subcategory subcategoryObj : subCatogries)
			{
				DefaultMutableTreeNode subCategoryNode = addObject(categoryNode, subcategoryObj);


				Vector<Product> products = Product
						.getAvailableProducts(subcategoryObj);

				for (Product productObj : products) 
				{
					DefaultMutableTreeNode productNode = addObject(
							subCategoryNode, productObj);
					Vector<Brand> brandList = Brand
							.getAvailableBrands(productObj);
					for (Brand brand2 : brandList)
					{
						DefaultMutableTreeNode brandNode = addObject(
								productNode, brand2);
					}

				}
			}
		}

	}
	private void createSampleNodes(DefaultMutableTreeNode top) throws SQLException
	{
	
		for (int i=0;i<2;i++) 
		{
			DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode("Category " +(i+1));

			DefaultMutableTreeNode subCategoryNode = new DefaultMutableTreeNode("Sub-Category "+(i+1));
			categoryNode.add(subCategoryNode);
			
			DefaultMutableTreeNode productNode = new DefaultMutableTreeNode("Product "+(i+1));
			subCategoryNode.add(productNode);
			
			DefaultMutableTreeNode brandNode = new DefaultMutableTreeNode("Brand "+(i+1));
			productNode.add(brandNode);
			
			sampleTop.add(categoryNode);
			
		}
	}

	public DefaultMutableTreeNode addObject(Object child) 
	{
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) 
		{
			parentNode = top;
		} 
		else 
		{
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}

		return addObject(parentNode, child, true);
	  }

	    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
	                                            Object child) 
	    {
	        return addObject(parent, child, false);
	    }

	    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
	                                            Object child, 
	                                            boolean shouldBeVisible) 
	    {
	        DefaultMutableTreeNode childNode = 
	                new DefaultMutableTreeNode(child);

	        if (parent == null)
	        {
	            parent = top;
	        }
		
	        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

	        if (shouldBeVisible) 
	        {
	            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
	        }
	        return childNode;
	    }


	
	@Override
	public void actionPerformed(ActionEvent e)
	{
			if(searchAddOrRemoveItemButton.isSelected())
		{
			if(myCreateSearchAddOrRemoveItemTree==null)
				
			{	
				myCreateSearchAddOrRemoveItemTree = new MyCreateSearchAddOrRemoveItemTree();
			}
			
			myCreateSearchAddOrRemoveItemTree.buildGUI();
			
			if(myGetInfoPanel!=null)
			{
				myGetInfoPanel.setVisible(false);
			}
		}
		else if(getRecentPurchaseInfoButton.isSelected())
		{
			if(myCreateSearchAddOrRemoveItemTree!=null)
			{	
				myCreateSearchAddOrRemoveItemTree.setVisible(false);
			
			}
			if(myGetInfoPanel==null)
			{	
				myGetInfoPanel = new MyGetInfoPanel();
			}
			try {
					myGetInfoPanel.buildGUI();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			}
			
			
			

		}	

		
	
	class MyGetInfoPanel implements ItemListener
	{
		
		List<Order> orderList = null;
		JPanel wrapperPanel = null;
		JComboBox<Product> productField = null;
		JTable jTable = null;
		JScrollPane jScrollPane = null;
		public void setVisible(boolean showFlag)
		{
			wrapperPanel.setVisible(showFlag);
		}
		public void buildGUI() throws SQLException
		{
			
			 wrapperPanel = new JPanel();
			wrapperPanel.setVisible(true);

			wrapperPanel.setLayout(new GridBagLayout());
			
			JLabel productJLabel = new JLabel("Select the product");
			addToPanel(wrapperPanel, productJLabel, 0, 0, 0.0,2);

			wrapperPanel.add(productJLabel);
			
			 productField = new JComboBox<Product>(Product.getAvailableProducts(null));
			addToPanel(wrapperPanel, productField, 1, 0, 0.0,2);

			productField.addItemListener(this);
			
			retrieveAndBuildTableForLastPurchaseInfo(((Product)productField.getSelectedItem()).getProductId());
			add(wrapperPanel);
			jFrame.pack();

			
		}

		public class MyTableModel implements TableModel
		 {

				private String[] columnNames = {"Date of Purchase", "Product Name", "Brand Name","Shop Name","Price","Comments" ,"Days Count"};

				@Override
				public void addTableModelListener(TableModelListener l) {
					// TODO Auto-generated method stub

				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if(getValueAt(0, columnIndex)!=null)
					{	
						return getValueAt(0, columnIndex).getClass();
					}
					else
					{
						return String.class;
					}
				}

				@Override
				public int getColumnCount() {
					return columnNames.length;
				}

				@Override
				public String getColumnName(int columnIndex) {
					return columnNames[columnIndex];
				}

				@Override
				public int getRowCount()
				{
					if (orderList != null
							&& !orderList.isEmpty()) 
					{
						return orderList.size();
					} 
					else 
					{
						return 0;
					}
				}

				@Override
				public Object getValueAt(int rowIndex, int columnIndex)
				{
					Calendar cal = Calendar.getInstance();
					Date currentDate = cal.getTime();
					Order order = (Order) orderList.get(rowIndex);
					switch (columnIndex) 
					{
					case 0:
						return order.getPurchaseDate();
					case 1:
						return order.getProductName();
					case 2:
						return order.getBrandName();
					case 3:
						return order.getShopName();
					case 4:
						return order.getPrice();
					
					case 5:
						return order.getCommentTxt();
						
					case 6:
				        return  "Bought " + (int) ((currentDate.getTime() - order.getPurchaseDate().getTime()) / (1000 * 60 * 60 * 24)) + " days ago ";


					}
					return null;
				}

				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void removeTableModelListener(TableModelListener arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setValueAt(Object arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub

				}

			}
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub

			retrieveAndBuildTableForLastPurchaseInfo(((Product)((JComboBox<Product>)e.getSource()).getSelectedItem()).getProductId());
			jFrame.pack();
		
		}

		private void retrieveAndBuildTableForLastPurchaseInfo(int productId)
		{
			
			try {
				orderList = Report.retrieveLastPurchaseInfo(productId);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(orderList.isEmpty() || orderList.size()==0)
			{
				setVisibleComponents(false);
				return;
			}
			else
			{
				jTable = new JTable(new MyTableModel());
				jTable.setPreferredScrollableViewportSize(new Dimension(450, 70));
				jTable.setFillsViewportHeight(true);
				jTable.setAutoCreateRowSorter(true);

				ExpenseTrackerUtility.initColumnSizes(jTable);
				jScrollPane = new JScrollPane(jTable);
				addToPanel(wrapperPanel, jScrollPane, 0, 1, 0.0, 2);
				setVisible(true);
			}
			jFrame.pack();
		
		
		}
		private void setVisibleComponents(boolean showFlag)
		{
			ExpenseTrackerUtility.showComponenets(showFlag, jScrollPane);
		}
	}
	

	class MyCreateSearchAddOrRemoveItemTree implements ActionListener,TreeSelectionListener,TreeModelListener,FocusListener
	{

		JPanel wrapperPanel;
		
		public void setVisible(boolean showFlag)
		{
			wrapperPanel.setVisible(showFlag);
		}
		public void buildGUI()
		{
		 wrapperPanel = new JPanel(new GridBagLayout());
		 wrapperPanel.setVisible(true);
		JPanel sampleTreePane = new JPanel(new GridBagLayout());
		
		JPanel originalTreePane = new JPanel(new GridBagLayout());

		
		top = new DefaultMutableTreeNode("All");
		sampleTop = new DefaultMutableTreeNode("All");
		treeModel = new DefaultTreeModel(top);

		try {
			createSampleNodes(top);
			sampleTree = new JTree(sampleTop);
			sampleTree.setName("Sample Tree");
			createNodes(top);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tree = new JTree(treeModel);
		tree.setName("Tree");
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		sampleTreePane
		.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory
						.createTitledBorder("Sample Tree"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		originalTreePane
		.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory
						.createTitledBorder("Original Tree"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JScrollPane sampleTreeView = new JScrollPane(sampleTree);
		addToPanel(sampleTreePane, sampleTreeView, 1, 3, 0.0,2);

		JScrollPane treeView = new JScrollPane(tree);
		addToPanel(originalTreePane, treeView, 2, 3, 0.0,2);

        ToolTipManager.sharedInstance().registerComponent(tree);

		tree.setEditable(true);
		tree.setCellRenderer(new MyRenderer());
		tree.addTreeSelectionListener(this);
		treeModel.addTreeModelListener(this);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(this);
		addButton.setActionCommand("Add");
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(this);

		JLabel jLabel = new JLabel("Enter the item to search");
		addToPanel(wrapperPanel, jLabel, 0, 1, 0.0,1);

		jTextField = new JTextField();
		jTextField.addFocusListener(this);
		addToPanel(wrapperPanel, jTextField, 1, 1, 0.0,2);


		addToPanel(wrapperPanel, sampleTreePane, 0, 2, 0,1);
		addToPanel(wrapperPanel, originalTreePane, 1, 2, 0,1);

		addToPanel(wrapperPanel, addButton, 0, 3, 0,1);
		addToPanel(wrapperPanel, removeButton, 1, 3, 0,1);
		
		add(wrapperPanel);
		jFrame.pack();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSampleTreeFocused = false;
			if(tree!=null)
				
			{	
				 isSampleTreeFocused = tree.getLastSelectedPathComponent() == null;
			
			}
			
			if(!isSampleTreeFocused)
			{
			if ("Add".equals(e.getActionCommand()))
			{
		        TreePath parentPath = tree.getSelectionPath();
		        DefaultMutableTreeNode parentNode = null;
		        if(parentPath==null)
		        {
		        	parentNode = top;
		        }
		        else
		        {	
		        	parentNode = (DefaultMutableTreeNode)
		                         (parentPath.getLastPathComponent());
		        }	
		         
		        String childNodeNewString = "New Item "+newNodeCount++;
		        //This piece of code avoid duplicate Category , Product and Brand names
	    		Enumeration en = top.breadthFirstEnumeration();
	    		DefaultMutableTreeNode defaultMutableTreeNode=null;
	    		while (en.hasMoreElements())
	    		{
	    			defaultMutableTreeNode = (DefaultMutableTreeNode) en.nextElement();
	    			if(defaultMutableTreeNode.getUserObject().toString().equals(childNodeNewString))
	    			{
	    				childNodeNewString = "New Item "+ newNodeCount++;
	    			}	

	    		}
	          
		        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childNodeNewString);

	           TreeNode[] treeNodes = treeModel.getPathToRoot(parentNode);
	           
	            //Brand should not have any children, so restrict it here
	            if(treeNodes.length==5)
	            {
	            	childNode.setAllowsChildren(false);
	    			JOptionPane.showMessageDialog(null,"Brand cannot have any children", "Invalid request", JOptionPane.INFORMATION_MESSAGE);
	    			return;
	            }
	           
				treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
	            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
	            //Need to identify if the user is trying to add a new Category or Product or Brand and invoke the corresponding db call
	            String itemType = determineItemType(childNode);
	            if("Category".equals(itemType))
	            {
	            	
	            	Category category = new Category(childNode.getUserObject().toString());
	            	childNode.setUserObject(category);
	            	try
	            	{
	            		category = category.addNewCategory();
	            		childNode.setUserObject(category);
					} 
	            	catch (SQLException sqlException)
	            	{
						ExpenseTrackerUtility.showFailureMessage(null, "Insert of new Category failed because of the error ::", sqlException);
					}
	            }
	            if("SubCategory".equals(itemType))
	            {
	            	
	            	Subcategory subcategory = new Subcategory(childNode.getUserObject().toString());
	            	childNode.setUserObject(subcategory);
	            	try
	            	{
	            		subcategory = subcategory.addNewSubCategory(((DefaultMutableTreeNode)treeNodes[1]).getUserObject());
	            		childNode.setUserObject(subcategory);
					} 
	            	catch (SQLException sqlException)
	            	{
						ExpenseTrackerUtility.showFailureMessage(null, "Insert of new Sub-Category failed because of the error ::", sqlException);
					}
	            }
	            if("Product".equals(itemType))
	            {
	            	Product product = new Product(childNode.getUserObject().toString());
	            	try 
	            	{
						product = product.addNewProduct(((DefaultMutableTreeNode)treeNodes[2]).getUserObject());
		            	childNode.setUserObject(product);

					} 
	            	catch (SQLException sqlException)
	            	{
						ExpenseTrackerUtility.showFailureMessage(null, "Insert of new Product failed because of the error ::", sqlException);

					}
	            }	
	            if("Brand".equals(itemType))
	            {
	            	Brand brand = new Brand(childNode.getUserObject().toString());
	            	try
	            	{
						brand = brand.addNewBrand(((DefaultMutableTreeNode)treeNodes[3]).getUserObject());
		            	childNode.setUserObject(brand);

					}
	            	catch (SQLException sqlException)
					{
						// TODO Auto-generated catch block
						ExpenseTrackerUtility.showFailureMessage(null, "Insert of new Brand failed because of the error ::", sqlException);
					}
	            }
	            
	           
	            

			}
			if("Remove".equals(e.getActionCommand()))
			{
				DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)tree.getAnchorSelectionPath().getLastPathComponent();
				TreeNode[] treeNode = treeModel.getPathToRoot(defaultMutableTreeNode);
				if(defaultMutableTreeNode!=null && defaultMutableTreeNode.getUserObject() instanceof String)
				{
					if("All".equals(defaultMutableTreeNode.getUserObject()))
					{
						ExpenseTrackerUtility.showFailureMessage(null, "Deletion of Root node is not permitted", null);
					}

				}
				if(treeNode.length==2)
				{
					Category category = (Category)defaultMutableTreeNode.getUserObject();
					int n = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the Category: " + category.getCategoryName(), "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(n==0)
					{
						try 
						{
							category.removeCategory((Category) defaultMutableTreeNode
									.getUserObject());
						} 
						catch (SQLException sqlException) 
						{
							ExpenseTrackerUtility.showFailureMessage(null, "Deletion of Category failed because of the error ::", sqlException);
							return;

						}
						treeModel
								.removeNodeFromParent((DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent());
					}
				}
				if(treeNode.length==3)
				{
					Subcategory subcategory = (Subcategory)defaultMutableTreeNode.getUserObject();
					int n = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the Sub-Category: " + subcategory.getSubCategoryName(), "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(n==0)
					{
						try 
						{
							subcategory.removeSubCategory((Subcategory) defaultMutableTreeNode
									.getUserObject());
						} 
						catch (SQLException sqlException) 
						{
							ExpenseTrackerUtility.showFailureMessage(null, "Deletion of Sub-Category failed because of the error ::", sqlException);
							return;

						}
						treeModel
								.removeNodeFromParent((DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent());
					}
				}

				if(treeNode.length==4)
				{
					Product product = (Product)defaultMutableTreeNode.getUserObject();
					int n = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the Product: " + product.getProductName(), "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(n==0)
					{
						try
						{
							product.removeProduct(product);
						} 
						catch (SQLException sqlException)
						{
							ExpenseTrackerUtility.showFailureMessage(null, "Deletion of Product failed because of the error ::", sqlException);
							return;
						}
						treeModel
								.removeNodeFromParent((DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent());
					}
				}
				if(treeNode.length==5)
				{
					Brand brand = (Brand)defaultMutableTreeNode.getUserObject();
					int n = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the Brand: " + brand.getBrandName(), "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(n==0)
					{
						try
						{
							brand.removeBrand(brand);
						} 
						catch (SQLException sqlException) 
						{
							ExpenseTrackerUtility.showFailureMessage(null, "Deletion of Brand failed because of the error ::", sqlException);
							return;
						}
						treeModel
								.removeNodeFromParent((DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent());
					}
				}
			}
			}
			
		}
		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void focusLost(FocusEvent arg0) {


			DefaultMutableTreeNode node = null;
			if(top!=null)
			{
			Enumeration e = top.breadthFirstEnumeration();
			boolean foundSucessFul = false;
			while (e.hasMoreElements())
			{
				node = (DefaultMutableTreeNode) e.nextElement();
				String REGEX = "\\A"+jTextField.getText();

				Pattern p = Pattern.compile(REGEX);
				Matcher m = p.matcher(node.getUserObject().toString()); // get a matcher object
				while (m.find()) 
				{

					
						if (node != null) 
						{
							TreeNode[] nodes = treeModel.getPathToRoot(node);
							TreePath path = new TreePath(nodes);
							tree.scrollPathToVisible(path);
							tree.setSelectionPath(path);
							foundSucessFul = true;
						} 
						else 
						{
							System.out.println("Node with string "
									+ jTextField.getText() + " not found");
						
					}
				}
			}
			if(!foundSucessFul)
			{
				JOptionPane.showMessageDialog(null, jTextField.getText() + " not found", "Not found", JOptionPane.INFORMATION_MESSAGE);
			}
			}
					
		}
		@Override
		public void treeNodesChanged(TreeModelEvent e) {

			// TODO Auto-generated method stub	
			//int a= 3;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
	        int index = e.getChildIndices()[0];
	        node = (DefaultMutableTreeNode)(node.getChildAt(index));

			String editedStringValue = (String)node.getUserObject();
			String itemType = determineItemType(node);
			if("Category".equals(itemType))
			{
				if (currentlySelectedNodeValue != null
						&& !currentlySelectedNodeValue.equals(editedStringValue))
				{
					try
					{
						Category category = new Category(editedStringValue);
						category.setCategoryId(currentlySelectedNodeKey);
						category.updateCategory(category);
						node.setUserObject(category);
					} 
					
					catch (SQLException sqlException) 
					{
						ExpenseTrackerUtility.showFailureMessage(null, "Update of Category failed because of the error ::", sqlException);

					}
				}
			}
			if("SubCategory".equals(itemType))
			{
				if (currentlySelectedNodeValue != null
						&& !currentlySelectedNodeValue.equals(editedStringValue))
				{
					try
					{
						Subcategory subcategory = new Subcategory(editedStringValue);
						subcategory.setSubCategoryId(currentlySelectedNodeKey);
						subcategory.updateSubCategory(subcategory);
						node.setUserObject(subcategory);
					} 
					
					catch (SQLException sqlException) 
					{
						ExpenseTrackerUtility.showFailureMessage(null, "Update of Category failed because of the error ::", sqlException);

					}
				}
			}

			if("Product".equals(itemType))
			{
				if (currentlySelectedNodeValue != null
						&& !currentlySelectedNodeValue.equals(editedStringValue))
				{
					try
					{
						Product product = new Product(editedStringValue);
						product.setProductId(currentlySelectedNodeKey);
						product.updateProduct(product);
						node.setUserObject(product);

					} 
					
					catch (SQLException sqlException)
					{
						ExpenseTrackerUtility.showFailureMessage(null, "Update of Product failed because of the error ::", sqlException);
					}
				}
			}
			if("Brand".equals(itemType))
			{
				if (currentlySelectedNodeValue != null
						&& !currentlySelectedNodeValue.equals(editedStringValue))
				{
					try
					{
						Brand brand = new Brand(editedStringValue);
						brand.setBrandId(currentlySelectedNodeKey);
						brand.updateBrand(brand);
						node.setUserObject(brand);

					} 
					
					catch (SQLException sqlException) 
					{
						ExpenseTrackerUtility.showFailureMessage(null, "Update of Brand failed because of the error ::", sqlException);
					}
				}
			}
					
		}
		public String determineItemType(DefaultMutableTreeNode defaultMutableTreeNode)
		{
			TreeNode[] treeNode = treeModel.getPathToRoot(defaultMutableTreeNode);
			if(treeNode.length==2)
			{
				return "Category";
			}
			if(treeNode.length==3)
			{
				return "SubCategory";
			}
			if(treeNode.length==4)
			{
				return "Product";
			}
			if(treeNode.length==5)
			{
				return "Brand";
			}
			return null;
		}
		@Override
		public void treeNodesInserted(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void treeStructureChanged(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void valueChanged(TreeSelectionEvent e) {

			JTree jTree = (JTree)e.getSource();
			DefaultMutableTreeNode defaultMutableTreeNode=(DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
			if(defaultMutableTreeNode!=null && defaultMutableTreeNode.getUserObject() instanceof Category)
			{
				Category category = (Category)defaultMutableTreeNode.getUserObject();
				currentlySelectedNodeValue = category.getCategoryName();
				currentlySelectedNodeKey = category.getCategoryId();

			}
			if(defaultMutableTreeNode!=null && defaultMutableTreeNode.getUserObject() instanceof Subcategory)
			{
				Subcategory subcategory = (Subcategory)defaultMutableTreeNode.getUserObject();
				currentlySelectedNodeValue = subcategory.getSubCategoryName();
				currentlySelectedNodeKey = subcategory.getSubCategoryId();

			}

			if(defaultMutableTreeNode!=null && defaultMutableTreeNode.getUserObject() instanceof Product)
			{
				Product product = (Product)defaultMutableTreeNode.getUserObject();
				currentlySelectedNodeValue = product.getProductName();
				currentlySelectedNodeKey = product.getProductId();

			}
			if(defaultMutableTreeNode!=null && defaultMutableTreeNode.getUserObject() instanceof Brand)
			{
				Brand brand = (Brand)defaultMutableTreeNode.getUserObject();
				currentlySelectedNodeValue = brand.getBrandName();
				currentlySelectedNodeKey = brand.getBrandId();

			}
					
		}

	}

}