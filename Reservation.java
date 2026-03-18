/*
import java.io.Serializable;

public class Reservation implements Serializable {

    private int id;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String nid;
    private String consent;
    private int roomNo;
    private String roomType;
    private double price;
    private String checkIn;
    private String checkOut;

    public Reservation(int id, String name, String gender, String email, String phone,
                        String city, String country, String nid, String consent,
                        int roomNo, String roomType, double price,
                        String checkIn, String checkOut) {

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.nid = nid;
        this.consent = consent;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getNid() { return nid; }
    public String getConsent() { return consent; }
    public int getRoomNo() { return roomNo; }
    public String getRoomType() { return roomType; }
    public double getPrice() { return price; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
}*/


import java.io.Serializable;

public class Reservation implements Serializable {

    private int id;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String nid;       // Added NID
    private String consent;   // Added consent
    private int roomNo;
    private String roomType;
    private double price;
    private String checkIn;
    private String checkOut;

    public Reservation(int id, String name, String gender, String email, String phone,
                        String city, String country, String nid, String consent,
                        int roomNo, String roomType, double price,
                        String checkIn, String checkOut) {

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.nid = nid;
        this.consent = consent;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getNid() { return nid; }
    public String getConsent() { return consent; }
    public int getRoomNo() { return roomNo; }
    public String getRoomType() { return roomType; }
    public double getPrice() { return price; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }

    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setNid(String nid) { this.nid = nid; }
    public void setConsent(String consent) { this.consent = consent; }
    public void setRoomNo(int roomNo) { this.roomNo = roomNo; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setPrice(double price) { this.price = price; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
}