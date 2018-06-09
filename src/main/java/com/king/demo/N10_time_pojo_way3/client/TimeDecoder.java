package com.king.demo.N10_time_pojo_way3.client;

import com.king.demo.N10_time_pojo_way3.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class TimeDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
