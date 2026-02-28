import java.io.Serializable;

public class Room implements Serializable {

    private int roomId;
    private RoomType type;
    private double price;
    private boolean available;

    public Room(int roomId, RoomType type, double price, boolean available) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}