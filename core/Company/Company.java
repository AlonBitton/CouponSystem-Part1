package Company;

import java.util.ArrayList;
import java.util.List;

import Coupon.Coupon;
import Coupon.CouponsDBDAO;

public class Company {

    private int id;
    private String name;
    private String email;
    private String password;
    CouponsDBDAO Cbd = new CouponsDBDAO();
    List<Coupon> coupuns = new ArrayList<>();

    public Company() {

    }

    public Company(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Company(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Company: ");
        stringBuilder.append("id: ").append(id);
        stringBuilder.append(",  Name: ").append(name);
        stringBuilder.append(",  Email: ").append(email);
        stringBuilder.append(",  Password: ").append(password);
        stringBuilder.append(",  Coupons: ").append(Cbd.getCompanyAllCoupons(id)).append("\n");
        return stringBuilder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Coupon> getCoupuns() {
        return coupuns;
    }

    public void setCoupuns(List<Coupon> coupuns) {
        this.coupuns = coupuns;
    }

}
