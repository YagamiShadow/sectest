package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

import java.util.Random;

public class XssGetOrderReportPhp1Min extends BaseTest {
    private Integer orderId;

    /*
    Attack description:
    - create order with plain xss payload as "grandTotal" (eg: <h1>Ciao</h1>) and a random date
    - POST request to the "php_action/getOrderReport.php" with start and end dates set to the previous date
     */
    @Test
    public void testA() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test("dummy", "dummy", payload.toString(), payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientName" (eg: <h1>Ciao</h1>)
    - POST request to the "php_action/getOrderReport.php" with start and end dates set to the previous date
     */
    @Test
    public void testC() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test(payload.toString(), "dummy", "100", payload);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientContact" (eg: <h1>Ciao</h1>)
    - POST request to the "php_action/getOrderReport.php" with start and end dates set to the previous date
     */
    @Test
    public void testD() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test("dummy", payload.toString(), "100", payload);
    }

    public void test() {
        testA();
        cleanSafely();
        testC();
        cleanSafely();
        testD();
        cleanSafely();
    }


    private void internal_test(String client_name, String client_contact, String grandTotal, XssPayload payload) {
        helper.requireLoginAdmin();
        int daysToAdd = new Random().nextInt(1000);
        orderId = helper.createOrder(GenericUtils.dateString(daysToAdd), client_name, client_contact, "100", "22", "100", "100", grandTotal, "0", "100", 0, 0, 0, "22", "-1");
        postGet("php_action/getOrderReport.php", "startDate", GenericUtils.dateString(daysToAdd), "endDate", GenericUtils.dateString(daysToAdd));
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
