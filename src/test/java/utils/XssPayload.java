package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class XssPayload {

    private String payloadString, identifier;

    private XssPayload(String payloadString, String identifier) {
        this.payloadString = payloadString;
        this.identifier = identifier;
    }

    public static XssPayload genOptionPayload() {
        return genCustomXssPayload("Almost vulnerable</option></select>", "<select style=\"display: none\"><option>");
    }

    public static XssPayload genPlainPayload() {
        return genCustomXssPayload("", "");
    }

    public static XssPayload genDoubleQuoteAttributePayload(String tagname, boolean singletag) {
        return getAttributeEscapePayload(tagname, "\"", singletag);
    }

    public static XssPayload genSingleQuoteAttributePayload(String tagname, boolean singletag) {
        return getAttributeEscapePayload(tagname, "'", singletag);
    }

    public static XssPayload genDoubleQuoteAttributePayload(String tagname) {
        return genDoubleQuoteAttributePayload(tagname, false);
    }

    public static XssPayload genSingleQuoteAttributePayload(String tagname) {
        return genSingleQuoteAttributePayload(tagname, false);
    }

    private static XssPayload getAttributeEscapePayload(String tagname, String escape, boolean singletag) {
        return genCustomXssPayload(escape + (singletag ? "/>" : "></" + tagname + ">"), "<" + tagname + " style=\"display: none\" data-fakeattrib=" + escape);
    }

    public static XssPayload genCustomXssPayload(String prefix, String suffix) {
        String identifier = generateIdentifier();
        String payload = prefix + "<script id=\"" + identifier + "\">vulnerable = true;</script>" + suffix;
        return new XssPayload(payload, identifier);
    }

    private static String generateIdentifier() {
        return "xsspayload_" + GenericUtils.genRandomString(16);
    }

    public boolean isInText(String text) {
        return text.contains(identifier);
    }

    public boolean isInDocument(WebDriver driver) {
        try {
            driver.findElement(By.id(identifier));
            Logging.i("Payload in document: " + identifier);
            return true;
        } catch (NoSuchElementException e) {
            Logging.w("Payload NOT in document: " + identifier);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.payloadString;
    }

}
