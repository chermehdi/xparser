package com.mehdi.xparser.core;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

/**
 * abstraction over Element xml nodes
 */
public class XNode {

    private Node node;
    private NamedNodeMap nodeMap;

    public XNode(Node node) {
        this.node = node;
        this.nodeMap = node.getAttributes();
    }

    public XNode(String source) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(source);
            node = document.getFirstChild();
            while (node.getNodeType() != Node.ELEMENT_NODE) {
                node = node.getNextSibling();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public XNode(InputStream source) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(source);
            node = document.getFirstChild();
            while (node.getNodeType() != Node.ELEMENT_NODE) {
                node = node.getNextSibling();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<XNode> getChildren() {
        List<XNode> nodes = new Vector<>();
        NodeList authorsList = node.getChildNodes();
        for (int i = 0; i < authorsList.getLength(); i++) {
            Node cur = authorsList.item(i);
            if (cur.getNodeType() == Node.ELEMENT_NODE)
                nodes.add(new XNode(cur));
        }

        return nodes;
    }

    public List<XNode> getChildren(String name) {
        List<XNode> nodes = new Vector<>();
        NodeList childList = node.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node cur = childList.item(i);
            if (cur.getNodeType() != Node.ELEMENT_NODE
                    || !cur.getNodeName().equals(name)) // the test could be implicit
                continue;
            nodes.add(new XNode(cur));
        }
        return nodes;
    }

    public XNode getChild(String name) {
        NodeList authorsList = node.getChildNodes();
        for (int i = 0; i < authorsList.getLength(); i++) {
            Node cur = authorsList.item(i);
            if (!cur.getNodeName().equals(name))
                continue;
            return new XNode(cur);
        }

        return null;
    }

    public String getAttribute(String name) {
        Node current = nodeMap.getNamedItem(name);
        if (current == null)
            return null;
        return current.getNodeValue();
    }

    public Integer getIntAttribute(String name) {
        Node current = nodeMap.getNamedItem(name);
        if (current == null)
            return null;
        try {
            return Integer.parseInt(current.getNodeValue());
        } catch (Exception e) {
            throw new RuntimeException("could not parse value named " + name + " \n value " + current.getNodeValue() + " is not an integer .");
        }
    }

    public Double getDoubleAttribute(String name) {
        Node current = nodeMap.getNamedItem(name);
        if (current == null)
            return null;
        try {
            return Double.parseDouble(current.getNodeValue());
        } catch (Exception e) {
            throw new RuntimeException("could not parse double");
        }
    }

    public String getValue() {
        Node cur = node.getFirstChild();
        if (cur == null) {
            return "";
        }
        return cur.getNodeValue();
    }

    public List<XNode> select(String keyword) {
        String key = keyword.toLowerCase();
        List<XNode> ret = new Vector<>();
        NodeList authorsList = node.getChildNodes();
        for (int i = 0; i < authorsList.getLength(); i++) {
            Node cur = authorsList.item(i);
            if (cur.getNodeType() == Node.ELEMENT_NODE) {
                XNode node = new XNode(cur);
                if (node.contains(key)) {
                    ret.add(node);
                }
            }
        }
        return ret;
    }

    private boolean contains(String keyword) {
        if (getValue().toLowerCase().contains(keyword))
            return true;
        List<XNode> children = getChildren();
        for (XNode child : children) {
            if (child.contains(keyword))
                return true;
        }
        return false;
    }

    public String toString() {
        return "<XNode>" + String.valueOf(getValue()) + "</XNode>";
    }

}
