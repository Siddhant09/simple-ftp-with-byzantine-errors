import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public Socket client = null;
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    public FileInputStream fileInputStream = null;
    public FileOutputStream fileOutputStream = null;
    public BufferedReader bufferedReader = null;
    public String inputFromUser = "";
    public String currentUser = null;
    public Scanner scanner = null;
    public Methods methods = null;

    public static void main(String[] args) {
        Client c = new Client();
        c.doConnections();
    }

    public void doConnections() {
        try {
            methods = new Methods();
            scanner = new Scanner(System.in);
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            bufferedReader = new BufferedReader(inputStreamReader);
            // client = new Socket("127.0.0.1", 7777);
            client = new Socket("in-csci-rrpc06.cs.iupui.edu", 7777);
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());

            Database.createData();
            // cleaing the console
            methods.clearScreen();
            System.out.println("==============================");
            System.out.println("Connected to Simple FTP Server");
            System.out.println("==============================");
            System.out.println();

            while (true) {
                try {
                    if (currentUser == null) {
                        methods.showLoginMenu();
    
                        // checking of the input is valid
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input, please select a valid option");
                            // scanner.next();
                            continue;
                        }
    
                        inputFromUser = scanner.nextLine();
                        int i = Integer.parseInt(inputFromUser);
                        switch (i) {
                            case 1:
                                currentUser = methods.login(scanner);
                                break;
                            case 2:
                                methods.register(scanner);
                                break;
                            case 3:
                                methods.exit();
                                break;
                            default:
                                System.out.println("Invalid Option!");
                        }
                    } else {
                        methods.showMenu(currentUser);
    
                        // checking of the input is valid
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input, please select a valid option");
                            // scanner.next();
                            continue;
                        }
    
                        inputFromUser = scanner.nextLine();
                        int i = Integer.parseInt(inputFromUser);
                        switch (i) {
                            case 1:
                                uploadFile();
                                break;
                            case 2:
                                downloadFile();
                                break;
                            case 3:
                                methods.exit();
                                break;
                            default:
                                System.out.println("Invalid Option!");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Client error - " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to Connect to Server");
        }
    }

    // method for uploading file
    public void uploadFile() {
        try {
            String filename = "", filedata = "";
            File file;
            byte[] data;
            System.out.println("Enter the name of the file you want to uploaded: ");
            filename = bufferedReader.readLine();

            Byzantine byzantine = new Byzantine();
            int error = byzantine.ByzantineError();

            if (error == 1)
                filename = "FileNotFound.txt";

            file = new File(filename);
            if (file.isFile()) {
                fileInputStream = new FileInputStream(file);
                data = new byte[fileInputStream.available()];
                fileInputStream.read(data);
                fileInputStream.close();
                filedata = new String(data);

                filedata = Cryptography.Encrypt(filedata, "1234");

                String acknowledgement = "Uploaded - acknowledgement from client";
                dataOutputStream.writeUTF("UPLOAD_FILE");
                dataOutputStream.writeUTF(currentUser);
                dataOutputStream.writeUTF(filename);
                dataOutputStream.writeUTF(filedata);
                dataOutputStream.writeUTF(acknowledgement);
                System.out.println("============================================");
                System.out.println("File Uploaded Successfully as - " + currentUser + "-Uploaded-" + filename);
                System.out.println("============================================");
            } else {
                if (error == 1) {
                    methods.clearScreen();
                    System.out.println("========================================");
                    System.out.println("Byzantine error occured - FILE NOT FOUND");
                    System.out.println("========================================");
                } else {
                    System.out.println("==============");
                    System.out.println("File Not Found");
                    System.out.println("==============");
                }
            }
        } catch (Exception ex) {
            System.out.println("Client error - " + ex.getMessage());
        }
    }

    // method of downloading file
    public void downloadFile() {
        try {
            String filename = "", filedata = "";
            System.out.println("Enter the name of the file you want to download: ");
            filename = bufferedReader.readLine();

            Byzantine byzantine = new Byzantine();
            int error = byzantine.ByzantineError();

            if (error == 1)
                filename = "FileNotFound.txt";

            String acknowledgement = "Downloaded - acknowledgement from client";
            dataOutputStream.writeUTF("DOWNLOAD_FILE");
            dataOutputStream.writeUTF(filename);
            dataOutputStream.writeUTF(acknowledgement);
            filedata = dataInputStream.readUTF();
            if (filedata.equals("")) {
                if (error == 1) {
                    methods.clearScreen();
                    System.out.println("========================================");
                    System.out.println("Byzantine error occured - FILE NOT FOUND");
                    System.out.println("========================================");
                } else {
                    System.out.println("==============");
                    System.out.println("File Not Found");
                    System.out.println("==============");
                }
            } else {
                filename = currentUser + "-Downloaded-" + filename;
                fileOutputStream = new FileOutputStream(filename);

                filedata = Cryptography.Decrypt(filedata, "1234");

                fileOutputStream.write(filedata.getBytes());
                fileOutputStream.close();
                System.out.println("============================================");
                System.out.println("File Downloaded Successfully as - " + filename);
                System.out.println("============================================");
            }
        } catch (Exception ex) {
            System.out.println("Client error - " + ex.getMessage());
        }
    }
}
