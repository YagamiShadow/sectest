package utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public abstract class BaseTest {
    public static String USE_HOST = null;
    protected ProcedureHelper helper;

    public static void quitHelper() {
        ProcedureHelper.quitInstance();
    }

    @BeforeAll
    public static void initHelper() {
        ProcedureHelper.requireInstance();
    }

    @BeforeEach
    public void prepareHelper() {
        if (USE_HOST == null) {
            helper = ProcedureHelper.requireInstance();
        } else {
            helper = ProcedureHelper.requireInstance(USE_HOST);
        }
    }

    public abstract void test();

    @AfterEach
    public void after() {
        registerShutdownHook();
        cleanSafely();
    }

    public void clean() {

    }

    protected void cleanSafely() {
        try {
            this.clean();
        } catch (Exception e) {
            Logging.e("Failed to clean test " + this.getClass().getSimpleName(), e);
        }
    }


    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean shutDownHookRegistered = false;

    private void registerShutdownHook() {
        if (shutDownHookRegistered) {
            return;
        }
        shutDownHookRegistered = true;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcedureHelper.quitInstance();
                } catch (Exception e) {
                    Logging.e("Did not quit helper before shutdown", e);
                }
            }
        }));
    }

    protected void assertPayloadNextTo(XssPayload payload, String elementId){
        WebElement payloadElement = GenericUtils.elementNextTo(helper.findElement(By.id(elementId)));
        assert payloadElement != null;
        assert payload.isTheElement(payloadElement);
    }

    private static final String formFormat = "<form method=\"POST\" id=\"form_id\" action=\"%s\">%s</form>";

    protected void postGet(String url, String... post_params){
        if (post_params.length % 2 != 0){
            throw new IllegalArgumentException("Invalid number of params");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<post_params.length; i+=2){
            builder.append("<input type=\"hidden\" name=\"").append(post_params[i]).append("\" value=\"").append(post_params[i+1].replace("\"", "\\\"")).append("\" />");
        }
        helper.getHtmlContent(String.format(formFormat, helper.getFullUrl(url), builder.toString()));
        helper.findElement(By.id("form_id")).submit();
        sleep(100);
        helper.waitDocumentReady();
    }

}