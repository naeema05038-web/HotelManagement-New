import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;

public class RoomForm extends JFrame {

    private JTextField txtRoomNo, txtPrice, txtFloor, txtCapacity, txtClean;
    private JComboBox<RoomType> cmbType;
    private JCheckBox chkAvailable;
    private JTable table;
    private DefaultTableModel model;

    public RoomForm(){

        setTitle("Hotel Room Management System");
        setSize(950,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        /* ---------- TITLE ---------- */

        JLabel title = new JLabel("Hotel Room Management System",SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(title,BorderLayout.NORTH);

        /* ---------- FORM PANEL ---------- */

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Information"));
        formPanel.setLayout(new GridLayout(7,2,10,10));

        txtRoomNo = new JTextField();
        txtPrice = new JTextField();
        txtFloor = new JTextField();
        txtCapacity = new JTextField();
        txtClean = new JTextField();

        cmbType = new JComboBox<>(RoomType.values());
        chkAvailable = new JCheckBox("Available");

        formPanel.add(new JLabel("Room Number:"));
        formPanel.add(txtRoomNo);

        formPanel.add(new JLabel("Room Type:"));
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Floor Number:"));
        formPanel.add(txtFloor);

        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(txtCapacity);

        formPanel.add(new JLabel("Clean Status:"));
        formPanel.add(txtClean);

        formPanel.add(new JLabel(""));
        formPanel.add(chkAvailable);

        add(formPanel,BorderLayout.WEST);

        /* ---------- TABLE ---------- */

        model = new DefaultTableModel(
                new String[]{"RoomNo","Type","Price","Floor","Capacity","Clean","Available"},0);

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room List"));

        add(scrollPane,BorderLayout.CENTER);

        /* ---------- BUTTON PANEL ---------- */

        JPanel btnPanel = new JPanel();

        JButton addBtn = new JButton("Add Room");
        JButton updateBtn = new JButton("Update Room");
        JButton deleteBtn = new JButton("Delete Room");
        JButton availableBtn = new JButton("Show Available");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(availableBtn);

        add(btnPanel,BorderLayout.SOUTH);

        /* ---------- BUTTON ACTIONS ---------- */

        addBtn.addActionListener(e -> addRoom());
        updateBtn.addActionListener(e -> updateRoom());
        deleteBtn.addActionListener(e -> deleteRoom());
        availableBtn.addActionListener(e -> showAvailable());

        refreshTable();

        setVisible(true);
    }

    /* ---------- ADD ROOM ---------- */

    private void addRoom(){

        try{

            int roomNo = Integer.parseInt(txtRoomNo.getText());
            RoomType type = (RoomType) cmbType.getSelectedItem();
            double price = Double.parseDouble(txtPrice.getText());
            int floor = Integer.parseInt(txtFloor.getText());
            int capacity = Integer.parseInt(txtCapacity.getText());
            String clean = txtClean.getText();
            boolean available = chkAvailable.isSelected();

            Room room = RoomFactory.createRoom(roomNo,type,price,floor,capacity,clean,available);

            RoomManager.getInstance().addRoom(room);

            refreshTable();
            clearFields();

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Please enter valid numeric values!");
        }
    }

    /* ---------- UPDATE ROOM ---------- */

    private void updateRoom(){

        int row = table.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this,"Select a room to update!");
            return;
        }

        try{

            int roomNo = Integer.parseInt(txtRoomNo.getText());
            RoomType type = (RoomType) cmbType.getSelectedItem();
            double price = Double.parseDouble(txtPrice.getText());
            int floor = Integer.parseInt(txtFloor.getText());
            int capacity = Integer.parseInt(txtCapacity.getText());
            String clean = txtClean.getText();
            boolean available = chkAvailable.isSelected();

            Room updatedRoom = RoomFactory.createRoom(roomNo,type,price,floor,capacity,clean,available);

            RoomManager.getInstance().updateRoom(updatedRoom);

            refreshTable();

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Invalid input!");
        }
    }

    /* ---------- DELETE ROOM ---------- */

    private void deleteRoom(){

        int row = table.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this,"Select a room to delete!");
            return;
        }

        int roomNo = (int) model.getValueAt(row,0);

        RoomManager.getInstance().deleteRoom(roomNo);

        refreshTable();
    }

    /* ---------- SHOW AVAILABLE ---------- */

    private void showAvailable(){

        model.setRowCount(0);

        Iterator<Room> iterator = RoomManager.getInstance().availableRoomsIterator();

        while(iterator.hasNext()){

            Room r = iterator.next();

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

    /* ---------- REFRESH TABLE ---------- */

    private void refreshTable(){

        model.setRowCount(0);

        for(Room r : RoomManager.getInstance().getAllRooms()){

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

    /* ---------- CLEAR INPUT ---------- */

    private void clearFields(){

        txtRoomNo.setText("");
        txtPrice.setText("");
        txtFloor.setText("");
        txtCapacity.setText("");
        txtClean.setText("");
        chkAvailable.setSelected(false);
    }

    public static void main(String[] args) {
        new RoomForm();
    }
}