
import java.util.ArrayList;

public class CustomerService {

    private CustomerRepository repo;

    public CustomerService() {
        repo = CustomerRepository.getInstance();
    }

    public void addCustomer(Customer c) {
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
}
