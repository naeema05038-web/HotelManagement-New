
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CustomerForm extends JFrame {

    private JTextField nameField, phoneField, emailField, cityField, countryField, nidField, txtSearch;
    private JComboBox<String> genderBox;
    private JCheckBox consentBox;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerService service = new CustomerService();

    public CustomerForm() {
        setTitle("Customer Management System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.WHITE, 3),
                "Customer Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));
        inputPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = createWhiteTextField();
        phoneField = createWhiteTextField();
        emailField = createWhiteTextField();
        cityField = createWhiteTextField();
        countryField = createWhiteTextField();
        nidField = createWhiteTextField();

        genderBox = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
        genderBox.setBackground(Color.WHITE);
        genderBox.setForeground(Color.BLACK);
        genderBox.setBorder(new LineBorder(Color.BLACK, 2));
        genderBox.setFont(new Font("Arial", Font.BOLD, 14));
        genderBox.setPreferredSize(new Dimension(130, 35));

        consentBox = new JCheckBox("Consent");
        consentBox.setBackground(Color.BLACK);
        consentBox.setForeground(Color.WHITE);
        consentBox.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel nameLabel = createStyledLabel("Name:");
        JLabel phoneLabel = createStyledLabel("Phone:");
        JLabel emailLabel = createStyledLabel("Email:");
        JLabel cityLabel = createStyledLabel("City:");
        JLabel countryLabel = createStyledLabel("Country:");
        JLabel nidLabel = createStyledLabel("NID:");
        JLabel genderLabel = createStyledLabel("Gender/Consent:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.15;
        inputPanel.add(phoneLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.35;
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.15;
        inputPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.15;
        inputPanel.add(cityLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.35;
        inputPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.15;
        inputPanel.add(countryLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        inputPanel.add(countryField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.15;
        inputPanel.add(nidLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.35;
        inputPanel.add(nidField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.15;
        inputPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;
        inputPanel.add(genderBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        inputPanel.add(consentBox, gbc);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Phone", "Email", "City", "Country", "NID", "Gender", "Consent"};
        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel);
        customerTable.setBackground(Color.BLACK);
        customerTable.setForeground(Color.WHITE);
        customerTable.setGridColor(Color.WHITE);
        customerTable.setBorder(new LineBorder(Color.WHITE, 2));
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.setRowHeight(35);
        customerTable.setSelectionBackground(Color.DARK_GRAY);
        customerTable.setSelectionForeground(Color.WHITE);
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        customerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(6).setPreferredWidth(130);
        customerTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        customerTable.getColumnModel().getColumn(8).setPreferredWidth(80);

        JTableHeader header = customerTable.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 15));
        ((JLabel) header.getDefaultRenderer()).setBorder(BorderFactory.createLineBorder(Color.WHITE));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(new LineBorder(Color.WHITE, 3));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.WHITE, 3),
                "Actions",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

        JButton btnAdd = createStyledButton("ADD", new Dimension(100, 45));
        JButton btnEdit = createStyledButton("EDIT", new Dimension(100, 45));
        JButton btnDelete = createStyledButton("DELETE", new Dimension(100, 45));
        JButton btnClear = createStyledButton("CLEAR", new Dimension(100, 45));
        JButton btnClearAll = createStyledButton("CLEAR ALL", new Dimension(120, 45));

        txtSearch = createWhiteTextField();
        txtSearch.setPreferredSize(new Dimension(160, 40));

        JButton btnSearch = createStyledButton("SEARCH", new Dimension(120, 45));

        JLabel searchLabel = new JLabel("Search Name:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 15));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClearAll);
        buttonPanel.add(searchLabel);
        buttonPanel.add(txtSearch);
        buttonPanel.add(btnSearch);

        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> saveCustomer());
        btnEdit.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());
        btnClearAll.addActionListener(e -> clearAllData());
        btnSearch.addActionListener(e -> searchCustomerByName()); // CHANGED: searchCustomerById to searchCustomerByName

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromTable();
            }
        });

        getContentPane().setBackground(Color.BLACK);

        loadTableData();
        setVisible(true);
    }

    private JTextField createWhiteTextField() {
        JTextField field = new JTextField();
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setBorder(new LineBorder(Color.BLACK, 2));
        field.setCaretColor(Color.BLACK);
        field.setFont(new Font("Arial", Font.BOLD, 14));
        field.setPreferredSize(new Dimension(180, 35));
        return field;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(120, 30));
        return label;
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(Color.BLACK, 2));
        button.setPreferredSize(size);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void saveCustomer() {
        try {
            Customer c = CustomerFactory.createCustomer(
                    nameField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    cityField.getText().trim(),
                    countryField.getText().trim(),
                    nidField.getText().trim(),
                    consentBox.isSelected(),
                    getGenderFromCombo()
            );
            service.addCustomer(c);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        int row = customerTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update");
            return;
        }

        try {
            Customer c = service.getCustomers().get(row);
            c.setFullName(nameField.getText().trim());
            c.setPhone(phoneField.getText().trim());
            c.setEmail(emailField.getText().trim());
            c.setCity(cityField.getText().trim());
            c.setCountry(countryField.getText().trim());
            c.setNationalId(nidField.getText().trim());
            c.setConsent(consentBox.isSelected());
            c.setGender(getGenderFromCombo());

            service.updateCustomer(row, c);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this customer?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteCustomer(row);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        cityField.setText("");
        countryField.setText("");
        nidField.setText("");
        consentBox.setSelected(false);
        genderBox.setSelectedIndex(0);
        txtSearch.setText("");
        customerTable.clearSelection();
    }

    private void clearAllData() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to DELETE ALL customers?",
                "Confirm Delete All",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.clearAllData();
            loadTableData();
            JOptionPane.showMessageDialog(this, "All customers deleted!");
        }
    }

    private void fillFieldsFromTable() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            Customer c = service.getCustomers().get(row);
            nameField.setText(c.getFullName());
            phoneField.setText(c.getPhone());
            emailField.setText(c.getEmail());
            cityField.setText(c.getCity());
            countryField.setText(c.getCountry());
            nidField.setText(c.getNationalId());
            consentBox.setSelected(c.isConsent());

            String genderStr = c.getGender().toString();
            for (int i = 0; i < genderBox.getItemCount(); i++) {
                if (genderBox.getItemAt(i).equals(genderStr)) {
                    genderBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<Customer> list = service.getCustomers();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getFullName(),
                c.getPhone(),
                c.getEmail(),
                c.getCity(),
                c.getCountry(),
                c.getNationalId(),
                c.getGender(),
                c.isConsent()
            });
        }
    }

    private void searchCustomerByName() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        if (searchText.isEmpty()) {
            loadTableData();
            return;
        }

        boolean found = false;
        ArrayList<Customer> list = service.getCustomers();

        for (Customer c : list) {
            if (c.getFullName().toLowerCase().contains(searchText)) {
                tableModel.addRow(new Object[]{
                    c.getCustomerId(),
                    c.getFullName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getCity(),
                    c.getCountry(),
                    c.getNationalId(),
                    c.getGender(),
                    c.isConsent()
                });
                found = true;
            }
        }

        txtSearch.setText("");

        if (!found) {
            JOptionPane.showMessageDialog(this, "Name '" + searchText + "' not found!", "Search Result", JOptionPane.WARNING_MESSAGE);
            loadTableData();
        }
    }

    private Gender getGenderFromCombo() {
        String selected = (String) genderBox.getSelectedItem();
        switch (selected) {
            case "MALE":
                return Gender.MALE;
            case "FEMALE":
                return Gender.FEMALE;
            default:
                return Gender.OTHER;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerForm());
    }
}
