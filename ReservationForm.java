import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservationForm extends JFrame {

    JTextField txtId, txtCustomerId, txtRoomId, txtCheckIn, txtCheckOut, txtSearch;

    JTable table;
    DefaultTableModel model;

    public ReservationForm(){

        setTitle("Reservation Management");
        setSize(950,550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Reservation Management System",SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,22));

        add(title,BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5,2,10,10));
        form.setBorder(BorderFactory.createTitledBorder("Reservation Information"));

        txtId = new JTextField();
        txtCustomerId = new JTextField();
        txtRoomId = new JTextField();
        txtCheckIn = new JTextField();
        txtCheckOut = new JTextField();

        form.add(new JLabel("Reservation ID"));
        form.add(txtId);

        form.add(new JLabel("Customer ID"));
        form.add(txtCustomerId);

        form.add(new JLabel("Room ID"));
        form.add(txtRoomId);

        form.add(new JLabel("Check-In (yyyy-mm-dd)"));
        form.add(txtCheckIn);

        form.add(new JLabel("Check-Out (yyyy-mm-dd)"));
        form.add(txtCheckOut);

        add(form,BorderLayout.WEST);

        model = new DefaultTableModel(
                new String[]{"ResID","CustomerID","RoomID","CheckIn","CheckOut"},0
        );

        table = new JTable(model);

        add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        txtSearch = new JTextField(10);
        JButton btnSearch = new JButton("Search");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        btnPanel.add(new JLabel("Search Room ID"));
        btnPanel.add(txtSearch);
        btnPanel.add(btnSearch);

        add(btnPanel,BorderLayout.SOUTH);

        btnAdd.addActionListener(e->addReservation());
        btnUpdate.addActionListener(e->updateReservation());
        btnDelete.addActionListener(e->deleteReservation());
        btnRefresh.addActionListener(e->refreshTable());
        btnSearch.addActionListener(e->searchReservation());

        table.getSelectionModel().addListSelectionListener(e->fillFields());

        refreshTable();

        setVisible(true);
    }

    private void addReservation(){

        try{

            int id = Integer.parseInt(txtId.getText());
            int customerId = Integer.parseInt(txtCustomerId.getText());
            int roomId = Integer.parseInt(txtRoomId.getText());

            String checkIn = txtCheckIn.getText();
            String checkOut = txtCheckOut.getText();

            Reservation r = ReservationFactory.createReservation(id,customerId,roomId,checkIn,checkOut);

            ReservationManager.getInstance().addReservation(r);

            refreshTable();
            clearFields();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Invalid Input!");
        }

    }

    private void updateReservation(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a reservation!");
            return;
        }

        try{

            int id = Integer.parseInt(txtId.getText());
            int customerId = Integer.parseInt(txtCustomerId.getText());
            int roomId = Integer.parseInt(txtRoomId.getText());

            String checkIn = txtCheckIn.getText();
            String checkOut = txtCheckOut.getText();

            Reservation r = ReservationFactory.createReservation(id,customerId,roomId,checkIn,checkOut);

            ReservationManager.getInstance().updateReservation(row,r);

            refreshTable();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Invalid Input!");
        }

    }

    private void deleteReservation(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select reservation to delete");
            return;
        }

        ReservationManager.getInstance().deleteReservation(row);

        refreshTable();
    }

    private void refreshTable(){

        model.setRowCount(0);

        for(Reservation r : ReservationManager.getInstance().getAllReservations()){

            model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getCustomerId(),
                    r.getRoomId(),
                    r.getCheckIn(),
                    r.getCheckOut()
            });

        }

    }

    private void searchReservation(){

        String key = txtSearch.getText();

        model.setRowCount(0);

        for(Reservation r : ReservationManager.getInstance().getAllReservations()){

            if(String.valueOf(r.getRoomId()).contains(key)){

                model.addRow(new Object[]{
                        r.getReservationId(),
                        r.getCustomerId(),
                        r.getRoomId(),
                        r.getCheckIn(),
                        r.getCheckOut()
                });

            }

        }

    }

    private void fillFields(){

        int row = table.getSelectedRow();

        if(row >= 0){

            Reservation r = ReservationManager.getInstance().getAllReservations().get(row);

            txtId.setText(String.valueOf(r.getReservationId()));
            txtCustomerId.setText(String.valueOf(r.getCustomerId()));
            txtRoomId.setText(String.valueOf(r.getRoomId()));
            txtCheckIn.setText(r.getCheckIn());
            txtCheckOut.setText(r.getCheckOut());

        }

    }

    private void clearFields(){

        txtId.setText("");
        txtCustomerId.setText("");
        txtRoomId.setText("");
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        table.clearSelection();

    }

    public static void main(String[] args) {
        new ReservationForm();
    }
}