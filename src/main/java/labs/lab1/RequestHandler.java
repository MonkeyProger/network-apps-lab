package labs.lab1;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request = in.readLine();
            if (request == null) return;

            String[] parts = request.split(" ");
            if (parts.length <= 1) return;
            String filename = parts[1];

            try (FileInputStream inputStream = new FileInputStream(filename)) {
                out.println("HTTP/1.1 200 OK\r\n");
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    socket.getOutputStream().write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                out.println("HTTP/1.1 404 Not Found\r\nFile Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}