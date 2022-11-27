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
     * Checks if coupon exists by sending Query to DB search if any coupon that
     * created by the same Company have the same Title.
     * Return boolean.
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
     * Add new coupon to program DB,
     * First checks if coupon exists by {@link #isCouponExists(Coupon)} ->
     * Search if any coupon created by the same Company have the same Title.
     * Return <int> / 0 if exist OR the new auto-generated CompanyID.
     */
    public int addCoupon(Coupon coupon){
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
     * Update exists coupon Category, Title, Description, Start_Date, End_Date,
     * Amount, Price, Image.
     * Identify coupon by ID.
     */
    public void updateCoupon(Coupon coupon){
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
            if(!isCouponExists(coupon)){
                return;
            }
            ps.executeUpdate();
            System.out.println("The Coupon has been updated: " + coupon);
        }catch(SQLException e){
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
     * Get Specific all company coupons, identify by ID.
     * Return <List> OR null if not exits.
     */
    public List<Coupon> getCompanyAllCoupons(int CompanyID){
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
            System.out.println(coupons.toString());
            return coupons;
        }catch(SQLException e){
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
     * Get Specifc comapny coupons with Price limit.
     * Identify company coupons by ID, set Price limit.
     * return <List> OR null if not exists.
     */
    public List<Coupon> getCompanyCouponsByPrice(int CompanyID, double Price){
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
        }catch(SQLException e){
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
     * Get all exists coupons in program DB by sending Query to DB.
     */
    public List<Coupon> getAllCoupons(){
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
        }catch(SQLException e){
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
     * Get specific company coupons sort by Category ID.
     * Identify company by CompanyID, Identify category by CategoryID.
     * retun <List> OR null if not exists/
     */
    public List<Coupon> getCompanyCouponsByCategory(int CompanyID, int CategoriesID){
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
        }catch(SQLException e){
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
     * Get one coupon by sending Query to program DB.
     * Identify coupon by CouponID.
     * Return Coupon ID. If found Print coupon details.
     */
    public int getOneCoupon(int CouponID){
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
        }catch(SQLException| CouponSystemException e){
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
     * Check coupon amount. Identify by CouponID.
     * Return <int> OR 0 if not exists.
     */
    private int amount;

    public int checkAmount(int CouponID) {
        String sql = "select * from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
                return amount;
            }
            throw new CouponSystemException(ExceptionMessage.COUPON_NOT_EXIST.getMessage());
        }catch(SQLException| CouponSystemException e){
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
     * Check coupon Expiration Date, Identify by CouponID.
     * Return <LocalDate> end_date OR null if not exists.
     */
    public LocalDate checkExpirationDate(int CouponID) {
        String sql = "select * from coupons where id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CouponID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate end_date = rs.getDate("end_date").toLocalDate();
                return end_date;
            }
            throw new CouponSystemException(ExceptionMessage.COUPON_NOT_EXIST.getMessage());
        }catch(SQLException| CouponSystemException e){
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
     * Check if Customer own specific Coupon.
     * Identify Customer by CustomerID, Identify Coupon by CouponID.
     * return <boolean>.s
     */
    public boolean checkIFhaveCoupon(int CustomerID, int CouponID) {
        String sql = "select * from customers_vs_coupons where Customer_id = ? AND Coupon_id = ?";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            ps.setInt(2, CouponID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch(SQLException e){
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
     * Purchase coupon by customer. Execute by sending Query to program DB.
     * Identify customer by CustomerID, identify coupond by CouponID.
     * First, Use {@link #checkAmount(int)} to check if amount > 0.
     * Then, Use {@link #checkExpirationDate(int)} to check ExpirationDate.
     * Last check before execute, {@link #checkIFhaveCoupon(int, int)} to check if
     * customer already owen the coupon.
     * Sending Qurey to program DB to finaly purechase the coupon.
     * For last deduct 1 coupon from Coupon amount using {@link #amount}
     */
    public void addCouponPurchase(int CustomerID, int CouponID){
        String insert = "insert into customers_vs_coupons values (?,  ?)";
        Connection con = ConnectionPool.getInstance().getConnection();
        try {
                        if (checkAmount(CouponID) <= 0) {
                throw new CouponSystemException(ExceptionMessage.COUPON_AMOUNT_IS_ZERO.getMessage());
            }
            else if (checkExpirationDate(CouponID).isBefore(LocalDate.now())) {
                throw new CouponSystemException(ExceptionMessage.COUPON_EXPIRED.getMessage());

            }
            else if (checkIFhaveCoupon(CustomerID, CouponID) == true) {
                throw new CouponSystemException(ExceptionMessage.COUPON_ALREADY_PURCHASED.getMessage());

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
                System.out.println("Coupon has been purchase");
            }
        }catch(SQLException| CouponSystemException e){
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
     * Delete coupon own by Customer. Identify by CustomerID, Identify coupon by
     * CouponID.
     * First sending Query to program DB, delete coupon from jonied table.
     * Then delete from Coupon table Identify by ID.
     */
    public void deleteCoupon(int CustomerID, int CouponID){
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
        }catch(SQLException e){
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
     * Delete coupons Identify coupon by CouponID.
     * First sending Query to program DB, delete coupon from jonied table.
     * Then delete from Coupon table Identify by ID.
     */
    public void deleteCoupon(int CouponID){
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
        }catch(SQLException e){
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
