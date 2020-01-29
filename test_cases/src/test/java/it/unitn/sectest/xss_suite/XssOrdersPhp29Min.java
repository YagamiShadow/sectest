package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp29Min extends BaseTest {
    private Integer orderId, productId;

    /*
    Attack description:
    - create product
    - create order with that specific product and "rate" set to quotes attribute escape xss payload
    - go to the order edit url for that specific order
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, "1", payload.toString(), "100");
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(orderId));
        assertPayloadNextTo(payload, "rate1");
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
