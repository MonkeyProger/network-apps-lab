package labs.lab1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private static final int PORT = 80;
    private static final int THREAD_POOL = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключение к сокету принято");

                Runnable requestHandler = new RequestHandler(clientSocket);
                executor.execute(requestHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
