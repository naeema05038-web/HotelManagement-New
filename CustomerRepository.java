
import java.io.*;
import java.util.ArrayList;

public class CustomerRepository {

    private static CustomerRepository instance;
    private ArrayList<Customer> customers;
    private final String FILE = "customers.dat";

    private CustomerRepository() {
        load();
    }

    public static CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    public ArrayList<Customer> getAll() {
        return customers;
    }

    public void add(Customer c) {
        customers.add(c);
        save();
    }

    public void update(int index, Customer c) {
        customers.set(index, c);
        save();
    }

    public void delete(int index) {
        customers.remove(index);
        save();
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(customers);
        } catch (Exception ignored) {
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            customers = (ArrayList<Customer>) ois.readObject();
        } catch (Exception e) {
            customers = new ArrayList<>();
        }
    }
}
