package com.database;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("欢迎使用简单数据库演示");
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("你选择了选项1");
                    break;
                case "2":
                    System.out.println("你选择了选项2");
                    break;
                case "3":
                    System.out.println("再见!");
                    return;
                default:
                    System.out.println("无效的选择，请重试");
            }
        }
    }
    
    private static void printMenu() {
        System.out.println("\n请选择操作:");
        System.out.println("1. 选项1");
        System.out.println("2. 选项2");
        System.out.println("3. 退出");
        System.out.print("请输入选择 (1-3): ");
    }
} 