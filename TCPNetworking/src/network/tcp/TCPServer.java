package network.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This program demonstrates the server side of a TCP connection.
 *
 * @author Hugo O.D.
 * @since 15/11/2023
 * @see TCPClient.java
 */
public class TCPServer {

    private ServerSocket server;
    private Timer timer;

    /**
     * The TCPServer constructor which initialises it's socket to the given params
     *
     * @param ipAddress IP address of server, of type String
     * @param port Port number of server, of type int
     * @throws Exception
     */
    public TCPServer(String ipAddress, int port) throws Exception {
            this.server = new ServerSocket(port, 1, InetAddress.getByName(ipAddress));
    }

    /**
     * Default constructor that calls defined constructor with port number 0,
     * max queue length 1 and the IP address of the local host
     * @throws Exception 
     */
    
    public TCPServer() throws Exception {
        this(0, 1, InetAddress.getLocalHost());
    }
    
    /**
     * Listens to incoming client requests, prints receieved message(s) from client
     * and automatically closes the socket after 30 seconds of inactivity.
     *
     * @throws Exception
     */
    private void listen() throws Exception {
        //Listen to incoming client's requests via the ServerSocket
        String data;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew client connection from " + clientAddress);

        //Print received datagrams from client
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while ((data = in.readLine()) != null) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
            client.sendUrgentData(1);
            // Creates a timer to automatically close the server if there is no activity for 30 seconds
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    closeServer();
                }
            }, 30000); // 30 seconds
        }
    }

    //Closes the sever & ends the program
    public void closeServer() {
        System.out.println("\r\nServer is closing due to inactivity.");
        try {
            this.server.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        //Set the server address (IP) and port number
        String serverIP = "127.0.0.1"; // local IP address
        int port = 8088;
        
        //If CL arguments are supplied, use them as IP and port #
        if (args.length > 0) {
            serverIP = args[0];
            port = Integer.parseInt(args[1]);
        }
        //Call the constructor and pass the IP & port #
        TCPServer server = new TCPServer(serverIP, port);
        System.out.println("\r\nRunning Server: "
                + "Host=" + server.getSocketAddress().getHostAddress()
                + " Port=" + server.getPort());
        server.listen();
    }

}
