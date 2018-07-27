package cn.fan.module.filter;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行linux命令
 */
public class LinuxOrder {
    /**
     *
     * @param pomXml 具体路径
     */
    public static void execrder(String pomXml){
        String orderStr = "mvn -f "+pomXml+" dependency:copy-dependencies";
        try {
            Process exec = Runtime.getRuntime().exec(orderStr);
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String sile = "";
            while((sile=reader.readLine())!=null){
                System.out.println(sile);
            }
            exec.destroy();
            //休息一会在执行
            Thread.sleep(2000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        execrder("/home/inspur/zfy/java_repository/pomFileCollection/haha/pom.xml");
    }
}
