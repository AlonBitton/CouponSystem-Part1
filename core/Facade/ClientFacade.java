package Facade;

import Company.CompaniesDBDAO;
import Company.CompanyDAO;
import Coupon.CouponsDBDAO;
import Coupon.CouponsDAO;
import Customer.CustomersDBDAO;
import Customer.CustomerDAO;

public abstract class ClientFacade {

    protected CompanyDAO companyDAO;
    protected CustomerDAO customerDao;
    protected CouponsDAO couponsDAO;
    
     /**
     * Empty constructor.
     * Initiates all daos.
     */

    public ClientFacade() {
        this.couponsDAO =  new CouponsDBDAO();
        this.companyDAO = new CompaniesDBDAO();
        this.customerDao = new CustomersDBDAO();
    }

    /**
     * Abstract login method.
     * Each child class implements it differently
     * @param email is the email the user typed in.
     * @param password is the password the user typed in.
     * @return true if login succeed, false if not.
     */
    public abstract boolean login(String email, String password);


    
}


