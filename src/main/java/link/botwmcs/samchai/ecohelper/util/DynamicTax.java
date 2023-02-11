package link.botwmcs.samchai.ecohelper.util;

public class DynamicTax {
    // Basic by https://github.com/Tining123/DemonMarket/blob/main/src/main/java/com/tining/demonmarket/common/util/MathUtil.java
    public static double priceDownByProperty(double price, double property, double basicProperty){
        // 0.5 is squareroot
        return ((price) / Math.pow(Math.exp((property / basicProperty)), 0.5) + (price / (1 + property / basicProperty))) / 2;
    }

}
