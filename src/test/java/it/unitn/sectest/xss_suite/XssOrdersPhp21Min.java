package it.unitn.sectest.xss_suite;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp21Min extends BaseTest {
    private Integer orderId;

    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input",true);
        helper.requireLoginAdmin();
        orderId = helper.createDummyOrder(payload.toString(), "dummy");
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean(){
        if (orderId != null){
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
