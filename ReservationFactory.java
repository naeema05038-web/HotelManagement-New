public class ReservationFactory {

    public static Reservation createReservation(
            int reservationId,
            int customerId,
            int roomId,
            String checkIn,
            String checkOut) {

        return new Reservation(reservationId, customerId, roomId, checkIn, checkOut);
    }

}