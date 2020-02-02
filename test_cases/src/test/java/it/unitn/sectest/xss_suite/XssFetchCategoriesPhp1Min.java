package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchCategoriesPhp1Min extends BaseTest {
    private Integer categoriesId;

    /*
    Attack description:
    - create category with plain single quote xss payload (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchCategories.php" api page
     */
    @Test
    public void test() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        categoriesId = helper.createCategory(GenericUtils.sqlEscape(payload.toString()));
        helper.get("php_action/fetchCategories.php");
        assert payload.isInDocument(helper);
    }


    @Override
    public void clean() {
        if (categoriesId != null) {
            helper.removeCategory(categoriesId);
            categoriesId = null;
        }
    }
}
