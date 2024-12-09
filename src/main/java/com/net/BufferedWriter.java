package com.net;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class BufferedWriter implements AutoCloseable {
    private final FileChannel channel;
    private final RingBuffer buffer;
    private boolean isClosed = false;
    
    public BufferedWriter(FileChannel channel) {
        this(channel, 8192); // 默认8KB缓冲区
    }
    
    public BufferedWriter(FileChannel channel, int bufferSize) {
        this.channel = channel;
        this.buffer = new RingBuffer(bufferSize);
    }
    
    public void write(byte[] data, int offset, int length) throws IOException {
        checkClosed();
        
        int written = 0;
        while (written < length) {
            // 如果缓冲区已满,需要先刷新
            if (buffer.remain() == 0) {
                flushInternal(length - written);
            }
            
            int writeSize = buffer.write(data, offset + written, length - written);
            written += writeSize;
        }
    }
    
    public void flush() throws IOException {
        checkClosed();
        
        while (buffer.size() > 0) {
            flushInternal(buffer.size());
        }
    }
    
    private void flushInternal(int size) throws IOException {
        byte[] tempBuffer = new byte[size];
        int readSize = buffer.read(tempBuffer, 0, size);
        
        int written = 0;
        while (written < readSize) {
            int writeSize = channel.write(ByteBuffer.wrap(tempBuffer, written, readSize - written));
            if (writeSize < 0) {
                throw new IOException("Failed to write to channel");
            }
            written += writeSize;
        }
    }
    
    @Override
    public void close() throws IOException {
        if (!isClosed) {
            flush();
            isClosed = true;
        }
    }
    
    private void checkClosed() throws IOException {
        if (isClosed) {
            throw new IOException("Writer is closed");
        }
    }
}