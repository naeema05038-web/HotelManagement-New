import java.io.Serializable;

public class Room implements Serializable {

   
    private int roomNo;
    private RoomType type;
    private double price;
    private int floor;
    private int capacity;
    private String cleanStatus;
    private boolean available;

    public Room(int roomNo, RoomType type, double price, int floor, int capacity, String cleanStatus, boolean available) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.floor = floor;
        this.capacity = capacity;
        this.cleanStatus = cleanStatus;
        this.available = available;
    }

    public int getRoomNo()
        { return roomNo; }
    public void setRoomNo(int roomNo)
        { this.roomNo = roomNo; }

    public RoomType getType()
        { return type; }
    public void setType(RoomType type)
        { this.type = type; }

    public double getPrice()
        { return price; }
    public void setPrice(double price)
        { this.price = price; }

    public int getFloor()
            { return floor; }
    public void setFloor(int floor)
            { this.floor = floor; }

    public int getCapacity()
        { return capacity; }
    public void setCapacity(int capacity)
        { this.capacity = capacity; }

    public String getCleanStatus()
        { return cleanStatus; }
    public void setCleanStatus(String cleanStatus)
        { this.cleanStatus = cleanStatus; }

    public boolean isAvailable()
            { return available; }
    public void setAvailable(boolean available)
            { this.available = available; }
}
