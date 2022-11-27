package Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Category.Category;
import Coupon.Coupon;
import Exception.CouponSystemException;
import Exception.ExceptionMessage;
import Pool.ConnectionPool;

public class CustomersDBDAO implements CustomerDAO {


    /**
     * Checks is customer exists by sending Query to DB,
     * Checks if any customer in the DB have the same email and password.
     * 
     * @param Email
     * @param password
     * @return boolean
     * @throws CouponSystemException
     */
    public boolean isCustomerExists(String Email, String password){
        String sql = "SELECT Email FROM Customers WHERE Email = ? AND password = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, Email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            } 
            throw new CouponSystemException(ExceptionMessage.AUTHENTICATION_FAILED.getMessage());
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Add new customer to DB,
     * First send Query to DB to check if any customer in DB has the same email.
     * If not insert the new customer the the DB by sending Query.
     * Return <int> CustomerID OR return if exists.
     * @param customer
     * @return int
     * @throws CouponSystemException
     */
    public int addCustomer(Customer customer) {
        String sql = "insert into Customers (id, First_Name, Last_Name, Email, password) values(0, ?, ?, ?, ?)";
        String sql2 = "select * from Customers where Email = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, customer.getEmail());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                throw new CouponSystemException(ExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());
            
            }
            ps.setString(1, customer.getFirst_Name());
            ps.setString(2, customer.getLast_Name());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            customer.setId(id);
            System.out.println(customer);
            return id;
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        return 0;

    }

