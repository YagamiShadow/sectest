package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssSettingPhp1Min extends BaseTest {
    private Integer userId;

    /*
    Attack description:
    - create user with quotes attribute escape xss payload as name (eg: " /><h1>Ciao</h1><input x=")
    - login with that username
    - go to the setting page url
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        userId = helper.createDummyUser(payload.toString());
        helper.logout();
        helper.requireLogin(payload.toString());
        helper.get(ProcedureHelper.SETTING_URL);
        assertPayloadNextTo(payload, "username");
    }

    @Override
    public void clean() {
        if (userId != null) {
            helper.requireLoginAdmin();
            helper.deleteUser(userId);
        }
    }
}
