package utils;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;

import javax.security.auth.login.FailedLoginException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcedureHelper extends RelativeWebDriver {

    public static final String LOGIN_PATH = "index.php";
    public static final String LOGIN_USERNAME_ID = "username";
    public static final String LOGIN_PASSWORD_ID = "password";
    public static final String LOGIN_FORM_ID = "loginForm";
    public static final String LOGOUT_PATH = "logout.php";
    public static final String DASHBOARD_PATH = "dashboard.php";
    public static final String ORDERS_PATH = "orders.php";
    public static final String REPORT_PATH = "report.php";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String ORDERS_ADD_URL = GenericUtils.composeUrl(ORDERS_PATH, "o", "add");
    public static final String ORDERS_MAN_URL = GenericUtils.composeUrl(ORDERS_PATH, "o", "manord");
    public static final String PRODUCT_URL = "product.php";
    public static final String SETTING_URL = "setting.php";
    private static final HashMap<String, ProcedureHelper> loaded_instances = new HashMap<String, ProcedureHelper>();
    private static final String DEFAULT_HOST = "http://localhost/inventory-management-system";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0";
    private static final byte[] DUMMY_IMAGE_DATA = Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAIAAABLbSncAAAACXBIWXMAAAsTAAALEwEAmpwYAAAGT2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE5LTEyLTI5VDEzOjEwOjA3KzAxOjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDE5LTEyLTI5VDEzOjEwOjA3KzAxOjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0xMi0yOVQxMzoxMDowNyswMTowMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoyNDdlYzUzNy1iN2VhLTU2NGYtYjU5Yy0yMjQ5ZTE2OGE3MjIiIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDoxMjE4NmNmOS00ZjhiLWI4NGUtYjViYS02NzEzMmI4NTk3M2IiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo2OTQ0NzE5MC1mY2I3LWRiNDMtYjU5ZS02YmEzNTc1NjBiNDMiIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo2OTQ0NzE5MC1mY2I3LWRiNDMtYjU5ZS02YmEzNTc1NjBiNDMiIHN0RXZ0OndoZW49IjIwMTktMTItMjlUMTM6MTA6MDcrMDE6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6MjQ3ZWM1MzctYjdlYS01NjRmLWI1OWMtMjI0OWUxNjhhNzIyIiBzdEV2dDp3aGVuPSIyMDE5LTEyLTI5VDEzOjEwOjA3KzAxOjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDxwaG90b3Nob3A6VGV4dExheWVycz4gPHJkZjpCYWc+IDxyZGY6bGkgcGhvdG9zaG9wOkxheWVyTmFtZT0iUCIgcGhvdG9zaG9wOkxheWVyVGV4dD0iUCIvPiA8L3JkZjpCYWc+IDwvcGhvdG9zaG9wOlRleHRMYXllcnM+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+3ITJFgAAAFhJREFUCJl1jsENACEIBBff1gF92Kd92AYF0IW+9x5i4iV38yGBzAQhiS/KHmYmImbWe88LyS25+5wTQESQLLe+1gJQa30ZAFprY4zc3CleZEpVs3CQv3cfwkNAgBJg7XAAAAAASUVORK5CYII=");
    private final OkHttpClient okHttpClient;
    private String logged_as = null;

    private ProcedureHelper(String host) {
        super(host, WebDriverProvider.FIREFOX);
        okHttpClient = buildOkHttpClient();
    }

    public static String ORDERS_EDIT_URL(Object orderId) {
        return GenericUtils.composeUrl(ORDERS_PATH, "o", "editOrd", "i", String.valueOf(orderId));
    }

    public static ProcedureHelper requireInstance(String host) {
        ProcedureHelper helper = loaded_instances.get(host);
        if (helper == null) {
            helper = new ProcedureHelper(host);
            loaded_instances.put(host, helper);
        }
        return helper;
    }

    public static ProcedureHelper requireInstance() {
        return requireInstance(DEFAULT_HOST);
    }

    public static void quitInstance(String host) {
        ProcedureHelper helper = loaded_instances.get(host);
        if (helper != null) {
            helper.quit();
            loaded_instances.remove(host);
        }
    }

    public static void quitInstance() {
        quitInstance(DEFAULT_HOST);
    }

    private static Cookie sCookieToOkCookie(org.openqa.selenium.Cookie c) {
        Cookie.Builder builder = new Cookie.Builder().domain(c.getDomain()).value(c.getValue()).path(c.getPath()).name(c.getName());
        if (c.isHttpOnly())
            builder.httpOnly();
        if (c.isSecure())
            builder.secure();
        if (c.getExpiry() != null)
            builder.expiresAt(c.getExpiry().getTime());

        return builder.build();
    }

    private static org.openqa.selenium.Cookie okCookieToSCookie(Cookie c) {
        return new org.openqa.selenium.Cookie.Builder(c.name(), c.value()).domain(c.domain()).expiresOn(new Date(c.expiresAt())).isHttpOnly(c.httpOnly()).isSecure(c.secure()).path(c.path()).build();
    }

    public static String bodyString(Response response) {
        try {
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void throwIfFalse(boolean condition, String message){
        if (!condition){
            throw new RuntimeException(message);
        }
    }

    private static void assertJsonTrue(String json, String key) {
        throwIfFalse(new JSONObject(json).getBoolean(key), "Response key "+key+" not true");
    }

    public void login(String username, String password) throws FailedLoginException {
        if (logged_as != null) {
            logout();
        }
        this.get(LOGIN_PATH);
        this.findElement(By.id(LOGIN_USERNAME_ID)).sendKeys(username);
        this.findElement(By.id(LOGIN_PASSWORD_ID)).sendKeys(password);
        this.findElement(By.id(LOGIN_FORM_ID)).submit();
        logged_as = username;
        waitDocumentReady();
        username = getLoginUsername();
        if (username == null) {
            throw new FailedLoginException();
        }
    }

    public void requireLogin(String username, String password) {
        try {
            String logged = getLoginUsername();
            if (!username.equals(logged)) {
                if (logged != null) {
                    logout();
                }
                login(username, password);
            }
        } catch (FailedLoginException e) {
            throw new RuntimeException(e);
        }
    }

    public void requireLogin(String username) {
        this.requireLogin(username, DEFAULT_PASSWORD);
    }

    public void requireLoginAdmin() {
        requireLogin("admin", "admin");
    }

    public String getLoginUsername() {
        if (logged_as == null) {
            return null;
        }
        get(DASHBOARD_PATH);
        try {
            String badge = findElement(By.xpath("/html/body/div/div[1]/div/div/div/a/span")).getText();
            if (!logged_as.equalsIgnoreCase(badge)) {
                Logging.w("Badge username and login username missmatch: " + badge + " - " + logged_as);
            }
            return badge;
        } catch (Exception e) {
            Logging.e("Failed to get dashboard badge", e);
            return null;
        }
    }

    public void logout() {
        get(LOGOUT_PATH);
        logged_as = null;
    }

    private List<Cookie> getAllCookies() {
        List<Cookie> list = new ArrayList<>();
        for (org.openqa.selenium.Cookie c : manage().getCookies()) {
            Logging.i(c);
            list.add(sCookieToOkCookie(c));
        }
        return list;
    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                //Do nothing
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                return getAllCookies();
            }
        }).followRedirects(false).followSslRedirects(false)
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                .build();
    }

    public Response httpGet(String path) {
        try {
            Logging.i("Http GET on " + path);
            return okHttpClient.newCall(stdRequestBuilder(path).build()).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Request.Builder stdRequestBuilder(String path) {
        Request.Builder builder = new Request.Builder();
        builder.header("user-agent", USER_AGENT);
        String url = getFullUrl(path);
        builder.url(url);
        return builder;
    }

    public Response httpPost(String path, Object... postParams) {
        return httpPost(path, GenericUtils.serializeParameters(postParams));
    }

    public Response httpPost(String path, String body) {
        return httpPost(path, body.getBytes(StandardCharsets.UTF_8));
    }

    public Response httpPost(String path, byte[] body) {
        return httpPost(path, body, "application/x-www-form-urlencoded");
    }

    public Response httpPost(String path, byte[] body, String mediaType) {
        Request.Builder builder = stdRequestBuilder(path);
        builder.post(RequestBody.create(MediaType.parse(mediaType), body));
        try {
            Logging.i("Http POST on " + path + " (" + body.length + " bytes)");
            return okHttpClient.newCall(builder.build()).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response httpPost(String path, MultipartBody body) {
        Request.Builder builder = stdRequestBuilder(path);
        builder.post(body);
        try {
            Logging.i("Http POST on " + path + " (" + body.parts().size() + " parts)");
            return okHttpClient.newCall(builder.build()).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password, String email) {
        Response response = httpPost("php_action/createUser.php", "userName", username, "upassword", password, "uemail", email);
        throwIfFalse(response.isSuccessful(), "response not successful");
        assertJsonTrue(bodyString(response), "success");
    }

    public void createDummyUser(String username) {
        this.createUser(username, DEFAULT_PASSWORD, GenericUtils.genRandomString(12) + "@gmail.com");
    }

    public int getUserId(String username) {
        Response response = httpGet("php_action/fetchUser.php");
        JSONObject jsonObject = new JSONObject(bodyString(response));
        JSONArray array = jsonObject.getJSONArray("data");
        for (int i = 0; i < array.length(); ++i) {
            JSONArray entry = array.getJSONArray(i);
            String u = entry.getString(0);
            String html = entry.getString(1);
            if (username.equals(u)) {
                Pattern p = Pattern.compile("removeUser\\((\\d+)\\)");
                Matcher m = p.matcher(html);
                if (!m.find()) {
                    throw new RuntimeException("Failed to match userId in html");
                } else {
                    return Integer.parseInt(m.group(1));
                }
            }
        }
        throw new RuntimeException("Failed to find the username: " + username);
    }

    public void deleteUser(String username) {
        deleteUser(getUserId(username));
    }

    public void deleteUser(int userId) {
        Response response = httpPost("php_action/removeUser.php", "userid", userId);
        throwIfFalse(response.isSuccessful(), "response not successful");
        assertJsonTrue(bodyString(response), "success");
    }

    public int createOrder(String orderDate, String clientName, String clientContact, String subTotalValue, String vatValue,
                           String totalAmountValue, String discount, String grandTotalValue, String paid, String dueValue,
                           int paymentType, int paymentStatus, int paymentPlace, Object gstn, Object[] productNames,
                           Object[] quantities, Object[] rates, Object[] totals) {
        int p = productNames.length;
        if (p != quantities.length || p != rates.length || p != totals.length) {
            throw new RuntimeException("Inconsistent amount of product details");
        }

        Object[] params = new Object[]{
                "orderDate", orderDate,
                "clientName", clientName,
                "clientContact", clientContact,
                "subTotalValue", subTotalValue,
                "vatValue", vatValue,
                "totalAmountValue", totalAmountValue,
                "discount", discount,
                "grandTotalValue", grandTotalValue,
                "paid", paid,
                "dueValue", dueValue,
                "paymentType", paymentType,
                "paymentStatus", paymentStatus,
                "paymentPlace", paymentPlace,
                "gstn", gstn
        };
        List<Object> list = new ArrayList<>(Arrays.asList(params));
        for (int i = 0; i < productNames.length; ++i) {

            list.add("productName[]");
            list.add(String.valueOf(productNames[i]));
            list.add("quantity[]");
            list.add(String.valueOf(quantities[i]));
            list.add("rateValue[]");
            list.add(String.valueOf(rates[i]));
            list.add("totalValue[]");
            list.add(String.valueOf(totals[i]));

        }
        Response response = httpPost("php_action/createOrder.php", list.toArray(new Object[0]));
        throwIfFalse(response.isSuccessful(), "response not successful");
        String body = bodyString(response);

        int id = 0;
        try {
            Pattern jsonPatt = Pattern.compile("\\{[^}]+\\}$");
            Matcher m = jsonPatt.matcher(body);
            throwIfFalse(m.find(), "JSON not found in response");
            body = m.group(0);
            JSONObject object = new JSONObject(body);
            throwIfFalse(object.getBoolean("success"), "success key is not true");
            id = object.getInt("order_id");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order_id: " + body, e);
        }
        throwIfFalse(id > 0, "Invalid id found");
        return id;
    }

    public int createDummyOrder() {
        return this.createDummyOrder("clientName", "clientContact");
    }

    public int createDummyOrder(String date, String clientName, String clientContact) {
        return this.createDummyOrderProducts(date, clientName, clientContact, "-1");
    }

    public int createDummyOrderProducts(String date, String clientName, String clientContact, Object... product_ids) {

        return this.createOrder(date, clientName, clientContact, "100", "100", "100", "100", "100", "100", "100", 0, 0, 0, 0, product_ids);
    }

    public int createOrder(String orderDate, String clientName, String clientContact, String subTotalValue, String vatValue,
                           String totalAmountValue, String discount, String grandTotalValue, String paid, String dueValue,
                           int paymentType, int paymentStatus, int paymentPlace, Object gstn, Object... product_ids) {
        return createOrder(orderDate, clientName, clientContact, subTotalValue, vatValue, totalAmountValue, discount, grandTotalValue, paid, dueValue, paymentType, paymentStatus, paymentPlace, gstn, product_ids,
                GenericUtils.listOf("1", product_ids.length).toArray(), GenericUtils.listOf("100", product_ids.length).toArray(), GenericUtils.listOf("100", product_ids.length).toArray());
    }

    public int createDummyOrderProducts(Object... products) {
        return createDummyOrderProducts(GenericUtils.dateString(0), "dummy", "dummy", products);
    }

    public int createDummyOrderProductDetail(String date, String clientName, String clientContact, Object productId, Object quantity, Object rate, Object total) {

        return this.createOrder(date, clientName, clientContact, "100", "100", "100", "100", "100", "100", "100", 0, 0, 0, 0,
                new Object[]{productId}, new Object[]{quantity}, new Object[]{rate}, new Object[]{total});
    }

    public int createDummyOrder(String clientName, String clientContact) {
        return this.createDummyOrder(GenericUtils.dateString(0), clientName, clientContact);
    }

    public void deleteOrder(int orderId) {
        Response response = httpPost("php_action/removeOrder.php", "orderId", orderId);
        throwIfFalse(response.isSuccessful(), "response is not successful");
        assertJsonTrue(bodyString(response), "success");
    }

    public Integer createProduct(String productName, String quantity, String rate, int brandId, int categoryId, int active, boolean needProductId) {
        MultipartBody body = GenericUtils.serializeMultipart(
                "productName", productName,
                "quantity", quantity,
                "rate", rate,
                "brandName", brandId,
                "categoryName", categoryId,
                "productStatus", active,
                "productImage", DUMMY_IMAGE_DATA);
        Response response = httpPost("php_action/createProduct.php", body);
        throwIfFalse(response.isSuccessful(), "response is not successful");
        assertJsonTrue(bodyString(response), "success");
        if (!needProductId) {
            return null;
        }
        JSONArray products = getAllProducts();
        JSONArray last_product = products.getJSONArray(products.length() - 1);
        return last_product.getInt(0);
    }

    public JSONArray getAllProducts() {
        Response response = httpGet("php_action/fetchProductData.php");
        return new JSONArray(bodyString(response));
    }

    public Integer createDummyProduct(String name) {
        return createDummyProduct(name, true);
    }

    public Integer createDummyProduct(String name, boolean needProductId) {
        return this.createProduct(name, "10000", "100", 0, 0, 1, needProductId);
    }

    public void removeProduct(int productId) {
        assertJsonTrue(bodyString(httpPost("php_action/removeProduct.php", "productId", productId)), "success");
    }


    public void editProduct(int productId, String productName, int brandId, int categoryId, String quantity, String rate, int active) {
        Response response = httpPost("php_action/editProduct.php", "productId", productId, "editProductName", productName, "editQuantity", quantity, "editRate", rate, "editBrandName", brandId, "editCategoryName", categoryId, "editProductStatus", active);
        assertJsonTrue(bodyString(response), "success");
    }

    public int createBrand(String brandName, int brandStatus) {
        assertJsonTrue(bodyString(httpPost("php_action/createBrand.php", "brandName", brandName, "brandStatus", brandStatus)), "success");
        JSONArray brands = getBrands();
        JSONArray last = brands.getJSONArray(brands.length() - 1);
        Pattern p = Pattern.compile("editBrands\\((\\d+)\\)");
        Matcher m = p.matcher(last.getString(2));
        throwIfFalse(m.find(), "failed to find id of brand from button html");
        return Integer.parseInt(m.group(1));
    }

    public int createBrand(String brandName) {
        return createBrand(brandName, 1);
    }

    private JSONArray getBrands() {
        String body = bodyString(httpGet("php_action/fetchBrand.php"));
        return new JSONObject(body).getJSONArray("data");
    }

    public void removeBrand(int brandId) {
        assertJsonTrue(bodyString(httpPost("php_action/removeBrand.php", "brandId", brandId)), "success");
    }

    public int createCategory(String categoryName, int categoryStatus) {
        assertJsonTrue(bodyString(httpPost("php_action/createCategories.php", "categoriesName", categoryName, "categoriesStatus", categoryStatus)), "success");
        JSONArray brands = getCategories();
        JSONArray last = brands.getJSONArray(brands.length() - 1);
        Pattern p = Pattern.compile("editCategories\\((\\d+)\\)");
        Matcher m = p.matcher(last.getString(2));
        throwIfFalse(m.find(), "failed to find category id from button html");
        return Integer.parseInt(m.group(1));
    }

    public int createCategory(String categoryName) {
        return createCategory(categoryName, 1);
    }

    private JSONArray getCategories() {
        String body = bodyString(httpGet("php_action/fetchCategories.php"));
        return new JSONObject(body).getJSONArray("data");
    }

    public void removeCategory(int categoryId) {
        assertJsonTrue(bodyString(httpPost("php_action/removeCategories.php", "categoriesId", categoryId)), "success");
    }

    public void changeBio(int userId, String bio) {
        assertJsonTrue(bodyString(httpPost("php_action/changeBio.php", "user_id", userId, "bio", bio)), "success");
    }
}
