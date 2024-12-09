package com.net;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class BufferedWriterTest {
    
    @Test
    void testBasicWriteAndFlush(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        
        try (FileChannel channel = FileChannel.open(file, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.WRITE);
             BufferedWriter writer = new BufferedWriter(channel, 4)) {
            
            byte[] data = {1, 2, 3, 4};
            writer.write(data, 0, data.length);
            writer.flush();
            
            // 验证写入的数据
            try (FileChannel readChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                ByteBuffer readBuffer = ByteBuffer.allocate(4);
                int bytesRead = readChannel.read(readBuffer);
                readBuffer.flip();
                
                assertEquals(4, bytesRead);
                byte[] readData = new byte[4];
                readBuffer.get(readData);
                assertArrayEquals(data, readData);
            }
        }
    }
    
    @Test
    void testLargeWrite(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("large.txt");
        int bufferSize = 4;
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        
        try (FileChannel channel = FileChannel.open(file, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.WRITE);
             BufferedWriter writer = new BufferedWriter(channel, bufferSize)) {
            
            writer.write(data, 0, data.length);
            writer.flush();
            
            // 验证写入的数据
            try (FileChannel readChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                ByteBuffer readBuffer = ByteBuffer.allocate(8);
                int bytesRead = readChannel.read(readBuffer);
                readBuffer.flip();
                
                assertEquals(8, bytesRead);
                byte[] readData = new byte[8];
                readBuffer.get(readData);
                assertArrayEquals(data, readData);
            }
        }
    }
    
    @Test
    void testWriteAfterClose(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("closed.txt");
        
        BufferedWriter writer = new BufferedWriter(
            FileChannel.open(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        writer.close();
        
        assertThrows(IOException.class, () -> {
            writer.write(new byte[]{1}, 0, 1);
        });
    }
} 