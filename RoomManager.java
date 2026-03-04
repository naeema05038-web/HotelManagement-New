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
        try {
            FileOutputStream fos = new FileOutputStream("rooms.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rooms);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void loadRoomsFromFile() {
        try {
            FileInputStream fis = new FileInputStream("rooms.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            rooms = (List<Room>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            rooms = new ArrayList<>();
        }
    }
}