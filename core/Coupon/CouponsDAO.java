package Coupon;
import java.util.List;
public interface CouponsDAO {
    
    boolean isCouponExists(Coupon coupon);

    int addCoupon(Coupon coupon);

    int getOneCoupon(int CouponID);

    List<Coupon> getAllCoupons();

    void updateCoupon(Coupon coupon);

    void deleteCoupon(int CustomerID, int CouponID);

    void deleteCoupon(int CouponID);
    
    void addCouponPurchase(int CustomerID, int CouponID);

    List<Coupon> getCompanyAllCoupons(int CompanyID);

    List<Coupon> getCompanyCouponsByCategory(int CompanyID, int CategoriesID);

    List<Coupon> getCompanyCouponsByPrice(int CompanyID, double Price);
   

}
