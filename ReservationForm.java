import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField search;
    JTextArea details;

    public ReservationForm(){

        setTitle("Reservation Management");
        setSize(1100,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

     
        getContentPane().setBackground(Color.BLACK);

        model = new DefaultTableModel(
                new String[]{"ID","Name","Email","Room","Type","Price/Day","Total Price","CheckIn","CheckOut"},0);

        table = new JTable(model);

  
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(0,120,215)); 
        table.setSelectionForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(
                    JTable table,Object value,boolean isSelected,
                    boolean hasFocus,int row,int col){

                Component c = super.getTableCellRendererComponent(
                        table,value,isSelected,hasFocus,row,col);

                String out = table.getValueAt(row,8).toString();

                if(LocalDate.parse(out).isAfter(LocalDate.now()))
                    c.setBackground(new Color(100,0,0)); 
                else
                    c.setBackground(new Color(0,100,0)); 

                c.setForeground(Color.WHITE);

                if(isSelected){
                    c.setBackground(new Color(0,120,215)); 
                    c.setForeground(Color.WHITE);
                }

                return c;
            }
        });

        add(new JScrollPane(table),BorderLayout.CENTER);

        details = new JTextArea();
        details.setEditable(false);
        details.setBorder(BorderFactory.createTitledBorder("Reservation Details"));
        details.setBackground(Color.BLACK);
        details.setForeground(Color.WHITE);
        add(new JScrollPane(details),BorderLayout.EAST);

        table.getSelectionModel().addListSelectionListener(e->showDetails());

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK); 

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton history = new JButton("Customer History");
        JButton roomHistory = new JButton("Room History");
        JButton revenue = new JButton("Revenue");
        JButton export = new JButton("Export CSV");

        search = new JTextField(10);
        JButton searchBtn = new JButton("Search");

        JButton[] btns={add,edit,delete,history,roomHistory,revenue,export,searchBtn};

        for(JButton b:btns){
            b.setBackground(Color.WHITE); 
            b.setForeground(Color.BLACK); 
        }

        panel.add(add);
        panel.add(edit);
        panel.add(delete);
        panel.add(history);
        panel.add(roomHistory);
        panel.add(revenue);
        panel.add(export);
        panel.add(new JLabel("Search Room"){{
            setForeground(Color.WHITE);
        }});
        search.setBackground(Color.DARK_GRAY);
        search.setForeground(Color.WHITE);
        panel.add(search);
        panel.add(searchBtn);

        add(panel,BorderLayout.SOUTH);

        add.addActionListener(e->addReservation());
        edit.addActionListener(e->editReservation());
        delete.addActionListener(e->deleteReservation());
        searchBtn.addActionListener(e->search());
        history.addActionListener(e->customerHistory());
        roomHistory.addActionListener(e->roomHistory());
        revenue.addActionListener(e->showRevenue());
        export.addActionListener(e->exportCSV());

        loadTable();

        setVisible(true);
    }

    void loadTable(){
        model.setRowCount(0);

        Iterator<Reservation> it =
                ReservationManager.getInstance().iterator();

        while(it.hasNext()){
            Reservation r = it.next();

            long days = ChronoUnit.DAYS.between(
                    LocalDate.parse(r.getCheckIn()),
                    LocalDate.parse(r.getCheckOut()));

            double total = r.getPrice();
            double pricePerDay = total/days;

            model.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getEmail(),
                    r.getRoomNo(),
                    r.getRoomType(),
                    pricePerDay,
                    total,
                    r.getCheckIn(),
                    r.getCheckOut()
            });
        }
    }

    void showDetails(){
        int row = table.getSelectedRow();
        if(row<0) return;

        Reservation r =
                ReservationManager.getInstance().getAllReservations().get(row);

        long days = ChronoUnit.DAYS.between(
                LocalDate.parse(r.getCheckIn()),
                LocalDate.parse(r.getCheckOut()));

        double total = r.getPrice();
        double pricePerDay = total/days;

        details.setText(
                "Reservation ID : "+r.getId()+"\n\n"+
                "Customer Name : "+r.getName()+"\n"+
                "Email : "+r.getEmail()+"\n\n"+
                "Room Number : "+r.getRoomNo()+"\n"+
                "Room Type : "+r.getRoomType()+"\n\n"+
                "Check In : "+r.getCheckIn()+"\n"+
                "Check Out : "+r.getCheckOut()+"\n"+
                "Days Stayed : "+days+"\n\n"+
                "Price Per Day : "+pricePerDay+"\n"+
                "Total Price : "+total
        );
    }

    void addReservation(){
        try{
            int id = new Random().nextInt(1000);

            String name = JOptionPane.showInputDialog("Customer Name");
            String email = JOptionPane.showInputDialog("Email");
            int room = Integer.parseInt(JOptionPane.showInputDialog("Room Number"));
            String type = JOptionPane.showInputDialog("Room Type");

            double pricePerDay = Double.parseDouble(
                    JOptionPane.showInputDialog("Price Per Day"));

            String in = JOptionPane.showInputDialog("CheckIn yyyy-mm-dd");
            String out = JOptionPane.showInputDialog("CheckOut yyyy-mm-dd");

            long days = ChronoUnit.DAYS.between(
                    LocalDate.parse(in),LocalDate.parse(out));

            double total = pricePerDay*days;

            Reservation r = new Reservation(
                    id,
                    name,
                    "Other",
                    email,
                    "",
                    "",
                    "",
                    "",
                    "",
                    room,
                    type,
                    total,
                    in,
                    out
            );

            ReservationManager.getInstance().addReservation(r);

            loadTable();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Invalid Input");
        }
    }

    void editReservation(){
        int row = table.getSelectedRow();

        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        deleteReservation();
        addReservation();
    }

    void deleteReservation(){
        int row = table.getSelectedRow();

        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        ReservationManager.getInstance().deleteReservation(row);

        loadTable();
    }

    void search(){
        String key = search.getText();
        model.setRowCount(0);

        for(Reservation r: ReservationManager.getInstance().getAllReservations()){
            if(String.valueOf(r.getRoomNo()).contains(key))
                model.addRow(new Object[]{
                        r.getId(),r.getName(),r.getEmail(),
                        r.getRoomNo(),r.getRoomType(),
                        r.getPrice(),r.getPrice(),
                        r.getCheckIn(),r.getCheckOut()
                });
        }
    }

    void customerHistory(){
        int row=table.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        String email=table.getValueAt(row,2).toString();

        Iterator<Reservation> it=
                ReservationManager.getInstance().iteratorByCustomer(email);

        StringBuilder sb=new StringBuilder();

        while(it.hasNext()){
            Reservation r=it.next();
            sb.append("Room ").append(r.getRoomNo())
                    .append(" ")
                    .append(r.getCheckIn())
                    .append(" -> ")
                    .append(r.getCheckOut())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this,sb.toString());
    }

    void roomHistory(){
        int row=table.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        int room=Integer.parseInt(table.getValueAt(row,3).toString());

        Iterator<Reservation> it=
                ReservationManager.getInstance().iteratorByRoom(room);

        StringBuilder sb=new StringBuilder();

        while(it.hasNext()){
            Reservation r=it.next();
            sb.append(r.getName())
                    .append(" ")
                    .append(r.getCheckIn())
                    .append(" -> ")
                    .append(r.getCheckOut())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this,sb.toString());
    }

    void showRevenue(){
        double total= ReservationManager.getInstance().totalRevenue();
        JOptionPane.showMessageDialog(this,"Total Revenue = "+total);
    }

    void exportCSV(){
        ReservationManager.getInstance().exportCSV();
        JOptionPane.showMessageDialog(this,"CSV Exported");
    }

    public static void main(String[] args){
        new ReservationForm();
    }
}
