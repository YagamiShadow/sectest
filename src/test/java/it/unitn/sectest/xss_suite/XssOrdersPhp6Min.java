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
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(payload.toString()));
        assert payload.isInDocument(helper);
    }
}
