
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea details;
    JTextField search;


    int generateNextId() {
    int max = 0;
    for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
        if (r.getId() > max) max = r.getId();
    }
    return max + 1;
}

    Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);

    CustomerService customerService = new CustomerService();
    RoomManager roomManager = RoomManager.getInstance();

    public ReservationForm() {
        setTitle("Hotel Reservation Management");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new String[]{"ID", "Customer", "Gender", "Email", "Phone", "City", "Country", "RoomNo", "Type", "Price/Day", "Total Price", "CheckIn", "CheckOut"}, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(mainFont);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(0, 120, 215));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Reservation r = ReservationManager.getInstance().getAllReservations().get(table.convertRowIndexToModel(row));
                LocalDate checkout = LocalDate.parse(r.getCheckOut());
                if (!isSelected) {
                    if (checkout.isBefore(LocalDate.now()))
                        c.setBackground(Color.DARK_GRAY);
                    else
                        c.setBackground(row % 2 == 0 ? new Color(30, 30, 30) : Color.BLACK);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(0, 120, 215));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Reservations", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        add(tableScroll, BorderLayout.CENTER);

        details = new JTextArea();
        details.setEditable(false);
        details.setBackground(Color.BLACK);
        details.setForeground(Color.WHITE);
        details.setFont(new Font("Consolas", Font.PLAIN, 14));
        details.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Reservation Details", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        JScrollPane detailScroll = new JScrollPane(details);
        detailScroll.setPreferredSize(new Dimension(320, 0));
        add(detailScroll, BorderLayout.EAST);

        table.getSelectionModel().addListSelectionListener(e -> showDetails());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottom.setBackground(Color.BLACK);

        JButton addBtn = new JButton("✚ Add");
        JButton editBtn = new JButton("✎ Edit");
        JButton deleteBtn = new JButton("🗑 Delete");
        JButton customerHistoryBtn = new JButton("Customer History");
        JButton roomHistoryBtn = new JButton("Room History");
        JButton revenueBtn = new JButton("Revenue");
        JButton exportBtn = new JButton("Export CSV");

        search = new JTextField(12);
        search.setBackground(new Color(30, 30, 30));
        search.setForeground(Color.WHITE);
        search.setCaretColor(Color.WHITE);
        JButton searchBtn = new JButton("Search");

        JButton[] btns = {addBtn, editBtn, deleteBtn, customerHistoryBtn, roomHistoryBtn, revenueBtn, exportBtn, searchBtn};
        for (JButton b : btns) {
            b.setBackground(Color.WHITE);
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial", Font.BOLD, 12));
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.LIGHT_GRAY);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.WHITE);
                }
            });
        }

        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        bottom.add(customerHistoryBtn);
        bottom.add(roomHistoryBtn);
        bottom.add(revenueBtn);
        bottom.add(exportBtn);
        bottom.add(new JLabel("Search Room:") {{
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
        }});
        bottom.add(search);
        bottom.add(searchBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openReservationPopup(null));
        editBtn.addActionListener(e -> editReservation());
        deleteBtn.addActionListener(e -> deleteReservation());
        searchBtn.addActionListener(e -> search());
        customerHistoryBtn.addActionListener(e -> showCustomerHistory());
        roomHistoryBtn.addActionListener(e -> showRoomHistory());
        revenueBtn.addActionListener(e -> showRevenue());
        exportBtn.addActionListener(e -> exportCSV());

        loadTable();
        setVisible(true);
    }

    

    void loadTable() {
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            long days = ChronoUnit.DAYS.between(LocalDate.parse(r.getCheckIn()), LocalDate.parse(r.getCheckOut()));
            double pricePerDay = r.getPrice() / days;
            model.addRow(new Object[]{
                    r.getId(), r.getName(), r.getGender(), r.getEmail(), r.getPhone(), r.getCity(), r.getCountry(),
                    r.getRoomNo(), r.getRoomType(), pricePerDay, r.getPrice(), r.getCheckIn(), r.getCheckOut()
            });
        }
    }

    void showDetails() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Reservation r = ReservationManager.getInstance().getAllReservations().get(table.convertRowIndexToModel(row));
        long days = ChronoUnit.DAYS.between(LocalDate.parse(r.getCheckIn()), LocalDate.parse(r.getCheckOut()));
        double pricePerDay = r.getPrice() / days;

        details.setText(
                "Reservation ID: " + r.getId() +
                        "\nCustomer Name: " + r.getName() +
                        "\nGender: " + r.getGender() +
                        "\nEmail: " + r.getEmail() +
                        "\nPhone: " + r.getPhone() +
                        "\nCity: " + r.getCity() +
                        "\nCountry: " + r.getCountry() +
                        "\n\nRoom Number: " + r.getRoomNo() +
                        "\nRoom Type: " + r.getRoomType() +
                        "\n\nCheck In: " + r.getCheckIn() +
                        "\nCheck Out: " + r.getCheckOut() +
                        "\nDays Stayed: " + days +
                        "\nPrice Per Day: " + pricePerDay +
                        "\nTotal Price: " + r.getPrice()
        );
    }


    void openReservationPopup(Reservation existing) {

        JDialog dialog = new JDialog(this, existing == null ? "Add Reservation" : "Edit Reservation", true);
        dialog.setSize(500, 550);
        dialog.setLayout(new GridLayout(12, 2, 5, 5));
        dialog.getContentPane().setBackground(Color.BLACK);

        JComboBox<String> customerBox = new JComboBox<>();
        JTextField nameField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField countryField = new JTextField();

        JComboBox<Integer> roomBox = new JComboBox<>();
        JTextField roomTypeField = new JTextField();
        JTextField priceField = new JTextField();

        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();

        for (Customer c : customerService.getCustomers()) {
            customerBox.addItem(c.getFullName());
        }
for (Room room : roomManager.getAllRooms()) {

    boolean isAvailable = true;

    for (Reservation r : ReservationManager.getInstance().getAllReservations()) {

        if (existing != null && r.getId() == existing.getId()) continue;

        if (r.getRoomNo() == room.getRoomNo()) {

            try {
                LocalDate newIn = LocalDate.parse(checkInField.getText());
                LocalDate newOut = LocalDate.parse(checkOutField.getText());

                LocalDate oldIn = LocalDate.parse(r.getCheckIn());
                LocalDate oldOut = LocalDate.parse(r.getCheckOut());

                if (!(newOut.isBefore(oldIn) || newIn.isAfter(oldOut))) {
                    isAvailable = false;
                    break;
                }

            } catch (Exception e) {
                isAvailable = false;
            }
        }
    }

    if (isAvailable) {
        roomBox.addItem(room.getRoomNo());
    }
}

        dialog.add(new JLabel("Customer Name") {{ setForeground(Color.WHITE); }}); dialog.add(customerBox);
        dialog.add(new JLabel("Gender") {{ setForeground(Color.WHITE); }}); dialog.add(genderField);
        dialog.add(new JLabel("Email") {{ setForeground(Color.WHITE); }}); dialog.add(emailField);
        dialog.add(new JLabel("Phone") {{ setForeground(Color.WHITE); }}); dialog.add(phoneField);
        dialog.add(new JLabel("City") {{ setForeground(Color.WHITE); }}); dialog.add(cityField);
        dialog.add(new JLabel("Country") {{ setForeground(Color.WHITE); }}); dialog.add(countryField);
        dialog.add(new JLabel("Room Number") {{ setForeground(Color.WHITE); }}); dialog.add(roomBox);
        dialog.add(new JLabel("Room Type") {{ setForeground(Color.WHITE); }}); dialog.add(roomTypeField);
        dialog.add(new JLabel("Price per Day") {{ setForeground(Color.WHITE); }}); dialog.add(priceField);
        dialog.add(new JLabel("Check In yyyy-mm-dd") {{ setForeground(Color.WHITE); }}); dialog.add(checkInField);
        dialog.add(new JLabel("Check Out yyyy-mm-dd") {{ setForeground(Color.WHITE); }}); dialog.add(checkOutField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(Color.WHITE);
        saveBtn.setForeground(Color.BLACK);
        dialog.add(new JLabel());
        dialog.add(saveBtn);

 
        if (existing != null) {
            nameField.setText(existing.getName());
            genderField.setText(existing.getGender());
            emailField.setText(existing.getEmail());
            phoneField.setText(existing.getPhone());
            cityField.setText(existing.getCity());
            countryField.setText(existing.getCountry());
            roomBox.setSelectedItem(existing.getRoomNo());
            roomTypeField.setText(existing.getRoomType());

            long days = ChronoUnit.DAYS.between(LocalDate.parse(existing.getCheckIn()), LocalDate.parse(existing.getCheckOut()));
            priceField.setText(String.valueOf(existing.getPrice() / days));

            checkInField.setText(existing.getCheckIn());
            checkOutField.setText(existing.getCheckOut());
            customerBox.setSelectedItem(existing.getName());
        }



    
customerBox.addActionListener(e -> {
    String cName = (String) customerBox.getSelectedItem();
    Customer selected = null;
    for (Customer c : customerService.getCustomers()) {
        if (c.getFullName().equals(cName)) {
            selected = c;
            break;
        }
    }
    if (selected != null) {
        nameField.setText(selected.getFullName());
        genderField.setText(selected.getGender().toString());
        emailField.setText(selected.getEmail());
        phoneField.setText(selected.getPhone());
        cityField.setText(selected.getCity());
        countryField.setText(selected.getCountry());
    }
});


    roomBox.addActionListener(e -> {
    Integer rId = (Integer) roomBox.getSelectedItem();
    Room selectedRoom = null;
    for (Room r : roomManager.getAllRooms()) {
        if (r.getRoomNo() == rId) {
            selectedRoom = r;
            break;
        }
    }
    if (selectedRoom != null) {
        roomTypeField.setText(selectedRoom.getType().toString());
        priceField.setText(String.valueOf(selectedRoom.getPrice()));
    }
});

        saveBtn.addActionListener(e -> {
            try {
                int id = existing == null ? generateNextId() : existing.getId();

                long days = ChronoUnit.DAYS.between(LocalDate.parse(checkInField.getText()), LocalDate.parse(checkOutField.getText()));
                double total = Double.parseDouble(priceField.getText()) * days;

                if (roomBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "No room available");
                    return;
}
                Reservation r = new Reservation(
                        id, nameField.getText(), genderField.getText(), emailField.getText(),
                        phoneField.getText(), cityField.getText(), countryField.getText(),
                        "", "", (Integer) roomBox.getSelectedItem(), roomTypeField.getText(),
                        total, checkInField.getText(), checkOutField.getText()
                );

                if (existing == null)
                    ReservationManager.getInstance().addReservation(r);
                else
                    ReservationManager.getInstance().updateReservation(
                            ReservationManager.getInstance().getAllReservations().indexOf(existing), r);

                loadTable();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input");
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    void editReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select reservation to edit");
            return;
        }
        Reservation r = ReservationManager.getInstance().getAllReservations().get(table.convertRowIndexToModel(row));
        openReservationPopup(r);
    }

    void deleteReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select reservation to delete");
            return;
        }
        ReservationManager.getInstance().deleteReservation(table.convertRowIndexToModel(row));
        loadTable();
    }

    void search() {
        String key = search.getText();
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            if (String.valueOf(r.getRoomNo()).contains(key) || r.getName().toLowerCase().contains(key.toLowerCase())) {
                long days = ChronoUnit.DAYS.between(LocalDate.parse(r.getCheckIn()), LocalDate.parse(r.getCheckOut()));
                double pricePerDay = r.getPrice() / days;
                model.addRow(new Object[]{
                        r.getId(), r.getName(), r.getGender(), r.getEmail(), r.getPhone(), r.getCity(), r.getCountry(),
                        r.getRoomNo(), r.getRoomType(), pricePerDay, r.getPrice(), r.getCheckIn(), r.getCheckOut()
                });
            }
        }
    }

    void showCustomerHistory() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select reservation");
            return;
        }
        String name = table.getValueAt(row, 1).toString();
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) if (r.getName().equalsIgnoreCase(name)) list.add(r);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        StringBuilder sb = new StringBuilder();
        for (Reservation r : list) {
            sb.append("Room ").append(r.getRoomNo()).append(" | ").append(r.getRoomType()).append(" | ")
                    .append(r.getCheckIn()).append(" -> ").append(r.getCheckOut()).append(" | Price: ").append(r.getPrice()).append("\n");
        }
        area.setText(sb.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Customer History: " + name, JOptionPane.INFORMATION_MESSAGE);
    }

    void showRoomHistory() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select reservation");
            return;
        }
        int roomNo = Integer.parseInt(table.getValueAt(row, 7).toString());
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) if (r.getRoomNo() == roomNo) list.add(r);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        StringBuilder sb = new StringBuilder();
        for (Reservation r : list) {
            sb.append(r.getName()).append(" | ").append(r.getCheckIn()).append(" -> ").append(r.getCheckOut()).append(" | Price: ").append(r.getPrice()).append("\n");
        }
        area.setText(sb.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Room History: " + roomNo, JOptionPane.INFORMATION_MESSAGE);
    }

    void showRevenue() {
        double total = 0;
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) total += r.getPrice();
        JOptionPane.showMessageDialog(this, "Total Revenue: " + total);
    }

    void exportCSV() {
        ReservationManager.getInstance().exportCSV();
        JOptionPane.showMessageDialog(this, "CSV Exported");
    }

    public static void main(String[] args) {
        new ReservationForm();
    }
}