package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssSettingPhp3Min extends BaseTest {
    private Integer userId;

    /*
    Attack description:
    - create random user
    - login with that username
    - change the bio to a quotes attribute escape xss payload
    - go to the setting page url
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genDoubleQuoteAttributePayload("input", true);
        String username = "user_" + GenericUtils.genRandomString(8);
        helper.createDummyUser(username);
        helper.requireLogin(username);
        userId = helper.getUserId(username);
        helper.changeBio(userId, payload.toString());
        helper.get(ProcedureHelper.SETTING_URL);
        assertPayloadNextTo(payload, "bio");
    }

    @Override
    public void clean() {
        if (userId != null) {
            helper.requireLoginAdmin();
            helper.deleteUser(userId);
        }
    }
}
