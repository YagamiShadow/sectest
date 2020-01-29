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

}