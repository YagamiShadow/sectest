package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp11Min extends BaseTest {
    private Integer productId;

    /*
    Attack description:
    - create product with option/select escape xss payload as name
    - go to the orders add url
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genOptionPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct(payload.toString());
        helper.get(ProcedureHelper.ORDERS_ADD_URL);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
