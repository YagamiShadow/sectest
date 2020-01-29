package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

import java.util.Random;

public class XssGetOrderReportPhp1Min extends BaseTest {
    private boolean switched = false;
    private Integer orderId;

    /*
    Attack description:
    - create order with plain xss payload as "grandTotal"
    - go to the report page url
    - generate report for that specific order (set a date before and a date after)
    - payload will be in the report html
     */
    @Test
    public void testA() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test("dummy", "dummy", payload.toString());
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientName"
    - go to the report page url
    - generate report for that specific order (set a date before and a date after)
    - payload will be in the report html
     */
    @Test
    public void testC() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test(payload.toString(), "dummy", "100");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create order with plain xss payload as "clientContact"
    - go to the report page url
    - generate report for that specific order (set a date before and a date after)
    - payload will be in the report html
     */
    @Test
    public void testD() {
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test("dummy", payload.toString(), "100");
        assert payload.isInDocument(helper);
    }

    public void test() {
        testA();
        cleanSafely();
        testC();
        cleanSafely();
        testD();
        cleanSafely();
    }



    private void internal_test(String client_name, String client_contact, String grandTotal) {
        switched = false;
        helper.requireLoginAdmin();
        int daysToAdd = new Random().nextInt(1000);
        orderId = helper.createOrder(GenericUtils.dateString(daysToAdd), client_name, client_contact, "100", "22", "100", "100", grandTotal, "0", "100", 0, 0,0,"22", "-1");
        helper.get(ProcedureHelper.REPORT_PATH);
        helper.findElement(By.id("startDate")).sendKeys(GenericUtils.dateString(daysToAdd));
        helper.findElement(By.id("endDate")).sendKeys(GenericUtils.dateString(daysToAdd));
        helper.stashHandles();
        helper.findElement(By.id("getOrderReportForm")).submit();
        helper.waitDocumentReady();
        if (helper.hasNewWindows()) {
            switched = true;
            helper.switchToLatestHandle();
        }
    }

    @Override
    public void clean() {
        if (switched) {
            helper.closeBackToLatestWindow();
            switched = false;
        }
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
