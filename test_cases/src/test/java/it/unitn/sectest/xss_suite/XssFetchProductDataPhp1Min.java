package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssFetchProductDataPhp1Min extends BaseTest {
    private Integer productId;

    /*
    Attack description:
    - create product with plain xss payload as name
    - go to the order add url
    - on the order creation form click "Add row" button
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genOptionPayload();
        productId = helper.createDummyProduct(payload.toString());
        helper.get(ProcedureHelper.ORDERS_ADD_URL);
        helper.findElement(By.id("addRowBtn")).click();
        sleep(100);
        assert payload.isInElement(helper.findElement(By.id("row4")));
    }

    @Override
    public void clean() {
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
