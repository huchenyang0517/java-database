package com.net;

public class RingBuffer {
    private byte[] buffer;
    private int readPos = 0;  // 读取位置
    private int writePos = 0; // 写入位置
    private int size = 0;     // 当前数据大小
    
    public RingBuffer() {
        this(8192); // 默认8KB缓冲区
    }
    
    public RingBuffer(int capacity) {
        buffer = new byte[capacity];
    }
    
    public int size() {
        return size;
    }
    
    public int remain() {
        return buffer.length - size;
    }
    
    public int write(byte[] data, int offset, int length) {
        int writeSize = Math.min(length, remain());
        if (writeSize <= 0) {
            return 0;
        }
        
        // 写入到缓冲区末尾
        int firstPart = Math.min(writeSize, buffer.length - writePos);
        System.arraycopy(data, offset, buffer, writePos, firstPart);
        
        // 如果还有剩余数据,写入到缓冲区开始
        if (firstPart < writeSize) {
            System.arraycopy(data, offset + firstPart, buffer, 0, writeSize - firstPart);
        }
        
        writePos = (writePos + writeSize) % buffer.length;
        size += writeSize;
        return writeSize;
    }
    
    public int read(byte[] data, int offset, int length) {
        int readSize = Math.min(length, size);
        if (readSize <= 0) {
            return 0;
        }
        
        // 从缓冲区读取到末尾
        int firstPart = Math.min(readSize, buffer.length - readPos);
        System.arraycopy(buffer, readPos, data, offset, firstPart);
        
        // 如果还需要读取,从缓冲区开始读取
        if (firstPart < readSize) {
            System.arraycopy(buffer, 0, data, offset + firstPart, readSize - firstPart);
        }
        
        readPos = (readPos + readSize) % buffer.length;
        size -= readSize;
        return readSize;
    }
}