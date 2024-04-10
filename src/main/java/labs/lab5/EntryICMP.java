package labs.lab5;

import java.io.IOException;

public class EntryICMP {
    public static void start() throws IOException {
        ICMPPinger icmpPinger = new ICMPPinger("www.vk.ru", 4);
        icmpPinger.ping();
    }
}