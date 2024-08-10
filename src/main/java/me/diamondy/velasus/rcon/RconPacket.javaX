package me.diamondy.velasus.rcon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class RconPacket {
    private final int length;
    private final int requestId;
    private final int type;
    private final String payload;

    public RconPacket(int requestId, int type, String payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
        // Correctly calculate length
        this.length = 4 + 4 + payload.length() + 2; // 4 (requestId) + 4 (type) + payload length + 2 (null bytes)
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian order
        buffer.putInt(length);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload.getBytes(StandardCharsets.US_ASCII));
        buffer.put((byte) 0); // Null terminator
        buffer.put((byte) 0); // Null terminator
        return buffer.array();
    }

    public static RconPacket fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure little-endian order

        int length = buffer.getInt(); // Get length of the packet
        int requestId = buffer.getInt();
        int type = buffer.getInt();

        // Check if data length is sufficient for the specified length
        if (data.length < length + 4) { // +4 to account for the length field itself
            throw new IllegalArgumentException("Data length is less than the packet length");
        }

        byte[] payloadBytes = new byte[length - 8]; // length - 4 (requestId) - 4 (type)
        buffer.get(payloadBytes);

        String payload = new String(payloadBytes, StandardCharsets.US_ASCII).trim();
        return new RconPacket(requestId, type, payload);
    }

    public int getRequestId() {
        return requestId;
    }

    public int getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }
}