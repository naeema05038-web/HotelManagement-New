/*import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea details;
    JTextField search;

    CustomerService customerService = new CustomerService();

    public ReservationForm() {

        setTitle("Reservation Management");
        setSize(1100,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.BLACK);

        model = new DefaultTableModel(
                new String[]{"ID","Name","Email","Room","Type","Price/Day","Total","CheckIn","CheckOut"},0);

        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table),BorderLayout.CENTER);

        details = new JTextArea();
        details.setEditable(false);
        details.setBackground(Color.BLACK);
        details.setForeground(Color.WHITE);
        details.setBorder(BorderFactory.createTitledBorder("Reservation Details"));

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

        search = new JTextField(10);
        JButton searchBtn = new JButton("Search");

        JButton[] btns = {add,edit,delete,history,roomHistory,revenue,searchBtn};

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
        panel.add(new JLabel("Search Room"));
        panel.add(search);
        panel.add(searchBtn);

        add(panel,BorderLayout.SOUTH);

        add.addActionListener(e->addPopup());
        edit.addActionListener(e->editPopup());
        delete.addActionListener(e->deleteReservation());
        history.addActionListener(e->customerHistory());
        roomHistory.addActionListener(e->roomHistory());
        revenue.addActionListener(e->showRevenue());
        searchBtn.addActionListener(e->search());

        loadTable();
        setVisible(true);
    }

    void loadTable(){

        model.setRowCount(0);

        Iterator<Reservation> it = ReservationManager.getInstance().iterator();

        while(it.hasNext()){

            Reservation r = it.next();

            long days = ChronoUnit.DAYS.between(
                    LocalDate.parse(r.getCheckIn()),
                    LocalDate.parse(r.getCheckOut()));

            double pricePerDay = r.getPrice()/days;

            model.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getEmail(),
                    r.getRoomNo(),
                    r.getRoomType(),
                    pricePerDay,
                    r.getPrice(),
                    r.getCheckIn(),
                    r.getCheckOut()
            });
        }
    }

    void showDetails(){

        int row = table.getSelectedRow();

        if(row<0) return;

        Reservation r = ReservationManager.getInstance().getAllReservations().get(row);

        long days = ChronoUnit.DAYS.between(
                LocalDate.parse(r.getCheckIn()),
                LocalDate.parse(r.getCheckOut()));

        double pricePerDay = r.getPrice()/days;

        details.setText(

                "Reservation ID : "+r.getId()+"\n\n"+

                "Customer Name : "+r.getName()+"\n"+
                "Email : "+r.getEmail()+"\n\n"+

                "Room Number : "+r.getRoomNo()+"\n"+
                "Room Type : "+r.getRoomType()+"\n\n"+

                "Check In : "+r.getCheckIn()+"\n"+
                "Check Out : "+r.getCheckOut()+"\n"+
                "Days : "+days+"\n\n"+

                "Price Per Day : "+pricePerDay+"\n"+
                "Total Price : "+r.getPrice()
        );
    }

    void addPopup(){

        JDialog d = new JDialog(this,"Add Reservation",true);
        d.setSize(400,400);
        d.setLayout(new GridLayout(9,2,5,5));
        d.setLocationRelativeTo(this);

        JComboBox<Customer> customerBox =
                new JComboBox<>(customerService.getCustomers().toArray(new Customer[0]));

        JTextField name = new JTextField();
        JTextField email = new JTextField();

        JComboBox<Room> roomBox =
                new JComboBox<>(RoomManager.getInstance().getAllRooms().toArray(new Room[0]));

        JTextField type = new JTextField();
        JTextField price = new JTextField();
        JTextField checkIn = new JTextField();
        JTextField checkOut = new JTextField();

        JButton save = new JButton("Save");

        name.setEditable(false);
        email.setEditable(false);
        type.setEditable(false);
        price.setEditable(false);

        d.add(new JLabel("Customer"));
        d.add(customerBox);
        d.add(new JLabel("Name"));
        d.add(name);
        d.add(new JLabel("Email"));
        d.add(email);
        d.add(new JLabel("Room"));
        d.add(roomBox);
        d.add(new JLabel("Room Type"));
        d.add(type);
        d.add(new JLabel("Price/Day"));
        d.add(price);
        d.add(new JLabel("CheckIn yyyy-mm-dd"));
        d.add(checkIn);
        d.add(new JLabel("CheckOut yyyy-mm-dd"));
        d.add(checkOut);
        d.add(new JLabel(""));
        d.add(save);

        customerBox.addActionListener(e->{

            Customer c=(Customer)customerBox.getSelectedItem();

            name.setText(c.getFullName());
            email.setText(c.getEmail());

        });

        roomBox.addActionListener(e->{

            Room r=(Room)roomBox.getSelectedItem();

            type.setText(r.getTypeString());
            price.setText(String.valueOf(r.getPrice()));

        });

        save.addActionListener(e->{

            try{

                Customer c=(Customer)customerBox.getSelectedItem();
                Room r=(Room)roomBox.getSelectedItem();

                LocalDate in=LocalDate.parse(checkIn.getText());
                LocalDate out=LocalDate.parse(checkOut.getText());

                long days=ChronoUnit.DAYS.between(in,out);

                if(days<=0){

                    JOptionPane.showMessageDialog(d,"Invalid dates");
                    return;

                }

                double total=r.getPrice()*days;

                Reservation res = new Reservation(

                        ReservationManager.getInstance().generateId(),

                        c.getFullName(),"",c.getEmail(),"","","","","",

                        r.getRoomNo(),
                        r.getTypeString(),
                        total,

                        in.toString(),
                        out.toString()
                );

                ReservationManager.getInstance().addReservation(res);

                loadTable();

                d.dispose();

            }catch(Exception ex){

                JOptionPane.showMessageDialog(d,"Invalid Input");

            }

        });

        d.setVisible(true);
    }

    void editPopup(){

        int row=table.getSelectedRow();

        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        Reservation rOld =
                ReservationManager.getInstance().getAllReservations().get(row);

        JDialog d=new JDialog(this,"Edit Reservation",true);

        d.setSize(400,400);
        d.setLayout(new GridLayout(9,2,5,5));
        d.setLocationRelativeTo(this);

        JTextField name=new JTextField(rOld.getName());
        JTextField email=new JTextField(rOld.getEmail());

        JComboBox<Room> roomBox =
                new JComboBox<>(RoomManager.getInstance().getAllRooms().toArray(new Room[0]));

        JTextField type=new JTextField(rOld.getRoomType());
        JTextField price=new JTextField(String.valueOf(rOld.getPrice()));

        JTextField checkIn=new JTextField(rOld.getCheckIn());
        JTextField checkOut=new JTextField(rOld.getCheckOut());

        JButton save=new JButton("Save");

        name.setEditable(false);
        email.setEditable(false);
        type.setEditable(false);
        price.setEditable(false);

        d.add(new JLabel("Name"));
        d.add(name);
        d.add(new JLabel("Email"));
        d.add(email);
        d.add(new JLabel("Room"));
        d.add(roomBox);
        d.add(new JLabel("Type"));
        d.add(type);
        d.add(new JLabel("Price"));
        d.add(price);
        d.add(new JLabel("CheckIn"));
        d.add(checkIn);
        d.add(new JLabel("CheckOut"));
        d.add(checkOut);
        d.add(new JLabel(""));
        d.add(save);

        save.addActionListener(e->{

            try{

                Room r=(Room)roomBox.getSelectedItem();

                LocalDate in=LocalDate.parse(checkIn.getText());
                LocalDate out=LocalDate.parse(checkOut.getText());

                long days=ChronoUnit.DAYS.between(in,out);

                double total=r.getPrice()*days;

                Reservation res=new Reservation(

                        rOld.getId(),

                        name.getText(),"",email.getText(),"","","","","",

                        r.getRoomNo(),
                        r.getTypeString(),
                        total,

                        in.toString(),
                        out.toString()
                );

                ReservationManager.getInstance().updateReservation(row,res);

                loadTable();

                d.dispose();

            }catch(Exception ex){

                JOptionPane.showMessageDialog(d,"Invalid Input");

            }

        });

        d.setVisible(true);
    }

    void deleteReservation(){

        int row=table.getSelectedRow();

        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        ReservationManager.getInstance().deleteReservation(row);

        loadTable();
    }

    void customerHistory(){

        int row=table.getSelectedRow();

        if(row<0){
            JOptionPane.showMessageDialog(this,"Select reservation");
            return;
        }

        String email=table.getValueAt(row,2).toString();

        Iterator<Reservation> it =
                ReservationManager.getInstance().iteratorByCustomer(email);

        StringBuilder sb=new StringBuilder();

        sb.append("CUSTOMER BOOKING HISTORY\n\n");

        while(it.hasNext()){

            Reservation r=it.next();

            sb.append("Reservation ID : ").append(r.getId()).append("\n");
            sb.append("Customer : ").append(r.getName()).append("\n");
            sb.append("Email : ").append(r.getEmail()).append("\n");
            sb.append("Room : ").append(r.getRoomNo()).append("\n");
            sb.append("Room Type : ").append(r.getRoomType()).append("\n");
            sb.append("CheckIn : ").append(r.getCheckIn()).append("\n");
            sb.append("CheckOut : ").append(r.getCheckOut()).append("\n");
            sb.append("Total Paid : ").append(r.getPrice()).append("\n");

            sb.append("---------------------------------\n\n");

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

        Iterator<Reservation> it =
                ReservationManager.getInstance().iteratorByRoom(room);

        StringBuilder sb=new StringBuilder();

        sb.append("ROOM BOOKING HISTORY\n\n");

        while(it.hasNext()){

            Reservation r=it.next();

            sb.append("Reservation ID : ").append(r.getId()).append("\n");
            sb.append("Customer : ").append(r.getName()).append("\n");
            sb.append("Email : ").append(r.getEmail()).append("\n");
            sb.append("CheckIn : ").append(r.getCheckIn()).append("\n");
            sb.append("CheckOut : ").append(r.getCheckOut()).append("\n");
            sb.append("Total Paid : ").append(r.getPrice()).append("\n");

            sb.append("---------------------------------\n\n");
        }

        JOptionPane.showMessageDialog(this,sb.toString());
    }

    void showRevenue(){

        double total=ReservationManager.getInstance().totalRevenue();

        JOptionPane.showMessageDialog(this,"Total Revenue : "+total);
    }

    void search(){

        String key=search.getText();

        model.setRowCount(0);

        for(Reservation r:ReservationManager.getInstance().getAllReservations()){

            if(String.valueOf(r.getRoomNo()).contains(key)){

                model.addRow(new Object[]{
                        r.getId(),
                        r.getName(),
                        r.getEmail(),
                        r.getRoomNo(),
                        r.getRoomType(),
                        r.getPrice(),
                        r.getPrice(),
                        r.getCheckIn(),
                        r.getCheckOut()
                });
            }
        }
    }

    public static void main(String[] args) {

        new ReservationForm();

    }
}
    */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea txtInfo;
    JTextField search;

    public ReservationForm() {

        setTitle("Reservation Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ---------------- TABLE ----------------
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Room", "Type", "Price", "CheckIn", "CheckOut"}, 0);

        table = new JTable(model);

        // Room status color: RED = future booking, GREEN = past/available
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                try {
                    LocalDate out = LocalDate.parse(table.getValueAt(row, 7).toString());
                    if (out.isAfter(LocalDate.now()))
                        c.setBackground(Color.RED);
                    else
                        c.setBackground(Color.GREEN);
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.NORTH);

        // ---------------- DETAILS TEXTAREA ----------------
        txtInfo = new JTextArea(6, 100);
        txtInfo.setEditable(false);
        txtInfo.setForeground(Color.BLUE);
        add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        // ---------------- BUTTONS ----------------
        JPanel panel = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton history = new JButton("Customer History");
        JButton roomHistory = new JButton("Room History");
        JButton revenue = new JButton("Revenue");
        JButton export = new JButton("Export CSV");

        search = new JTextField(10);
        JButton searchBtn = new JButton("Search");

        JButton[] btns = {add, edit, delete, history, roomHistory, revenue, export, searchBtn};
        for (JButton b : btns) {
            b.setBackground(new Color(30, 144, 255));
            b.setForeground(Color.WHITE);
        }

        panel.add(add);
        panel.add(edit);
        panel.add(delete);
        panel.add(history);
        panel.add(roomHistory);
        panel.add(revenue);
        panel.add(export);
        panel.add(new JLabel("Search Room"));
        panel.add(search);
        panel.add(searchBtn);

        add(panel, BorderLayout.SOUTH);

        // ---------------- EVENT LISTENERS ----------------
        add.addActionListener(e -> openReservationDialog(null));
        edit.addActionListener(e -> editReservation());
        delete.addActionListener(e -> deleteReservation());
        searchBtn.addActionListener(e -> searchReservation());
        history.addActionListener(e -> showCustomerHistory());
        roomHistory.addActionListener(e -> showRoomHistory());
        revenue.addActionListener(e -> showRevenue());
        export.addActionListener(e -> exportCSV());

        table.getSelectionModel().addListSelectionListener(e -> showReservationDetails());

        loadTable();
        setVisible(true);
    }

    // ---------------- LOAD TABLE ----------------
    void loadTable() {
        model.setRowCount(0);
        Iterator<Reservation> it = ReservationManager.getInstance().iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            model.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getEmail(),
                    r.getRoomNo(),
                    r.getRoomType(),
                    r.getPrice(),
                    r.getCheckIn(),
                    r.getCheckOut()
            });
        }
    }

    // ---------------- SHOW DETAILS ----------------
    void showReservationDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            txtInfo.setText("");
            return;
        }

        Reservation r = ReservationManager.getInstance().getAllReservations().get(row);
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation ID: ").append(r.getId()).append("\n");
        sb.append("Customer Name: ").append(r.getName()).append("\n");
        sb.append("Email: ").append(r.getEmail()).append("\n");
        sb.append("Phone: ").append(r.getPhone()).append("\n");
        sb.append("City: ").append(r.getCity()).append(", Country: ").append(r.getCountry()).append("\n");
        sb.append("Room No: ").append(r.getRoomNo()).append(", Type: ").append(r.getRoomType()).append("\n");
        sb.append("Price: ").append(r.getPrice()).append("\n");
        sb.append("CheckIn: ").append(r.getCheckIn()).append("\n");
        sb.append("CheckOut: ").append(r.getCheckOut()).append("\n");
        txtInfo.setText(sb.toString());
    }

    // ---------------- ADD / EDIT DIALOG ----------------
    void openReservationDialog(Reservation existing) {

        JTextField id = new JTextField();
        JComboBox<Customer> customerBox = new JComboBox<>();
        JTextField gender = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField city = new JTextField();
        JTextField country = new JTextField();
        JTextField nid = new JTextField();
        JTextField consent = new JTextField();
        JComboBox<Room> roomBox = new JComboBox<>();
        JTextField type = new JTextField();
        JTextField price = new JTextField();
        JTextField in = new JTextField();
        JTextField out = new JTextField();

        // Load customers
        for (Customer c : new CustomerService().getCustomers())
            customerBox.addItem(c);

        customerBox.addActionListener(e -> {
            Customer sel = (Customer) customerBox.getSelectedItem();
            if (sel != null) {
                gender.setText(sel.getGender().toString());
                email.setText(sel.getEmail());
                phone.setText(sel.getPhone());
                city.setText(sel.getCity());
                country.setText(sel.getCountry());
                consent.setText(String.valueOf(sel.isConsent()));
            }
        });

        // Load rooms
        for (Room r : RoomManager.getInstance().getAllRooms())
            roomBox.addItem(r);

        roomBox.addActionListener(e -> {
            Room sel = (Room) roomBox.getSelectedItem();
            if (sel != null) {
                type.setText(sel.getTypeString());
                price.setText(String.valueOf(sel.getPrice()));
            }
        });

        if (existing == null)
            id.setText("" + ReservationManager.getInstance().generateNextId());
        else {
            id.setText("" + existing.getId());
            in.setText(existing.getCheckIn());
            out.setText(existing.getCheckOut());

            // Pre-select customer
            for (int i = 0; i < customerBox.getItemCount(); i++)
                if (customerBox.getItemAt(i).getEmail().equals(existing.getEmail())) {
                    customerBox.setSelectedIndex(i);
                    break;
                }

            // Pre-select room
            for (int i = 0; i < roomBox.getItemCount(); i++)
                if (roomBox.getItemAt(i).getRoomNo() == existing.getRoomNo()) {
                    roomBox.setSelectedIndex(i);
                    break;
                }
        }

        Object[] fields = {
                "ID", id,
                "Customer", customerBox,
                "Gender", gender,
                "Email", email,
                "Phone", phone,
                "City", city,
                "Country", country,
                "NID", nid,
                "Consent", consent,
                "Room", roomBox,
                "Type", type,
                "Price", price,
                "CheckIn (yyyy-MM-dd)", in,
                "CheckOut (yyyy-MM-dd)", out
        };

        int option = JOptionPane.showConfirmDialog(this, fields, existing == null ? "Add Reservation" : "Edit Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            // VALIDATION
            LocalDate checkInDate, checkOutDate;
            try {
                checkInDate = LocalDate.parse(in.getText());
                checkOutDate = LocalDate.parse(out.getText());
                if (checkOutDate.isBefore(checkInDate)) {
                    JOptionPane.showMessageDialog(this, "CheckOut must be after CheckIn");
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format");
                return;
            }

            Room room = (Room) roomBox.getSelectedItem();
            if (!ReservationManager.getInstance().isRoomAvailable(room.getRoomNo(), in.getText(), out.getText(), Integer.parseInt(id.getText()))) {
                JOptionPane.showMessageDialog(this, "Room not available for selected dates");
                return;
            }

            Customer c = (Customer) customerBox.getSelectedItem();

            Reservation res = ReservationFactory.createReservation(
                    Integer.parseInt(id.getText()),
                    c.getFullName(),
                    c.getGender().toString(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getCity(),
                    c.getCountry(),
                    nid.getText(),
                    consent.getText(),
                    room.getRoomNo(),
                    room.getTypeString(),
                    room.getPrice(),
                    in.getText(),
                    out.getText()
            );

            if (existing == null)
                ReservationManager.getInstance().addReservation(res);
            else
                ReservationManager.getInstance().updateReservation(table.getSelectedRow(), res);

            loadTable();
        }
    }

    // ---------------- EDIT / DELETE ----------------
    void editReservation() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select reservation"); return; }
        Reservation r = ReservationManager.getInstance().getAllReservations().get(row);
        openReservationDialog(r);
    }

    void deleteReservation() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select reservation"); return; }
        ReservationManager.getInstance().deleteReservation(row);
        loadTable();
    }

    // ---------------- SEARCH ----------------
    void searchReservation() {
        String key = search.getText();
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            if (String.valueOf(r.getRoomNo()).contains(key)) {
                model.addRow(new Object[]{
                        r.getId(), r.getName(), r.getEmail(),
                        r.getRoomNo(), r.getRoomType(),
                        r.getPrice(), r.getCheckIn(), r.getCheckOut()
                });
            }
        }
    }

    // ---------------- CUSTOMER / ROOM HISTORY ----------------
    void showCustomerHistory() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select reservation"); return; }
        String email = table.getValueAt(row, 2).toString();
        Iterator<Reservation> it = ReservationManager.getInstance().iteratorByCustomer(email);
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Reservation r = it.next();
            sb.append("Room ").append(r.getRoomNo()).append(" | ").append(r.getCheckIn())
                    .append(" -> ").append(r.getCheckOut()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    void showRoomHistory() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select reservation"); return; }
        int roomNo = (int) table.getValueAt(row, 3);
        Iterator<Reservation> it = ReservationManager.getInstance().iteratorByRoom(roomNo);
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Reservation r = it.next();
            sb.append(r.getName()).append(" | ").append(r.getCheckIn())
                    .append(" -> ").append(r.getCheckOut()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // ---------------- REVENUE / CSV ----------------
    void showRevenue() {
        JOptionPane.showMessageDialog(this, "Total Revenue: " + ReservationManager.getInstance().totalRevenue());
    }

    void exportCSV() {
        ReservationManager.getInstance().exportCSV();
        JOptionPane.showMessageDialog(this, "CSV Exported");
    }

    public static void main(String[] args) {
        new ReservationForm();
    }
}