    /**
     * Update Exists Customer first name, last name. email and password
     * Identify customer by ID number.
     * @param customer
     * @throws CouponSystemException
     */
    public void updateCutomer(Customer customer) {
        String sql = "update Customers set First_Name = ?, Last_Name = ?, Email = ?, password = ? where id = ?"; // quarry
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, customer.getFirst_Name());
            ps.setString(2, customer.getLast_Name());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPassword());
            ps.setInt(5, customer.getId());
            if(!isCustomerExists(customer.getEmail(), customer.getPassword())){
             return;
            }
            String sql2 = "select * from Customers where Email = ?";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, customer.getEmail());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                throw new CouponSystemException(ExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());
            }
            ps.executeUpdate();
            System.out.println("Customer has beed updtaed: " + customer);
        }catch(SQLException| CouponSystemException e){
            System.out.println(e.getMessage());
        }
        return;
    }


    
    /** 
     * Delete customer from program DB,
     * Identify customer by CustomerID
     * First delete customer coupons by {@link #deleteCutomerCoupons(int)}
     * Then send Query to DB to delete the Customer.
     * @param CustomerID
     * @throws CouponSystemException
     */
    public void deleteCutomer(int CustomerID){
        String sql = "delete from Customers where id = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            deleteCutomerCoupons(CustomerID);
            ps.executeUpdate();
            System.out.println("The Customer has been deleted. " + "("+ CustomerID + ")");
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.CUSTOMER_NOT_EXIST.getMessage());
        }
    }


    
    /** 
     * Delete customer coupons by sending Query to program DB.
     * Identify customer by CustomerID.
     * @param CustomerID
     * @throws CouponSystemException
     */
    private void deleteCutomerCoupons(int CustomerID){
        String sql = "delete from customers_vs_coupons where Customer_id = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            ps.executeUpdate();
            System.out.println("Customer (" + CustomerID + ") Coupons has been deleted.");
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
    }

    
    
    /**
     * Get customer coupons by Price limit.
     * Send Query to program DB, Identify customer by CustomerID to get all customer
     * coupons.
     * Sort return coupons by Price limit <double>
     * Return <List> OR null if not exists.
     * @param customerID
     * @param Price
     * @return List<Coupon> || null
     * @throws CouponSystemException
     */
    public List<Coupon> getCouponsByPrice(int customerID, double Price){
        String sql = "Select * from coupons INNER JOIN customers_vs_coupons ON coupons.id = customers_vs_coupons.Coupon_id where Customer_id = ? AND coupons.price < ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setDouble(2, Price);
            ResultSet rs = ps.executeQuery();
            List<Coupon> coupons = new ArrayList<>();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("id"));
                coupon.setCompanies_ID(rs.getInt("Company_ID"));
                coupon.setCategory(Category.getCategoryByValue(rs.getInt("Category_ID")));
                coupon.setTitle(rs.getString("title"));
                coupon.setDescripton(rs.getString("description"));
                coupon.setStart_Date(rs.getDate("start_date").toLocalDate());
                coupon.setEnd_Date(rs.getDate("end_date").toLocalDate());
                coupon.setAmoumt(rs.getInt("amount"));
                coupon.setPrice(rs.getDouble("price"));
                coupon.setImage(rs.getString("image"));
                coupons.add(coupon);
            }
            System.out.println(coupons.toString());
            return coupons;
        }catch(SQLException| CouponSystemException e){
            System.out.println(sql);
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
        return null;
    }


    
    /**
     * Get coustomer coupons by category.
     * Send Query to program DB, Identify customer by CustomerID to get all customer
     * coupons.
     * Sort return coupons by CategoryID <int>
     * Return <List> OR null if not exists.
     * @param customerID
     * @param categoryID
     * @return List<Coupon>
     * @throws CustomerException
     */
    public List<Coupon> getCouponsByCategory(int customerID, int categoryID) {
        String sql = "Select * from coupons INNER JOIN customers_vs_coupons ON coupons.id = customers_vs_coupons.Coupon_id where Customer_id = ? AND coupons.category_id = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setInt(2, categoryID);
            ResultSet rs = ps.executeQuery();
            List<Coupon> coupons = new ArrayList<>();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("ID"));
                coupon.setCompanies_ID(rs.getInt("Company_ID"));
                coupon.setCategory(Category.getCategoryByValue(rs.getInt("Category_ID")));
                coupon.setTitle(rs.getString("Title"));
                coupon.setDescripton(rs.getString("description"));
                coupon.setStart_Date(rs.getDate("Start_Date").toLocalDate());
                coupon.setEnd_Date(rs.getDate("end_date").toLocalDate());
                coupon.setAmoumt(rs.getInt("amount"));
                coupon.setPrice(rs.getDouble("price"));
                coupon.setImage(rs.getString("image"));
                coupons.add(coupon);
            }
            System.out.println(coupons.toString());
            return coupons;
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
        return null;
    }


    
    /**
     * Get coustomer coupons by category.
     * Send Query to program DB, Identify customer by CustomerID to get all customer
     * coupons.
     * Return <List> OR null if not exists.
     * @param customerID
     * @return List<Coupon>
     */
    public List<Coupon> getCustomerCoupons(int customerID){
        String sql = "Select * from coupons INNER JOIN customers_vs_coupons ON coupons.id = customers_vs_coupons.Coupon_id where Customer_id = ? ";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            List<Coupon> coupons = new ArrayList<>();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("ID"));
                coupon.setCompanies_ID(rs.getInt("Company_ID"));
                coupon.setCategory(Category.getCategoryByValue(rs.getInt("Category_ID")));
                coupon.setTitle(rs.getString("Title"));
                coupon.setDescripton(rs.getString("Description"));
                coupon.setStart_Date(rs.getDate("Start_Date").toLocalDate());
                coupon.setEnd_Date(rs.getDate("End_Date").toLocalDate());
                coupon.setAmoumt(rs.getInt("Amount"));
                coupon.setPrice(rs.getDouble("Price"));
                coupon.setImage(rs.getString("Image"));
                coupons.add(coupon);
            }
            System.out.println(coupons.toString());
            return coupons;
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
        return null;
    }


    
    /** 
     * Get all customer exists in program DB.
     * Return <List> OR null if not exists
     * @return List<Customer>
     */
    public List<Customer> getAllCustomers(){
        String sql = "select * from Customers";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirst_Name(rs.getString("First_Name"));
                customer.setLast_Name(rs.getString("Last_Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setPassword(rs.getString("password"));
                customers.add(customer);
            }
            System.out.println(customers.toString());
            return customers;
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
        return null;
    }


    
    /** 
     * Get one coustomer.
     * Send Query to program DB, Identify customer by CustomerID to get customer
     * details.
     * Return <Object> Customer OR null if not exists.
     * @param CustomerID
     * @return Customer
     * @throws CouponSystemException
     */
    public Customer getOneCustomer(int CustomerID){
        String sql = "select * from Customers where id = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirst_Name(rs.getString("First_Name"));
                customer.setLast_Name(rs.getString("Last_Name"));
                customer.setEmail(rs.getString("Email"));
                System.out.println(customer);
                return customer;
            } else{ 
                throw new CouponSystemException(ExceptionMessage.CUSTOMER_NOT_EXIST.getMessage());
            }
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


}
