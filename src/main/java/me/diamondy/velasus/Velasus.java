package me.diamondy.velasus;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.diamondy.velasus.commands.*;
import org.slf4j.Logger;


@Getter
public class Velasus {
    private final ProxyServer proxyServer;

    @Inject
    public Velasus(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;

        logger.info("Velasus has been enabled!");

        CommandManager commandManager = proxyServer.getCommandManager();
        ServerCMD serverCMD = new ServerCMD(proxyServer);
        commandManager.register(commandManager.metaBuilder("server").build(), serverCMD);
        commandManager.register(commandManager.metaBuilder("send").build(), new SendCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("pull").build(), new PullCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("jump").build(), new JumpCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("vglistplayers").build(), new ListPlayerCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("vgservlist").build(), new ListPlayerOnServerCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("veloaddstaff").build(), new AddStaffCommand(proxyServer));
        commandManager.register(commandManager.metaBuilder("velremovestaff").build(), new RemoveStaffCommand(proxyServer));

        // Start the Rcon server
  //      int rconPort = 25575;
    //    String rconPassword = "1234";

      //  RconServer rconServer = new RconServer(rconPort, rconPassword, this, logger);
        //new Thread(rconServer::start).start();
    }

    public void executeCommand(String command) {
        proxyServer.getCommandManager().executeAsync(proxyServer.getConsoleCommandSource(), command);
    }
}