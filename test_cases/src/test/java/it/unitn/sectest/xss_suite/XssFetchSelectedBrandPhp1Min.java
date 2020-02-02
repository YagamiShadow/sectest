package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchSelectedBrandPhp1Min extends BaseTest {
    private Integer brandId;

    /*
    Attack description:
    - create brand with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedBrand.php" api page with brandId set to the id of the created brand
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        helper.requireLoginAdmin();
        brandId = helper.createBrand(GenericUtils.sqlEscape(payload.toString()));
        postGet("php_action/fetchSelectedBrand.php", "brandId", String.valueOf(brandId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (brandId != null) {
            helper.removeBrand(brandId);
            brandId = null;
        }
    }
}
