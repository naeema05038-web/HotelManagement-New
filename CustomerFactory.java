public class CustomerFactory {

    public static Customer createCustomer(String fullName, String phone, String email,
                                          String city, String country, String nationalId,
                                          boolean consent, Gender gender) {
        return new Customer(fullName, phone, email, city, country, nationalId, consent, gender);
    }
}