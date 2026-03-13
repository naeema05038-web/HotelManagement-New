public class RoomFactory {

    public static Room createRoom(int roomNo, RoomType type, double price, int floor, int capacity, String cleanStatus, boolean available) {
        return new Room(roomNo, type, price, floor, capacity, cleanStatus, available);
    }

    
}
