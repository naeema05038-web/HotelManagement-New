import java.util.*;

public class ReservationManager {

    private static ReservationManager instance;
    private List<Reservation> reservations;

    private ReservationManager(){
        reservations = new ArrayList<>();
    }

    public static ReservationManager getInstance(){

        if(instance==null){
            instance = new ReservationManager();
        }

        return instance;
    }

    public void addReservation(Reservation r){
        reservations.add(r);
    }

    public void deleteReservation(int index){
        reservations.remove(index);
    }

    public void updateReservation(int index, Reservation r){
        reservations.set(index,r);
    }

    public List<Reservation> getAllReservations(){
        return reservations;
    }


    public interface SearchStrategy{
        boolean match(Reservation r,String key);
    }


    public List<Reservation> search(String key, SearchStrategy strategy){

        List<Reservation> result = new ArrayList<>();

        for(Reservation r:reservations){

            if(strategy.match(r,key)){
                result.add(r);
            }

        }

        return result;
    }

}
