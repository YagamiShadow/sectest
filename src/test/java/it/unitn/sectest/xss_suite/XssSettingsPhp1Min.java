package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssSettingsPhp1Min extends BaseTest {
    private String username;

    /*
    Attack description:
    - create user with quotes attribute escape xss payload as name
    - login with that username
    - go to the setting page url
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        helper.createDummyUser(payload.toString());
        username = payload.toString();
        helper.logout();
        helper.requireLogin(username);
        helper.get(ProcedureHelper.SETTING_URL);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (username != null) {
            helper.requireLoginAdmin();
            helper.deleteUser(username);
        }
    }
}
