package org.example;

import java.io.FileWriter;
import java.io.IOException;

public class DnsZoneUpdater {
    private static final String ZONE_FILE = "/var/named/examplenv.demo.zone"; // Adjust path

    public static void updateZoneFile(DnsRecord record) {
        String dnsEntry = record.getName() + " IN " + record.getType() + " " + record.getValue();

        try (FileWriter writer = new FileWriter(ZONE_FILE, true)) {
            writer.write("\n" + dnsEntry);
            System.out.println("✅ Added to zone file: " + dnsEntry);
        } catch (IOException e) {
            System.err.println("❌ Error updating zone file: " + e.getMessage());
        }
    }
}
