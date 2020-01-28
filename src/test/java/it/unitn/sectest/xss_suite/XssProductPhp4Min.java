package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssProductPhp4Min extends BaseTest {
    private Integer categoryId;

    /*
    Attack description:
    - create category with option/select escape xss payload as "name"
    - go to the product page url
     */
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
        if (categoryId != null) {
            helper.removeCategory(categoryId);
        }
    }
}
