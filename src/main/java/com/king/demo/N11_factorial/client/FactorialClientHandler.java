package com.king.demo.N11_factorial.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FactorialClientHandler extends SimpleChannelInboundHandler<BigInteger> {
    final BlockingQueue<BigInteger> answer = new LinkedBlockingQueue<BigInteger>();
    private int next = 1;
    private int receivedMessages;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        sendNumbers();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
        receivedMessages++;
        if (receivedMessages == FactorialClient.COUNT) {
            // Offer the answer after closing the connection.
            ctx.channel().close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    answer.offer(msg);
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public BigInteger getFactorial() {
        boolean interrupted = false;
        try {
            for (; ; ) {
                try {
                    BigInteger result;
                    if ((result = answer.take()) != null) {
                        return result;
                    }
                } catch (InterruptedException ignore) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendNumbers() {
        for (int i = 0; i < 4096 && next <= FactorialClient.COUNT; i++) {
            ctx.write(next);
            next++;
        }
        ctx.flush();
    }
}
