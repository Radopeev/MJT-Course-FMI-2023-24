import bg.sofia.uni.fmi.mjt.order.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerTest {
    private static final int SERVER_PORT = 4444;
    private static Server server;

    @BeforeAll
    static void setUp() {
        server = new Server();
        Thread serverThread = new Thread(() -> Server.main(new String[0]));
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @Test
    void testServerHandlesClientValidRequest() {
        try (var clientSocket = new Socket("localhost", SERVER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("request size=S color=BLACK shipTo=EUROPE");
            String response = in.readLine();

            assertNotNull(response);
            assertTrue(response.contains("CREATED"));

        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testServerHandlesClientInvalidRequest() {
        try (var clientSocket = new Socket("localhost", SERVER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("request size=S color=ORANGE shipTo=EUROPE");
            String response = in.readLine();

            assertNotNull(response);
            assertTrue(response.contains("DECLINE"));

        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testServerHandlesGetAllOrders() {
        try (Socket clientSocket = new Socket("localhost", SERVER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("get all");
            String response = in.readLine();

            assertNotNull(response);
            assertTrue(response.contains("OK"));

        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testServerHandlesGetOrderById() {
        try (Socket clientSocket = new Socket("localhost", SERVER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("get my-order id=-1000");
            String response = in.readLine();

            assertNotNull(response);
            assertTrue(response.contains("NOT_FOUND"));

        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
