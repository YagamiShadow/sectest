package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp27Min extends BaseTest {
    private Integer orderId, productId;

    /*
    Attack description:
    - create product with option/select escape xss payload as name
    - create order with that specific product
    - go to the order edit url for that specific order
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genOptionPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct(payload.toString());
        orderId = helper.createDummyOrderProducts(productId);
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
