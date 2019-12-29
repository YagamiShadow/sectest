package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.*;

public class XssDashboardPhp10MinA extends BaseTest {
    private Integer orderId, userId;

    @Test
    public void test() {
        XssPayload payload = XssPayload.genPlainPayload();
        helper.createUser(payload.toString(), "password", GenericUtils.genRandomString(16)+"@gmail.com");
        userId = helper.getUserId(payload.toString());
        helper.requireLogin(payload.toString(), "password");
        orderId = helper.createDummyOrder();
        helper.requireLoginAdmin();
        helper.get(ProcedureHelper.DASHBOARD_PATH);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean(){
        if (userId != null){
            helper.deleteUser(userId);
        }
        if (orderId != null){
            helper.deleteOrder(orderId);
        }
    }
}
