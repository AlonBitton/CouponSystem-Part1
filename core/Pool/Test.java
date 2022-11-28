package Pool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Category.Category;
import Company.Company;
import Coupon.Coupon;
import Customer.Customer;

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

    /**
     * The enitre project is automated, CreateDB with {@link #createDB()} &
     * {@link #DropDB}
     * All the Companies, Customers, Coupons are generated with
     * random values for check proof the exsists methods and purchase limits.
     * for testing its reccomendded to change sleep time at
     * {@link #CouponExpirationDailyJob().run() line 38-39 }
     * default sleep time set for 1 day.
     */
    public Test() {
        System.out.println("=========== Test Started ===========");
        DropDB();
        createDB();
        this.couponExpirationDailyJob = new CouponExpirationDailyJob();
        thread = new Thread(couponExpirationDailyJob);
        thread.start();
        loginMangerAdministrator();
        loginMangerComapny();
        loginMangerCustomer();
        try {
            System.out.println("Sleep in 25 sec");
            ConnectionPool.getInstance().closeAllConnections();
        } catch (Exception e) {
        }
        couponExpirationDailyJob.stopJob();
        thread.interrupt();
        // couponExpirationDailyJob.stopJob();

    }

    public String RandomCompanyNames() {
        String[] names = { "Teva", "Leumi", "Check-Point", "IL-Bank", "Telma", "Osem", "Elite", "Nike", "Adidas" };
        int ran = (int) (Math.random() * names.length);
        return names[ran];
    }

    public String RandomCustomerNames() {
        String[] names = { "Dan", "Bob", "Tom", "Eden", "Lea", "Bar", "Mark", "Lee", "Ian", "David" };
        int ran = (int) (Math.random() * names.length);
        return names[ran];
    }

    private Category randomCategory() {
        int ran = new Random().nextInt(Category.values().length);
        return Category.values()[ran];
    }

    public double RandomPrice() {
        double price = (int) (Math.random() * 101);
        return price;
    }

    public String Email() {
        return "@email.com";
    }

    public void loginMangerAdministrator() {
        LoginManager LM = LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
        if (clientFacade instanceof AdminFacade) {
            AdminFacade adminFacade = (AdminFacade) clientFacade;
            System.out.println("=========== Start Admin Facade ===========");
            // CompanyFacade Login Company
            System.out.println("=========== Creating CompanyFacade login details ===========");
            Company c1 = new Company(0, "C1@email.com", "C1C1");
            adminFacade.addCompany(c1);
            System.out.println("=========== Create new Companies ===========");
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

            System.out.println("=========== Update Company" + "(" + company1.getId() + ")" + "===========");
            Company company6 = new Company(company1.getId(), company1.getName() + "Updated",
                    company1.getEmail() + "Updated", company1.getPassword() + "Updated");
            adminFacade.updateCompany(company6);

            System.out.println("=========== Delete Company" + "(" + company1.getId() + ")" + "===========");
            adminFacade.deleteCompany(company1.getId());

            System.out.println("=========== Verify Company has been Deleted ===========");
            adminFacade.getOneCompany(company1.getId());

            // CustomerFacade login Customer
            System.out.println("=========== Creating CustomerFacade login details ===========");
            Customer A1 = new Customer(0, "A1", "A1", "A1@email.com", "A1A1");
            A1.setPassword("A1A1");
            adminFacade.addCustomer(A1);
            System.out.println("=========== Create new Customers ===========");
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

            System.out.println("=========== Delete Customer" + "(" + customer1.getId() + ")" + "===========");
            adminFacade.deleteCutomer(customer1.getId());

            System.out.println("=========== Verify Customer has been Deleted ===========");
            adminFacade.getOneCustomer(customer1.getId());

            System.out.println("=========== End Admin Facade ===========");
        }

    }

    public void loginMangerComapny() {
        LoginManager LM = LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("C1@email.com", "C1C1", ClientType.COMPANY);
        if (clientFacade instanceof CompanyFacade) {
            CompanyFacade companyFacade = (CompanyFacade) clientFacade;
            System.out.println();
            System.out.println("=========== Start Company Facade ===========");

            Coupon coupon1 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon1.setCompanies_ID(companyFacade.getCompanyID());
            coupon1.setCategory(randomCategory());
            coupon1.setTitle("Coupon");
            coupon1.setDescripton(coupon1.getTitle() + " Description");
            coupon1.setImage(coupon1.getTitle() + " image");
            coupon1.setStart_Date(LocalDate.now());
            coupon1.setEnd_Date(LocalDate.now().minusDays(2));
            coupon1.setAmoumt(0);
            coupon1.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon1);

            Coupon coupon2 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon2.setCompanies_ID(companyFacade.getCompanyID());
            coupon2.setCategory(randomCategory());
            coupon2.setTitle("Coupon");
            coupon2.setDescripton(coupon2.getTitle() + " Description");
            coupon2.setImage(coupon2.getTitle() + " image");
            coupon2.setStart_Date(LocalDate.now());
            coupon2.setEnd_Date(LocalDate.now().minusDays(2));
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
            coupon3.setEnd_Date(LocalDate.now().minusDays(4));
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

            Coupon coupon7 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon7.setCompanies_ID(companyFacade.getCompanyID());
            coupon7.setCategory(randomCategory());
            coupon7.setTitle("Coupon");
            coupon7.setDescripton(coupon7.getTitle() + " Description");
            coupon7.setImage(coupon7.getTitle() + " image");
            coupon7.setStart_Date(LocalDate.now());
            coupon7.setEnd_Date(LocalDate.now().plusDays(1));
            coupon7.setAmoumt(5);
            coupon7.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon7);

            Coupon coupon8 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon8.setCompanies_ID(companyFacade.getCompanyID());
            coupon8.setCategory(randomCategory());
            coupon8.setTitle("Coupon");
            coupon8.setDescripton(coupon8.getTitle() + " Description");
            coupon8.setImage(coupon8.getTitle() + " image");
            coupon8.setStart_Date(LocalDate.now());
            coupon8.setEnd_Date(LocalDate.now().plusDays(1));
            coupon8.setAmoumt(5);
            coupon8.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon8);

            Coupon coupon9 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon9.setCompanies_ID(companyFacade.getCompanyID());
            coupon9.setCategory(randomCategory());
            coupon9.setTitle("Coupon");
            coupon9.setDescripton(coupon9.getTitle() + " Description");
            coupon9.setImage(coupon9.getTitle() + " image");
            coupon9.setStart_Date(LocalDate.now());
            coupon9.setEnd_Date(LocalDate.now().plusDays(1));
            coupon9.setAmoumt(5);
            coupon9.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon9);

            Coupon coupon10 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon10.setCompanies_ID(companyFacade.getCompanyID());
            coupon10.setCategory(randomCategory());
            coupon10.setTitle("Coupon");
            coupon10.setDescripton(coupon10.getTitle() + " Description");
            coupon10.setImage(coupon10.getTitle() + " image");
            coupon10.setStart_Date(LocalDate.now());
            coupon10.setEnd_Date(LocalDate.now().plusDays(1));
            coupon10.setAmoumt(5);
            coupon10.setPrice(RandomPrice());
            companyFacade.addCoupon(coupon10);

            System.out.println("=========== Print Coupon" + "(" + coupon7.getId() + ")" + " Before Update ===========");
            System.out.println(coupon7);
            Coupon coupon6 = new Coupon(0, 0, null, null, null, null, null, null, 0, 0);
            coupon6.setCompanies_ID(companyFacade.getCompanyID());
            coupon6.setCategory(coupon7.getCategory());
            coupon6.setTitle(coupon7.getTitle() + " Updated");
            coupon6.setDescripton(coupon7.getDescripton() + " Updated");
            coupon6.setImage(coupon7.getImage() + " Updated");
            coupon6.setStart_Date(LocalDate.now());
            coupon6.setEnd_Date(LocalDate.now().plusDays(5));
            coupon6.setAmoumt(3);
            coupon6.setPrice(coupon7.getPrice());

            System.out.println("=========== Print Coupon" + "(" + coupon7.getId() + ")" + " After Update ===========");
            companyFacade.updateCoupon(coupon6);
            System.out.println(coupon6);

            System.out.println("=========== Print all Coupons ===========");
            System.out.println(companyFacade.getCompanyAllCoupons(companyFacade.getCompanyID()));

            System.out.println("=========== Delete Coupon" + "(" + coupon6.getId() + ")" + " ===========");
            companyFacade.deleteCoupon(companyFacade.getCompanyID(), coupon6.getId());
            Category ran = randomCategory();
            System.out.println("=========== Print all Coupons By Category " + ran + " ===========");
            companyFacade.getCompanyCouponsByCategory(companyFacade.getCompanyID(), ran.value);

            int price = (int) (Math.random() * 50 + 40);
            System.out.println("=========== Print all Coupons By Price " + price + " ===========");
            companyFacade.getCompanyCouponsByPrice(companyFacade.getCompanyID(), price);

            System.out.println("=========== End Company Facade ===========");
        }
    }

    public void loginMangerCustomer() {
        LoginManager LM = LoginManager.getInstance();
        ClientFacade clientFacade;
        clientFacade = LM.login("A1@email.com", "A1A1", ClientType.CUSTOMER);
        if (clientFacade instanceof CustomerFacade) {
            System.out.println();
            System.out.println("=========== Start Customer Facade ===========");

            CustomerFacade customerFacade = (CustomerFacade) clientFacade;
            System.out.println("=========== Trying to Purchase 8 Random Coupons ===========");
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 1);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 2);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 3);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 4);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 5);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 6);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 7);
            customerFacade.purchaseCoupon(customerFacade.getCustomerID(), 8);

            System.out.println("=========== Print all Customer Coupons ===========");
            System.out.println(customerFacade.getCustomerCoupons());

            Category ran = randomCategory();
            System.out.println("=========== Print all Coupons By Category " + ran + " ===========");
            customerFacade.getCouponsByCategory(ran.value);

            int price = (int) (Math.random() * 50 + 40);
            System.out.println("=========== Print all Coupons By Price " + price + " ===========");
            customerFacade.getCouponsByPrice(price);

            System.out.println("=========== Print Customer Details ===========");
            customerFacade.getCustomerDetails();

            System.out.println("=========== End Customer Facade ===========");
        }
    }

    /**
     * Automated program create MySQL database using ScriptRunner.
     * No need to create any Schema / table for this project.
     */
    private void createDB() {
        try {
            Connection con = ConnectionPool.getInstance().getConnection();
            ScriptRunner runner = new ScriptRunner(con, false, false);
            String db = "mySQL_CouponSystem.sql";
            runner.runScript(new BufferedReader(new FileReader(db)));
            System.out.println("========== Database Created ===========");
        } catch (SQLException | IOException e) {
            e.getMessage();
        }
    }

    /**
     * Drop MySQL enitre database.
     */
    private void DropDB() {
        String sql = "drop SCHEMA CouponSystem";
        try (Connection con = ConnectionPool.getInstance().getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("========== Database Dropped ===========");
        } catch (SQLException e) {
            e.getMessage();
        }
    }

}
