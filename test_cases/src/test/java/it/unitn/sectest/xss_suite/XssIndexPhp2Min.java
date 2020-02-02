package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.XssPayload;

public class XssIndexPhp2Min extends BaseTest {

    /*
    Attack description:
    - GET request to the "index.php" page with the additional double quote attribute escape XSS payload (eg: GET /index.php/"></form><h1>Ciao</h1><form x="
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("form");
        helper.logout();
        helper.get("/index.php/" + payload.toString());
        assert payload.isInDocument(helper);
    }
}
