import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea txtInfo;
    JButton addBtn, editBtn, deleteBtn, refreshBtn;
    JTextField txtSearch;

    public ReservationForm() {
        setTitle("Reservation Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Gender", "Email", "Phone",
                "City", "Country", "NID", "Consent",
                "RoomNo", "RoomType", "Price", "CheckIn", "CheckOut"
        }, 0);
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(0, 250));
        add(tableScroll, BorderLayout.NORTH);


        txtInfo = new JTextArea(6, 80);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtInfo.setForeground(Color.BLUE);
        txtInfo.setBorder(BorderFactory.createTitledBorder("Selected Reservation Info"));
        add(new JScrollPane(txtInfo), BorderLayout.CENTER);


        JPanel btnPanel = new JPanel();
        addBtn = new JButton("Add Reservation");
        editBtn = new JButton("Edit Reservation");
        deleteBtn = new JButton("Delete Reservation");
        refreshBtn = new JButton("Refresh");
        txtSearch = new JTextField(10);
        JButton searchBtn = new JButton("Search");


        JButton[] buttons = {addBtn, editBtn, deleteBtn, refreshBtn, searchBtn};
        for (JButton b : buttons) {
            b.setBackground(new Color(30, 144, 255));
            b.setForeground(Color.WHITE);
            b.setPreferredSize(new Dimension(150, 35));
        }

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(new JLabel("Search Room:"));
        btnPanel.add(txtSearch);
        btnPanel.add(searchBtn);

        add(btnPanel, BorderLayout.SOUTH);


        addBtn.addActionListener(e -> openReservationDialog("Add Reservation", null));
        editBtn.addActionListener(e -> editSelectedReservation());
        deleteBtn.addActionListener(e -> deleteSelectedReservation());
        refreshBtn.addActionListener(e -> refreshTable());
        searchBtn.addActionListener(e -> searchReservation());

        table.getSelectionModel().addListSelectionListener(e -> fillTextArea());

        refreshTable();
        setVisible(true);
    }


    void refreshTable() {
        model.setRowCount(0);
        List<Reservation> list = ReservationManager.getInstance().getAllReservations();
        for (Reservation r : list) {
            model.addRow(new Object[]{
                    r.getId(), r.getName(), r.getGender(), r.getEmail(), r.getPhone(),
                    r.getCity(), r.getCountry(), r.getNid(), r.getConsent(),
                    r.getRoomNo(), r.getRoomType(), r.getPrice(), r.getCheckIn(), r.getCheckOut()
            });
        }
    }

    void fillTextArea() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtInfo.setText(
                    "ID: " + table.getValueAt(row, 0) +
                    " | Name: " + table.getValueAt(row, 1) +
                    " | Gender: " + table.getValueAt(row, 2) +
                    " | Email: " + table.getValueAt(row, 3) +
                    " | Phone: " + table.getValueAt(row, 4) +
                    " | City: " + table.getValueAt(row, 5) +
                    " | Country: " + table.getValueAt(row, 6) +
                    " | NID: " + table.getValueAt(row, 7) +
                    " | Consent: " + table.getValueAt(row, 8) +
                    " | Room No: " + table.getValueAt(row, 9) +
                    " | Room Type: " + table.getValueAt(row, 10) +
                    " | Price: " + table.getValueAt(row, 11) +
                    " | Check In: " + table.getValueAt(row, 12) +
                    " | Check Out: " + table.getValueAt(row, 13)
            );
        } else {
            txtInfo.setText("");
        }
    }


    void openReservationDialog(String title, Reservation r) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(600, 550);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField gender = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField city = new JTextField();
        JTextField country = new JTextField();
        JTextField nid = new JTextField();
        JTextField consent = new JTextField();
        JTextField roomNo = new JTextField();
        JTextField roomType = new JTextField();
        JTextField price = new JTextField();
        JTextField checkIn = new JTextField();
        JTextField checkOut = new JTextField();

        JTextField[] fields = {id, name, gender, email, phone, city, country, nid, consent,
                roomNo, roomType, price, checkIn, checkOut};

        String[] labels = {"ID","Name","Gender","Email","Phone","City","Country","NID","Consent",
                "Room No","Room Type","Price","Check In","Check Out"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            dialog.add(new JLabel(labels[i]+":"), gbc);
            gbc.gridx = 1; gbc.gridy = i;
            dialog.add(fields[i], gbc);
        }

        if (r != null) {
            id.setText(String.valueOf(r.getId()));
            name.setText(r.getName());
            gender.setText(r.getGender());
            email.setText(r.getEmail());
            phone.setText(r.getPhone());
            city.setText(r.getCity());
            country.setText(r.getCountry());
            nid.setText(r.getNid());
            consent.setText(r.getConsent());
            roomNo.setText(String.valueOf(r.getRoomNo()));
            roomType.setText(r.getRoomType());
            price.setText(String.valueOf(r.getPrice()));
            checkIn.setText(r.getCheckIn());
            checkOut.setText(r.getCheckOut());
        }

        JButton save = new JButton("Save");
        save.setBackground(new Color(30, 144, 255));
        save.setForeground(Color.WHITE);
        save.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        dialog.add(save, gbc);

        save.addActionListener(e -> {
            try {
                for (JTextField f : fields) {
                    if (f.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "All fields must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                Reservation res = ReservationFactory.createReservation(
                        Integer.parseInt(id.getText()),
                        name.getText(),
                        gender.getText(),
                        email.getText(),
                        phone.getText(),
                        city.getText(),
                        country.getText(),
                        nid.getText(),
                        consent.getText(),
                        Integer.parseInt(roomNo.getText()),
                        roomType.getText(),
                        Double.parseDouble(price.getText()),
                        checkIn.getText(),
                        checkOut.getText()
                );

                if (r == null) {
                    ReservationManager.getInstance().addReservation(res);
                } else {
                    ReservationManager.getInstance().updateReservation(table.getSelectedRow(), res);
                }

                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    void editSelectedReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation!");
            return;
        }
        Reservation r = ReservationManager.getInstance().getAllReservations().get(row);
        openReservationDialog("Edit Reservation", r);
    }

    void deleteSelectedReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation!");
            return;
        }
        ReservationManager.getInstance().deleteReservation(row);
        refreshTable();
    }

    void searchReservation() {
        String key = txtSearch.getText();
        model.setRowCount(0);
        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            if (String.valueOf(r.getRoomNo()).contains(key)) {
                model.addRow(new Object[]{
                        r.getId(), r.getName(), r.getGender(), r.getEmail(), r.getPhone(),
                        r.getCity(), r.getCountry(), r.getNid(), r.getConsent(),
                        r.getRoomNo(), r.getRoomType(), r.getPrice(), r.getCheckIn(), r.getCheckOut()
                });
            }
        }
    }

    public static void main(String[] args) {
        new ReservationForm();
    }
}
