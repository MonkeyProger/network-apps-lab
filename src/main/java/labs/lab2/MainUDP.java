package labs.lab2;

import java.io.IOException;
import java.util.Random;

public class MainUDP {
    
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        int port = random.nextInt(65536 - 1024) + 1024;

        try (UDPServer udpServer = new UDPServer(port)) {
            new Thread(() -> {
                try {
                    udpServer.start();
                } catch (IOException e) {
                    System.err.println("Проблема запуска сервера: " + e);
                }
            }).start();

            try (UDPClient udpClient = new UDPClient()) {
                int i = 0;
                while (i < 10) {
                    i++;
                    udpClient.ping("127.0.0.1", port, 1000);
                }
            }
        }
    }
}