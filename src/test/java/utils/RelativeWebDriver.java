package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

public class RelativeWebDriver implements WebDriver {

    private RemoteWebDriver driver;
    private WebDriverProvider preferredProvider;
    private String hostURL;
    private Set<String> stashedHandles = null;
    private String latestHandle = null;

    public RelativeWebDriver(String hostURL, WebDriverProvider preferredProvider) {
        this.preferredProvider = preferredProvider;
        this.hostURL = hostURL;
        requireWebDriver();
    }

    private static Object executeScript(WebDriver driver, String script, Object... objects) throws JavascriptNotSupported {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeScript(script, objects);
        } else {
            throw new JavascriptNotSupported();
        }
    }

    private static RemoteWebDriver createWebDriver(WebDriverProvider provider) {
        RemoteWebDriver driver;

        switch (provider) {
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case IEXPLORER:
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }
        return driver;
    }

    private WebDriver requireWebDriver() {
        if (driver == null) {
            driver = createWebDriver(preferredProvider);
        }
        return driver;
    }

    protected String getFullUrl(String relativePath) {
        if (relativePath == null) {
            return null;
        }
        return hostURL + (relativePath.startsWith("/") ? "" : "/") + relativePath;
    }

    protected String getRelativeUrl(String fullUrl) {
        if (fullUrl == null) {
            return null;
        }
        String host = hostURL;
        if (fullUrl.toLowerCase().startsWith(host.toLowerCase())) {
            return fullUrl.substring(host.length());
        }
        return fullUrl;
    }

    public void get(String s) {
        this.get(s, true);
    }

    public String getCurrentUrl() {
        return getRelativeUrl(requireWebDriver().getCurrentUrl());
    }

    public String getTitle() {
        return requireWebDriver().getTitle();
    }

    public List<WebElement> findElements(By by) {
        return requireWebDriver().findElements(by);
    }

    public WebElement findElement(By by) {
        return requireWebDriver().findElement(by);
    }

    public String getPageSource() {
        return requireWebDriver().getPageSource();
    }

    public void close() {
        requireWebDriver().close();
    }

    public void quit() {
        requireWebDriver().quit();
    }

    public Set<String> getWindowHandles() {
        return requireWebDriver().getWindowHandles();
    }

    public String getWindowHandle() {
        return requireWebDriver().getWindowHandle();
    }

    public TargetLocator switchTo() {
        return requireWebDriver().switchTo();
    }

    public Navigation navigate() {
        return requireWebDriver().navigate();
    }

    public Options manage() {
        return requireWebDriver().manage();
    }

    public void maximizeWindow() {
        manage().window().maximize();
    }

    public void get(String path, boolean waitDocumentReady) {
        requireWebDriver().get(getFullUrl(path));
        if (waitDocumentReady) {
            this.waitDocumentReady();
        }
    }

    public void waitDocumentReady() {
        try {
            Thread.sleep(200); //MaybeWait after some click or stuff
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebDriverWait wait = new WebDriverWait(this, 5L);
        wait.until(driver -> {
            try {
                return executeScript(driver, "return document.readyState").equals("complete");
            } catch (JavascriptNotSupported javascriptNotSupported) {
                return true;
            }
        });
    }

    public Object executeScript(String script, Object... objects) throws JavascriptNotSupported {
        return executeScript(this.requireWebDriver(), script, objects);
    }

    public void stashHandles() {
        stashedHandles = getWindowHandles();
    }

    public void switchToLatestHandle() {
        latestHandle = getWindowHandle();
        for (String handle : getWindowHandles()) {
            if (stashedHandles != null && stashedHandles.contains(handle)) {
                continue;
            }
            switchTo().window(handle);
        }
    }

    public void closeBackToLatestWindow() {
        if (latestHandle == null) {
            throw new RuntimeException("Cannot get the latest handle");
        }
        close();
        switchTo().window(latestHandle);
        latestHandle = null;
    }

    public boolean hasNewWindows() {
        return stashedHandles != null && getWindowHandles().size() > stashedHandles.size();
    }

    public enum WebDriverProvider {
        CHROME,
        FIREFOX,
        EDGE,
        IEXPLORER
    }

    public static class JavascriptNotSupported extends Exception {
    }

}
