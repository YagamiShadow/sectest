package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchOrderPhp1Min extends BaseTest {
    private Integer orderId;

    /*
    Attack description:
    - create order with plain single quote xss payload as clientName (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchOrder.php" api page
     */
    @Test
    public void testA() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createDummyOrder(GenericUtils.sqlEscape(payload.toString()), "dummy");
        helper.get("php_action/fetchOrder.php");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain single quote xss payload as clientContact (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchOrder.php" api page
     */
    @Test
    public void testB() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        orderId = helper.createDummyOrder("dummy", GenericUtils.sqlEscape(payload.toString()));
        helper.get("php_action/fetchOrder.php");
        assert payload.isInDocument(helper);
    }


    @Override
    public void test() {
        testA();
        cleanSafely();
        testB();
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
