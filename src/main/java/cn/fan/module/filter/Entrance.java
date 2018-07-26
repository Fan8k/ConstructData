package cn.fan.module.filter;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;

public class Entrance {
    public static void main(String[] args) {
        System.out.println("开始处理");
        try {
            ExtractPom.AccessDirectory(new File("/home/inspur/zfy/java_repository/prior_repository"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
