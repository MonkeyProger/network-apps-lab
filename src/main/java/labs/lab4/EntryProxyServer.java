package labs.lab4;

import java.io.*;
import java.net.Socket;

public class EntryProxyServer {
    public static void start() throws IOException {
        boolean isUsingBrowser = false;
        try (ProxyServer proxyServer = new ProxyServer("google.com", 80, 44235);
             Socket socket = new Socket("127.0.0.1", 44235);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            new Thread(() -> {
                try {
                    proxyServer.start(!isUsingBrowser);
                } catch (IOException e) {
                    System.out.println("Server error");
                    throw new RuntimeException(e);
                }
            }).start();
            if (!isUsingBrowser) {
                new Thread(() -> {
                    while (!proxyServer.isReady()) ;
                    System.out.println("Sending GET request");
                    writer.println("GET /index.html");

                    System.out.println("Response from server: ");
                    try {
                        while (proxyServer.isReady()) {
                            String respLine = reader.readLine();
                            System.out.println(respLine);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            while (proxyServer.isReady()) ;
        }
    }
}