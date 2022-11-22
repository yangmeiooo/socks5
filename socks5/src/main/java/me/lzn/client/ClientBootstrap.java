package me.lzn.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.lzn.client.init.ClientInitializer;

public class ClientBootstrap {

    private static final int PORT= 8888;
    private static final String LISTEN_ADDR = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup acceptLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup handleIoLoopGroup = new NioEventLoopGroup();

        ServerBootstrap clientBootstrap = new ServerBootstrap();
        clientBootstrap.group(acceptLoopGroup, handleIoLoopGroup)
                       .channel(NioServerSocketChannel.class)
                       .childHandler(new ClientInitializer());

        try {
            clientBootstrap.bind(LISTEN_ADDR,PORT).sync();
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
