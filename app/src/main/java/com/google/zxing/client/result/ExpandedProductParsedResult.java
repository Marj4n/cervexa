package com.google.zxing.client.result;

import java.util.Hashtable;

/* loaded from: classes.dex */
public class ExpandedProductParsedResult extends ParsedResult {
    public static final String KILOGRAM = "KG";
    public static final String POUND = "LB";
    private final String bestBeforeDate;
    private final String expirationDate;
    private final String lotNumber;
    private final String packagingDate;
    private final String price;
    private final String priceCurrency;
    private final String priceIncrement;
    private final String productID;
    private final String productionDate;
    private final String sscc;
    private final Hashtable uncommonAIs;
    private final String weight;
    private final String weightIncrement;
    private final String weightType;

    ExpandedProductParsedResult() {
        super(ParsedResultType.PRODUCT);
        this.productID = "";
        this.sscc = "";
        this.lotNumber = "";
        this.productionDate = "";
        this.packagingDate = "";
        this.bestBeforeDate = "";
        this.expirationDate = "";
        this.weight = "";
        this.weightType = "";
        this.weightIncrement = "";
        this.price = "";
        this.priceIncrement = "";
        this.priceCurrency = "";
        this.uncommonAIs = new Hashtable();
    }

    public ExpandedProductParsedResult(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, Hashtable hashtable) {
        super(ParsedResultType.PRODUCT);
        this.productID = str;
        this.sscc = str2;
        this.lotNumber = str3;
        this.productionDate = str4;
        this.packagingDate = str5;
        this.bestBeforeDate = str6;
        this.expirationDate = str7;
        this.weight = str8;
        this.weightType = str9;
        this.weightIncrement = str10;
        this.price = str11;
        this.priceIncrement = str12;
        this.priceCurrency = str13;
        this.uncommonAIs = hashtable;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExpandedProductParsedResult)) {
            return false;
        }
        ExpandedProductParsedResult expandedProductParsedResult = (ExpandedProductParsedResult) obj;
        return this.productID.equals(expandedProductParsedResult.productID) && this.sscc.equals(expandedProductParsedResult.sscc) && this.lotNumber.equals(expandedProductParsedResult.lotNumber) && this.productionDate.equals(expandedProductParsedResult.productionDate) && this.bestBeforeDate.equals(expandedProductParsedResult.bestBeforeDate) && this.expirationDate.equals(expandedProductParsedResult.expirationDate) && this.weight.equals(expandedProductParsedResult.weight) && this.weightType.equals(expandedProductParsedResult.weightType) && this.weightIncrement.equals(expandedProductParsedResult.weightIncrement) && this.price.equals(expandedProductParsedResult.price) && this.priceIncrement.equals(expandedProductParsedResult.priceIncrement) && this.priceCurrency.equals(expandedProductParsedResult.priceCurrency) && this.uncommonAIs.equals(expandedProductParsedResult.uncommonAIs);
    }

    public String getBestBeforeDate() {
        return this.bestBeforeDate;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        return this.productID;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public String getLotNumber() {
        return this.lotNumber;
    }

    public String getPackagingDate() {
        return this.packagingDate;
    }

    public String getPrice() {
        return this.price;
    }

    public String getPriceCurrency() {
        return this.priceCurrency;
    }

    public String getPriceIncrement() {
        return this.priceIncrement;
    }

    public String getProductID() {
        return this.productID;
    }

    public String getProductionDate() {
        return this.productionDate;
    }

    public String getSscc() {
        return this.sscc;
    }

    public Hashtable getUncommonAIs() {
        return this.uncommonAIs;
    }

    public String getWeight() {
        return this.weight;
    }

    public String getWeightIncrement() {
        return this.weightIncrement;
    }

    public String getWeightType() {
        return this.weightType;
    }

    public int hashCode() {
        return ((((((((((((this.productID.hashCode() * 31) + this.sscc.hashCode()) * 31) + this.lotNumber.hashCode()) * 31) + this.productionDate.hashCode()) * 31) + this.bestBeforeDate.hashCode()) * 31) + this.expirationDate.hashCode()) * 31) + this.weight.hashCode()) ^ ((((((((((this.weightType.hashCode() * 31) + this.weightIncrement.hashCode()) * 31) + this.price.hashCode()) * 31) + this.priceIncrement.hashCode()) * 31) + this.priceCurrency.hashCode()) * 31) + this.uncommonAIs.hashCode());
    }
}
