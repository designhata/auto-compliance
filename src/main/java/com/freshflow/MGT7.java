package com.freshflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

public class MGT7 {
    public void addCompany(Document document) {
        Node company = document.getElementsByTagName("T_ZNCA_MGT_7_S3").item(0);
        Node noCompanies = document.getElementsByTagName("NO_COMPANIES").item(0).getFirstChild();
        int numChildren = Integer.parseInt(noCompanies.getNodeValue());

        if (numChildren == 0) {
            Node deleteData = ((Element) company).getElementsByTagName("DATA").item(0);
            company.removeChild(deleteData);
        }

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
}
