
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class HotelMain extends JFrame {

    private HotelFacade hotelFacade;
    private CommandFactory commandFactory;
    private ButtonCommandInvoker invoker;
    private HotelCollection hotelCollection;

    JLabel gifLabel;
    JButton receiptBtn, customerBtn, roomBtn, bookingBtn, dashboardBtn;
    JTextField searchField, roomSearchField;
    JButton searchBtn, roomSearchBtn;

    public HotelMain() {
        setTitle("Hotel Management System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        hotelFacade = new HotelFacade();
        commandFactory = new CommandFactory(hotelFacade, this);
        invoker = new ButtonCommandInvoker();
        hotelCollection = new HotelCollection();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 700));
        setContentPane(layeredPane);

        JLabel bgLabel = new JLabel();
        bgLabel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(bgLabel, Integer.valueOf(0));

        ImageIcon icon = new ImageIcon("giphy (3).gif");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                Image img = icon.getImage().getScaledInstance(
                        getWidth(), getHeight(), Image.SCALE_DEFAULT);
                bgLabel.setIcon(new ImageIcon(img));
                bgLabel.setBounds(0, 0, getWidth(), getHeight());
            }
        });

        JLabel hotelName = new JLabel("GOLDEN SAND BEACH HOTEL", SwingConstants.CENTER);
        hotelName.setFont(new Font("Serif", Font.BOLD, 48));
        hotelName.setForeground(new Color(210, 180, 140));
        hotelName.setBounds(0, 20, 1200, 60);
        layeredPane.add(hotelName, Integer.valueOf(1));

        int leftX = 100;

        ImageIcon gifIcon = new ImageIcon("giphy (4).gif");
        Image gifImg = gifIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        gifLabel = new JLabel(new ImageIcon(gifImg));
        gifLabel.setBounds(leftX, 120, 150, 150);
        layeredPane.add(gifLabel, Integer.valueOf(1));

        receiptBtn = createStyledButton("RECEIPT", new Dimension(180, 50));
        receiptBtn.setBounds(leftX, 280, 180, 50);
        layeredPane.add(receiptBtn, Integer.valueOf(1));

        int startY = 350;
        int spacing = 60;

        customerBtn = createStyledButton("👤 CUSTOMER", new Dimension(180, 45));
        customerBtn.setBounds(leftX, startY, 180, 45);
        customerBtn.setVisible(false);
        layeredPane.add(customerBtn, Integer.valueOf(1));

        roomBtn = createStyledButton("🛏️ ROOM", new Dimension(180, 45));
        roomBtn.setBounds(leftX, startY + spacing, 180, 45);
        roomBtn.setVisible(false);
        layeredPane.add(roomBtn, Integer.valueOf(1));

        bookingBtn = createStyledButton("🏨 BOOKING", new Dimension(180, 45));
        bookingBtn.setBounds(leftX, startY + spacing * 2, 180, 45);
        bookingBtn.setVisible(false);
        layeredPane.add(bookingBtn, Integer.valueOf(1));

        dashboardBtn = createStyledButton("📊 DASHBOARD", new Dimension(180, 45));
        dashboardBtn.setBounds(leftX, startY + spacing * 3, 180, 45);
        dashboardBtn.setVisible(false);
        layeredPane.add(dashboardBtn, Integer.valueOf(1));

        searchField = new JTextField();
        searchField.setBounds(leftX + 190, startY, 200, 35);
        searchField.setVisible(false);
        layeredPane.add(searchField, Integer.valueOf(1));

        searchBtn = createStyledButton("SEARCH", new Dimension(100, 35));
        searchBtn.setBounds(leftX + 400, startY, 100, 35);
        searchBtn.setVisible(false);
        layeredPane.add(searchBtn, Integer.valueOf(1));

        roomSearchField = new JTextField();
        roomSearchField.setBounds(leftX + 190, startY + spacing, 200, 35);
        roomSearchField.setVisible(false);
        layeredPane.add(roomSearchField, Integer.valueOf(1));

        roomSearchBtn = createStyledButton("SEARCH", new Dimension(100, 35));
        roomSearchBtn.setBounds(leftX + 400, startY + spacing, 100, 35);
        roomSearchBtn.setVisible(false);
        layeredPane.add(roomSearchBtn, Integer.valueOf(1));

        receiptBtn.addActionListener(e -> {
            gifLabel.setVisible(false);
            receiptBtn.setVisible(false);
            customerBtn.setVisible(true);
            roomBtn.setVisible(true);
            bookingBtn.setVisible(true);
            dashboardBtn.setVisible(true);
            searchField.setVisible(true);
            searchBtn.setVisible(true);
            roomSearchField.setVisible(true);
            roomSearchBtn.setVisible(true);
        });

        customerBtn.addActionListener(e -> {
            invoker.setCommand(commandFactory.createCommand("OPEN_CUSTOMER"));
            invoker.executeCommand();
        });

        roomBtn.addActionListener(e -> {
            invoker.setCommand(commandFactory.createCommand("OPEN_ROOM"));
            invoker.executeCommand();
        });

        bookingBtn.addActionListener(e -> {
            invoker.setCommand(commandFactory.createCommand("OPEN_BOOKING"));
            invoker.executeCommand();
        });

        dashboardBtn.addActionListener(e -> {
            invoker.setCommand(commandFactory.createCommand("DASHBOARD"));
            invoker.executeCommand();
        });

        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                searchCustomerByName(searchText);
                searchField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a customer name");
            }
        });

        roomSearchBtn.addActionListener(e -> {
            String searchText = roomSearchField.getText().trim();
            if (!searchText.isEmpty()) {
                searchRoomByNumber(searchText);
                roomSearchField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a room number");
            }
        });

        loadDataIntoCollection();
        setVisible(true);
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton b = new JButton(text);
        b.setBackground(new Color(92, 64, 51));
        b.setForeground(new Color(245, 240, 220));
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setPreferredSize(size);
        return b;
    }

    private void searchCustomerByName(String name) {
        try {
            ArrayList<Customer> customers = FileUtil.load("customers.dat");
            ArrayList<Reservation> bookings = new ArrayList<>();
            try {
                bookings = FileUtil.load("bookings.dat");
            } catch (Exception e) {
            }

            Customer foundCustomer = null;
            for (Customer c : customers) {
                if (c.getFullName().toLowerCase().contains(name.toLowerCase())) {
                    foundCustomer = c;
                    break;
                }
            }

            if (foundCustomer == null) {
                JOptionPane.showMessageDialog(this, "Customer '" + name + "' not found!");
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("CUSTOMER FOUND\n");
            message.append("====================\n");
            message.append("ID: ").append(foundCustomer.getCustomerId()).append("\n");
            message.append("Name: ").append(foundCustomer.getFullName()).append("\n");
            message.append("Phone: ").append(foundCustomer.getPhone()).append("\n");
            message.append("Email: ").append(foundCustomer.getEmail()).append("\n");
            message.append("City: ").append(foundCustomer.getCity()).append("\n");
            message.append("Country: ").append(foundCustomer.getCountry()).append("\n");
            message.append("NID: ").append(foundCustomer.getNationalId()).append("\n");
            message.append("Gender: ").append(foundCustomer.getGender()).append("\n");
            message.append("Consent: ").append(foundCustomer.isConsent() ? "Yes" : "No").append("\n");

            ArrayList<Reservation> customerBookings = new ArrayList<>();
            for (Reservation b : bookings) {
                if (b.getId() == foundCustomer.getCustomerId()) {
                    customerBookings.add(b);
                }
            }

            if (customerBookings.isEmpty()) {
                message.append("\nNo bookings found for this customer.");
            } else {
                message.append("\nBOOKING HISTORY (").append(customerBookings.size()).append(" booking(s)):\n");
                message.append("────────────────────\n");
                for (int i = 0; i < customerBookings.size(); i++) {
                    Reservation b = customerBookings.get(i);
                    message.append("Booking #").append(i + 1).append(":\n");
                    message.append("  Room No: ").append(b.getRoomNo()).append("\n");
                    message.append("  Room Type: ").append(b.getRoomType()).append("\n");
                    message.append("  Price: $").append(b.getPrice()).append("\n");
                    message.append("  Check-in: ").append(b.getCheckIn()).append("\n");
                    message.append("  Check-out: ").append(b.getCheckOut()).append("\n");
                    if (i < customerBookings.size() - 1) {
                        message.append("  ────────────────────\n");
                    }
                }
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Customer Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void searchRoomByNumber(String roomNoStr) {
        try {
            int roomNo = Integer.parseInt(roomNoStr);
            ArrayList<Room> rooms = new ArrayList<>();
            try {
                rooms = FileUtil.load("rooms.dat");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No rooms found!");
                return;
            }

            Room foundRoom = null;
            for (Room r : rooms) {
                if (r.getRoomNo() == roomNo) {
                    foundRoom = r;
                    break;
                }
            }

            if (foundRoom == null) {
                JOptionPane.showMessageDialog(this, "Room " + roomNo + " not found!");
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("ROOM FOUND\n");
            message.append("================\n");
            message.append("Room Number: ").append(foundRoom.getRoomNo()).append("\n");
            message.append("Type: ").append(foundRoom.getType()).append("\n");
            message.append("Price per Night: $").append(foundRoom.getPrice()).append("\n");
            message.append("Floor: ").append(foundRoom.getFloor()).append("\n");
            message.append("Capacity: ").append(foundRoom.getCapacity()).append(" persons\n");
            message.append("Clean Status: ").append(foundRoom.getCleanStatus()).append("\n");
            message.append("Available: ").append(foundRoom.isAvailable() ? "Yes" : "No").append("\n");

            ArrayList<Reservation> reservations = ReservationManager.getInstance().getAllReservations();
            boolean hasBookings = false;
            for (Reservation r : reservations) {
                if (r.getRoomNo() == roomNo) {
                    if (!hasBookings) {
                        message.append("\nCURRENT RESERVATIONS:\n");
                        message.append("────────────────────\n");
                        hasBookings = true;
                    }
                    message.append("Guest: ").append(r.getName()).append("\n");
                    message.append("Check-in: ").append(r.getCheckIn()).append("\n");
                    message.append("Check-out: ").append(r.getCheckOut()).append("\n");
                    message.append("Total Price: $").append(r.getPrice()).append("\n");
                    message.append("────────────────────\n");
                }
            }

            if (!hasBookings) {
                message.append("\nNo current reservations for this room.");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 350));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Room Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid room number");
        }
    }

    private void loadDataIntoCollection() {
        try {
            ArrayList<Customer> customers = FileUtil.load("customers.dat");
            for (Customer c : customers) {
                hotelCollection.addCustomer(c);
            }
        } catch (Exception e) {
        }

        try {
            ArrayList<Room> rooms = FileUtil.load("rooms.dat");
            for (Room r : rooms) {
                hotelCollection.addRoom(r);
            }
        } catch (Exception e) {
        }

        for (Reservation r : ReservationManager.getInstance().getAllReservations()) {
            hotelCollection.addReservation(r);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelMain());
    }
}
