
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerForm extends JFrame {

    JTextField txtFullName, txtPhone, txtEmail, txtCity, txtCountry, txtNationalId, txtSearch;
    JCheckBox chkConsent;
    JComboBox<Gender> cmbGender;

    JTable table;
    DefaultTableModel model;

    CustomerService service = new CustomerService();

    public CustomerForm() {
        setTitle("Customer Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Customer Information"));

        txtFullName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtCity = new JTextField();
        txtCountry = new JTextField();
        txtNationalId = new JTextField();
        chkConsent = new JCheckBox("Consent Given");
        cmbGender = new JComboBox<>(Gender.values());

        form.add(new JLabel("Full Name"));
        form.add(txtFullName);
        form.add(new JLabel("Phone"));
        form.add(txtPhone);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("City"));
        form.add(txtCity);
        form.add(new JLabel("Country"));
        form.add(txtCountry);
        form.add(new JLabel("National ID"));
        form.add(txtNationalId);
        form.add(new JLabel("Gender"));
        form.add(cmbGender);
        form.add(new JLabel("Consent"));
        form.add(chkConsent);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Full Name", "Phone", "Email", "City", "Country", "National ID", "Gender", "Consent"},
                0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Search");

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnSave);
        panelBtn.add(btnUpdate);
        panelBtn.add(btnDelete);
        panelBtn.add(btnClear);
        panelBtn.add(new JLabel("Search Name:"));
        panelBtn.add(txtSearch);
        panelBtn.add(btnSearch);

        add(panelBtn, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchCustomer());

        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        loadTable();

        setVisible(true);
    }

    void saveCustomer() {
        try {
            Customer c = CustomerFactory.createCustomer(
                    txtFullName.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    txtCity.getText(),
                    txtCountry.getText(),
                    txtNationalId.getText(),
                    chkConsent.isSelected(),
                    (Gender) cmbGender.getSelectedItem()
            );
            service.addCustomer(c);
            loadTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    void updateCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select row to update");
            return;
        }

        try {
            Customer c = service.getCustomers().get(row);
            c.setFullName(txtFullName.getText());
            c.setPhone(txtPhone.getText());
            c.setEmail(txtEmail.getText());
            c.setCity(txtCity.getText());
            c.setCountry(txtCountry.getText());
            c.setNationalId(txtNationalId.getText());
            c.setConsent(chkConsent.isSelected());
            c.setGender((Gender) cmbGender.getSelectedItem());
            service.updateCustomer(row, c);
            loadTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select row to delete");
            return;
        }

        service.deleteCustomer(row);
        loadTable();
        clearFields();
    }

    void clearFields() {
        txtFullName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCity.setText("");
        txtCountry.setText("");
        txtNationalId.setText("");
        chkConsent.setSelected(false);
        cmbGender.setSelectedIndex(0);
        table.clearSelection();
    }

    void fillFields() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Customer c = service.getCustomers().get(row);
            txtFullName.setText(c.getFullName());
            txtPhone.setText(c.getPhone());
            txtEmail.setText(c.getEmail());
            txtCity.setText(c.getCity());
            txtCountry.setText(c.getCountry());
            txtNationalId.setText(c.getNationalId());
            chkConsent.setSelected(c.isConsent());
            cmbGender.setSelectedItem(c.getGender());
        }
    }

    void loadTable() {
        model.setRowCount(0);
        ArrayList<Customer> list = service.getCustomers();
        for (Customer c : list) {
            model.addRow(new Object[]{
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

    void searchCustomer() {
        String key = txtSearch.getText().toLowerCase();
        model.setRowCount(0);
        for (Customer c : service.getCustomers()) {
            if (c.getFullName().toLowerCase().contains(key)) {
                model.addRow(new Object[]{
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
    }

    public static void main(String[] args) {
        new CustomerForm();
    }
}
