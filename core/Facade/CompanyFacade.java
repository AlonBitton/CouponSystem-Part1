package Facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import Company.Company;
import Coupon.Coupon;
import Exception.ExceptionMessage;
import Pool.ConnectionPool;

public class CompanyFacade extends ClientFacade{
    private int CompanyID;

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    @Override
    public boolean login(String email, String password) {
  String sql = "SELECT * FROM Companies WHERE email = ? AND password = ?";
        try( Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                setCompanyID(rs.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            System.out.println(ExceptionMessage.AUTHENTICATION_FAILED.getMessage());
        }

        return false;
    }



    public CompanyFacade(){
        super();
    }
    
    public void addCoupon(Coupon coupon ){
        this.couponsDAO.addCoupon(coupon);
    }

    public void updateCoupon(Coupon coupon){
        this.couponsDAO.updateCoupon(coupon);
    }


    public void deleteCoupon(int CustomerID, int CouponID){
        this.couponsDAO.deleteCoupon(CustomerID, CouponID);
    }

    public Company getOneCompany(int CompanyID){
        return this.companyDAO.getOneCompany(CompanyID);
    }


    public List<Coupon> getCompanyAllCoupons(int CompanyID){
        return this.couponsDAO.getCompanyAllCoupons(CompanyID);
    }

    public List<Coupon> getCompanyCouponsByCategory(int CompanyID, int CategoriesID){
        return this.couponsDAO.getCompanyCouponsByCategory(CompanyID, CategoriesID);
    }

    public List<Coupon> getCompanyCouponsByPrice(int CompanyID, double Price){
        return this.couponsDAO.getCompanyCouponsByPrice(CompanyID, Price);
    }

public static void main(String[] args) {
    
CompanyFacade cf = new CompanyFacade();
cf.login("A1@email.com", "A1A1");

}
}
