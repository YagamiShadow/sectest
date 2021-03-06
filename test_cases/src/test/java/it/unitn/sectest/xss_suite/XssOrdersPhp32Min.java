package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp32Min extends BaseTest {
    private Integer orderId, productId;

    /*
    Attack description:
    - create product
    - create order with that specific product and
    - edit the order and set the product "quantity" to "1" concat to a quotes attribute escape xss payload (eg: 1" /><h1>Ciao</h1><input x=")
    - go to the order edit url for that specific order
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProducts(productId);
        helper.editProduct(productId, "dummy", 0, 0, "1" + payload.toString(), "100", 1);
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assert payload.isInElement(helper.findElement(By.id("available_quantity" + productId)));
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
