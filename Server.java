import java.io.*;
import java.net.*;

class Server {
    ServerSocket server = null;
    Socket client = null;

    public static void main(String[] arg) {
        Methods methods = new Methods();
        methods.clearScreen();
        System.out.println("=========================");
        System.out.println("Simple FTP Server Started");
        System.out.println("=========================");
        Server server = new Server();
        server.doConnections();
    }

    public void doConnections() {
        try {
            server = new ServerSocket(7777);
            while (true) {
                client = server.accept();
                ClientThread clientThread = new ClientThread(client);
                clientThread.start();
            }
        } catch (Exception ex) {
            System.out.println("Server error - " + ex.getMessage());
        }
    }
}

class ClientThread extends Thread {
    public Socket client = null;
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    public FileInputStream fileInputStream = null;
    public FileOutputStream fileOutputStream = null;
    public BufferedReader bufferedReader = null;
    public String inputFromUser = "";
    public File file = null;

    public ClientThread(Socket c) {
        try {
            client = c;
            dataInputStream = new DataInputStream(c.getInputStream());
            dataOutputStream = new DataOutputStream(c.getOutputStream());
        } catch (Exception ex) {
            System.out.println("Server error - " + ex.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String input = dataInputStream.readUTF();
                String filename = "", filedata = "", currentUser = "";
                byte[] data;

                if (input.equals("UPLOAD_FILE")) {
                    currentUser = dataInputStream.readUTF();
                    filename = dataInputStream.readUTF();
                    filedata = dataInputStream.readUTF();
                    String acknowledgement = dataInputStream.readUTF();
                    System.out.println(acknowledgement);
                   
                    filedata = Cryptography.Decrypt(filedata, "1234");

                    filename = currentUser + "-Uploaded-" + filename;
                    fileOutputStream = new FileOutputStream(filename);
                    fileOutputStream.write(filedata.getBytes());
                    fileOutputStream.close();
                } else if (input.equals("DOWNLOAD_FILE")) {

                    filename = dataInputStream.readUTF();
                    file = new File(filename);
                    if (file.isFile()) {
                        String acknowledgement = dataInputStream.readUTF();
                        System.out.println(acknowledgement);
                        
                        fileInputStream = new FileInputStream(file);
                        data = new byte[fileInputStream.available()];
                        fileInputStream.read(data);
                        filedata = new String(data);
                        fileInputStream.close();                        
                    
                        filedata = Cryptography.Encrypt(filedata, "1234");

                        dataOutputStream.writeUTF(filedata);
                    } else {
                        dataOutputStream.writeUTF(""); // NO FILE FOUND
                    }
                } else {
                    System.out.println("Server error");
                }
            } catch (Exception ex) {
                System.out.println("Server error - " + ex.getMessage());
            }
        }
    }
}
