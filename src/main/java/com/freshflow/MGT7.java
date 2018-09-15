package com.freshflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MGT7 {
    public void addCompany(Document document) {
        Node company = document.getElementsByTagName("T_ZNCA_MGT_7_S3").item(0);
        Node noCompanies = document.getElementsByTagName("NO_COMPANIES").item(0).getFirstChild();
        int numChildren = Integer.parseInt(noCompanies.getNodeValue());

        System.out.println(numChildren);

        Element data = document.createElement("DATA");
        Element serialNum = document.createElement("S_NO");
        serialNum.appendChild(document.createTextNode(Integer.toString(numChildren + 1)));
        data.appendChild(serialNum);

        Element companyName = document.createElement("NAME_COMPANY");
        companyName.appendChild(document.createTextNode("Test"));
        data.appendChild(companyName);

        Element cinFcrn = document.createElement("CIN_FCRN");
        cinFcrn.appendChild(document.createTextNode("T00000MH0000PLC000000"));
        data.appendChild(cinFcrn);

        Element holdSubAssoc = document.createElement("HOLD_SUB_ASSOC");
        holdSubAssoc.appendChild(document.createTextNode("HOLD"));
        data.appendChild(holdSubAssoc);

        Element percent = document.createElement("PERCENT_SHARE");
        percent.appendChild(document.createTextNode("100.00000000"));
        data.appendChild(percent);

        noCompanies.setNodeValue(Integer.toString(numChildren + 1));

        company.appendChild(data);
    }
}
