package com.espendwise.tools.gencode.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class JoinDbBaseXML extends Task {
  
    private String inputFiles;
    private String outputFile;
    private String rootElement;

    public String getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(String inputFiles) {
        this.inputFiles = inputFiles;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getRootElement() {
        return rootElement;
    }

    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }

    public void execute() throws BuildException {
        try {
            
            String[] inputFiles = getInputFiles().split(",");
          
            log("execute()=> Checking input files...");
           
            for (String inputFile : inputFiles) {
                File f = new File(inputFile);
                if (!f.exists()) {
                    throw new IllegalArgumentException("File '" + inputFile + "' doesn't exist!");
                } else {
                    log("execute()=> \tfile '" + inputFile + "' exists.");
                }
            }
          
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document documentMain = builder.newDocument();
          
            Node databaseNode = documentMain.createElement(getRootElement());
           
            documentMain.appendChild(databaseNode);
           
            for (String inputFile : inputFiles) {
               
                Document document = builder.parse(inputFile);
                NodeList nodes = document.getDocumentElement().getChildNodes();
              
                for (int i = 0; nodes != null && i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    if (node instanceof Element) {
                        Node newNode = documentMain.importNode(node, true);
                        databaseNode.appendChild(newNode);
                    }
                }
                
            }
            
            Source source = new DOMSource(documentMain);            
            File outputFile = new File(getOutputFile());
           
            Result result = new StreamResult(outputFile);
           
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        
        } catch (Exception e) {       
            throw new BuildException(e);
        }
    }
}
