package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.*;

public class XssPrintOrderPhp1Min extends BaseTest {
    private Integer orderId, productId;
    private boolean switched = false;

    /*
    Attack description:
    - create order with plain xss payload as "clientName"
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testA() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createDummyOrder(payload.toString(), "dummy");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "gstn"
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testB() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", "100", "0", "100", 0, 0, 0, "'" + payload.toString() + "'", "-1");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientContact"
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testC() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createDummyOrder("dummy", payload.toString());
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "rate" set to plain xss payload
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testE() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, "1", payload.toString(), "100");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "quantity" set to plain xss payload
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testF() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, payload.toString(), "100", "100");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "totalValue" set to plain xss payload
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testG() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, "1", "100", payload.toString());
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product with plain xss payload as name
    - create order with that specific product
    - go to manage orders url
    - print report of that specific order
    - payload will be in the report window html
     */
    @Test
    public void testH() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct(payload.toString());
        orderId = helper.createDummyOrderProducts(productId);
        testPrintOrder(orderId, payload);
    }


    private void testPrintOrder(int orderId, XssPayload payload) {
        helper.requireLoginAdmin();
        helper.get(ProcedureHelper.ORDERS_MAN_URL);
        helper.stashHandles();
        sleep(200);
        try {
            helper.executeScript("printOrder(" + orderId + ");");
        } catch (RelativeWebDriver.JavascriptNotSupported javascriptNotSupported) {
            throw new RuntimeException(javascriptNotSupported);
        }
        sleep(300);
        assert helper.hasNewWindows();
        helper.switchToLatestHandle();
        switched = true;
        try {
            assert payload.isInDocument(helper);
        } finally {
            helper.closeBackToLatestWindow();
            switched = false;
        }
    }

    @Override
    public void test() {
        testA();
        cleanSafely();
        testB();
        cleanSafely();
        testC();
        cleanSafely();
        testE();
        cleanSafely();
        testF();
        cleanSafely();
        testG();
        cleanSafely();
        testH();
    }


    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
        if (switched) {
            helper.switchToLatestHandle();
            switched = false;
        }
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
