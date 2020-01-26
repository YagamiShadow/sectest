package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssProductPhp2Min extends BaseTest {
    private Integer categoryId;

    @Test
    public void test() {
        XssPayload payload = XssPayload.genOptionPayload();
        helper.requireLoginAdmin();
        categoryId = helper.createCategory(payload.toString());
        helper.get(ProcedureHelper.PRODUCT_URL);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (categoryId != null){
            helper.removeCategory(categoryId);
        }
    }
}
