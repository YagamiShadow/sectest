package it.unitn.sectest.xss_suite;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssSettingsPhp3Min extends BaseTest {
    private Integer userId;


    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        String username = "user_"+ GenericUtils.genRandomString(8);
        helper.createDummyUser(username);
        helper.requireLogin(username);
        userId = helper.getUserId(username);
        helper.changeBio(userId, payload.toString());
        helper.get(ProcedureHelper.SETTING_URL);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (userId != null) {
            helper.requireLoginAdmin();
            helper.deleteUser(userId);
        }
    }
}
