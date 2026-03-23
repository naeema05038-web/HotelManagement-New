
import java.util.ArrayList;

public class CustomerService {

    private CustomerRepository repo;

    public CustomerService() {
        repo = CustomerRepository.getInstance();
    }

    public void addCustomer(Customer c) {

        ArrayList<Customer> customers = repo.getAll();

        int maxId = 0;
        for (Customer existing : customers) {
            if (existing.getCustomerId() > maxId) {
                maxId = existing.getCustomerId();
            }
        }

        c.setCustomerId(maxId + 1);

        repo.add(c);
    }

    public void updateCustomer(int index, Customer c) {
        repo.update(index, c);
    }

    public void deleteCustomer(int index) {
        repo.delete(index);
    }

    public ArrayList<Customer> getCustomers() {
        return repo.getAll();
    }

    public void clearAllData() {
        repo.clearAllData();
    }
}