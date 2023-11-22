package network.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program demonstrates the client side of a TCP connection, it allows
 * for data to be continuously sent to the Server which is running locally.
 *
 * @author Hugo O.D.
 * @version 1.0
 * @since 15/11/2023
 */
public class TCPClient {

    private Socket tcpSocket;
    private InetAddress serverAddress;
    private int serverPort;
    private Scanner scanner;

    /**
     * The TCPClient constructor, which initialises the Server's IP address
     * & port number to the parameters which is used to initialise a new TCP socket,
     * also sets up the Scanner.
     * @param serverAddress Server's IP address, of type InetAddress
     * @param serverPort Server's port, of type int
     * @throws Exception 
     */
    private TCPClient(InetAddress serverAddress, int serverPort) throws Exception {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        //Initiate the connection with the server using Socket. 
        //For this, creates a stream socket and connects it to the specified port number at the specified IP address. 
        this.tcpSocket = new Socket(this.serverAddress, this.serverPort);
        this.scanner = new Scanner(System.in);
    }

    /**
     * The start method which connects to the server and allows for datagrams to
     * be continuously sent
     *
     * @throws IOException
     */
    private void start() throws IOException {
        String input;
        //create a new PrintWriter from the socket's existing OutputStream 
        //This convenience constructor creates the necessary intermediate OutputStreamWriter,
        //which will convert characters into bytes using the default character encoding
        while (true) {
            input = scanner.nextLine();
            PrintWriter output = new PrintWriter(this.tcpSocket.getOutputStream(), true);
            output.println(input);
            output.flush();
        }
    }

    
    
    public static void main(String[] args) throws Exception {
        // set the server address (IP) and port number
        InetAddress serverIP = InetAddress.getLocalHost(); // local IP address
        int port = 8088;

        if (args.length > 0) {
            serverIP = InetAddress.getByName(args[0]);            
            port = Integer.parseInt(args[1]);
        }

        // call the constructor and pass the IP and port
        TCPClient client = new TCPClient(serverIP, port);
        System.out.println("\r\n Connected to Server: " + client.tcpSocket.getInetAddress());
        client.start();
    }
}
