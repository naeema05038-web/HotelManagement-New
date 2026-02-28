import java.util.ArrayList;
import java.util.Iterator;

public class RoomManager {

    private static RoomManager instance;
    private ArrayList<Room> rooms;

    private RoomManager() {
        rooms = new ArrayList<>();
    }

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    public Iterator<Room> availableRoomsIterator() {
        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        return availableRooms.iterator();
    }
}