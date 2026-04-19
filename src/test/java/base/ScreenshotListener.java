package base;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotListener implements IInvokedMethodListener {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private String time() {
        return LocalTime.now().format(FMT);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            System.out.println("[" + time() + "] >>> RUNNING: " + method.getTestMethod().getMethodName());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            String name = method.getTestMethod().getMethodName();
            String status;
            switch (result.getStatus()) {
                case ITestResult.SUCCESS: status = "PASS v"; break;
                case ITestResult.FAILURE: status = "FAIL x"; break;
                case ITestResult.SKIP:    status = "SKIP -"; break;
                default:                  status = "UNKNOWN"; break;
            }
            System.out.println("[" + time() + "] <<< " + status + ": " + name);
        }

    }
}
