package org.example;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class BulkDnsUpdater {
    public static void main(String[] args) {
        String host = "192.168.124.54";  // DNS server IP
        String user = "root";            // SSH user
        String password = "Alooparatha@2025";  // Replace with actual password or use SSH keys
        String zoneFilePath = "/var/named/examplenv.demo.zone";  // Full path to the zone file

        // Path to the CSV file containing DNS records (assuming format: domain_name,type,value,ttl)
        String csvFilePath = "dns_records.csv";

        // Read DNS records from CSV
        List<String> dnsRecords = readDnsRecordsFromCsv("src/main/resources/dns_records.csv");

        // Forming the command to append records to the zone file
        StringBuilder commandBuilder = new StringBuilder();
        for (String record : dnsRecords) {
            commandBuilder.append("echo '").append(record).append("' >> ").append("var/named/examplenv.demo.zone").append(" && ");
        }

        // Add the command to restart the named service
        commandBuilder.append("systemctl restart named");

        // SSH into the server and execute the command
        executeRemoteCommand(host, user, password, commandBuilder.toString());
    }

    // Method to read DNS records from a CSV file
    private static List<String> readDnsRecordsFromCsv(String filePath) {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    // Construct the DNS record: domain_name type value ttl
                    String record = fields[0] + " " + fields[1] + " " + fields[2];
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading CSV: " + e.getMessage());
        }
        return records;
    }

    // Method to execute the command on the remote DNS server via SSH
    private static void executeRemoteCommand(String host, String user, String password, String command) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setErrStream(System.err);
            channel.connect();

            System.out.println("✅ Bulk DNS records updated and named restarted!");

            // Disconnect after the operation
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.err.println("❌ Error executing SSH command: " + e.getMessage());
        }
    }
}
