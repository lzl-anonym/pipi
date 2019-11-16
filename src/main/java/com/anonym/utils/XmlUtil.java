package com.anonym.utils;

import com.alibaba.fastjson.JSON;
import com.anonym.module.user.basic.domain.UserDTO;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * xml 工具类
 */
public class XmlUtil {

    /**
     * 生成微信 xml 格式的参数
     *
     * @param param
     */
    public static String generateXml(String rootNode, Map<String, Object> param) {
        Element root = new Element(rootNode);
        Document document = new Document(root);
        Set<String> keySet = param.keySet();
        Element element;
        Object obj;
        for (String key : keySet) {
            element = new Element(key);
            obj = param.get(key);
            if (null == obj) {
                continue;
            }

            element.setText(String.valueOf(obj));
            root.addContent(element);
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            new XMLOutputter().output(document, bo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bo.toString().trim();
    }

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "listen");
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone("<13592027440>");
        userDTO.setUserId(2008L);
        map.put("info", JSON.toJSONString(userDTO));
        String xml = XmlUtil.generateXml("xml", map);
        System.out.println(xml);
    }

    public static TreeMap<String, String> parseXml(String xml) {
        TreeMap<String, String> rtnMap = new TreeMap<>();
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(new StringReader(xml));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 得到根节点
        Element root = doc.getRootElement();
        String rootName = root.getName();
        //  rtnMap.put("root.name", rootName);
        // 调用递归函数，得到所有最底层元素的名称和值，加入map中
        convert(root, rtnMap, rootName);
        return rtnMap;
    }

    @SuppressWarnings("rawtypes")
    public static void convert(Element e, TreeMap<String, String> map, String lastname) {
        if (e.getAttributes().size() > 0) {
            Iterator it_attr = e.getAttributes().iterator();
            while (it_attr.hasNext()) {
                Attribute attribute = (Attribute) it_attr.next();
                String attrname = attribute.getName();
                String attrvalue = e.getAttributeValue(attrname);
                // map.put( attrname, attrvalue);
                map.put(lastname + "." + attrname, attrvalue); // key 根据根节点 进行生成
            }
        }
        List children = e.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element child = (Element) it.next();
            /* String name = lastname + "." + child.getName(); */
            String name = child.getName();
            // 如果有子节点，则递归调用
            if (child.getChildren().size() > 0) {
                convert(child, map, lastname + "." + child.getName());
            } else {
                // 如果没有子节点，则把值加入map
                map.put(name, child.getText());
                // 如果该节点有属性，则把所有的属性值也加入map
                if (child.getAttributes().size() > 0) {
                    Iterator attr = child.getAttributes().iterator();
                    while (attr.hasNext()) {
                        Attribute attribute = (Attribute) attr.next();
                        String attrname = attribute.getName();
                        String attrvalue = child.getAttributeValue(attrname);
                        map.put(lastname + "." + child.getName() + "." + attrname, attrvalue);
                    }
                }
            }
        }
    }

}
