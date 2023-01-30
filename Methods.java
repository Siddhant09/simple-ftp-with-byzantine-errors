import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Methods {

    Byzantine byzantine = new Byzantine();

    // method for displaying the login menu options
    public void showLoginMenu() {
        try {
            System.out.println("-----------------------------------");
            System.out.println("Please select an option");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("-----------------------------------");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // method for displaying the menu options
    public void showMenu(String user) {
        try {
            System.out.println("-----------------------------------");
            System.out.println("Current Session: " + user);
            System.out.println("-----------------------------------");
            System.out.println("What would you like to do next?");
            System.out.println("1. Upload file");
            System.out.println("2. Download file");
            System.out.println("3. Exit");
            System.out.println("-----------------------------------");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // method for clearing the console
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // method for exiting the application
    public void exit() {
        try {
            clearScreen();
            System.out.println("==============================");
            System.out.println("Thank you for using Simple FTP");
            System.out.println("==============================");

            // exiting the application
            System.exit(0);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // method for login
    public String login(Scanner scanner) {
        String user = null;
        try {
            System.out.println("Enter user name");
            String userName = scanner.nextLine();

            System.out.println("Enter password");
            String password = scanner.nextLine();

            int error = byzantine.ByzantineError();
            if (error == 3) {
                clearScreen();
                System.out.println("=======================================");
                System.out.println("Byzantine error occured - LOGIN FAILED!");
                System.out.println("=======================================");
                return null;
            }

            List<String> list = Database.users.stream()
                    .filter(x -> x.getName().equals(userName) && x.getPassword().equals(password))
                    .map(x -> String.valueOf(x.getName())).collect(Collectors.toList());
            if (list.size() > 0)
                return list.get(0);
            else {
                System.out.println("===================");
                System.out.println("Invalid credentials");
                System.out.println("===================");
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return user;
    }

    // method for registration
    public boolean register(Scanner scanner) {
        try {
            System.out.println("Enter user name");
            String userName = scanner.nextLine();

            System.out.println("Enter password");
            String password = scanner.nextLine();

            int error = byzantine.ByzantineError();
            if (error == 4) {
                clearScreen();
                System.out.println("==============================================");
                System.out.println("Byzantine error occured - REGISTRATION FAILED!");
                System.out.println("==============================================");
                return false;
            }

            List<String> list = Database.users.stream()
                    .filter(x -> x.getName().equals(userName) && x.getPassword().equals(password))
                    .map(x -> String.valueOf(x.getName())).collect(Collectors.toList());
            if (list.size() > 0) {
                System.out.println("=======================");
                System.out.println("Username already exists");
                System.out.println("=======================");
                return false;
            } else {
                User newUser = new User(userName, password);
                Database.users.add(newUser);
                System.out.println("=======================");
                System.out.println("Registration Successful");
                System.out.println("=======================");

                return true;
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
