import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;

public class RoomForm extends JFrame {

    private JTextField txtId, txtPrice;
    private JComboBox<RoomType> cmbType;
    private JCheckBox chkAvailable;
    private JTable table;
    private DefaultTableModel model;

    public RoomForm() {

        setTitle("Room Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        txtId = new JTextField();
        txtPrice = new JTextField();
        cmbType = new JComboBox<>(RoomType.values());
        chkAvailable = new JCheckBox("Available");

        formPanel.add(new JLabel("Room ID:"));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Type:"));
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel(""));
        formPanel.add(chkAvailable);

        add(formPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Type", "Price", "Available"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnSave = new JButton("Save");
        JButton btnShowAvailable = new JButton("Show Available");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnShowAvailable);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveRoom());
        btnShowAvailable.addActionListener(e -> showAvailableRooms());

        setVisible(true);
    }

    private void saveRoom() {
        try {
            int id = Integer.parseInt(txtId.getText());
            RoomType type = (RoomType) cmbType.getSelectedItem();
            double price = Double.parseDouble(txtPrice.getText());
            boolean available = chkAvailable.isSelected();

            Room room = RoomFactory.createRoom(id, type, price, available);
            RoomManager.getInstance().addRoom(room);

            refreshTable();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void showAvailableRooms() {

        model.setRowCount(0);
        Iterator<Room> iterator = RoomManager.getInstance().availableRoomsIterator();

        while (iterator.hasNext()) {
            Room room = iterator.next();

            model.addRow(new Object[]{
                    room.getRoomId(),
                    room.getType(),
                    room.getPrice(),
                    room.isAvailable()
            });
        }
    }

    private void refreshTable() {

        model.setRowCount(0);

        for (Room room : RoomManager.getInstance().getAllRooms()) {
            model.addRow(new Object[]{
                    room.getRoomId(),
                    room.getType(),
                    room.getPrice(),
                    room.isAvailable()
            });
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtPrice.setText("");
        chkAvailable.setSelected(false);
    }
}