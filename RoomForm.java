import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;

public class RoomForm extends JFrame {

    JTable table;
    DefaultTableModel model;

    JTextArea txtInfo;

    JButton addBtn, editBtn, deleteBtn;

    public RoomForm() {

        setTitle("Room Management");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table setup
        model = new DefaultTableModel(
                new String[]{"RoomNo", "Type", "Price", "Floor", "Capacity", "Clean", "Available"}, 0);
        table = new JTable(model);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(0, 200));
        add(tableScroll, BorderLayout.NORTH);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(5, 5));

        txtInfo = new JTextArea(4, 80);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtInfo.setForeground(Color.BLUE);
        txtInfo.setBorder(BorderFactory.createTitledBorder("Selected Room Info"));
        bottomPanel.add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        addBtn = new JButton("Add Room");
        addBtn.setPreferredSize(new Dimension(120, 35));
        editBtn = new JButton("Edit Room");
        editBtn.setPreferredSize(new Dimension(120, 35));
        deleteBtn = new JButton("Delete Room");
        deleteBtn.setPreferredSize(new Dimension(120, 35));

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.CENTER);

        // Listeners
        table.getSelectionModel().addListSelectionListener(e -> fillTextArea());
        addBtn.addActionListener(e -> openRoomDialog("Add Room", null));
        editBtn.addActionListener(e -> editSelectedRoom());
        deleteBtn.addActionListener(e -> deleteRoom());

        refreshTable();
        setVisible(true);
    }

    // Fill selected room info
    void fillTextArea() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String info = "Room No: " + table.getValueAt(row, 0) +
                    " | Type: " + table.getValueAt(row, 1) +
                    " | Price: " + table.getValueAt(row, 2) +
                    " | Floor: " + table.getValueAt(row, 3) +
                    " | Capacity: " + table.getValueAt(row, 4) +
                    " | Clean: " + table.getValueAt(row, 5) +
                    " | Available: " + table.getValueAt(row, 6);
            txtInfo.setText(info);
        } else {
            txtInfo.setText("");
        }
    }

    // Edit selected room
    void editSelectedRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a room first");
            return;
        }

        Room r = RoomFactory.createRoom(
                Integer.parseInt(table.getValueAt(row, 0).toString()),
                (RoomType) table.getValueAt(row, 1),
                Double.parseDouble(table.getValueAt(row, 2).toString()),
                Integer.parseInt(table.getValueAt(row, 3).toString()),
                Integer.parseInt(table.getValueAt(row, 4).toString()),
                table.getValueAt(row, 5).toString(),
                Boolean.parseBoolean(table.getValueAt(row, 6).toString())
        );

        openRoomDialog("Edit Room", r);
    }

    // Open Add/Edit Room Dialog
    void openRoomDialog(String title, Room room) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500, 520);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField rNo = new JTextField();
        JTextField price = new JTextField();
        JTextField floor = new JTextField();
        JTextField cap = new JTextField();
        JTextField clean = new JTextField();
        JComboBox<RoomType> type = new JComboBox<>(RoomType.values());
        JCheckBox avail = new JCheckBox("Available");

        if (room != null) {
            rNo.setText(String.valueOf(room.getRoomNo()));
            price.setText(String.valueOf(room.getPrice()));
            floor.setText(String.valueOf(room.getFloor()));
            cap.setText(String.valueOf(room.getCapacity()));
            clean.setText(room.getCleanStatus());
            type.setSelectedItem(room.getType());
            avail.setSelected(room.isAvailable());
        }

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Room No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; dialog.add(rNo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; dialog.add(type, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; dialog.add(price, gbc);
        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Floor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; dialog.add(floor, gbc);
        gbc.gridx = 0; gbc.gridy = 4; dialog.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; dialog.add(cap, gbc);
        gbc.gridx = 0; gbc.gridy = 5; dialog.add(new JLabel("Clean Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; dialog.add(clean, gbc);
        gbc.gridx = 0; gbc.gridy = 6; dialog.add(new JLabel(""), gbc);
        gbc.gridx = 1; gbc.gridy = 6; dialog.add(avail, gbc);

        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(130, 40));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        dialog.add(save, gbc);

        save.addActionListener(e -> {
            if (rNo.getText().isEmpty() || price.getText().isEmpty() || floor.getText().isEmpty() ||
                    cap.getText().isEmpty() || clean.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int roomNoVal = Integer.parseInt(rNo.getText());
                double priceVal = Double.parseDouble(price.getText());
                int floorVal = Integer.parseInt(floor.getText());
                int capVal = Integer.parseInt(cap.getText());

                Room rNew = RoomFactory.createRoom(
                        roomNoVal,
                        (RoomType) type.getSelectedItem(),
                        priceVal,
                        floorVal,
                        capVal,
                        clean.getText(),
                        avail.isSelected()
                );

                if (room == null) {
                    RoomManager.getInstance().addRoom(rNew);
                } else {
                    RoomManager.getInstance().updateRoom(rNew);
                }

                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Delete Room
    void deleteRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a room first");
            return;
        }

        int roomNo = Integer.parseInt(table.getValueAt(row, 0).toString());
        RoomManager.getInstance().deleteRoom(roomNo);
        refreshTable();
    }

    // Refresh Table using Iterator
    void refreshTable() {
        model.setRowCount(0);
        Iterator<Room> it = RoomManager.getInstance().availableRoomsIterator();
        while(it.hasNext()) {
            Room r = it.next();
            model.addRow(new Object[]{
                    r.getRoomNo(),
                    r.getType(),
                    r.getPrice(),
                    r.getFloor(),
                    r.getCapacity(),
                    r.getCleanStatus(),
                    r.isAvailable()
            });
        }
    }

    public static void main(String[] args) {
        new RoomForm();
    }
}