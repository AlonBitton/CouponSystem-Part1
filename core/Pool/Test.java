package Pool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import Pool.ConnectionPool;
import Category.Category;
import Company.Company;
import Coupon.Coupon;
import Customer.Customer;
import Exception.CouponSystemException;
import Facade.AdminFacade;
import Facade.ClientFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CustomerFacade;
import Facade.LoginManager;
import Job.CouponExpirationDailyJob;

public class Test {

    private CouponExpirationDailyJob couponExpirationDailyJob;
    private Thread thread;

    public Test() {
        this.couponExpirationDailyJob = new CouponExpirationDailyJob();
        thread = new Thread(couponExpirationDailyJob);
        System.out.println("=========== Test Started ===========");
        createDB();
        loginMangerAdministrator();
        loginMangerComapny();
        loginMangerCustomer(); 
        DropDB();

        /*          thread.start();


        /*
         *
         * try {
         * //ConnectionPool.getInstance().closeAllConnections();
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         * System.out.println("stop???");
         * couponExpirationDailyJob.stop();
         * thread.interrupt();
         */

    }

    public String RandomCompanyNames(){
        String[] names = {"Teva", "Leumi", "Check-Point", "IL-Bank", "Telma", "Osem", "Elite", "Nike", "Adidas"};
        int ran = (int) (Math.random() * names.length);
        return names[ran];
    }

    public String RandomCustomerNames(){
    String[] names = {"Dan", "Bob", "Tom", "Eden", "Lea", "Bar", "Mark", "Lee", "Ian", "David"};
    int ran = (int) (Math.random() * names.length);
    return names[ran];
    }

    private Category randomCategory(){
        int ran = new Random().nextInt(Category.values().length);
        return Category.values()[ran];
    }

    public double RandomPrice(){
        double price = (int) (Math.random() * 101);
        return price;
    }

