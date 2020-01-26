package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssProductPhp1Min extends BaseTest {
    private Integer brandId;

    @Test
    public void test() {
        XssPayload payload = XssPayload.genOptionPayload();
        helper.requireLoginAdmin();
        brandId = helper.createBrand(payload.toString());
        helper.get(ProcedureHelper.PRODUCT_URL);
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (brandId != null) {
            helper.removeBrand(brandId);
        }
    }
}
