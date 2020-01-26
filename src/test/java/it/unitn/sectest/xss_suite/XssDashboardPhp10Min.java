package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.*;

public class XssDashboardPhp10Min extends BaseTest {
    private Integer orderId, userId;

    @Test
    public void test() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.createDummyUser(payload.toString());
        userId = helper.getUserId(payload.toString());
        helper.requireLogin(payload.toString());
        orderId = helper.createDummyOrder();
        Logging.i(orderId+ " - "+payload);
        helper.requireLoginAdmin();
        helper.get(ProcedureHelper.DASHBOARD_PATH);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean(){
        if (orderId != null){
            helper.deleteOrder(orderId);
            orderId = null;
        }
        if (userId != null) {
            helper.deleteUser(userId);
            userId = null;
        }
    }
}