    public String Email(){
        return "@email.com";
    }
  
public void loginMangerAdministrator(){
        LoginManager LM=LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("admin@admin.com","admin", ClientType.ADMINISTRATOR);
        if(clientFacade instanceof AdminFacade) {
            AdminFacade adminFacade = (AdminFacade) clientFacade;
            System.out.println("=========== Start Admin Facade ===========");
            // CompanyFacade Login Company
            Company c1 = new Company(0, "C1@email.com", "C1C1");
            adminFacade.addCompany(c1);

            Company company1 = new Company();
            company1.setName(RandomCompanyNames());
            company1.setEmail(company1.getName() + Email());
            company1.setPassword(company1.getName() + "1234");
            adminFacade.addCompany(company1);

            Company company2 = new Company();
            company2.setName(RandomCompanyNames());
            company2.setEmail(company2.getName() + Email());
            company2.setPassword(company2.getName() + "1234");
            adminFacade.addCompany(company2);

            Company company3 = new Company();
            company3.setName(RandomCompanyNames());
            company3.setEmail(company3.getName() + Email());
            company3.setPassword(company3.getName() + "1234");
            adminFacade.addCompany(company3);
            
            Company company4 = new Company();
            company4.setName(RandomCompanyNames());
            company4.setEmail(company4.getName() + Email());
            company4.setPassword(company4.getName() + "1234");
            adminFacade.addCompany(company4);

            Company company5 = new Company();
            company5.setName(RandomCompanyNames());
            company5.setEmail(company5.getName() + Email());
            company5.setPassword(company5.getName() + "1234");
            adminFacade.addCompany(company5);

            System.out.println("=========== Print all Companies ===========");
            adminFacade.getAllCompanies();
            
            System.out.println("=========== Update Company" +"(" + company1.getId() + ")"+ "===========");
            Company company6 = new Company(company1.getId(), company1.getName() + "Updated", company1.getEmail() + "Updated", company1.getPassword() + "Updated");
            adminFacade.updateCompany(company6);

            System.out.println("=========== Delete Company" +"(" + company1.getId() + ")"+ "===========");
            adminFacade.deleteCompany(company1.getId());

            
            System.out.println("=========== Verify Company has been Deleted ===========");
            adminFacade.getOneCompany(company1.getId());

            // CustomerFacade login Customer
            Customer A1 = new Customer(0, "A1", "A1", "A1@email.com", "A1A1");
            adminFacade.addCustomer(A1);

            Customer customer1 = new Customer();
            customer1.setFirst_Name(RandomCustomerNames());
            customer1.setLast_Name(RandomCustomerNames());
            customer1.setEmail(customer1.getFirst_Name() + Email());
            customer1.setPassword(customer1.getFirst_Name() + "1234");
            adminFacade.addCustomer(customer1);

            Customer customer2 = new Customer();
            customer2.setFirst_Name(RandomCustomerNames());
            customer2.setLast_Name(RandomCustomerNames());
            customer2.setEmail(customer2.getFirst_Name() + Email());
            customer2.setPassword(customer2.getFirst_Name() + "1234");
            adminFacade.addCustomer(customer2);

            Customer customer3 = new Customer();
            customer3.setFirst_Name(RandomCustomerNames());
            customer3.setLast_Name(RandomCustomerNames());
            customer3.setEmail(customer3.getFirst_Name() + Email());
            customer3.setPassword(customer3.getFirst_Name() + "1234");
            adminFacade.addCustomer(customer3);

            Customer customer4 = new Customer();
            customer4.setFirst_Name(RandomCustomerNames());
            customer4.setLast_Name(RandomCustomerNames());
            customer4.setEmail(customer4.getFirst_Name() + Email());
            customer4.setPassword(customer4.getFirst_Name() + "1234");
            adminFacade.addCustomer(customer4);

            Customer customer5 = new Customer();
            customer5.setFirst_Name(RandomCustomerNames());
            customer5.setLast_Name(RandomCustomerNames());
            customer5.setEmail(customer5.getFirst_Name() + Email());
            customer5.setPassword(customer5.getFirst_Name() + "1234");
            adminFacade.addCustomer(customer5);

            System.out.println("=========== Print all Customers ===========");
            adminFacade.getAllCustomers();

            System.out.println("=========== Delete Customer" +"(" + customer1.getId() + ")"+ "===========");
            adminFacade.deleteCutomer(customer1.getId());

            System.out.println("=========== Verify Customer has been Deleted ===========");
            adminFacade.getOneCustomer(customer1.getId());

            System.out.println("=========== End Admin Facade ===========");
        }
        

    }

public void loginMangerComapny(){
        LoginManager LM=LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("C1@email.com","C1C1", ClientType.COMPANY);
        if(clientFacade instanceof CompanyFacade){
            CompanyFacade companyFacade = (CompanyFacade) clientFacade;

            System.out.println("=========== Start Company Facade ===========");

            Coupon coupon1 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon1.setCompanies_ID(companyFacade.getCompanyID());
            coupon1.setCategory(randomCategory());
            coupon1.setTitle("Coupon");
            coupon1.setDescripton(coupon1.getTitle() + " Description");
            coupon1.setImage(coupon1.getTitle() + " image");
            coupon1.setStart_Date(LocalDate.now());
            coupon1.setEnd_Date(LocalDate.now().plusDays(1));
            coupon1.setAmoumt(5);
            coupon1.setPrice((Math.random() * 101));
            companyFacade.addCoupon(coupon1);

            Coupon coupon2 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon2.setCompanies_ID(companyFacade.getCompanyID());
            coupon2.setCategory(randomCategory());
            coupon2.setTitle("Coupon");
            coupon2.setDescripton(coupon2.getTitle() + " Description");
            coupon2.setImage(coupon2.getTitle() + " image");
            coupon2.setStart_Date(LocalDate.now());
            coupon2.setEnd_Date(LocalDate.now().plusDays(1));
            coupon2.setAmoumt(5);
            coupon2.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon2);

            Coupon coupon3 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon3.setCompanies_ID(companyFacade.getCompanyID());
            coupon3.setCategory(randomCategory());
            coupon3.setTitle("Coupon");
            coupon3.setDescripton(coupon3.getTitle() + " Description");
            coupon3.setImage(coupon3.getTitle() + " image");
            coupon3.setStart_Date(LocalDate.now());
            coupon3.setEnd_Date(LocalDate.now().plusDays(1));
            coupon3.setAmoumt(5);
            coupon3.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon3);

