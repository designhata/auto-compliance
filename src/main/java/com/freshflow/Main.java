package com.freshflow;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import org.w3c.dom.Node;

import java.io.*;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Main {
    private static String source = "";
    private static String dest = "";
    private static PdfDocument pdfDocument;

    private XfaForm xfaForm;

    private static void printUsage() {
        System.err.println("Program takes two parameters:");
        System.err.println(" 1. Previously submitted filled form.");
        System.err.println(" 2. New form to be filled.");
        System.err.println("Parameters should be whole path to the files mentioned above.");
        System.err.println("Example:");
        System.err.println("javac C:\\testUser\\Documents\\previousform.pdf C:\\testUser\\Documents\\newform.pdf");
    }

    private static void parseInput(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }

        dest = args[1];
        source = args[0];
    }

    private void getXfa() throws IOException {
        PdfReader newReader = new PdfReader(source);
        newReader.setUnethicalReading(true);

        pdfDocument = new PdfDocument(newReader, new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        this.xfaForm = form.getXfaForm();
    }

    private Document getDomDocument() throws TransformerException {
        Document domDocument = this.xfaForm.getDomDocument();
        DOMSource domSource = new DOMSource(domDocument);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, result);
        writer.flush();

        return domDocument;
    }

    private Document getCurrentXMLData() throws ParserConfigurationException, TransformerException,
            SAXException, IOException {

        Node node = this.xfaForm.getDatasetsNode();
        Document doc = null;
        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getLocalName().equals("data")) {

                DOMSource domSource = new DOMSource(list.item(i)); // Gets the data value from <xml:data> node
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();

                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(domSource, result);
                writer.flush();

                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = db.parse(new InputSource(new StringReader(writer.toString())));
            }
        }

        return doc;
    }

    private void accessXMLData(Node element) {
        // Document document;
//        Node element = document.getElementsByTagName("data").item(0);
//        System.out.println(element.getTextContent());

        NodeList childNodes = element.getChildNodes();

        for (int nodeIdx = 0; nodeIdx < childNodes.getLength(); nodeIdx++) {
            Node node = childNodes.item(nodeIdx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
//                System.out.println(node.getNodeName());
                /*
                TODO: Access XML nodes only
                 */
            }
            if (node.hasChildNodes()) {
                accessXMLData(node);
            }
        }
    }

    private void writeToPdf(Document doc) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("C:/Users/HaTa/Desktop/new-MGT_7.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, result);

        pdfDocument = new PdfDocument(new PdfReader(source), new PdfWriter(dest));
        this.xfaForm.fillXfaForm(new FileInputStream("C:/Users/HaTa/Desktop/new-MGT_7.xml"));
        this.xfaForm.write(pdfDocument);
        pdfDocument.close();
    }

    public static void main(String[] args) {
        MGT7 mgt7 = new MGT7();

        parseInput(args);

        try {
            Scanner scanner = new Scanner(System.in);
            Main formHandler = new Main();

            // Retrieve XFA form in existing pdf
            formHandler.getXfa();

            // Extract DOM document from XFA form
//            Document doc = formHandler.getDomDocument();

//          // Extract XML data from existing pdf into iterable document for modification
            Document document = formHandler.getCurrentXMLData();

//          // Access XML data for modification
            formHandler.accessXMLData(document.getDocumentElement());
//
            System.out.println("Have you added any new company (y/n)?");
            if (scanner.nextLine().compareToIgnoreCase("y") == 0) {
                mgt7.addCompany(document, scanner);
            }
            mgt7.calculateDebentures(document);
            mgt7.getTurnoverAndNetworth(document);
            mgt7.setComplianceDisclosure(document);

            System.out.println("Have you added any new line of business (y/n)?");
            if (scanner.nextLine().compareToIgnoreCase("y") == 0) {
                mgt7.addBusinessActivity(document, scanner);
            }


            formHandler.writeToPdf(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
