package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchOrderDataPhp1Min extends BaseTest {
    private Integer orderId;

    /*
    Attack description:
    - create order with plain single quote xss payload as clientName (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testC() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createDummyOrder(GenericUtils.sqlEscape(payload.toString()), "dummy");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as clientContact (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testD() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createDummyOrder("dummy", GenericUtils.sqlEscape(payload.toString()));
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as sub_total (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testE() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", GenericUtils.sqlEscape(payload.toString()), "22", "100", "0", "100", "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as vat (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testF() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", GenericUtils.sqlEscape(payload.toString()), "100", "0", "100", "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as totalAmount (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testG() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", GenericUtils.sqlEscape(payload.toString()), "0", "100", "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as grandTotal (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testH() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", GenericUtils.sqlEscape(payload.toString()), "100", "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as grandTotal (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testI() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", GenericUtils.sqlEscape(payload.toString()), "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as paid (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testJ() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", "100", GenericUtils.sqlEscape(payload.toString()), "100", 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as due (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchOrderData.php" api page with orderId set to the id of the created order
     */
    @Test
    public void testK() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createOrder(GenericUtils.dateString(0), "dummy", "dummy", "100", "22", "100", "0", "100", "0", GenericUtils.sqlEscape(payload.toString()), 0, 0, 0, "22", "-1");
        postGet("php_action/fetchOrderData.php", "orderId", String.valueOf(orderId));
        assert payload.isInDocument(helper);
    }


    @Override
    public void test() {
        testC();
        cleanSafely();
        testD();
        cleanSafely();
        testE();
        cleanSafely();
        testF();
        cleanSafely();
        testG();
        cleanSafely();
        testH();
        cleanSafely();
        testI();
        cleanSafely();
        testJ();
        cleanSafely();
        testK();
        cleanSafely();
    }

    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
