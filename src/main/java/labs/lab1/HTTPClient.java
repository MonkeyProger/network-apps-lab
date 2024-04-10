package labs.lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPClient {
    public static void main(String[] args) {
        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
            printHelp();
            return;
        }
        if (args.length != 3) {
            printError();
            return;
        }

        String host = args[0];
        String filePath = args[2];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            printError();
            System.out.println(e.getMessage());
            return;
        }

        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String request = "GET " + filePath + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n";
            out.println(request);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("""
                HTTPClient - Клиент для отправки HTTP GET запросов на сервер.
                Использование: java HTTPClient [host] [port] [path]
                Аргументы:
                  host   - Хост сервера, к которому нужно отправить запрос.
                  port   - Порт сервера, на котором работает HTTP сервер.
                  path     - Путь к файлу на сервере.

                Опции:
                  -h, --help    - Вывести это справочное сообщение и выйти.""");
    }

    public static void printError() {
        System.out.println("""
                            Ошибка использования
                            Запустите 'java HTTPClient -h' или 'java HTTPClient --help' для справки.
                            """);
    }
}
