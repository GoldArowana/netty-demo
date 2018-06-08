package com.king.demo.N01_discard.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        //boosGroup:用来接收进来的连接。
        //workerGroup:用来处理已经被接收的连接
        //一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })

                    //服务端处理客户端连接请求是顺序处理的，
                    // 所以同一时间只能处理一个客户端连接，
                    // 多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，
                    // backlog参数指定了队列的大小
                    .option(ChannelOption.SO_BACKLOG, 128)

                    //用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，
                    // 这个选项用于可能长时间没有数据交流的连接。
                    // 当设置该选项以后，如果在两小时内没有数据的通信时，
                    // TCP会自动发送一个活动探测数据报文。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(PORT).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            System.out.println("server exception...");
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
