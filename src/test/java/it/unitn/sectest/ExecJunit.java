package it.unitn.sectest;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import utils.BaseTest;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Hello world!
 *
 */

public class ExecJunit
{
    public static void main(String[] args) {
        runAll();
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
    }

    private static SummaryGeneratingListener listener = new SummaryGeneratingListener();

    private static void runAll() {
        BaseTest.initHelper();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("it.unitn.sectest.xss_suite"))
                .filters(includeClassNamePatterns(".+[.$]Xss.*"))
                .build();
        Launcher launcher = LauncherFactory.create();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        BaseTest.quitHelper();
    }



}
