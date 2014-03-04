package com.expensetracker.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.expensetracker.utility.ExpenseTrackerUtility;

public class Brand
{
        public String brandName;
        
        public int brandId;
         
         public Brand(String brandName)
         {
                 this.brandName = brandName;
         }

        public Brand() {
        }

        public String getBrandName() {
                return brandName;
        }

        public int getBrandId() {
                return brandId;
        }

        public void setBrandId(int brandId) {
                this.brandId = brandId;
        }

        public void setBrandName(String brandName) {
                this.brandName = brandName;
        }
         
        public static Vector<Brand> getAvailableBrands(Object obj)throws SQLException
        {
                Vector<Brand> brandList = new Vector<Brand>();
                Connection connection = ExpenseTrackerUtility.getConnection();
                ResultSet resultSet = null;
                PreparedStatement        stmt = null;
                if(connection!=null)
                {
                        try 
                        {
                                Product product = obj!=null?(Product)obj:null;
                                if(product!=null)
                                {
                                    stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Brand.getAvailableBrandsByProduct"));
                                	stmt.setInt(1, product.getProductId());
                                }
                                else
                                {
                                	stmt= connection.prepareStatement(ExpenseTrackerUtility.getQuery("Brand.getAvailableBrands"));
                                }
                                resultSet = stmt.executeQuery();
                                while (resultSet.next()) 
                                {
                                        Brand brand = new Brand();
                                        brand.setBrandId(resultSet.getInt(1));
                                        brand.setBrandName(resultSet.getString(2));
                                        
                                        brandList.add(brand);
                                
                                }
                        } 
                        catch (SQLException e)
                        {
                                e.printStackTrace();
                                throw e;
                        }
                        finally
                        {
                                ExpenseTrackerUtility.releaseResources(connection, stmt);
                        }
                }


                return brandList;
        }

        public  Brand addNewBrand(Object object)throws SQLException
        {
                Connection connection = ExpenseTrackerUtility.getConnection();
                Integer productId = null;
                Brand brand =null;
                PreparedStatement        stmt = null;
                if(connection!=null)
                {
                        try 
                        {
                                stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Brand.addBrand"));
                                Product product= object!=null?(Product) object:null;
                                if(product!=null)
                                {
                                        productId = product.getProductId();
                                }
                                stmt.setInt(2, productId);
                                stmt.setString(1, brandName);
                                stmt.executeUpdate();
                                
                                Statement statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
                                brand = new Brand(brandName);
                                 while(resultSet.next())
                                 { 
                                         brand.setBrandId(resultSet.getInt(1)); 
                                 } 
        
                        } 
                        catch (SQLException e)
                        {
                                e.printStackTrace();
                                throw e;                                
                        }
                        finally
                        {
                                ExpenseTrackerUtility.releaseResources(connection, stmt);
                        }
                }
                return brand;
        }
        public void removeBrand(Brand brand)throws SQLException
        {
                Connection connection = ExpenseTrackerUtility.getConnection();
                PreparedStatement        stmt = null;
                if(connection!=null)
                {
                        try 
                        {
                                stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Brand.deleteBrand"));
                                stmt.setInt(1, brand.getBrandId());
                                stmt.executeUpdate();
                        } catch (SQLException e)
                        {
                                e.printStackTrace();
                                throw e;
                        }
                        finally
                        {
                                ExpenseTrackerUtility.releaseResources(connection, stmt);
                        }
                }

        }
        public void updateBrand(Brand brand)throws SQLException
        {

                Connection connection = ExpenseTrackerUtility.getConnection();
                PreparedStatement        stmt = null;
                
                if(connection!=null)
                {
                        try 
                        {
                                stmt = connection.prepareStatement(ExpenseTrackerUtility.getQuery("Brand.updatedBrand"));
                                stmt.setString(1, brandName);
                                stmt.setInt(2, brand.getBrandId());
                                stmt.executeUpdate();
                        } 
                        catch (SQLException e)
                        {
                                e.printStackTrace();
                                throw e;
                        }
                        finally
                        {
                                ExpenseTrackerUtility.releaseResources(connection, stmt);
                        }
                }

        
        }
        
        @Override
    	public boolean equals(Object obj) {
    		if (this == obj)
    			return true;
    		
    		if (obj == null)
    			return false;
    		
    		if (obj instanceof Brand == false)
    			return false;
    		
    		return this.brandId == ((Brand)obj).brandId;
    	}


        @Override
        public String toString()
        {
                
                return brandName;
        }
        
        
        }

