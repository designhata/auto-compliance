package com.freshflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import java.util.Scanner;

public class MGT7 {
    private void addParentNodeForData(Document document, Scanner scanner, String parent, String child, String[] children, String[] questions, boolean isSN) {
        int numChildren = 0;
        Node noChildren = null;
        Node parentNode = document.getElementsByTagName(parent).item(0);
        Element element = null;

        if (child != null) {
            noChildren = document.getElementsByTagName(child).item(0).getFirstChild();
            numChildren = Integer.parseInt(noChildren.getNodeValue());
            noChildren.setNodeValue(Integer.toString(numChildren + 1));
        }

        if (numChildren == 0) {
            Node deleteData = ((Element) parentNode).getElementsByTagName("DATA").item(0);
            parentNode.removeChild(deleteData);
        }

        Element data = document.createElement("DATA");

        data.appendChild(document.createElement("MANDT"));
        data.appendChild(document.createElement("SERIAL_NUM"));
        data.appendChild(document.createElement("PARENT_GUID"));
        data.appendChild(document.createElement("SRN_NUM"));

        if (isSN) {
            element = document.createElement("S_NO");
            element.appendChild(document.createTextNode(Integer.toString(numChildren + 1)));
            data.appendChild(element);
        }

        for (int childIdx = 0; childIdx < children.length; childIdx++) {
            element = document.createElement(children[childIdx]);
            System.out.println(questions[childIdx]);
            String input = scanner.nextLine();
            System.out.println(input);
            element.appendChild(document.createTextNode(input));
            data.appendChild(element);
        }

        data.appendChild(document.createElement("R_SUB_FLAG"));
        data.appendChild(document.createElement("MIGRATE_FLAG"));
        data.appendChild(document.createElement("IP_ADDRESS"));
        parentNode.appendChild(data);
    }

    public void addCompany(Document document, Scanner scanner) {
        System.out.println("How many new companies have been added?");
        int noCompanies = scanner.nextInt();
        scanner.nextLine();
        String[] children = {"NAME_COMPANY", "CIN_FCRN", "HOLD_SUB_ASSOC", "PERCENT_SHARE"};
        String[] questions = {"Enter name of company:",
                "Enter CIN of company:", "Enter holding pattern for company: (HOLD - Holding, SUBS - Subsidiary)",
                "Enter share percentage of company (please append '.00000000' after value):"};

        for (int iter = 0; iter < noCompanies; iter++) {
            this.addParentNodeForData(document, scanner, "T_ZNCA_MGT_7_S3", "NO_BUSINESS_ACT",
                    children, questions, true);
        }
    }

    public void calculateDebentures(Document document) {
        document.getElementsByTagName("NO_UNITS_NCD").item(0).getFirstChild().setNodeValue("12.00000000");
        document.getElementsByTagName("NOM_VAL_UNIT_NCD").item(0).getFirstChild().setNodeValue("12");
        document.getElementsByTagName("TOTAL_VAL_NCD").item(0).getFirstChild().setNodeValue("144.00000000");

        float total = Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_NCD")
                .item(0).getFirstChild().getNodeValue())
                + Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_PCD")
                .item(0).getFirstChild().getNodeValue())
                + Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_FCD")
                .item(0).getFirstChild().getNodeValue())
                + Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_SLED")
                .item(0).getFirstChild().getNodeValue())
                + Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_ULED")
                .item(0).getFirstChild().getNodeValue())
                + Float.parseFloat(document.getElementsByTagName("TOTAL_VAL_DEP")
                .item(0).getFirstChild().getNodeValue());


        document.getElementsByTagName("TOT_TOTAL_VAL").item(0)
                .getFirstChild().setNodeValue(Float.toString(total));
    }

    public void addBusinessActivity(Document document, Scanner scanner) {
        System.out.println("How many new business activities have been added?");
        int noActivities = scanner.nextInt();
        scanner.nextLine();
        String[] children = {"MAIN_ACT_GRP_COD", "DES_MAIN_ACT_GRP", "BUSINESS_ACT_COD",
                "DES_BUSINESS_ACT", "PERCENT_TURN_OVR"};
        String[] questions = {"Please enter main activity group code: (A-U)",
                "Please enter description of the main activity group:",
                "Please enter business activity code based on given activity group:",
                "Please enter description of the business activity code:",
                "Please enter percent turn over of the business:"};

        for (int iter = 0; iter < noActivities; iter++) {
            this.addParentNodeForData(document, scanner, "T_ZNCA_MGT_7_S2", "NO_BUSINESS_ACT",
                    children, questions, true);
        }
    }
}
