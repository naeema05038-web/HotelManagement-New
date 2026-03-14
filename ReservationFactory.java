public class ReservationFactory {

    public static Reservation createReservation(int id, String name, String gender,
                                                String email, String phone, String city,
                                                String country, String nid, String consent,
                                                int roomNo, String roomType, double price,
                                                String checkIn, String checkOut) {
        return new Reservation(id, name, gender, email, phone, city, country,
                nid, consent, roomNo, roomType, price, checkIn, checkOut);
    }
}
