
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

interface HotelIterator<T> {

    boolean hasNext();

    T next();

    void reset();

    int size();
}

class CustomerIterator implements HotelIterator<Customer> {

    private List<Customer> customers;
    private int position;

    public CustomerIterator(List<Customer> customers) {
        this.customers = customers;
        this.position = 0;
    }

    public boolean hasNext() {
        return position < customers.size();
    }

    public Customer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return customers.get(position++);
    }

    public void reset() {
        position = 0;
    }

    public int size() {
        return customers.size();
    }
}

class RoomIterator implements HotelIterator<Room> {

    private List<Room> rooms;
    private int position;

    public RoomIterator(List<Room> rooms) {
        this.rooms = rooms;
        this.position = 0;
    }

    public boolean hasNext() {
        return position < rooms.size();
    }

    public Room next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return rooms.get(position++);
    }

    public void reset() {
        position = 0;
    }

    public int size() {
        return rooms.size();
    }
}

class AvailableRoomIterator implements HotelIterator<Room> {

    private List<Room> availableRooms;
    private int position;

    public AvailableRoomIterator(List<Room> allRooms) {
        this.availableRooms = new ArrayList<>();
        for (Room r : allRooms) {
            if (r.isAvailable()) {
                this.availableRooms.add(r);
            }
        }
        this.position = 0;
    }

    public boolean hasNext() {
        return position < availableRooms.size();
    }

    public Room next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return availableRooms.get(position++);
    }

    public void reset() {
        position = 0;
    }

    public int size() {
        return availableRooms.size();
    }
}

class HotelCollection {

    private List<Customer> customers = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public void addRoom(Room r) {
        rooms.add(r);
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public HotelIterator<Customer> getCustomerIterator() {
        return new CustomerIterator(customers);
    }

    public HotelIterator<Room> getRoomIterator() {
        return new RoomIterator(rooms);
    }

    public HotelIterator<Room> getAvailableRoomIterator() {
        return new AvailableRoomIterator(rooms);
    }
}
