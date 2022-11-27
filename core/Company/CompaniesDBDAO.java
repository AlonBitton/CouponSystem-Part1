package Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Coupon.Coupon;
import Pool.ConnectionPool;
import Exception.CouponSystemException;
import Exception.ExceptionMessage;

public class CompaniesDBDAO implements CompanyDAO {

    /**
     * Checks if comany exists by sending Query to DB,
     * Checks if any exists comany have the same email & password
     */
    public boolean isCompanyExists(String email, String password) {
        String sql = "SELECT email FROM Companies WHERE email = ? AND password = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            throw new CouponSystemException(ExceptionMessage.AUTHENTICATION_FAILED.getMessage());
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

    /**
     * Add new company to the program DB,
     * checks before adding if company exists using
     * {@link #isCompanyExists(Email, Password)}
     */
    public int addCompany(Company company) {
        String sql = "insert into Companies (id, name, email, password) values(0, ?, ?, ?)"; // get values from
                                                                                             // Company() constractur.
        String sql2 = "select * from Companies where Email = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, company.getEmail());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                throw new CouponSystemException(ExceptionMessage.EMAIL_ALREADY_EXIST.getMessage() + " " + company.getEmail());
            }
            ps.setString(1, company.getName());
            ps.setString(2, company.getEmail());
            ps.setString(3, company.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            company.setId(id);
            System.out.println("New Company id: " + id);
            System.out.println(company);
            return id;
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        return 0;

    }

    /**
     * Update exists company Name, email. password.
     * Update exists company by specific CopmanyID
     */
    public void updateCompany(Company company) {
        String sql = "update Companies set name = ?, email = ?, password = ? where id = ?"; // quarry
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, company.getName()); // set index change name
            ps.setString(2, company.getEmail()); // set index chgange email
            ps.setString(3, company.getPassword()); // set index change password
            ps.setInt(4, company.getId()); // change company where id match --= initialize index
            String sql2 = "select * from Companies where Email = ?";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, company.getEmail());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                throw new CouponSystemException(ExceptionMessage.EMAIL_ALREADY_EXIST.getMessage() + " " + company.getEmail());
            }
            ps.executeUpdate();
            System.out.println("Company has been updated: " + company);
        }catch(SQLException| CouponSystemException e){
            System.out.println(e.getMessage());
        }
        return;
    }

    /**
     * Delete exists company by sending Query to DB,
     * Will work only if company not own any coupons!!
     */
    public void deleteCompany(int CompanyID){
        String sql = "delete from Companies where id = ?";
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ps.executeUpdate();
            System.out.println("The Company has been deleted. " + "("+ CompanyID + ")");
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.CUSTOMER_NOT_EXIST.getMessage());
        }
    }

    /**
     * Should be used to delete companies with coupons.
     * First gets all company coupons identify them by CompanyID,
     * Then deletes all companies coupons by sending Query to DB,
     * Delete exists company by sending Query to DB.
     */
    public void deleteCompanyWithCoupons(int CompanyID){
        String sql = "select * from Coupons where Company_ID = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ResultSet rs = ps.executeQuery();
            List<Coupon> CouponID = new ArrayList<>();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                String sql2 = "delete from customers_vs_coupons where Coupon_id =?";
                int id = (rs.getInt("id"));
                CouponID.add(coupon);
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }
            String sql3 = "delete from coupons where Company_ID = ? ";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setInt(1, CompanyID);
            ps3.executeUpdate();
            deleteCompany(CompanyID);
            System.out.println("Company has been deleted.");
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
    }

    /**
     * Get all exists companies by sending Query to DB.
     * return <List>
     */
    public List<Company> getAllCompanies() {
        String sql = "select * from Companies";
        List<Company> companies = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setEmail(rs.getString("email"));
                company.setPassword(rs.getString("password"));
                companies.add(company);
            }
            System.out.println(companies.toString());
            return companies;
        }catch(SQLException| CouponSystemException e){
            System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
        }
        return null;
    }

    /**
     * Get one company identify by CompanyID.
     * return Object -> company / return null if not found.
     */
    public Company getOneCompany(int CompanyID){
        String sql = "select * from Companies where id = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setEmail(rs.getString("email"));
                System.out.println(company);
                return company;
            } else {
                throw new CouponSystemException(ExceptionMessage.COMPANY_NOT_EXIST.getMessage());
            }
        } catch (SQLException | CouponSystemException e) {
            System.out.println(ExceptionMessage.COMPANY_NOT_EXIST.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {
        CompaniesDBDAO cd = new CompaniesDBDAO();
        Company c1 = new Company(0, "C1", "A1Updated@email.com", "A1A1Updated");
        cd.addCompany(c1);
    }

}