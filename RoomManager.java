import java.io.*;
import java.util.*;

public class RoomManager implements Serializable {

    private static RoomManager instance;
    private List<Room> rooms;

    private RoomManager() {
        loadRoomsFromFile();
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
    }


    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }


    public void addRoom(Room room) {
        rooms.add(room);
        saveRoomsToFile();
    }


    public void deleteRoom(int roomNo) {
        rooms.removeIf(r -> r.getRoomNo() == roomNo);
        saveRoomsToFile();
    }


    public void updateRoom(Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNo() == updatedRoom.getRoomNo()) {
                rooms.set(i, updatedRoom);
                break;
            }
        }
        saveRoomsToFile();
    }


    public List<Room> getAllRooms() {
        return rooms;
    }


    public Iterator<Room> availableRoomsIterator() {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) {
                available.add(r);
            }
        }
        return available.iterator();
    }


    private void saveRoomsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("rooms.dat"))) {
            oos.writeObject(rooms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void loadRoomsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rooms.dat"))) {
            rooms = (List<Room>) ois.readObject();
        } catch (Exception e) {
            rooms = new ArrayList<>();
        }
    }
}