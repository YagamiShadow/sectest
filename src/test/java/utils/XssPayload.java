package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class XssPayload {

    public static XssPayload genOptionPayload(){
        return genCustomXssPayload("Almost hacked</option></select>", "<select style=\"display: none\"><option>");
    }

    public static XssPayload genPlainPayload(){
        return genCustomXssPayload("","");
    }
    public static XssPayload genDoubleQuoteAttributePayload(String tagname){
        return genCustomXssPayload("\"></"+tagname+">", "<"+tagname+" data-fakeattrib=\"");
    }
    public static XssPayload genSingleQuoteAttributePayload(String tagname){
        return genCustomXssPayload("'></"+tagname+">", "<"+tagname+" data-fakeattrib='");
    }

    public static XssPayload genCustomXssPayload(String prefix, String suffix){
        String identifier = generateIdentifier();
        String payload =  prefix+"<a id=\""+identifier+"\" href=\"http://www.maliciouslink.com\">"+identifier+"</a>"+suffix;
        return new XssPayload(payload, identifier);
    }

    private static String generateIdentifier(){
        return "xsspayload_"+GenericUtils.genRandomString(16);
    }

    public boolean isInText(String text){
        return text.contains(identifier);
    }

    public boolean isInDocument(WebDriver driver){
        try {
            driver.findElement(By.id(identifier));
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }


    private String payloadString, identifier;
    private XssPayload(String payloadString, String identifier){
        this.payloadString = payloadString;
        this.identifier = identifier;
    }

    @Override
    public String toString(){
        return this.payloadString;
    }

}
