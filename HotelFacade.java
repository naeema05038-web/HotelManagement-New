
import java.util.ArrayList;

public class HotelFacade {

    private CustomerService customerService;
    private RoomManager roomManager;
    private ReservationManager reservationManager;

    public HotelFacade() {
        this.customerService = new CustomerService();
        this.roomManager = RoomManager.getInstance();
        this.reservationManager = ReservationManager.getInstance();
    }

    public Customer findCustomerById(int customerId) {
        for (Customer c : customerService.getCustomers()) {
            if (c.getCustomerId() == customerId) {
                return c;
            }
        }
        return null;
    }

    public Room findRoomByNumber(int roomNo) {
        for (Room r : roomManager.getAllRooms()) {
            if (r.getRoomNo() == roomNo) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<Reservation> findReservationsByCustomerId(int customerId) {
        ArrayList<Reservation> customerReservations = new ArrayList<>();
        for (Reservation r : reservationManager.getAllReservations()) {

            if (r.getId() == customerId) {
                customerReservations.add(r);
            }
        }
        return customerReservations;
    }

    public DashboardSummary getDashboardSummary() {
        int totalCustomers = customerService.getCustomers().size();
        int totalRooms = roomManager.getAllRooms().size();

        int availableRooms = 0;
        for (Room r : roomManager.getAllRooms()) {
            if (r.isAvailable()) {
                availableRooms++;
            }
        }

        int totalReservations = reservationManager.getAllReservations().size();
        double totalRevenue = 0;
        for (Reservation r : reservationManager.getAllReservations()) {
            totalRevenue += r.getPrice();
        }

        return new DashboardSummary(totalCustomers, totalRooms, availableRooms,
                totalReservations, totalRevenue);
    }

    public void openCustomerForm() {
        new CustomerForm();
    }

    public void openRoomForm() {
        new RoomForm();
    }

    public void openBookingForm() {
        new ReservationForm();
    }
}

class DashboardSummary {

    private int totalCustomers;
    private int totalRooms;
    private int availableRooms;
    private int totalReservations;
    private double totalRevenue;

    public DashboardSummary(int totalCustomers, int totalRooms, int availableRooms,
            int totalReservations, double totalRevenue) {
        this.totalCustomers = totalCustomers;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.totalReservations = totalReservations;
        this.totalRevenue = totalRevenue;
    }

    @Override
    public String toString() {
        return String.format(
                "🏨 HOTEL DASHBOARD\n"
                + "==================\n"
                + "Total Customers: %d\n"
                + "Total Rooms: %d (Available: %d)\n"
                + "Active Reservations: %d\n"
                + "Total Revenue: $%.2f",
                totalCustomers, totalRooms, availableRooms, totalReservations, totalRevenue
        );
    }
}
