package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public class XssIndexPhp2Min extends BaseTest {
    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("form");
        helper.logout();
        helper.get("/index.php/"+payload.toString());
        assert payload.isInDocument(helper);
    }
}
