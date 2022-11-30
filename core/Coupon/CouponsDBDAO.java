package Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Category.Category;
import Exception.CouponSystemException;
import Exception.ExceptionMessage;
import Pool.ConnectionPool;

public class CouponsDBDAO implements CouponsDAO {

    
    /** 
     * Checks if the Coupon exists in DB with the same Title from the same Company. 
     * @param coupon
     * @return boolean
     */
    public boolean isCouponExists(Coupon coupon) {
        String sql = " SELECT * FROM coupons where Title = ? AND Company_ID = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, coupon.getTitle());
            ps.setObject(2, coupon.getCategory().value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    
    /** 
     * Add new Coupon to DB. 
     * First chekcs if any Coupon exists with {@link #isCouponExists(Coupon)} 
     * @param coupon
     * @return int
     */
    public int addCoupon(Coupon coupon) {
        String sql = "insert into coupons values(0, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (isCouponExists(coupon)) {
                System.out.println("Opertaion failed, Coupon already exists.");
                return 0;
            }
            ps.setInt(1, coupon.getCompanies_ID());
            ps.setObject(2, coupon.getCategory().value);
            ps.setString(3, coupon.getTitle());
            ps.setString(4, coupon.getDescripton());
            ps.setObject(5, coupon.getStart_Date());
            ps.setObject(6, coupon.getEnd_Date());
            ps.setInt(7, coupon.getAmoumt());
            ps.setDouble(8, coupon.getPrice());
            ps.setString(9, coupon.getImage());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            coupon.setId(id);
            System.out.println("Coupon added: " + coupon);
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return 0;

    }

    
    /** 
     * Update exists Coupon: Category, title, description, Start Date, and End Date.
     * @param coupon
     */
    public void updateCoupon(Coupon coupon) {
        String sql = "update coupons set category_id = ?, title = ?, description = ?, start_date = ?, end_date = ?, amount = ?, price = ?, image = ? where id = ? ";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, coupon.getCategory().value);
            ps.setString(2, coupon.getTitle());
            ps.setString(3, coupon.getDescripton());
            ps.setObject(4, coupon.getStart_Date());
            ps.setObject(5, coupon.getEnd_Date());
            ps.setInt(6, coupon.getAmoumt());
            ps.setDouble(7, coupon.getPrice());
            ps.setString(8, coupon.getImage());
            ps.setInt(9, coupon.getId());
            if (!isCouponExists(coupon)) {
                return;
            }
            ps.executeUpdate();
            System.out.println("The Coupon has been updated: " + coupon);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return;
    }

    
    /** 
     * Get all Coupons from a specific Company.
     * Identify by CompanyID.
     * @param CompanyID
     * @return List<Coupon>
     */
    public List<Coupon> getCompanyAllCoupons(int CompanyID) {
        String sql = "select * from coupons where Company_ID = ?";
        List<Coupon> coupons = new ArrayList<>();
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("id"));
                coupon.setCompanies_ID(rs.getInt("Company_ID"));
                coupon.setCategory(Category.getCategoryByValue(rs.getInt("Category_ID")));
                coupon.setTitle(rs.getString("title"));
                coupon.setDescripton(rs.getString("description"));
                coupon.setStart_Date(rs.getDate("Start_Date").toLocalDate());
                coupon.setEnd_Date(rs.getDate("End_Date").toLocalDate());
                coupon.setAmoumt(rs.getInt("amount"));
                coupon.setPrice(rs.getDouble("price"));
                coupon.setImage(rs.getString("image"));
                coupons.add(coupon);
            }
            return coupons;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    
    /** 
     * Get all Coupons from a specific Company by Price limit.
     * Identify by CompanyID, sorted by Price.
     * @param CompanyID
     * @param Price
     * @return List<Coupon>
     */
    public List<Coupon> getCompanyCouponsByPrice(int CompanyID, double Price) {
        String sql = "select * from coupons where Company_ID = ? AND price < ?";
        List<Coupon> coupons = new ArrayList<>();
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ps.setDouble(2, Price);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    
    /** 
     * Get all exists Coupuns in DB.
     * @return List<Coupon>
     */
    public List<Coupon> getAllCoupons() {
        String sql = "select * from coupons";
        List<Coupon> coupons = new ArrayList<>();
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
            return coupons;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    
    /** 
     * Get all Coupons from a specific Company by Catergory.
     * Identify by CompanyID, sorted by CategoryID.
     * @param CompanyID
     * @param CategoriesID
     * @return List<Coupon>
     */
    public List<Coupon> getCompanyCouponsByCategory(int CompanyID, int CategoriesID) {
        String sql = "select * from coupons where Company_ID = ? AND Category_ID = ?";
        List<Coupon> coupons = new ArrayList<>();
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CompanyID);
            ps.setInt(2, CategoriesID);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    
    /** 
     * Get one specific Coupun from DB.
     * Identify by CoupunID
     * @param CouponID
     * @return int
     */
    public int getOneCoupon(int CouponID) {
        String sql = "select * from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
                System.out.println(coupon);
                return CouponID;
            }
            throw new CouponSystemException(ExceptionMessage.COUPON_NOT_EXIST.getMessage());
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return 0;
    }

    
   
    /** 
     * Check specific Coupun amount.
     * Identify Coupun by CouponID.
     * @param CouponID
     * @return int
     */
    public int checkAmount(int CouponID) {
        String sql = "select * from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int amount = rs.getInt("amount");
                return amount;
            }
            if (!rs.next()) {
                throw new CouponSystemException(ExceptionMessage.COUPON_NOT_EXIST.getMessage());
            }
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return 0;
    }

    
    /** 
     * Check specific Coupun expiration date.
     * Identify by CoupunID
     * @param CouponID
     * @return LocalDate
     */
    public LocalDate checkExpirationDate(int CouponID) {
        String sql = "select * from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate end_date = rs.getDate("end_date").toLocalDate();
                return end_date;
            }
            throw new CouponSystemException(ExceptionMessage.COUPON_NOT_EXIST.getMessage());
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    
    /** 
     * Checks if Customer already own the Coupun.
     * Identify Custoemr by CustomerID, identify Coupun by CouponID.
     * @param CustomerID
     * @param CouponID
     * @return boolean
     */
    public boolean checkIFhaveCoupon(int CustomerID, int CouponID) {
        String sql = "select * from customers_vs_coupons where Customer_id = ? AND Coupon_id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            ps.setInt(2, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    
    /** 
     * Purchase method for Coupuns.
     * Checks Coupon amoumt > 0 with {@link #checkAmount(int)}.
     * Checks expiration date is not expired with {@link #checkExpirationDate(int)}.
     * Checks if the Custoemr not already own the Coupon with {@link #checkIFhaveCoupon(int, int)}.
     * Identify Custoemr by CustomerID, identify Coupun by CouponID.
     * @param CustomerID
     * @param CouponID
     */
    public void addCouponPurchase(int CustomerID, int CouponID) {
        String insert = "insert into customers_vs_coupons values (?,  ?)";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            if (checkAmount(CouponID) <= 0) {
                throw new CouponSystemException(ExceptionMessage.COUPON_AMOUNT_IS_ZERO.getMessage() + " " + CouponID);
            } else if (checkExpirationDate(CouponID).isBefore(LocalDate.now())) {
                throw new CouponSystemException(ExceptionMessage.COUPON_EXPIRED.getMessage() + " " + CouponID);

            } else if (checkIFhaveCoupon(CustomerID, CouponID) == true) {
                throw new CouponSystemException(
                        ExceptionMessage.COUPON_ALREADY_PURCHASED.getMessage() + " " + CouponID);

            } else {
                PreparedStatement ps2 = con.prepareStatement(insert);
                ps2.setInt(1, CustomerID);
                ps2.setInt(2, CouponID);
                ps2.executeUpdate();
                int amount = this.checkAmount(CouponID);
                int amountc = amount - 1;
                String sql4 = "update coupons SET amount =" + "'" + amountc + "'" + "where id = ?";
                PreparedStatement ps4 = con.prepareStatement(sql4);
                ps4.setInt(1, CouponID);
                ps4.executeUpdate();
                System.out.println("Coupon has been purchase" + " " + CouponID);
            }
        } catch (SQLException | CouponSystemException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return;
    }

    
    /** 
     * Delete Coupon from Copuns table and Purchase table.
     * Identify Custoemr by CustomerID, identify Coupun by CouponID.
     * @param CustomerID
     * @param CouponID
     */
    public void deleteCoupon(int CustomerID, int CouponID) {
        String sql = "delete from customers_vs_coupons where Customer_id = ? and Coupon_id = ? ";
        String sql2 = "delete from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps.setInt(1, CustomerID);
            ps.setInt(2, CouponID);
            ps.executeUpdate();
            ps2.setInt(1, CouponID);
            ps2.executeUpdate();
            System.out.println("Coupon " + CouponID + " has been deleted.");
        } catch (SQLException e) {
            System.out.println(ExceptionMessage.COMPANY_NOT_EXIST.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    
    /** 
     * Another method of Coupon deletion, only with 1 identifier.
     * Delete Coupon from Copuns table and Purchase table.
     * Identify Coupon by CouponID.
     * @param CouponID
     */
    public void deleteCoupon(int CouponID) {
        String sql = "delete from customers_vs_coupons where Coupon_id = ? ";
        String sql2 = "delete from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps.setInt(1, CouponID);
            ps.executeUpdate();
            ps2.setInt(1, CouponID);
            ps2.executeUpdate();
            System.out.println("Coupon " + CouponID + " has been deleted.");
        } catch (SQLException e) {
            System.out.println(ExceptionMessage.COMPANY_NOT_EXIST.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().returnConnection(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
