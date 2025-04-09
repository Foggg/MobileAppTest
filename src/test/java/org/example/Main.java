package org.example;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Docker Test!!!");
        final LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(DiscoverySelectors.selectClass(StartTest.class))
                .build();
        final Launcher launcher = LauncherFactory.create();
        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        long testFoundCount = summary.getTestsFoundCount();
        List<TestExecutionSummary.Failure> failures = summary.getFailures();
        System.out.println("getTestsSucceededCount() - " + summary.getTestsSucceededCount());    failures.forEach(failure -> System.out.println("failure - " + failure.getException()));
    }
}

