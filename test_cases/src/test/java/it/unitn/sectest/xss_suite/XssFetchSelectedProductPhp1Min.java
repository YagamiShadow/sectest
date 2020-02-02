package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchSelectedProductPhp1Min extends BaseTest {
    private Integer productId;

    /*
    Attack description:
    - create product with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedProduct.php" api page with productId set to the id of the created product
     */
    @Test
    public void testB() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        testInternal(GenericUtils.sqlEscape(payload.toString()), "100", "100", payload);
    }

    /*
    Attack description:
    - create product with plain single quote xss payload as quantity (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedProduct.php" api page with productId set to the id of the created product
     */
    @Test
    public void testF() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        testInternal("dummy", GenericUtils.sqlEscape(payload.toString()), "100", payload);
    }

    /*
    Attack description:
    - create product with plain single quote xss payload as rate (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedProduct.php" api page with productId set to the id of the created product
     */
    @Test
    public void testG() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        testInternal("dummy", "100", GenericUtils.sqlEscape(payload.toString()), payload);
    }

    private void testInternal(String name, String quantity, String rate, XssPayload payload) {
        helper.requireLoginAdmin();
        productId = helper.createProduct(name, quantity, rate, 0, 0, 1, true);
        postGet("php_action/fetchSelectedProduct.php", "productId", String.valueOf(productId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void test() {
        testB();
        cleanSafely();
        testF();
        cleanSafely();
        testG();
        cleanSafely();
    }

    @Override
    public void clean() {
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
