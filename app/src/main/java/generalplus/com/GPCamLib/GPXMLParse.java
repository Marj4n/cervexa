package generalplus.com.GPCamLib;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: classes2.dex */
public class GPXMLParse {
    public static int CaptureResolution_Setting_ID = 256;
    public static int CategoryLevel = 12;
    public static int RecordResolution_Setting_ID = 0;
    public static int SettingLevel = 6;
    public static int ValueLevel = 0;
    public static int Version_Setting_ID = 521;
    public static int Version_Value_Index;
    private static ArrayList<GPXMLSetting> m_aryListGPXMLSettings;
    private static ArrayList<GPXMLValue> m_aryListGPXMLValues;
    private GPXMLCategory m_GPXMLCategory;
    private GPXMLSetting m_GPXMLSetting;
    private GPXMLValue m_GPXMLValue;
    private static ArrayList<GPXMLCategory> m_aryListGPXMLCategroies = new ArrayList<>();
    private static String GPTag = "GPXMLParseLog";

    public GPXMLParse() {
        if (m_aryListGPXMLValues == null) {
            m_aryListGPXMLValues = new ArrayList<>();
        }
        if (m_aryListGPXMLSettings == null) {
            m_aryListGPXMLSettings = new ArrayList<>();
        }
        if (m_aryListGPXMLCategroies == null) {
            m_aryListGPXMLCategroies = new ArrayList<>();
        }
    }

    public ArrayList<GPXMLCategory> GetCategories() {
        m_aryListGPXMLValues.clear();
        m_aryListGPXMLSettings.clear();
        m_aryListGPXMLCategroies.clear();
        return m_aryListGPXMLCategroies;
    }

    public class GPXMLValue {
        public int i32TreeLevel;
        public String strXMLValueID;
        public String strXMLValueName;

        public GPXMLValue(String str, String str2, int i) {
            this.strXMLValueName = str;
            this.strXMLValueID = str2;
            this.i32TreeLevel = i;
        }
    }

    public class GPXMLSetting {
        public ArrayList<GPXMLValue> aryListGPXMLValues;
        public int i32TreeLevel;
        public String strXMLSettingCurrent;
        public String strXMLSettingDefaultValue;
        public String strXMLSettingID;
        public String strXMLSettingName;
        public String strXMLSettingReflash;
        public String strXMLSettingType;

