package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DnsRecordParser {
    private static final String CSV_FILE = "src/main/resources/dns_records.csv";

    public static List<DnsRecord> parseDnsRecords() {
        List<DnsRecord> dnsRecords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length < 3) continue; // Skip invalid rows

                // Parse Name, Type, and Value (Ignore TTL)
                dnsRecords.add(new DnsRecord(record[0].trim(), record[1].trim(), record[2].trim()));
            }
            System.out.println("✅ Successfully parsed " + dnsRecords.size() + " DNS records.");
        } catch (IOException | CsvException e) {
            System.err.println("❌ Error reading CSV file: " + e.getMessage());
        }

        return dnsRecords;
    }
}
