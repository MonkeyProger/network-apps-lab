package labs.lab2;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class UDPClient implements Closeable {
    public UDPClient() throws IOException {
        socket = new DatagramSocket();
    }

    private final DatagramSocket socket;
    private int pingIdx = -1;
    private final byte[] receivingDataBuffer = new byte[1048];

    public void ping(String server, int port, int timeout) throws IOException {
        pingIdx++;
        socket.setSoTimeout(timeout);

        LocalDateTime startTime = LocalDateTime.now();
        String message = "client: Пинг к " + server + ", #" + pingIdx +
                " в " + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        byte[] messageArr = message.getBytes();
        InetAddress serverAddress = InetAddress.getByName(server);
        DatagramPacket packet = new DatagramPacket(messageArr, messageArr.length, serverAddress, port);

        socket.send(packet);
        System.out.println("client: Отправлен пинг на сервер " +
                serverAddress.getHostAddress() + ":" + port);

        DatagramPacket receivedPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        try {
            socket.receive(receivedPacket);
        } catch (SocketTimeoutException e) {
            System.out.println("client: #" + pingIdx + " Тайм-аут запроса");
            return;
        }
        LocalDateTime endTime = LocalDateTime.now();
        String receivedMsg = new String(receivedPacket.getData(), 0, receivedPacket.getLength()).toLowerCase();
        long rtt = ChronoUnit.MILLIS.between(startTime, endTime);
        System.out.println("client: #" + pingIdx + " | " + receivedMsg +
                " | RTT: " + rtt + " мс");
    }

    @Override
    public void close() {
        socket.close();
    }
}