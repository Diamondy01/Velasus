package me.diamondy.velasus.rcon;

import me.diamondy.velasus.Velasus;
import org.slf4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RconClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String password;
    private final Velasus velasus;
    private final Logger logger;

    public RconClientHandler(Socket clientSocket, String password, Velasus velasus, Logger logger) {
        this.clientSocket = clientSocket;
        this.password = password;
        this.velasus = velasus;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                try {
                    // Read packet size (4 bytes)
                    int packetSize = readInt(in);
                    logger.info("Raw packet size received: " + packetSize);

                    // Validate packet size
                    if (packetSize < 10 || packetSize > 4096) {
                        logger.warn("Invalid packet size: " + packetSize);
                        continue; // Skip to the next iteration to read another packet
                    }

                    // Read the full packet
                    byte[] packetBytes = new byte[packetSize];
                    in.readFully(packetBytes);

                    // Debugging the received bytes
                    logger.debug("Received packet bytes: " + bytesToHex(packetBytes));

                    // Process the packet
                    RconPacket packet = RconPacket.fromBytes(packetBytes);
                    logger.info("Received command: " + packet.getPayload());

                    // Authentication check and command handling
                    if (packet.getType() == 3 && packet.getPayload().equals(password)) {
                        out.write(new RconPacket(packet.getRequestId(), 2, "").toBytes()); // Auth response
                        out.flush();
                        logger.info("Client authenticated");
                        handleCommands(in, out, packet.getRequestId());
                    } else {
                        out.write(new RconPacket(-1, 2, "").toBytes()); // Auth failed
                        out.flush();
                        logger.warn("Client failed authentication");
                        clientSocket.close();
                        break;
                    }
                } catch (IOException e) {
                    logger.error("Error reading or processing packet", e);
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Error handling client connection", e);
        }
    }

    private void handleCommands(DataInputStream in, DataOutputStream out, int requestId) throws IOException {
        while (true) {
            try {
                int packetSize = readInt(in); // Read packet size
                logger.info("Raw packet size received: " + packetSize);

                if (packetSize < 10 || packetSize > 4096) {
                    logger.warn("Invalid packet size: " + packetSize);
                    continue; // Skip to the next iteration to read another packet
                }

                byte[] packetBytes = new byte[packetSize];
                in.readFully(packetBytes);

                logger.debug("Received packet data: " + bytesToHex(packetBytes));

                RconPacket packet = RconPacket.fromBytes(packetBytes);
                logger.info("Received command: " + packet.getPayload());

                // Check authentication and handle commands here...
            } catch (IOException e) {
                logger.error("Error reading or processing packet", e);
                break; // Exit loop on read error
            }
        }
    }

    private int readInt(DataInputStream in) throws IOException {
        // Reads an integer from the DataInputStream and handles endianness
        byte[] bytes = new byte[4];
        in.readFully(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian order
        return buffer.getInt();
    }

    // Utility method to convert bytes to hex for debugging
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}