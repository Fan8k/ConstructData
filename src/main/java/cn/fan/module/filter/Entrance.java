package cn.fan.module.filter;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

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
        System.out.println("end");
        HashMap<String, String> projectToPom = ExtractPom.getProjectToPom();
        Collection<String> values = projectToPom.values();
        for(String s:values){
            System.out.println("开始执行mvn "+s);
            LinuxOrder.execrder(s);
        }
    }
}
