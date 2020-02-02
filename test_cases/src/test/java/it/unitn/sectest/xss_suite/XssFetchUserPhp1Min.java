package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchUserPhp1Min extends BaseTest {
    private Integer userId;

    /*
    Attack description:
    - create user with plain single quote xss payload as username (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchUser.php" api page
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        userId = helper.createDummyUser(GenericUtils.sqlEscape(payload.toString()));
        helper.get("php_action/fetchUser.php");
        assert payload.isInDocument(helper);
    }


    @Override
    public void clean() {
        if (userId != null) {
            helper.deleteUser(userId);
            userId = null;
        }
    }
}
