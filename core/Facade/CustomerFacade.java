package Facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import Coupon.Coupon;
import Customer.Customer;
import Pool.ConnectionPool;

public class CustomerFacade extends ClientFacade {

    private int customerID;

    public CustomerFacade() {
        super();
        this.customerID = 0;

    }

    /**
     * Login for customer, Identify customer by sending Query to program DB.
     * Checks if customer exists.
     * Return <boolean>.
     */
    @Override
    public boolean login(String email, String password) {
        String sql = "SELECT * FROM Customers WHERE email = ? AND password = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                setCustomerID(rs.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Operation failed");
            e.printStackTrace();
        }

        return false;
    }

    public void purchaseCoupon(int customerID, int CouponID){
        this.couponsDAO.addCouponPurchase(this.customerID, CouponID);
    }

    public List<Coupon> getCustomerCoupons(){
        return this.customerDao.getCustomerCoupons(this.customerID);
    }


    public void getCouponsByCategory(int Category){
        this.customerDao.getCouponsByCategory(this.customerID, Category);
    }

    public List<Coupon> getCouponsByPrice(double price)  {
        return this.customerDao.getCouponsByPrice(this.customerID, price);
	}
	

    public Customer getCustomerDetails() {
        return this.customerDao.getOneCustomer(this.customerID);
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

}