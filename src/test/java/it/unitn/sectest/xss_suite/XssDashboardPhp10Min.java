package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.Logging;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssDashboardPhp10Min extends BaseTest {
    private Integer orderId, userId;

    /*
    Attack description:
    - create user with plain xss payload as username
    - login as admin and go to dashboard page
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.createDummyUser(payload.toString());
        userId = helper.getUserId(payload.toString());
        helper.requireLogin(payload.toString());
        orderId = helper.createDummyOrder();
        helper.requireLoginAdmin();
        helper.get(ProcedureHelper.DASHBOARD_PATH);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (orderId != null) {
            helper.deleteOrder(orderId);
            orderId = null;
        }
        if (userId != null) {
            helper.deleteUser(userId);
            userId = null;
        }
    }
}
