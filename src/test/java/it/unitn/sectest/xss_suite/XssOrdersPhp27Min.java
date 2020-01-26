package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp27Min extends BaseTest {
    private Integer orderId, productId;

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
    public void clean(){
        if (orderId != null){
            helper.deleteOrder(orderId);
            orderId = null;
        }
        if (productId != null){
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
