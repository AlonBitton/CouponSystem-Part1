package Facade;

import java.util.List;

import Company.Company;
import Customer.Customer;

public class AdminFacade extends ClientFacade{
    public static final String ADMIN_EMAIL = "admin@admin.com";

    public static final String ADMIN_PASSWORD = "admin";
    public AdminFacade(){
     super();
    }

    @Override
    public boolean login(String email, String password) {
        return ADMIN_EMAIL.equals(email)&&ADMIN_PASSWORD.equals(password);
    }
    
    public void addCompany(Company company){
        companyDAO.addCompany(company);
    }


    public void updateCompany(Company company){
        companyDAO.updateCompany(company);
    }

    public void deleteCompanyWithCoupons(int CompanyID){
        companyDAO.deleteCompanyWithCoupons(CompanyID);
    }

    public void deleteCompany(int companyID){
        companyDAO.deleteCompany(companyID);
    }

    public Company getOneCompany(int CompanyID){
        return this.companyDAO.getOneCompany(CompanyID);
    }

    public List<Company> getAllCompanies(){
       return this.companyDAO.getAllCompanies();
        
    }

    public void addCustomer(Customer customer){
        this.customerDao.addCustomer(customer);

    }

    public void updateCutomer(Customer customer){
        this.customerDao.updateCutomer(customer);
    }

    public void deleteCutomer(int customerID){
        this.customerDao.deleteCutomer(customerID);
    }

    public List<Customer> getAllCustomers(){
        return this.customerDao.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID){
       return this.customerDao.getOneCustomer(customerID);
    }

}
