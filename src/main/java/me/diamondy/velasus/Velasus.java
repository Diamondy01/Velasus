package me.diamondy.velasus;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.diamondy.velasus.commands.JumpCommand;
import me.diamondy.velasus.commands.PullCommand;
import me.diamondy.velasus.commands.SendCommand;
import me.diamondy.velasus.commands.ServerCMD;
import me.diamondy.velasus.rcon.RconServer;
import org.slf4j.Logger;

import java.nio.file.Path;

public class Velasus {
    @Getter
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public Velasus(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("Velasus has been enabled!");

        CommandManager commandManager = proxyServer.getCommandManager();
        ServerCMD serverCMD = new ServerCMD(proxyServer);
        commandManager.register(commandManager.metaBuilder("server").build(), serverCMD);
        commandManager.register(commandManager.metaBuilder("send").build(), new SendCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("pull").build(), new PullCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("jump").build(), new JumpCommand(proxyServer));

        // Start the Rcon server
        int rconPort = 25575;
        String rconPassword = "1234";

        RconServer rconServer = new RconServer(rconPort, rconPassword, this, logger);
        new Thread(rconServer::start).start();
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public void executeCommand(String command) {
        proxyServer.getCommandManager().executeAsync(proxyServer.getConsoleCommandSource(), command);
    }
}