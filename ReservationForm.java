import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Main Reservation Form
public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea txtInfo;
    JTextField txtSearch;

    JButton addBtn, editBtn, deleteBtn, refreshBtn, historyBtn, roomHistoryBtn, revenueBtn, exportBtn;

    public ReservationForm() {
        setTitle("Hotel Reservation System");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Gender", "Email", "Phone", "City", "Country",
                "NID", "Consent", "Room", "Type", "Price", "CheckIn", "CheckOut"}, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.NORTH);

        txtInfo = new JTextArea(6, 80);
        txtInfo.setEditable(false);
        txtInfo.setForeground(Color.BLUE);
        add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");
        historyBtn = new JButton("Customer History");
        roomHistoryBtn = new JButton("Room History");
        revenueBtn = new JButton("Total Revenue");
        exportBtn = new JButton("Export CSV");
        txtSearch = new JTextField(8);
        JButton searchBtn = new JButton("Search");

        JButton[] buttons = {addBtn, editBtn, deleteBtn, refreshBtn,
                historyBtn, roomHistoryBtn, revenueBtn, exportBtn, searchBtn};

        for (JButton b : buttons) {
            b.setBackground(new Color(30, 144, 255));
            b.setForeground(Color.WHITE);
        }

        panel.add(addBtn); panel.add(editBtn); panel.add(deleteBtn); panel.add(refreshBtn);
        panel.add(historyBtn); panel.add(roomHistoryBtn); panel.add(revenueBtn); panel.add(exportBtn);
        panel.add(new JLabel("Search Room:")); panel.add(txtSearch); panel.add(searchBtn);

        add(panel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openDialog(null));
        editBtn.addActionListener(e -> editReservation());
        deleteBtn.addActionListener(e -> deleteReservation());
        refreshBtn.addActionListener(e -> refreshTable());
        searchBtn.addActionListener(e -> search());
        historyBtn.addActionListener(e -> showCustomerHistory());
        roomHistoryBtn.addActionListener(e -> showRoomHistory());
        revenueBtn.addActionListener(e -> showRevenue());

        table.getSelectionModel().addListSelectionListener(e -> showInfo());

        refreshTable();
        setVisible(true);
    }

    void refreshTable() {
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            model.addRow(new Object[]{
                    r.getId(), r.getName(), r.getGender(), r.getEmail(),
                    r.getPhone(), r.getCity(), r.getCountry(),
                    r.getNid(), r.getConsent(), r.getRoomNo(),
                    r.getRoomType(), r.getPrice(),
                    r.getCheckIn(), r.getCheckOut()
            });
        }
    }

    void showInfo() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        txtInfo.setText("Customer: " + table.getValueAt(row, 1)
                + " | Room: " + table.getValueAt(row, 9)
                + " | CheckIn: " + table.getValueAt(row, 12)
                + " | CheckOut: " + table.getValueAt(row, 13));
    }

    void showCustomerHistory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select customer first");
            return;
        }
        String email = table.getValueAt(row, 3).toString();
        List<Reservation> list = ReservationManager.getInstance().getCustomerHistory(email);

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history for this customer");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Customer History for ").append(table.getValueAt(row, 1)).append(":\n\n");
        for (Reservation r : list) {
            sb.append("Room ").append(r.getRoomNo())
              .append(" | Type: ").append(r.getRoomType())
              .append(" | Price: ").append(r.getPrice())
              .append(" | CheckIn: ").append(r.getCheckIn())
              .append(" | CheckOut: ").append(r.getCheckOut())
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    void showRoomHistory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select room first");
            return;
        }
        int roomNo = (int) table.getValueAt(row, 9);
        List<Reservation> list = ReservationManager.getInstance().getRoomHistory(roomNo);

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history for this room");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Room History for Room ").append(roomNo).append(":\n\n");
        for (Reservation r : list) {
            sb.append("Customer: ").append(r.getName())
              .append(" | Email: ").append(r.getEmail())
              .append(" | Phone: ").append(r.getPhone())
              .append(" | CheckIn: ").append(r.getCheckIn())
              .append(" | CheckOut: ").append(r.getCheckOut())
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    void showRevenue() {
        double total = ReservationManager.getInstance().totalRevenue();
        JOptionPane.showMessageDialog(this, "Total Revenue: " + total);
    }

    void exportCSV() {
        ReservationManager.getInstance().exportCSV();
        JOptionPane.showMessageDialog(this, "File exported: reservations.csv");
    }

    void search() {
        String key = txtSearch.getText();
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            if (String.valueOf(r.getRoomNo()).contains(key)) {
                model.addRow(new Object[]{
                        r.getId(), r.getName(), r.getGender(), r.getEmail(),
                        r.getPhone(), r.getCity(), r.getCountry(),
                        r.getNid(), r.getConsent(), r.getRoomNo(),
                        r.getRoomType(), r.getPrice(),
                        r.getCheckIn(), r.getCheckOut()
                });
            }
        }
    }

    void deleteReservation() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        ReservationManager.getInstance().deleteReservation(row);
        refreshTable();
    }

    void editReservation() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Reservation r = ReservationManager.getInstance().getAllReservations().get(row);
        openDialog(r);
    }

    void openDialog(Reservation r) {
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
        for (Customer c : CustomerServiceHolder.getInstance().getCustomers()) {
            customerBox.addItem(c);
        }
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

        // Load available rooms
        for (Room room : RoomManager.getInstance().getAllRooms()) {
            roomBox.addItem(room);
        }
        roomBox.addActionListener(e -> {
            Room sel = (Room) roomBox.getSelectedItem();
            if (sel != null) {
                type.setText(sel.getTypeString());
                price.setText("" + sel.getPrice());
            }
        });

        if (r == null) id.setText("" + ReservationManager.getInstance().generateNextId());
        else {
            id.setText("" + r.getId());
            // Pre-select customer
            for (int i = 0; i < customerBox.getItemCount(); i++) {
                if (customerBox.getItemAt(i).getEmail().equals(r.getEmail())) {
                    customerBox.setSelectedIndex(i); break;
                }
            }
            gender.setText(r.getGender());
            email.setText(r.getEmail());
            phone.setText(r.getPhone());
            city.setText(r.getCity());
            country.setText(r.getCountry());
            nid.setText(r.getNid());
            consent.setText(r.getConsent());
            in.setText(r.getCheckIn());
            out.setText(r.getCheckOut());

            // Pre-select room
            for (int i = 0; i < roomBox.getItemCount(); i++) {
                if (roomBox.getItemAt(i).getRoomNo() == r.getRoomNo()) {
                    roomBox.setSelectedIndex(i); break;
                }
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
                "CheckIn", in,
                "CheckOut", out
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Customer c = (Customer) customerBox.getSelectedItem();
            Room room = (Room) roomBox.getSelectedItem();
            if (c == null || room == null) { JOptionPane.showMessageDialog(this, "Select customer and room"); return; }

            Reservation res = new Reservation(
                    Integer.parseInt(id.getText()), c.getFullName(), c.getGender().toString(),
                    c.getEmail(), c.getPhone(), c.getCity(), c.getCountry(),
                    nid.getText(), consent.getText(),
                    room.getRoomNo(), room.getTypeString(), room.getPrice(),
                    in.getText(), out.getText()
            );

            if (r == null) ReservationManager.getInstance().addReservation(res);
            else ReservationManager.getInstance().updateReservation(table.getSelectedRow(), res);

            refreshTable();
        }
    }

    public static void main(String[] args) {
        new ReservationForm();
    }
}