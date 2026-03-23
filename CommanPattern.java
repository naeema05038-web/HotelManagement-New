import javax.swing.*;
import java.util.ArrayList;

interface Command {

    void execute();
}

class OpenCustomerFormCommand implements Command {

    private HotelFacade facade;

    public OpenCustomerFormCommand(HotelFacade facade) {
        this.facade = facade;
    }

    public void execute() {
        facade.openCustomerForm();
    }
}

class OpenRoomFormCommand implements Command {

    private HotelFacade facade;

    public OpenRoomFormCommand(HotelFacade facade) {
        this.facade = facade;
    }

    public void execute() {
        facade.openRoomForm();
    }
}

class OpenBookingFormCommand implements Command {

    private HotelFacade facade;

    public OpenBookingFormCommand(HotelFacade facade) {
        this.facade = facade;
    }

    public void execute() {
        facade.openBookingForm();
    }
}

class SearchCustomerCommand implements Command {

    private HotelFacade facade;
    private String customerId;
    private JFrame parentFrame;

    public SearchCustomerCommand(HotelFacade facade, String customerId, JFrame parentFrame) {
        this.facade = facade;
        this.customerId = customerId;
        this.parentFrame = parentFrame;
    }

    public void execute() {
        try {
            int id = Integer.parseInt(customerId.trim());
            Customer customer = facade.findCustomerById(id);
            if (customer == null) {
                JOptionPane.showMessageDialog(parentFrame, "Customer not found!");
                return;
            }
            JOptionPane.showMessageDialog(parentFrame, "Customer: " + customer.getFullName());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid ID");
        }
    }
}

class SearchRoomCommand implements Command {

    private HotelFacade facade;
    private String roomNo;
    private JFrame parentFrame;

    public SearchRoomCommand(HotelFacade facade, String roomNo, JFrame parentFrame) {
        this.facade = facade;
        this.roomNo = roomNo;
        this.parentFrame = parentFrame;
    }

    public void execute() {
        try {
            int roomNumber = Integer.parseInt(roomNo.trim());
            Room room = facade.findRoomByNumber(roomNumber);
            if (room == null) {
                JOptionPane.showMessageDialog(parentFrame, "Room not found!");
                return;
            }
            JOptionPane.showMessageDialog(parentFrame, "Room: " + room.getRoomNo());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid Room No");
        }
    }
}

class ShowDashboardCommand implements Command {

    private HotelFacade facade;
    private JFrame parentFrame;

    public ShowDashboardCommand(HotelFacade facade, JFrame parentFrame) {
        this.facade = facade;
        this.parentFrame = parentFrame;
    }

    public void execute() {
        JOptionPane.showMessageDialog(parentFrame, facade.getDashboardSummary().toString());
    }
}

class ButtonCommandInvoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();

        }
    }
}

class CommandFactory {

    private HotelFacade facade;
    private JFrame parentFrame;

    public CommandFactory(HotelFacade facade, JFrame parentFrame) {
        this.facade = facade;
        this.parentFrame = parentFrame;
    }

    public Command createCommand(String commandType, String... params) {
        switch (commandType) {
            case "OPEN_CUSTOMER":
                return new OpenCustomerFormCommand(facade);
            case "OPEN_ROOM":
                return new OpenRoomFormCommand(facade);
            case "OPEN_BOOKING":
                return new OpenBookingFormCommand(facade);
            case "SEARCH_CUSTOMER":
                return new SearchCustomerCommand(facade, params[0], parentFrame);
            case "SEARCH_ROOM":
                return new SearchRoomCommand(facade, params[0], parentFrame);
            case "DASHBOARD":
                return new ShowDashboardCommand(facade, parentFrame);
            default:
                return null;
        }
    }
}
