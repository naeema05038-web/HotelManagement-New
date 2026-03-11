import java.io.*;
import java.util.*;

public class ReservationManager implements Serializable {

    private static ReservationManager instance;
    private List<Reservation> reservations;

    private ReservationManager() {

        loadFromFile();

        if(reservations == null){
            reservations = new ArrayList<>();
        }

    }

    public static ReservationManager getInstance(){

        if(instance == null){
            instance = new ReservationManager();
        }

        return instance;
    }

    public void addReservation(Reservation r){
        reservations.add(r);
        saveToFile();
    }

    public void updateReservation(int index, Reservation r){
        reservations.set(index,r);
        saveToFile();
    }

    public void deleteReservation(int index){
        reservations.remove(index);
        saveToFile();
    }

    public List<Reservation> getAllReservations(){
        return reservations;
    }

    private void saveToFile(){

        try(ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream("reservations.dat"))){

            oos.writeObject(reservations);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    private void loadFromFile(){

        try(ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream("reservations.dat"))){

            reservations = (List<Reservation>) ois.readObject();

        }catch(Exception e){
            reservations = new ArrayList<>();
        }

    }
}