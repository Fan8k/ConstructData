package cn.fan.module.filter;


import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @description
 * @author fan
 * @time 2018/07/25
 */
public class ParserPomXml {
    /**
     * 添加一个dependency 修改pom头中的groupid version artificatid值！
     * @param values  规定的groupid version artificatid
     * @param xmlFilePath 需要修改的xml路径
     * @param aimXmlPath 最终会写入的xml文件的位置
     * @return 是否操作成功
     * @throws DocumentException xml文档解析出错
     * @throws IOException 文件流异常
     */
    public static boolean oneStopAddAndChange(String[] values,String xmlFilePath,String aimXmlPath) throws IOException {
        File file  =new File(xmlFilePath);
        if(file.exists()){
            SAXReader reader = new SAXReader();
            Document document = null;
            try {
                document = reader.read(file);
            } catch (DocumentException e) {
                return false;

            }
            Element rootElement = document.getRootElement();
            Element groupId = rootElement.element("groupId");
            Element artifactId = rootElement.element("artifactId");
            Element version = rootElement.element("version");

            if(groupId==null||artifactId==null||version==null){
                return false;
            }

            //先添加dependency
            Element dependencyManagement = rootElement.element("dependencyManagement");
            Element dependencies = null;
            if(dependencyManagement!=null) {
               dependencies = dependencyManagement.element("dependencies");
            }else{
                dependencies =rootElement.element("dependencies");
            }
            if(dependencies==null){
                return false;
            }
            Element dependency = dependencies.addElement("dependency", rootElement.getNamespace().getURI());
            Element groupIdNew = dependency.addElement("groupId", rootElement.getNamespace().getURI());
            groupIdNew.setText(groupId.getStringValue());
            Element artifactIdNew = dependency.addElement("artifactId", rootElement.getNamespace().getURI());
            artifactIdNew.setText(artifactId.getStringValue());
            Element versionNew = dependency.addElement("version", rootElement.getNamespace().getURI());
            versionNew.setText(version.getStringValue());

            //修改前面的版本
            groupId.setText(values[0]);
            artifactId.setText(values[1]);
            version.setText(values[2]);

            //写入文件流
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            File aimFile = new File(aimXmlPath);
            if(!aimFile.exists()){
                aimFile.createNewFile();
            }
            System.out.println(aimFile.getName());
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(aimFile),format);
            xmlWriter.write(document);
            xmlWriter.close();
            return true;
        }

        return false;
    }

    /**
     * 通过解析pom.xml 自动根据groupid artifactID version 返回一个depency字符串
     * @param XmlfilePath
     * @return
     */
    public static String constructedDependency(String XmlfilePath) throws DocumentException {
        StringBuilder stringBuilder = new StringBuilder("");
        File file  =new File(XmlfilePath);
        if(file.exists()){
            stringBuilder.append("<dependency>\n");
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            //根标签 project
            Element rootElement = document.getRootElement();
            Element groupId = rootElement.element("groupId");
            Element artifactId = rootElement.element("artifactId");
            Element version = rootElement.element("version");

            stringBuilder.append("\t<groupId>"+groupId.getStringValue()+"</groupId>\n");
            stringBuilder.append("\t<artifactId>"+artifactId.getStringValue()+"</artifactId>\n");
            stringBuilder.append("\t<version>"+version.getStringValue()+"</version>\n");
            stringBuilder.append("</dependency>");
        }
        return stringBuilder.toString();
    }

    /**
     * 把dependency 这个小型xml加入到一个pom文件中
     * @param xmlFile 目标xml文件 将来作为pom.xml进行
     * @param dependencyXMl
     * @return 是否添加成功
     * @throws DocumentException
     */
    public static boolean addDependencyToXml(String xmlFile,String dependencyXMl) throws DocumentException, IOException {
        Document document = DocumentHelper.parseText(dependencyXMl);
        File file  =new File(xmlFile);
        if(file.exists()) {
            SAXReader reader = new SAXReader();
            Document aimDocument = reader.read(file);
            //目标文件的根元素
            Element rootElement = aimDocument.getRootElement();
            Element dependeny = rootElement.element("dependencies");
            //目标dependency
            Element rootElement1 = document.getRootElement();
            dependeny.add(rootElement1);
            //写入文件
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(xmlFile)),format);
            xmlWriter.write(aimDocument);
            xmlWriter.close();
            return true;
        }
        return  false;
    }

    @Test
    public void test() throws DocumentException, URISyntaxException, IOException {
        String aimFile = Paths.get(this.getClass().getClassLoader().getResource("aim.xml").toURI()).toString();
        String a= Paths.get(this.getClass().getClassLoader().getResource("").toURI()).toString()+"/a.xml";
        String[] values = {"cn.fan","ConstructData","2.0"};
        oneStopAddAndChange(values,aimFile,a);
    }
}
