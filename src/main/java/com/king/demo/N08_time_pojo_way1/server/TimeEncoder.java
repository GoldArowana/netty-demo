package com.king.demo.N08_time_pojo_way1.server;

import com.king.demo.N08_time_pojo_way1.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        UnixTime time = (UnixTime) msg;
        ByteBuf buf = ctx.alloc().buffer(4);
        buf.writeInt((int) time.value());
        ctx.writeAndFlush(buf);
        System.out.println("向客户端发送时间:" + time.toString());
    }
}
