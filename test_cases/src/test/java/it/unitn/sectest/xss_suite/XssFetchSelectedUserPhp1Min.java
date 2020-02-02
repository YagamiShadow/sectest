package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssFetchSelectedUserPhp1Min extends BaseTest {
    private Integer userId;

    /*
    Attack description:
    - create user with plain single quote xss payload as username (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedUser.php" api page with userId set to the id of the created user
     */
    @Test
    public void testB() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        testInternal(GenericUtils.sqlEscape(payload.toString()), GenericUtils.genRandomString(8) + "@gmail.com", payload);
    }

    /*
    Attack description:
    - create user with plain single quote xss payload as email (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedUser.php" api page with userId set to the id of the created user
     */
    @Test
    public void testD() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        testInternal("dummy", GenericUtils.sqlEscape(payload.toString()), payload);
    }

    /*
    Attack description:
    - create random user
    - change bio to plain single quote xss payload (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedUser.php" api page with userId set to the id of the created user
     */
    @Test
    public void testE() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        helper.requireLoginAdmin();
        String username = GenericUtils.genRandomString(12);
        userId = helper.createDummyUser(username);
        helper.changeBio(userId, payload.toString());
        postGet("php_action/fetchSelectedUser.php", "userid", String.valueOf(userId));
        assert payload.isInDocument(helper);
    }

    private void testInternal(String username, String email, XssPayload payload) {
        helper.requireLoginAdmin();
        userId = helper.createUser(username, ProcedureHelper.DEFAULT_PASSWORD, email);
        postGet("php_action/fetchSelectedUser.php", "userid", String.valueOf(userId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void test() {
        testB();
        cleanSafely();
        testD();
        cleanSafely();
        testE();
        cleanSafely();
    }

    @Override
    public void clean() {
        if (userId != null) {
            helper.deleteUser(userId);
            userId = null;
        }
    }
}
