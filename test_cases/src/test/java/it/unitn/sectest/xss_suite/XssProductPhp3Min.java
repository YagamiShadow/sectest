package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import utils.BaseTest;
import utils.GenericUtils;
import utils.ProcedureHelper;
import utils.XssPayload;

public class XssProductPhp3Min extends BaseTest {
    private Integer brandId;

    /*
    Attack description:
    - create brand with option/select escape xss payload as "name" (eg: </option></select><h1>Ciao</h1><select><option>)
    - go to the product page url
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genOptionPayload();
        helper.requireLoginAdmin();
        brandId = helper.createBrand(payload.toString());
        helper.get(ProcedureHelper.PRODUCT_URL);
        assert payload.isInElement(GenericUtils.parentOf(helper.findElement(By.id("editBrandName"))));
    }

    @Override
    public void clean() {
        if (brandId != null) {
            helper.removeBrand(brandId);
        }
    }
}
