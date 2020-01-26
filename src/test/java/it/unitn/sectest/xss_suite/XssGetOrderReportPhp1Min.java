package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.*;

import java.util.Random;
import java.util.Set;

public class XssGetOrderReportPhp1Min extends BaseTest {
    private boolean switched = false;
    private Integer orderId;

    @Test
    public void testB(){
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test(payload.toString(), "");
        assert payload.isInDocument(helper);
    }

    @Test
    public void testC(){
        XssPayload payload = XssPayload.genPlainPayload();
        internal_test("", payload.toString());
        assert payload.isInDocument(helper);
    }

    public void test(){
        testB();
        cleanSafely();
        testC();
        cleanSafely();
    }

    private void internal_test(String client_name, String client_contact) {
        switched = false;
        helper.requireLoginAdmin();
        int daysToAdd = new Random().nextInt(1000);
        orderId = helper.createDummyOrder(GenericUtils.dateString(daysToAdd),client_name,client_contact);
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
        if (switched){
            helper.closeBackToLatestWindow();
            switched = false;
        }
        if (orderId != null){
            helper.deleteOrder(orderId);
            orderId = null;
        }
    }
}
