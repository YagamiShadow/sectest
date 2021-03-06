package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssOrdersPhp6Min extends BaseTest {

    /*
    Attack description:
    - go to the order edit url and replace get parameter "i" with plain xss payload (eg: GET /orders.php?o=editOrd&i=<h1>Ciao</h1>)
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genPlainPayload();
        helper.get(ProcedureHelper.ORDERS_EDIT_URL(payload.toString()));
        assert payload.isInElement(helper.findElement(By.xpath("/html/body/div[1]/h4")));
    }
}
