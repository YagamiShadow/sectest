package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp64Min extends BaseTest {

    /*
    Attack description:
    - go to the order edit url with get parameter "i" set to quotes attribute escape xss payload
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input");
        String path = ProcedureHelper.ORDERS_EDIT_URL(payload.toString());
        helper.get(path);
        assert payload.isInDocument(helper);
    }
}
