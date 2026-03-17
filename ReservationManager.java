import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ReservationManager implements Serializable {

    private static ReservationManager instance;
    private ArrayList<Reservation> reservations;
    private final String FILE="reservations.dat";

    private ReservationManager(){
        load();
    }

    public static ReservationManager getInstance(){
        if(instance==null)
            instance=new ReservationManager();
        return instance;
    }

    public void addReservation(Reservation r){
        reservations.add(r);
        save();
    }

    public void updateReservation(int index,Reservation r){
        reservations.set(index,r);
        save();
    }

    public void deleteReservation(int index){
        reservations.remove(index);
        save();
    }

    public ArrayList<Reservation> getAllReservations(){
        return reservations;
    }

    public Iterator<Reservation> iterator(){
        return reservations.iterator();
    }

    public Iterator<Reservation> iteratorByCustomer(String email){

        ArrayList<Reservation> list=new ArrayList<>();

        for(Reservation r:reservations)
            if(r.getEmail().equalsIgnoreCase(email))
                list.add(r);

        return list.iterator();
    }

    public Iterator<Reservation> iteratorByRoom(int room){

        ArrayList<Reservation> list=new ArrayList<>();

        for(Reservation r:reservations)
            if(r.getRoomNo()==room)
                list.add(r);

        return list.iterator();
    }

    public int generateNextId(){

        int max=0;

        for(Reservation r:reservations)
            if(r.getId()>max)
                max=r.getId();

        return max+1;
    }

    public boolean isRoomAvailable(int room,String checkIn,String checkOut,int excludeId){

        LocalDate in=LocalDate.parse(checkIn);
        LocalDate out=LocalDate.parse(checkOut);

        for(Reservation r:reservations){

            if(r.getId()==excludeId) continue;

            if(r.getRoomNo()==room){

                LocalDate rIn=LocalDate.parse(r.getCheckIn());
                LocalDate rOut=LocalDate.parse(r.getCheckOut());

                if(!(out.isBefore(rIn)||in.isAfter(rOut)))
                    return false;
            }
        }

        return true;
    }

    public double totalRevenue(){

        double total=0;

        for(Reservation r:reservations)
            total+=r.getPrice();

        return total;
    }

    public void exportCSV(){

        try{

            FileWriter fw=new FileWriter("reservations.csv");

            fw.write("ID,Name,Email,Room,Type,Price,CheckIn,CheckOut\n");

            for(Reservation r:reservations){

                fw.write(
                        r.getId()+","+
                        r.getName()+","+
                        r.getEmail()+","+
                        r.getRoomNo()+","+
                        r.getRoomType()+","+
                        r.getPrice()+","+
                        r.getCheckIn()+","+
                        r.getCheckOut()+"\n"
                );
            }

            fw.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void save(){

        try(ObjectOutputStream oos=
                    new ObjectOutputStream(new FileOutputStream(FILE))){

            oos.writeObject(reservations);

        }catch(Exception e){}
    }

    private void load(){

        try(ObjectInputStream ois=
                    new ObjectInputStream(new FileInputStream(FILE))){

            reservations=(ArrayList<Reservation>)ois.readObject();

        }catch(Exception e){

            reservations=new ArrayList<>();
        }
    }
}