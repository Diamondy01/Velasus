package me.diamondy.velasus.rcon;

import lombok.Getter;
import me.diamondy.velasus.Velasus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.velocitypowered.api.proxy.ProxyServer;
import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RconServer {
    private final int port;
    private final String password;
    private final Velasus velasus;
    @Getter
    private final Logger logger;

    public RconServer(int port, String password, Velasus velasus, Logger logger) {
        this.port = port;
        this.password = password;
        this.velasus = velasus;
        this.logger = logger;
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(@NotNull SocketChannel ch) {
                        ch.pipeline().addLast(new RconHandler(RconServer.this, password));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(new InetSocketAddress(port)).sync();
            logger.info("Rcon server started on port {}", port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Rcon server interrupted", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

   public ProxyServer getProxyServer() {
     return velasus.getProxyServer();
    }

}