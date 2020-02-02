package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

public class XssFetchSelectedCategoriesPhp1Min extends BaseTest {
    private Integer categoryId;

    /*
    Attack description:
    - create category with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - POST request to the "php_action/fetchSelectedCategories.php" api page with categoriesId set to the id of the created category
     */
    @Test
    public void test() {
        XssPayload payload = XssPayload.genJsonApiPayload();
        helper.requireLoginAdmin();
        categoryId = helper.createCategory(GenericUtils.sqlEscape(payload.toString()));
        postGet("php_action/fetchSelectedCategories.php", "categoriesId", String.valueOf(categoryId));
        assert payload.isInDocument(helper);
    }

    @Override
    public void clean() {
        if (categoryId != null) {
            helper.removeCategory(categoryId);
        }
    }
}
