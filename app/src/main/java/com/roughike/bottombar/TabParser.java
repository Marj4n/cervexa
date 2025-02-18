package com.roughike.bottombar;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import com.roughike.bottombar.BottomBarTab;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
class TabParser {
    private static final int AVG_NUMBER_OF_TABS = 5;
    private static final int COLOR_NOT_SET = -1;
    private static final int RESOURCE_NOT_FOUND = 0;
    private static final String TAB_TAG = "tab";
    private final Context context;
    private final BottomBarTab.Config defaultTabConfig;
    private final XmlResourceParser parser;
    private List<BottomBarTab> tabs = null;

    public static class TabParserException extends RuntimeException {
    }

    TabParser(Context context, BottomBarTab.Config config, int i) {
        this.context = context;
        this.defaultTabConfig = config;
        this.parser = context.getResources().getXml(i);
    }

    public List<BottomBarTab> parseTabs() {
        int next;
        if (this.tabs == null) {
            this.tabs = new ArrayList(5);
            do {
                try {
                    next = this.parser.next();
                    if (next == 2 && TAB_TAG.equals(this.parser.getName())) {
                        this.tabs.add(parseNewTab(this.parser, this.tabs.size()));
                    }
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    throw new TabParserException();
                }
            } while (next != 1);
        }
        return this.tabs;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private BottomBarTab parseNewTab(XmlResourceParser xmlResourceParser, int i) {
        char c;
        BottomBarTab tabWithDefaults = tabWithDefaults();
        tabWithDefaults.setIndexInContainer(i);
        int attributeCount = xmlResourceParser.getAttributeCount();
        for (int i2 = 0; i2 < attributeCount; i2++) {
            String attributeName = xmlResourceParser.getAttributeName(i2);
            switch (attributeName.hashCode()) {
                case -1765033179:
                    if (attributeName.equals("barColorWhenSelected")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1077332995:
                    if (attributeName.equals("activeColor")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -424740686:
                    if (attributeName.equals("badgeBackgroundColor")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 3355:
                    if (attributeName.equals("id")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3226745:
                    if (attributeName.equals("icon")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 110371416:
                    if (attributeName.equals("title")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 1110315790:
                    if (attributeName.equals("badgeHidesWhenActive")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 1162188184:
                    if (attributeName.equals("inActiveColor")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    tabWithDefaults.setId(xmlResourceParser.getIdAttributeResourceValue(i2));
                    break;
                case 1:
                    tabWithDefaults.setIconResId(xmlResourceParser.getAttributeResourceValue(i2, 0));
                    break;
                case 2:
                    tabWithDefaults.setTitle(getTitleValue(xmlResourceParser, i2));
                    break;
                case 3:
                    int colorValue = getColorValue(xmlResourceParser, i2);
                    if (colorValue == -1) {
                        break;
                    } else {
                        tabWithDefaults.setInActiveColor(colorValue);
                        break;
                    }
                case 4:
                    int colorValue2 = getColorValue(xmlResourceParser, i2);
                    if (colorValue2 == -1) {
                        break;
                    } else {
                        tabWithDefaults.setActiveColor(colorValue2);
                        break;
                    }
                case 5:
                    int colorValue3 = getColorValue(xmlResourceParser, i2);
                    if (colorValue3 == -1) {
                        break;
                    } else {
                        tabWithDefaults.setBarColorWhenSelected(colorValue3);
                        break;
                    }
                case 6:
                    int colorValue4 = getColorValue(xmlResourceParser, i2);
                    if (colorValue4 == -1) {
                        break;
                    } else {
                        tabWithDefaults.setBadgeBackgroundColor(colorValue4);
                        break;
                    }
                case 7:
                    tabWithDefaults.setBadgeHidesWhenActive(xmlResourceParser.getAttributeBooleanValue(i2, true));
                    break;
            }
        }
        return tabWithDefaults;
    }

    private BottomBarTab tabWithDefaults() {
        BottomBarTab bottomBarTab = new BottomBarTab(this.context);
        bottomBarTab.setConfig(this.defaultTabConfig);
        return bottomBarTab;
    }

    private String getTitleValue(XmlResourceParser xmlResourceParser, int i) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(i, 0);
        return attributeResourceValue == 0 ? xmlResourceParser.getAttributeValue(i) : this.context.getString(attributeResourceValue);
    }

    private int getColorValue(XmlResourceParser xmlResourceParser, int i) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(i, 0);
        if (attributeResourceValue == 0) {
            try {
                return Color.parseColor(xmlResourceParser.getAttributeValue(i));
            } catch (Exception unused) {
                return -1;
            }
        }
        return ContextCompat.getColor(this.context, attributeResourceValue);
    }
}
