import java.util.ArrayList;

public class CustomerService {

    private ArrayList<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public void updateCustomer(int index, Customer c) {
        if(index >= 0 && index < customers.size()) {
            customers.set(index, c);
        }
    }

    public void deleteCustomer(int index) {
        if(index >=0 && index < customers.size()) {
            customers.remove(index);
        }
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
