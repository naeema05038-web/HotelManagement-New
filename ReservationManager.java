import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ReservationManager implements Serializable {

    private static ReservationManager instance;
    private List<Reservation> reservations;
    private PriceStrategy priceStrategy = new StandardPrice();

    private ReservationManager() {
        load();
        if (reservations == null)
            reservations = new ArrayList<>();
    }

    public static ReservationManager getInstance() {
        if (instance == null)
            instance = new ReservationManager();
        return instance;
    }

    public void setPriceStrategy(PriceStrategy strategy) {
        this.priceStrategy = strategy;
    }


    public void addReservation(Reservation r) {
        reservations.add(r);
        save();
    }

    public void updateReservation(int index, Reservation r) {
        reservations.set(index, r);
        save();
    }

    public void deleteReservation(int index) {
        reservations.remove(index);
        save();
    }

    public int generateNextId() {
        int max = 0;
        for (Reservation r : reservations)
            if (r.getId() > max)
                max = r.getId();
        return max + 1;
    }


    public Iterator<Reservation> iterator() {
        return reservations.iterator();
    }

    public Iterator<Reservation> iteratorByCustomer(String email) {
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : reservations)
            if (r.getEmail().equalsIgnoreCase(email))
                list.add(r);
        return list.iterator();
    }

    public Iterator<Reservation> iteratorByRoom(int roomNo) {
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : reservations)
            if (r.getRoomNo() == roomNo)
                list.add(r);
        return list.iterator();
    }


    public double totalRevenue() {
        double total = 0;
        for (Reservation r : reservations)
            total += priceStrategy.calculatePrice(r);
        return total;
    }

    public boolean isRoomAvailable(int roomNo, String checkInStr, String checkOutStr, int excludeId) {
        LocalDate checkIn = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);

        for (Reservation r : reservations) {
            if (r.getId() == excludeId) continue;
            if (r.getRoomNo() == roomNo) {
                LocalDate rIn = LocalDate.parse(r.getCheckIn());
                LocalDate rOut = LocalDate.parse(r.getCheckOut());
                if (!(checkOut.isBefore(rIn) || checkIn.isAfter(rOut))) {
                    return false;
                }
            }
        }
        return true;
    }


    public void exportCSV() {
        try (FileWriter fw = new FileWriter("reservations.csv")) {
            fw.write("ID,Name,Gender,Email,Phone,City,Country,NID,Consent,RoomNo,RoomType,Price,CheckIn,CheckOut\n");
            for (Reservation r : reservations) {
                fw.write(r.getId() + "," + r.getName() + "," + r.getGender() + "," +
                        r.getEmail() + "," + r.getPhone() + "," + r.getCity() + "," +
                        r.getCountry() + "," + r.getNid() + "," + r.getConsent() + "," +
                        r.getRoomNo() + "," + r.getRoomType() + "," +
                        priceStrategy.calculatePrice(r) + "," +
                        r.getCheckIn() + "," + r.getCheckOut() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.dat"))) {
            oos.writeObject(reservations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.dat"))) {
            reservations = (List<Reservation>) ois.readObject();
        } catch (Exception e) {
            reservations = new ArrayList<>();
        }
    }
}
