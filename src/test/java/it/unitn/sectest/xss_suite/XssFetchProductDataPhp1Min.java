package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssFetchProductDataPhp1Min extends BaseTest {
    private Integer productId;

    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genOptionPayload();
        productId = helper.createDummyProduct(payload.toString());
        helper.get(GenericUtils.composeUrl(ProcedureHelper.ORDERS_PATH, "o", "add"));
        helper.findElement(By.id("addRowBtn")).click();
        sleep(100);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (productId != null){
            helper.removeProduct(productId);
            productId = null;
        }
    }
}
