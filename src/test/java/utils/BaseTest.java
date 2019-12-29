package utils;



import org.junit.jupiter.api.*;


public abstract class BaseTest {
	protected ProcedureHelper helper;

	@BeforeEach
	public void prepareHelper(){
		helper = ProcedureHelper.requireInstance();
	}


	public static void quitHelper(){
		ProcedureHelper.quitInstance();
	}

	@BeforeAll
	public static void initHelper(){
		ProcedureHelper.requireInstance();
	}

	public abstract void test();

	@AfterEach
	public void after(){
		try {
			clean();
		} catch (Exception e){
			Logging.e("Failed to clean test "+this.getClass().getSimpleName(), e);
		}
	}

	public void clean(){

	}


	protected void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}