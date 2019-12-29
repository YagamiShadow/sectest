package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp6Min extends BaseTest {

    @Test
    public void test(){
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genPlainPayload();
        String path = GenericUtils.composeUrl(ProcedureHelper.ORDERS_PATH, "o", "editOrd", "i", payload.toString());
        helper.get(path);
        assert payload.isInDocument(helper);
    }
}
