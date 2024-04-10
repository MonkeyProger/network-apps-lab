package labs.lab5;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class ICMPPinger {
    public ICMPPinger(String server, int iterations) {
        this.server = server;
        this.iterations = iterations;
    }

    private final String server;
    private final int iterations;

    public void ping() throws IOException {
        InetAddress address = InetAddress.getByName(server);
        ArrayList<Long> rtts = new ArrayList<>();

        int passedIterations = 0;
        while (passedIterations != iterations) {
            passedIterations++;
            long startTime = Instant.now().toEpochMilli();
            boolean isReachable = address.isReachable(1000);
            long endTime = Instant.now().toEpochMilli();

            if (isReachable) {
                long duration = endTime - startTime;
                rtts.add(duration);
                System.out.println("Reply from " + address.getHostAddress() + ", time = " + duration + "ms");
            } else {
                System.out.println("Request timed out");
            }
        }

        int failures = iterations - rtts.size();
        int received = iterations - failures;
        double lossPerc = ((double) failures / iterations) * 100;
        long sumMs = rtts.stream().mapToLong(Long::longValue).sum();

        System.out.println("Ping statistics for " + server + " [" + address.getHostAddress() + "]");
        System.out.println("Packets: Sent = " + iterations + ", Received = " + received + ", Lost = " + failures + ", " + lossPerc + "% loss");
        System.out.println("Approximate round trip times in ms");
        long minMs = rtts.isEmpty() ? 0 : Collections.min(rtts);
        long maxMs = rtts.isEmpty() ? 0 : Collections.max(rtts);
        long averageMs = rtts.isEmpty() ? 0 : sumMs / rtts.size();
        System.out.println("Minimum = " + minMs + "ms, Maximum = " + maxMs + "ms, Average = " + averageMs + "ms");
    }
}