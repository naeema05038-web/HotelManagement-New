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

    public Reservation(int id,String name,String gender,String email,String phone,
                        String city,String country,String nid,String consent,
                        int roomNo,String roomType,double price,
                        String checkIn,String checkOut){

        this.id=id;
        this.name=name;
        this.gender=gender;
        this.email=email;
        this.phone=phone;
        this.city=city;
        this.country=country;
        this.nid=nid;
        this.consent=consent;
        this.roomNo=roomNo;
        this.roomType=roomType;
        this.price=price;
        this.checkIn=checkIn;
        this.checkOut=checkOut;
    }

    public int getId()
        { return id; }
    public String getName()
        { return name; }
    public String getGender()
        { return gender; }
    public String getEmail()
        { return email; }
    public String getPhone()
        { return phone; }
    public String getCity()
        { return city; }
    public String getCountry()
        { return country; }
    public String getNid()
        { return nid; }
    public String getConsent()
        { return consent; }
    public int getRoomNo()
        { return roomNo; }
    public String getRoomType()
        { return roomType; }
    public double getPrice()
        { return price; }
    public String getCheckIn()
        { return checkIn; }
    public String getCheckOut()
        { return checkOut; }

}
