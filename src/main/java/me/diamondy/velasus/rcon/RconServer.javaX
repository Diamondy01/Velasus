package me.diamondy.velasus.rcon;

import me.diamondy.velasus.Velasus;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RconServer {
    private final int port;
    private final String password;
    private final Velasus velasus;
    private final Logger logger;
    private final ExecutorService threadPool;

    public RconServer(int port, String password, Velasus velasus, Logger logger) {
        this.port = port;
        this.password = password;
        this.velasus = velasus;
        this.logger = logger;
        this.threadPool = Executors.newFixedThreadPool(10); // Adjust the pool size as needed
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("RconServer started on port " + port);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Accepted connection from " + clientSocket.getInetAddress());
                    threadPool.execute(new RconClientHandler(clientSocket, password, velasus, logger));
                } catch (IOException e) {
                    logger.error("Error accepting connection", e);
                }
            }
        } catch (IOException e) {
            logger.error("Error starting RconServer", e);
        } finally {
            threadPool.shutdown(); // Ensure the thread pool is properly shut down
        }
    }
}
