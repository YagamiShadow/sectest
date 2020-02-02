package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssPrintOrderPhp1Min extends BaseTest {
    private Integer orderId, productId;

    /*
    Attack description:
    - create order with plain xss payload as "subTotal" (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testA() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", payload.toString(), "22", "100", "0", "100", "0", "100", 0, 0, 0, "22", "-1");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientName" (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testB() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createDummyOrder(payload.toString(), "dummy");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "gstn" (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testC() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", "100", "0", "100", 0, 0, 0, "'" + payload.toString() + "'", "-1");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientContact" (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testD() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        orderId = helper.createDummyOrder("dummy", payload.toString());
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "rate" set to plain xss payload (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testF() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, "1", payload.toString(), "100");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "quantity" set to plain xss payload (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testG() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, payload.toString(), "100", "100");
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product
    - create order with that specific product and "totalValue" set to plain xss payload (eg: <h1>Ciao</h1>)
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testH() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct("dummy");
        orderId = helper.createDummyOrderProductDetail(GenericUtils.dateString(0), "dummy", "dummy", productId, "1", "100", payload.toString());
        testPrintOrder(orderId, payload);
    }

    /*
    Attack description:
    - create product with plain xss payload as name (eg: <h1>Ciao</h1>)
    - create order with that specific product
    - POST request to "php_action/printOrder.php" page with orderId set to the id of the created order
     */
    @Test
    public void testI() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.requireLoginAdmin();
        productId = helper.createDummyProduct(payload.toString());
        orderId = helper.createDummyOrderProducts(productId);
        testPrintOrder(orderId, payload);
    }


    private void testPrintOrder(int orderId, XssPayload payload) {
        postGet("php_action/printOrder.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void test() {
        testA();
        cleanSafely();
        testB();
        cleanSafely();
        testC();
        cleanSafely();
        testD();
        cleanSafely();
        testF();
        cleanSafely();
        testG();
        cleanSafely();
        testH();
        cleanSafely();
        testI();
        cleanSafely();
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
