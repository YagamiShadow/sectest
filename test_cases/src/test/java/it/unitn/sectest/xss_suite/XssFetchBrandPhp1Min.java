package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchBrandPhp1Min extends BaseTest {
    private Integer brandId;

    /*
    Attack description:
    - create brand with plain single quote xss payload (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchBrand.php" api page
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        brandId = helper.createBrand(GenericUtils.sqlEscape(payload.toString()));
        helper.get("php_action/fetchBrand.php");
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
