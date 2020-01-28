package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp41Min extends BaseTest {
    private Integer orderId;

    /*
    Attack description:
    - create order with quotes attribute escape xss payload as "subTotalValue"
    - go to the order edit url for that specific order
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        helper.requireLoginAdmin();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", payload.toString(), "22", "200", "0", "100", "0", "100", 0, 0, 0, 0, "-1");
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
