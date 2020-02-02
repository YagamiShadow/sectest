package it.unitn.sectest.xss_suite;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utils.BaseTest;
import utils.GenericUtils;
import utils.XssPayload;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XssFetchProductPhp1Min extends BaseTest {
    private Integer productId, brandId, categoryId, normalBrand, normalCategory;

    private Integer requireNormalCategory() {
        if (normalCategory == null) {
            normalCategory = helper.createCategory(GenericUtils.genRandomString(10));
        }
        return normalCategory;
    }

    private Integer requireNormalBrand() {
        if (normalBrand == null) {
            normalBrand = helper.createBrand(GenericUtils.genRandomString(10));
        }
        return normalBrand;
    }

    /*
    Attack description:
    - create product with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchProduct.php" api page
     */
    @Test
    public void testB() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        productId = helper.createProduct(GenericUtils.sqlEscape(payload.toString()), "100", "100", requireNormalBrand(), requireNormalCategory(), 1, true);
        helper.get("php_action/fetchProduct.php");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create product with plain single quote xss payload as quantity (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchProduct.php" api page
     */
    @Test
    public void testC() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        productId = helper.createProduct("dummy", GenericUtils.sqlEscape("1" + payload.toString()), "100", requireNormalBrand(), requireNormalCategory(), 1, true);
        helper.get("php_action/fetchProduct.php");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create product with plain single quote xss payload as rate (eg: <h1 id='id'>Ciao</h1>)
    - GET request to the "php_action/fetchProduct.php" api page
     */
    @Test
    public void testD() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        productId = helper.createProduct("dummy", "1000", GenericUtils.sqlEscape(payload.toString()), requireNormalBrand(), requireNormalCategory(), 1, true);
        helper.get("php_action/fetchProduct.php");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create brand with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - create product with the id of the created brand as brandId
    - GET request to the "php_action/fetchProduct.php" api page
     */
    @Test
    public void testE() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        brandId = helper.createBrand(GenericUtils.sqlEscape(payload.toString()));
        productId = helper.createProduct("dummy", "1000", "100", brandId, requireNormalCategory(), 1, true);
        helper.get("php_action/fetchProduct.php");
        assert payload.isInDocument(helper);
    }

    /*
    Attack description:
    - create category with plain single quote xss payload as name (eg: <h1 id='id'>Ciao</h1>)
    - create product with the id of the created category as categoryId
    - GET request to the "php_action/fetchProduct.php" api page
     */
    @Test
    public void testF() {
        helper.requireLoginAdmin();
        XssPayload payload = XssPayload.genJsonApiPayload();
        categoryId = helper.createCategory(GenericUtils.sqlEscape(payload.toString()));
        productId = helper.createProduct("dummy", "1000", "100", requireNormalBrand(), categoryId, 1, true);
        helper.get("php_action/fetchProduct.php");
        assert payload.isInDocument(helper);
    }


    @Override
    public void test() {
        testB();
        cleanSafely();
        testC();
        cleanSafely();
        testD();
        cleanSafely();
        testE();
        cleanSafely();
        testF();
        cleanSafely();
        cleanRequired();
    }

    @Override
    public void clean() {
        if (productId != null) {
            helper.removeProduct(productId);
            productId = null;
        }
        if (brandId != null) {
            helper.removeBrand(brandId);
            brandId = null;
        }
        if (categoryId != null) {
            helper.removeProduct(categoryId);
            categoryId = null;
        }
    }

    @AfterAll
    public void cleanRequired() {
        if (normalBrand != null) {
            helper.removeBrand(normalBrand);
            normalBrand = null;
        }
        if (normalCategory != null) {
            helper.removeCategory(normalCategory);
            normalCategory = null;
        }
    }
}
