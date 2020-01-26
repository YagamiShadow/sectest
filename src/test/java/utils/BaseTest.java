package utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public abstract class BaseTest {
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
        helper = ProcedureHelper.requireInstance();
    }

    public abstract void test();

    @AfterEach
    public void after() {
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

}