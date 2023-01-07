package me.lzn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.lzn.server.init.Socks5HandlerInitializer;

public class ServerBootstrapClient {

    private static final int PORT= 8888;
    private static final String LISTEN_ADDR = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup acceptLoopGroup = null;
        NioEventLoopGroup handleIoLoopGroup = null;
        try {
            acceptLoopGroup = new NioEventLoopGroup();
            handleIoLoopGroup = new NioEventLoopGroup();

        ServerBootstrap clientBootstrap = new ServerBootstrap();
        clientBootstrap.group(acceptLoopGroup, handleIoLoopGroup)
                       .channel(NioServerSocketChannel.class)
                .localAddress(LISTEN_ADDR, PORT)
                       .childHandler(new Socks5HandlerInitializer());

            ChannelFuture future = clientBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("clientServer-bootstrap have error:" + e.getMessage());
            close(acceptLoopGroup, handleIoLoopGroup);
        }
    }

    public static void close(NioEventLoopGroup acceptor,NioEventLoopGroup worker) {
        if (!acceptor.isTerminated()) {
            acceptor.shutdownGracefully();
        }
        acceptor = null;

        if (!worker.isTerminated()) {
            worker.shutdownGracefully();
        }
        worker = null;
    }
}
