package com.king.demo.N12_file.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;

public class FileClientHandler extends SimpleChannelInboundHandler<String> {

    private String dest;

    public FileClientHandler(String dest) {
        this.dest = dest;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        File file = new File(dest);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);

        fos.write(msg.getBytes());
        fos.close();
    }

}
