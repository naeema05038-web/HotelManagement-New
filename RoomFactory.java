public class RoomFactory {

    public static Room createRoom(int id, RoomType type, double price, boolean available) {
        return new Room(id, type, price, available);
    }
}