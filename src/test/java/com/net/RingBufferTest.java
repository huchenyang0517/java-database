package com.net;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest {
    
    @Test
    public void testInitialization() {
        RingBuffer buffer = new RingBuffer(1024);
        assertEquals(0, buffer.size());
        assertEquals(1024, buffer.remain());
    }
    
    @Test
    public void testDefaultConstructor() {
        RingBuffer buffer = new RingBuffer();
        assertEquals(0, buffer.size());
        assertEquals(8192, buffer.remain());
    }
    
    @Test
    public void testWriteAndRead() {
        RingBuffer buffer = new RingBuffer(8);
        byte[] writeData = {1, 2, 3, 4};
        byte[] readData = new byte[4];
        
        // 测试写入
        int written = buffer.write(writeData, 0, writeData.length);
        assertEquals(4, written);
        assertEquals(4, buffer.size());
        assertEquals(4, buffer.remain());
        
        // 测试读取
        int read = buffer.read(readData, 0, readData.length);
        assertEquals(4, read);
        assertArrayEquals(writeData, readData);
        assertEquals(0, buffer.size());
        assertEquals(8, buffer.remain());
    }
    
    @Test
    public void testCircularWriteAndRead() {
        RingBuffer buffer = new RingBuffer(4);
        byte[] writeData = {1, 2, 3, 4};
        byte[] readData = new byte[2];
        
        // 写满缓冲区
        int written = buffer.write(writeData, 0, writeData.length);
        assertEquals(4, written);
        
        // 读取部分数据
        int read = buffer.read(readData, 0, 2);
        assertEquals(2, read);
        assertArrayEquals(new byte[]{1, 2}, readData);
        
        // 写入新数据，测试循环写入
        byte[] newData = {5, 6};
        written = buffer.write(newData, 0, newData.length);
        assertEquals(2, written);
        
        // 读取剩余数据，验证循环读取
        byte[] finalRead = new byte[4];
        read = buffer.read(finalRead, 0, 4);
        assertEquals(4, read);
        assertArrayEquals(new byte[]{3, 4, 5, 6}, finalRead);
    }
    
    @Test
    public void testOverflow() {
        RingBuffer buffer = new RingBuffer(4);
        byte[] writeData = {1, 2, 3, 4, 5, 6};
        
        // 尝试写入超过缓冲区大小的数据
        int written = buffer.write(writeData, 0, writeData.length);
        assertEquals(4, written); // 应该只写入4个字节
        assertEquals(4, buffer.size());
        assertEquals(0, buffer.remain());
    }
    
    @Test
    public void testEmptyRead() {
        RingBuffer buffer = new RingBuffer(4);
        byte[] readData = new byte[4];
        
        // 从空缓冲区读取
        int read = buffer.read(readData, 0, readData.length);
        assertEquals(0, read);
    }
    
    @Test
    public void testPartialWrite() {
        RingBuffer buffer = new RingBuffer(4);
        byte[] writeData = {1, 2};
        
        // 部分写入
        int written = buffer.write(writeData, 0, writeData.length);
        assertEquals(2, written);
        assertEquals(2, buffer.size());
        assertEquals(2, buffer.remain());
    }
} 