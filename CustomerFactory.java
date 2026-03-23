
public class CustomerFactory {

    public static Customer createCustomer(String name, String phone, String email,
            String city, String country, String nid,
            boolean consent, Gender gender) {
        return new Customer(name, phone, email, city, country, nid, consent, gender);
    }
}
