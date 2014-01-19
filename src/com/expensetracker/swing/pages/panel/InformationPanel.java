
package com.expensetracker.swing.pages.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.expensetracker.classes.Brand;
import com.expensetracker.classes.Category;
import com.expensetracker.classes.Product;
import com.expensetracker.classes.Subcategory;
import com.expensetracker.utility.ExpenseTrackerUtility;

public class InformationPanel extends JPanel implements FocusListener,ActionListener,TreeModelListener,TreeSelectionListener
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

	public InformationPanel() 
	{
		setLayout(new GridBagLayout());
		
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
		addToPanel(sampleTreePane, sampleTreeView, 0, 3, 0.0,2);

		JScrollPane treeView = new JScrollPane(tree);
		addToPanel(originalTreePane, treeView, 1, 3, 0.0,2);

		tree.setEditable(true);
		tree.addTreeSelectionListener(this);
		treeModel.addTreeModelListener(this);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(this);
		addButton.setActionCommand("Add");
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(this);

		JLabel jLabel = new JLabel("Enter the item to search");
		addToPanel(this, jLabel, 0, 0, 0.0,1);

		jTextField = new JTextField();
		jTextField.addFocusListener(this);
		addToPanel(this, jTextField, 1, 0, 0.0,2);


		addToPanel(this, sampleTreePane, 0, 2, 0,1);
		addToPanel(this, originalTreePane, 1, 2, 0,1);

		addToPanel(this, addButton, 0, 3, 0,1);
		addToPanel(this, removeButton, 1, 3, 0,1);

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
	public void focusGained(FocusEvent arg0) {

	}

	@Override
	public void focusLost(FocusEvent arg0)
 {

		DefaultMutableTreeNode node = null;
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
			JOptionPane.showMessageDialog(this, jTextField.getText() + " not found", "Not found", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean isSampleTreeFocused = tree.getLastSelectedPathComponent() == null;
		
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
    			JOptionPane.showMessageDialog(this,"Brand cannot have any children", "Invalid request", JOptionPane.INFORMATION_MESSAGE);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Insert of new Category failed because of the error ::", sqlException);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Insert of new Sub-Category failed because of the error ::", sqlException);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Insert of new Product failed because of the error ::", sqlException);

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
					ExpenseTrackerUtility.showFailureMessage(this, "Insert of new Brand failed because of the error ::", sqlException);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Deletion of Root node is not permitted", null);
				}

			}
			if(treeNode.length==2)
			{
				Category category = (Category)defaultMutableTreeNode.getUserObject();
				int n = JOptionPane.showConfirmDialog(this,
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
						ExpenseTrackerUtility.showFailureMessage(this, "Deletion of Category failed because of the error ::", sqlException);
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
				int n = JOptionPane.showConfirmDialog(this,
						"Are you sure you want to delete the Category: " + subcategory.getSubCategoryName(), "Confirm",
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
						ExpenseTrackerUtility.showFailureMessage(this, "Deletion of Sub-Category failed because of the error ::", sqlException);
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
				int n = JOptionPane.showConfirmDialog(this,
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
						ExpenseTrackerUtility.showFailureMessage(this, "Deletion of Product failed because of the error ::", sqlException);
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
				int n = JOptionPane.showConfirmDialog(this,
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
						ExpenseTrackerUtility.showFailureMessage(this, "Deletion of Brand failed because of the error ::", sqlException);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Update of Category failed because of the error ::", sqlException);

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
					ExpenseTrackerUtility.showFailureMessage(this, "Update of Category failed because of the error ::", sqlException);

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
					ExpenseTrackerUtility.showFailureMessage(this, "Update of Product failed because of the error ::", sqlException);
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
					ExpenseTrackerUtility.showFailureMessage(this, "Update of Brand failed because of the error ::", sqlException);
				}
			}
		}
		System.out.println("Now i must be getting invoked");
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
		System.out.println("Inserted");
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub
		System.out.println("removed");
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Structure chgsed");
		
	}
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
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
