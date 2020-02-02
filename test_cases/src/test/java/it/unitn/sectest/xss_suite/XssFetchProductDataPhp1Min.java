package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchProductDataPhp1Min extends BaseTest {
    private Integer productId;

    /*
    Attack description:
    - create product with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchProductData.php" api page
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        productId = helper.createDummyProduct(GenericUtils.sqlEscape(payload.toString()));
        helper.get("php_action/fetchProductData.php");
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