        public GPXMLSetting(String str, String str2, String str3, String str4, String str5, int i, ArrayList<GPXMLValue> arrayList) {
            this.strXMLSettingCurrent = null;
            this.strXMLSettingName = str;
            this.strXMLSettingID = str2;
            this.strXMLSettingType = str3;
            this.strXMLSettingReflash = str4;
            this.strXMLSettingDefaultValue = str5;
            if (arrayList != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    }
                    if (arrayList.get(i2).strXMLValueID.equalsIgnoreCase(str5)) {
                        this.strXMLSettingCurrent = arrayList.get(i2).strXMLValueName;
                        break;
                    }
                    i2++;
                }
            }
            this.i32TreeLevel = i;
            this.aryListGPXMLValues = (ArrayList) arrayList.clone();
        }
    }

    public class GPXMLCategory {
        public ArrayList<GPXMLSetting> aryListGPXMLSettings;
        public String strXMLCategoryName;
        public int i32TreeLevel = this.i32TreeLevel;

        public GPXMLCategory(String str, int i, ArrayList<GPXMLSetting> arrayList) {
            this.strXMLCategoryName = str;
            this.aryListGPXMLSettings = (ArrayList) arrayList.clone();
        }
    }

    public ArrayList<GPXMLCategory> GetGPXMLInfo(String str) {
        String str2;
        int i;
        NodeList nodeList;
        String str3;
        int i2;
        NodeList nodeList2;
        int i3;
        NodeList nodeList3;
        String str4;
        int i4;
        NodeList nodeList4;
        String str5;
        int i5;
        String str6;
        String str7;
        String str8 = "ID";
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(str)).getElementsByTagName("Categories");
            m_aryListGPXMLCategroies.clear();
            int i6 = 0;
            int i7 = 0;
            while (i7 < elementsByTagName.getLength()) {
                Node item = elementsByTagName.item(i7);
                short s = 1;
                if (item.getNodeType() == 1) {
                    NodeList elementsByTagName2 = ((Element) item).getElementsByTagName("Category");
                    int i8 = 0;
                    while (i8 < elementsByTagName2.getLength()) {
                        Node item2 = elementsByTagName2.item(i8);
                        if (item2.getNodeType() == s) {
                            Element element = (Element) item2;
                            m_aryListGPXMLSettings.clear();
                            NodeList elementsByTagName3 = element.getElementsByTagName("Name");
                            String nodeValue = elementsByTagName3.getLength() > 0 ? ((Element) elementsByTagName3.item(i6)).getChildNodes().item(i6).getNodeValue() : "";
                            NodeList elementsByTagName4 = element.getElementsByTagName("Settings");
                            int i9 = 0;
                            while (i9 < elementsByTagName4.getLength()) {
                                Node item3 = elementsByTagName4.item(i9);
                                if (item3.getNodeType() == s) {
                                    NodeList elementsByTagName5 = ((Element) item3).getElementsByTagName("Setting");
                                    int i10 = 0;
                                    while (i10 < elementsByTagName5.getLength()) {
                                        Node item4 = elementsByTagName5.item(i10);
                                        if (item4.getNodeType() == s) {
                                            Element element2 = (Element) item4;
                                            m_aryListGPXMLValues.clear();
                                            NodeList elementsByTagName6 = element2.getElementsByTagName("Name");
                                            String nodeValue2 = elementsByTagName6.getLength() > 0 ? ((Element) elementsByTagName6.item(i6)).getChildNodes().item(i6).getNodeValue() : "";
                                            NodeList elementsByTagName7 = element2.getElementsByTagName(str8);
                                            String nodeValue3 = elementsByTagName7.getLength() > 0 ? ((Element) elementsByTagName7.item(i6)).getChildNodes().item(i6).getNodeValue() : "";
                                            NodeList elementsByTagName8 = element2.getElementsByTagName("Type");
                                            if (elementsByTagName8.getLength() > 0) {
                                                nodeList2 = elementsByTagName5;
                                                str5 = ((Element) elementsByTagName8.item(0)).getChildNodes().item(0).getNodeValue();
                                            } else {
                                                nodeList2 = elementsByTagName5;
                                                str5 = "";
                                            }
                                            NodeList elementsByTagName9 = element2.getElementsByTagName("Reflash");
                                            if (elementsByTagName9.getLength() > 0) {
                                                i5 = i9;
                                                str6 = ((Element) elementsByTagName9.item(0)).getChildNodes().item(0).getNodeValue();
                                            } else {
                                                i5 = i9;
                                                str6 = "";
                                            }
                                            NodeList elementsByTagName10 = element2.getElementsByTagName("Default");
                                            String nodeValue4 = elementsByTagName10.getLength() > 0 ? ((Element) elementsByTagName10.item(0)).getChildNodes().item(0).getNodeValue() : "";
                                            NodeList elementsByTagName11 = element2.getElementsByTagName("Values");
                                            int i11 = 0;
                                            while (i11 < elementsByTagName11.getLength()) {
                                                Node item5 = elementsByTagName11.item(i11);
                                                NodeList nodeList5 = elementsByTagName11;
                                                NodeList nodeList6 = elementsByTagName4;
                                                if (item5.getNodeType() == 1) {
                                                    NodeList elementsByTagName12 = ((Element) item5).getElementsByTagName("Value");
                                                    int i12 = 0;
                                                    while (i12 < elementsByTagName12.getLength()) {
                                                        Node item6 = elementsByTagName12.item(i12);
                                                        NodeList nodeList7 = elementsByTagName12;
                                                        String str9 = nodeValue;
                                                        if (item6.getNodeType() == 1) {
                                                            Element element3 = (Element) item6;
                                                            NodeList elementsByTagName13 = element3.getElementsByTagName("Name");
                                                            String nodeValue5 = elementsByTagName13.getLength() > 0 ? ((Element) elementsByTagName13.item(0)).getChildNodes().item(0).getNodeValue() : "";
                                                            NodeList elementsByTagName14 = element3.getElementsByTagName(str8);
                                                            str7 = str8;
                                                            GPXMLValue gPXMLValue = new GPXMLValue(nodeValue5, elementsByTagName14.getLength() > 0 ? ((Element) elementsByTagName14.item(0)).getChildNodes().item(0).getNodeValue() : "", ((1 << CategoryLevel) * i8) + ((1 << SettingLevel) * i10) + ((1 << ValueLevel) * i12));
                                                            this.m_GPXMLValue = gPXMLValue;
                                                            m_aryListGPXMLValues.add(gPXMLValue);
                                                            this.m_GPXMLValue = null;
                                                        } else {
                                                            str7 = str8;
                                                        }
                                                        i12++;
                                                        elementsByTagName12 = nodeList7;
                                                        nodeValue = str9;
                                                        str8 = str7;
                                                    }
                                                }
                                                i11++;
                                                elementsByTagName11 = nodeList5;
                                                elementsByTagName4 = nodeList6;
                                                nodeValue = nodeValue;
                                                str8 = str8;
                                            }
                                            str3 = str8;
                                            i2 = i10;
                                            String str10 = nodeValue2;
                                            String str11 = nodeValue3;
                                            i3 = i5;
                                            String str12 = str5;
                                            nodeList3 = elementsByTagName4;
                                            str4 = nodeValue;
                                            i4 = i8;
                                            nodeList4 = elementsByTagName2;
                                            GPXMLSetting gPXMLSetting = new GPXMLSetting(str10, str11, str12, str6, nodeValue4, (i8 << CategoryLevel) | (i10 << SettingLevel), m_aryListGPXMLValues);
                                            this.m_GPXMLSetting = gPXMLSetting;
                                            m_aryListGPXMLSettings.add(gPXMLSetting);
                                            this.m_GPXMLSetting = null;
                                        } else {
                                            str3 = str8;
                                            i2 = i10;
                                            nodeList2 = elementsByTagName5;
                                            i3 = i9;
                                            nodeList3 = elementsByTagName4;
                                            str4 = nodeValue;
                                            i4 = i8;
                                            nodeList4 = elementsByTagName2;
                                        }
                                        i10 = i2 + 1;
                                        elementsByTagName4 = nodeList3;
                                        i9 = i3;
                                        elementsByTagName5 = nodeList2;
                                        i8 = i4;
                                        elementsByTagName2 = nodeList4;
                                        str8 = str3;
                                        nodeValue = str4;
                                        i6 = 0;
                                        s = 1;
                                    }
                                }
                                i9++;
                                elementsByTagName4 = elementsByTagName4;
                                i8 = i8;
                                elementsByTagName2 = elementsByTagName2;
                                str8 = str8;
                                nodeValue = nodeValue;
                                i6 = 0;
                                s = 1;
                            }
                            str2 = str8;
                            i = i8;
                            nodeList = elementsByTagName2;
                            GPXMLCategory gPXMLCategory = new GPXMLCategory(nodeValue, i << CategoryLevel, m_aryListGPXMLSettings);
                            this.m_GPXMLCategory = gPXMLCategory;
                            m_aryListGPXMLCategroies.add(gPXMLCategory);
                            this.m_GPXMLCategory = null;
                        } else {
                            str2 = str8;
                            i = i8;
                            nodeList = elementsByTagName2;
                        }
                        i8 = i + 1;
                        elementsByTagName2 = nodeList;
                        str8 = str2;
                        i6 = 0;
                        s = 1;
                    }
                }
                i7++;
                str8 = str8;
                i6 = 0;
            }
        } catch (Exception e) {
            Log.e(GPTag, e.getMessage());
        }
        return m_aryListGPXMLCategroies;
    }
}