            Coupon coupon4 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon4.setCompanies_ID(companyFacade.getCompanyID());
            coupon4.setCategory(randomCategory());
            coupon4.setTitle("Coupon");
            coupon4.setDescripton(coupon4.getTitle() + " Description");
            coupon4.setImage(coupon4.getTitle() + " image");
            coupon4.setStart_Date(LocalDate.now());
            coupon4.setEnd_Date(LocalDate.now().plusDays(1));
            coupon4.setAmoumt(5);
            coupon4.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon4);

            Coupon coupon5 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon5.setCompanies_ID(companyFacade.getCompanyID());
            coupon5.setCategory(randomCategory());
            coupon5.setTitle("Coupon");
            coupon5.setDescripton(coupon5.getTitle() + " Description");
            coupon5.setImage(coupon5.getTitle() + " image");
            coupon5.setStart_Date(LocalDate.now());
            coupon5.setEnd_Date(LocalDate.now().plusDays(1));
            coupon5.setAmoumt(5);
            coupon5.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon5);

            System.out.println("=========== Print Coupon" + "(" + coupon5.getId() + ")" + " Before Update ===========");
            System.out.println(coupon5);

            Coupon coupon6 = new Coupon();
            coupon6.setCompanies_ID(companyFacade.getCompanyID());
            coupon6.setCategory(coupon5.getCategory());
            coupon6.setTitle(coupon5.getTitle() + " Updated" );
            coupon6.setDescripton(coupon5.getDescripton() + " Updated");
            coupon6.setImage(coupon5.getImage() + " Updated");
            coupon6.setStart_Date(LocalDate.now());
            coupon6.setEnd_Date(LocalDate.now().plusDays(5));
            coupon6.setAmoumt(3);
            coupon6.setPrice(coupon5.getPrice());

            System.out.println("=========== Print Coupon" + "(" + coupon5.getId() + ")" + " After Update ===========");
            companyFacade.updateCoupon(coupon6);
            System.out.println(coupon6);

            System.out.println("=========== Print all Coupons ===========");
            companyFacade.getCompanyAllCoupons(companyFacade.getCompanyID());

            System.out.println("=========== Delete Coupon" + "(" + coupon6.getId() + ")" + " ===========");
            companyFacade.deleteCoupon(companyFacade.getCompanyID(), coupon6.getId());
            
            System.out.println("=========== Print all Coupons By Category ===========");
            companyFacade.getCompanyCouponsByCategory(companyFacade.getCompanyID(), randomCategory().value);
            
            System.out.println("=========== Print all Coupons By Price ===========");
            companyFacade.getCompanyCouponsByPrice(companyFacade.getCompanyID(), 25);

            System.out.println("=========== End Company Facade ===========");
        }
    }
 
public void loginMangerCustomer() {
     LoginManager LM = LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("A1@email.com", "A1A1", ClientType.CUSTOMER);
        if (clientFacade instanceof CustomerFacade) {
            System.out.println("=========== Start Customer Facade ===========");

            CustomerFacade customerFacade = (CustomerFacade) clientFacade;
            System.out.println("=========== Purchase 4 Random Coupons ===========");
            customerFacade.purchaseCoupon(1, 1);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 2);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 3);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 4);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 5);

            System.out.println("=========== Print all Customer Coupons ===========");
            customerFacade.getCustomerCoupons();

            System.out.println("=========== Print all Coupons By Category ===========");
            customerFacade.getCouponsByCategory(randomCategory().value);

            System.out.println("=========== Print all Coupons By Price ===========");
            customerFacade.getCouponsByPrice(RandomPrice());

            System.out.println("=========== Print Customer Details ===========");
            customerFacade.getCustomerDetails();

            System.out.println("=========== End Customer Facade ===========");
        }
    }


public void createDB(){
    try {
        Connection con = ConnectionPool.getInstance().getConnection();
        ScriptRunner runner = new ScriptRunner(con, false, false);
        String db = "mySQL_CouponSystem.sql";
        runner.runScript(new BufferedReader(new FileReader(db)));
        System.out.println("========== Database Created ===========");
    } catch (SQLException | CouponSystemException | IOException e) {
        e.getMessage();
    } 
}

private  void DropDB(){
    String sql = "drop SCHEMA CouponSystem";
        try (Connection con = ConnectionPool.getInstance().getConnection();){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("========== Database Dropped ===========");
        } catch (SQLException | CouponSystemException e) {
            e.getMessage();
        }
}

    public static void main(String[] args) {
        Test t1 = new Test();
    }

}
