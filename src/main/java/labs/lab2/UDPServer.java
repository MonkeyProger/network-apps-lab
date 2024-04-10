package labs.lab2;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UDPServer implements Closeable {
    public UDPServer(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    private final DatagramSocket socket;

    private byte[] receivingDataBuffer = new byte[1048];
    private byte[] sendingDataBuffer = new byte[1048];

    public void start() throws IOException {
        System.out.println("server: Сервер запущен на порту " + socket.getLocalPort());
        Random random = new Random();

        while (true) {
            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("server: Ожидание сообщения от клиента");

            if (random.nextInt(10) < 4) {
                System.out.println("server: Пакет потерян!");
                continue;
            }

            try {
                socket.receive(inputPacket);
            } catch (IOException e) {
                break;
            }

            String receivedData = new String(inputPacket.getData(), 0, inputPacket.getLength());

            System.out.println("server: Получено сообщение от клиента (" + inputPacket.getAddress().getHostAddress() +
                    ":" + inputPacket.getPort() + ")" + ": " + receivedData);

            sendingDataBuffer = receivedData.toUpperCase().getBytes();

            InetAddress senderAddress = inputPacket.getAddress();
            int senderPort = inputPacket.getPort();

            DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, senderAddress, senderPort);
            socket.send(outputPacket);

            System.out.println("server: Отправлен ответ клиенту: " + outputPacket.getAddress().getHostAddress() +
                    ":" + outputPacket.getPort());
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}