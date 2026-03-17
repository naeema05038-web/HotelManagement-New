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
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10,10));

    
        model = new DefaultTableModel(
                new String[]{"ID","Name","Email","Room","Type","Price/Day","Total Price","CheckIn","CheckOut"},0);
        table = new JTable(model);
        table.setRowHeight(30);

    
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(0,120,215));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);

    
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column){
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                if(isSelected){
                    c.setBackground(new Color(0,120,215));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(30,30,30) : Color.BLACK);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Reservations",0,0, new Font("Arial",Font.BOLD,14), Color.WHITE));
        add(tableScroll, BorderLayout.CENTER);

    
        details = new JTextArea();
        details.setEditable(false);
        details.setBackground(Color.BLACK);
        details.setForeground(Color.WHITE);
        details.setFont(new Font("Consolas", Font.PLAIN, 14));
        details.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Reservation Details",0,0,new Font("Arial",Font.BOLD,14),Color.WHITE));
        JScrollPane detailScroll = new JScrollPane(details);
        detailScroll.setPreferredSize(new Dimension(300,0));
        add(detailScroll, BorderLayout.EAST);

        table.getSelectionModel().addListSelectionListener(e -> showDetails());

    
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton history = new JButton("Customer History");
        JButton roomHistory = new JButton("Room History");
        JButton revenue = new JButton("Revenue");
        JButton export = new JButton("Export CSV");

        search = new JTextField(12);
        search.setBackground(new Color(30,30,30));
        search.setForeground(Color.WHITE);
        search.setCaretColor(Color.WHITE);
        JButton searchBtn = new JButton("Search");

        JButton[] btns = {add, edit, delete, history, roomHistory, revenue, export, searchBtn};
        for(JButton b:btns){
            b.setBackground(Color.WHITE);
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial",Font.BOLD,12));
        
            b.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent evt){b.setBackground(Color.LIGHT_GRAY);}
                public void mouseExited(java.awt.event.MouseEvent evt){b.setBackground(Color.WHITE);}
            });
        }

        bottomPanel.add(add);
        bottomPanel.add(edit);
        bottomPanel.add(delete);
        bottomPanel.add(history);
        bottomPanel.add(roomHistory);
        bottomPanel.add(revenue);
        bottomPanel.add(export);
        bottomPanel.add(new JLabel("Search Room"){{
            setForeground(Color.WHITE);
            setFont(new Font("Arial",Font.BOLD,12));
        }});
        bottomPanel.add(search);
        bottomPanel.add(searchBtn);

        add(bottomPanel, BorderLayout.SOUTH);

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
