package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_FIVE = 5;
    private static final int INDEX_SIX = 6;
    private static final int INDEX_SEVEN = 7;
    private static final int INDEX_THREE = 3;

    private Socket socket;

    private final MJTOrderRepository repository;

    public ClientRequestHandler(Socket socket, MJTOrderRepository repository) {
        this.socket = socket;
        this.repository = repository;
    }

    private String extractSize(String size) {
        char[] arr = size.toCharArray();
        return String.valueOf(arr[INDEX_FIVE]);
    }

    private String extractColor(String color) {
        StringBuilder result = new StringBuilder();
        char[] arr = color.toCharArray();
        for (int i = INDEX_SIX; i < arr.length; i++) {
            result.append(arr[i]);
        }
        return result.toString();
    }

    private String extractDestination(String destination) {
        StringBuilder result = new StringBuilder();
        char[] arr = destination.toCharArray();
        for (int i = INDEX_SEVEN; i < arr.length; i++) {
            result.append(arr[i]);
        }
        return result.toString();
    }

    private int extractId(String id) {
        StringBuilder result = new StringBuilder();
        char[] arr = id.toCharArray();
        for (int i = INDEX_THREE; i < arr.length; i++) {
            result.append(arr[i]);
        }
        return Integer.parseInt(result.toString());
    }

    private void readCommand(String inputLine, PrintWriter out) {
        String[] separated = inputLine.split(" ");
        if (separated[INDEX_ZERO].equals("request")) {
            String size = extractSize(separated[INDEX_ONE]);
            String color = extractColor(separated[INDEX_TWO]);
            String destination = extractDestination(separated[INDEX_THREE]);
            out.println(repository.request(size, color, destination).toString());
        } else if (separated[INDEX_ZERO].equals("get")) {
            switch (separated[INDEX_ONE]) {
                case "all" -> out.println(repository.getAllOrders().toString());
                case "all-successful" -> out.println(repository.getAllSuccessfulOrders().toString());
                case "my-order" -> out.println(repository.getOrderById(extractId(separated[2])));
            }
        } else {
            out.println("Unknown command");
        }
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                readCommand(inputLine, out);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}