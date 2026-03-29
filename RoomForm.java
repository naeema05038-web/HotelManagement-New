import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Iterator;


public class RoomForm extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextArea txtInfo;

    JButton addBtn, editBtn, deleteBtn;
    JTextField searchField;

    TableRowSorter<DefaultTableModel> sorter;

    Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);

    public RoomForm() {

        setTitle("Hotel Room Management");
        setSize(1000,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        getContentPane().setBackground(Color.BLACK);

       
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        JLabel searchLabel = new JLabel("Search Room: ");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(mainFont);

        searchField = new JTextField();
        searchField.setFont(mainFont);

        topPanel.add(searchLabel,BorderLayout.WEST);
        topPanel.add(searchField,BorderLayout.CENTER);

        add(topPanel,BorderLayout.NORTH);

       
        model = new DefaultTableModel(
                new String[]{"RoomNo","Type","Price","Floor","Capacity","Clean","Available"},0);

        table = new JTable(model);
        table.setFont(mainFont);
        table.setRowHeight(25);

        table.setBackground(new Color(30,30,30));
        table.setForeground(Color.WHITE);

        table.setSelectionBackground(new Color(0,120,215));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.BLACK);

        add(scroll,BorderLayout.CENTER);

    
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = searchField.getText();
                if(text.length()==0)
                    sorter.setRowFilter(null);
                else
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+text));
            }
        });

    
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);

        txtInfo = new JTextArea(4,80);
        txtInfo.setEditable(false);
        txtInfo.setFont(mainFont);
        txtInfo.setBackground(new Color(20,20,20));
        txtInfo.setForeground(Color.WHITE);
        txtInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Selected Room Info",
                0,0,
                new Font("Segoe UI",Font.BOLD,14),
                Color.WHITE
        ));

        bottomPanel.add(new JScrollPane(txtInfo),BorderLayout.CENTER);

    
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.BLACK);

        addBtn = new JButton("✚ Add Room");
        editBtn = new JButton("✎ Edit Room");
        deleteBtn = new JButton("🗑 Delete Room");

        styleButton(addBtn);
        styleButton(editBtn);
        styleButton(deleteBtn);

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        bottomPanel.add(btnPanel,BorderLayout.SOUTH);

        add(bottomPanel,BorderLayout.SOUTH);

    
        table.getSelectionModel().addListSelectionListener(e -> fillTextArea());

        addBtn.addActionListener(e -> openRoomDialog("Add Room",null));
        editBtn.addActionListener(e -> editSelectedRoom());
        deleteBtn.addActionListener(e -> deleteRoom());

        refreshTable();

        setVisible(true);
    }


    void styleButton(JButton b){
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(140,35));
    }


    void fillTextArea(){

        int row = table.getSelectedRow();

        if(row>=0){

            int modelRow = table.convertRowIndexToModel(row);

            txtInfo.setText(
                    "Room No: "+model.getValueAt(modelRow,0)+
                    " | Type: "+model.getValueAt(modelRow,1)+
                    " | Price: "+model.getValueAt(modelRow,2)+
                    " | Floor: "+model.getValueAt(modelRow,3)+
                    " | Capacity: "+model.getValueAt(modelRow,4)+
                    " | Clean: "+model.getValueAt(modelRow,5)+
                    " | Available: "+model.getValueAt(modelRow,6)
            );
        }
        else
            txtInfo.setText("");
    }


    void editSelectedRoom(){

        int row = table.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this,"Select a room first");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        Room r = RoomFactory.createRoom(
                Integer.parseInt(model.getValueAt(modelRow,0).toString()),
                (RoomType) model.getValueAt(modelRow,1),
                Double.parseDouble(model.getValueAt(modelRow,2).toString()),
                Integer.parseInt(model.getValueAt(modelRow,3).toString()),
                Integer.parseInt(model.getValueAt(modelRow,4).toString()),
                model.getValueAt(modelRow,5).toString(),
                Boolean.parseBoolean(model.getValueAt(modelRow,6).toString())
        );

        openRoomDialog("Edit Room",r);
    }


    void openRoomDialog(String title, Room room){

        JDialog dialog = new JDialog(this,title,true);
        dialog.setSize(450,450);
        dialog.setLayout(new GridLayout(8,2,10,10));
        dialog.getContentPane().setBackground(Color.BLACK);

        JTextField rNo = new JTextField();
        JTextField price = new JTextField();
        JTextField floor = new JTextField();
        JTextField cap = new JTextField();

        JComboBox<RoomType> type = new JComboBox<>(RoomType.values());

        JComboBox<String> clean = new JComboBox<>(new String[]{
                "Clean","Dirty","Cleaning"
        });

        JCheckBox avail = new JCheckBox("Available");
        avail.setForeground(Color.WHITE);
        avail.setBackground(Color.BLACK);

        if(room!=null){

            rNo.setText(""+room.getRoomNo());
            price.setText(""+room.getPrice());
            floor.setText(""+room.getFloor());
            cap.setText(""+room.getCapacity());

            type.setSelectedItem(room.getType());
            clean.setSelectedItem(room.getCleanStatus());
            avail.setSelected(room.isAvailable());
        }

        dialog.add(label("Room No"));
        dialog.add(rNo);

        dialog.add(label("Type"));
        dialog.add(type);

        dialog.add(label("Price"));
        dialog.add(price);

        dialog.add(label("Floor"));
        dialog.add(floor);

        dialog.add(label("Capacity"));
        dialog.add(cap);

        dialog.add(label("Clean Status"));
        dialog.add(clean);

        dialog.add(new JLabel());
        dialog.add(avail);

        JButton save = new JButton("Save");
        styleButton(save);

        dialog.add(new JLabel());
        dialog.add(save);

        save.addActionListener(e->{

            try{

                Room rNew = RoomFactory.createRoom(
                        Integer.parseInt(rNo.getText()),
                        (RoomType) type.getSelectedItem(),
                        Double.parseDouble(price.getText()),
                        Integer.parseInt(floor.getText()),
                        Integer.parseInt(cap.getText()),
                        (String) clean.getSelectedItem(),
                        avail.isSelected()
                );

                if(room==null)
                    RoomManager.getInstance().addRoom(rNew);
                else
                    RoomManager.getInstance().updateRoom(rNew);

                refreshTable();
                dialog.dispose();

            }catch(Exception ex){
                JOptionPane.showMessageDialog(dialog,"Invalid Input");
            }

        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(mainFont);
        return l;
    }


    void deleteRoom(){

        int row = table.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this,"Select a room first");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        int roomNo = Integer.parseInt(model.getValueAt(modelRow,0).toString());

        RoomManager.getInstance().deleteRoom(roomNo);

        refreshTable();
    }


    void refreshTable(){

        model.setRowCount(0);

        Iterator<Room> it = RoomManager.getInstance().availableRoomsIterator();

        while(it.hasNext()){

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
