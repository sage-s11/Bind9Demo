package org.example;

import java.util.List;

public class DnsAutomation {
    public static void main(String[] args) {
        // 1. Parse CSV file
        List<DnsRecord> records = DnsRecordParser.parseDnsRecords();

        // 2. Send parsed records to remote DNS
        for (DnsRecord record : records) {
            RemoteDnsUpdater.sendDnsRecord(record);
        }

        // 3. Update local zone file
        for (DnsRecord record : records) {
            DnsZoneUpdater.updateZoneFile(record);
        }
    }
}
