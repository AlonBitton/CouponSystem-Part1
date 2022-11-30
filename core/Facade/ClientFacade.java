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
     */

    public ClientFacade() {
        this.couponsDAO =  new CouponsDBDAO();
        this.companyDAO = new CompaniesDBDAO();
        this.customerDao = new CustomersDBDAO();
    }

    public abstract boolean login(String email, String password);


    
}


