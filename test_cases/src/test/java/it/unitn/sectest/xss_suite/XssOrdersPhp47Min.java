package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp47Min extends BaseTest {
    private Integer orderId;

    /*
    Attack description:
    - create order with quotes attribute escape xss payload as "grandTotalValue" (eg: " /><h1>Ciao</h1><input x=")
    - go to the order edit url for that specific order
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        helper.requireLoginAdmin();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", payload.toString(), "0", "100", 0, 0, 0, 0, "-1");
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assertPayloadNextTo(payload, "grandTotalValue");
    }

    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
