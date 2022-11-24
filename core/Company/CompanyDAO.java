package Company;
import java.util.List;

public interface CompanyDAO {
    
    boolean isCompanyExists(String email, String password);

    int addCompany(Company company);

    Company getOneCompany(int CompanyID);

    List<Company> getAllCompanies();

    void updateCompany(Company company);

    public void deleteCompanyWithCoupons(int CompanyID);

    public void deleteCompany(int CompanyID);

}
