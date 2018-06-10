package com.king.demo.N11_factorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.math.BigInteger;
import java.util.List;

public class BigIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 前缀代表正文内容的大小,
        // 如果可读取部分不够,
        // 那么就直接返回
        // 直到够前缀大小位置
        if (in.readableBytes() < 5) {
            return;
        }

        // 记录下当前reader的位置.
        in.markReaderIndex();

        int magicNumber = in.readUnsignedByte();
        if (magicNumber != 'F') {
            // 根据刚才备份的reader位置
            // 在这里进行恢复
            in.resetReaderIndex();

            throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
        }

        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] decode = new byte[dataLength];
        in.readBytes(decode);
        out.add(new BigInteger(decode));
    }
}
