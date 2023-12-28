import bg.sofia.uni.fmi.mjt.order.server.Client;
import bg.sofia.uni.fmi.mjt.order.server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientTest {
    private Server server;
    private Client client;

    @Test
    void testMain(){
        Thread serverThread = new Thread(() -> {
            Server.main(new String[0]);
        });
        serverThread.start();

        // Start the client in a new thread
        Thread clientThread = new Thread(() -> {
            Client.main(new String[0]);
        });
        clientThread.start();

        try {
            Thread.sleep(2000); // Adjust delay time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
