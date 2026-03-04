
import java.io.Serializable;

public class Customer implements Serializable {

    private static int counter = 1;

    private int customerId;
    private String fullName;
    private String phone;
    private String email;
    private String city;
    private String country;
    private String nationalId;
    private boolean consent;
    private Gender gender;

    public Customer(String fullName, String phone, String email, String city,
            String country, String nationalId, boolean consent, Gender gender) {
        this.customerId = counter++;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.country = country;
        this.nationalId = nationalId;
        this.consent = consent;
        this.gender = gender;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
