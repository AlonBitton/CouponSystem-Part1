package Customer;
import java.util.List;

import Coupon.Coupon;


public interface CustomerDAO {
    
    boolean isCustomerExists(String email, String password);

    int addCustomer(Customer customer);

    Customer getOneCustomer(int CompanyID);

    List<Customer> getAllCustomers();

    void updateCutomer(Customer customer);

    public void deleteCutomer(int CompanyID);

    List<Coupon> getCustomerCoupons(int customerID);

    List<Coupon> getCouponsByCategory(int customerID, int category) ;

    List<Coupon> getCouponsByPrice(int customerID, double Price);

}
