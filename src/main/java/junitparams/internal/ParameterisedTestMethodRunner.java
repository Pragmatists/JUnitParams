package junitparams.internal;

import java.lang.reflect.Field;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

/**
 * Testmethod-level functionalities for parameterised tests
 *
 * @author Pawel Lipinski
 */
public class ParameterisedTestMethodRunner {

    public final TestMethod method;
    private int count;

    public ParameterisedTestMethodRunner(TestMethod testMethod) {
        this.method = testMethod;
    }

    public int nextCount() {
        return count++;
    }

    public int count() {
        return count;
    }

    Object currentParamsFromAnnotation() {
        return method.parametersSets()[nextCount()];
    }

    void runTestMethod(Statement methodInvoker, RunNotifier notifier) {
        Description methodWithParams = findChildForParams(methodInvoker, method.describe());

        runMethodInvoker(notifier, methodInvoker, methodWithParams);
    }

    private void runMethodInvoker(RunNotifier notifier, Statement methodInvoker, Description methodWithParams) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, methodWithParams);
        eachNotifier.fireTestStarted();
        try {
            methodInvoker.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    private Description findChildForParams(Statement methodInvoker, Description methodDescription) {
        if (System.getProperty("JUnitParams.flat") != null)
            return methodDescription;

        InvokeParameterisedMethod parameterisedInvoker = findParameterisedMethodInvokerInChain(methodInvoker);

        for (Description child : methodDescription.getChildren()) {
            if (parameterisedInvoker.matchesDescription(child))
                return child;
        }
        return null;
    }

    private InvokeParameterisedMethod findParameterisedMethodInvokerInChain(Statement methodInvoker) {
        while (methodInvoker != null && !(methodInvoker instanceof InvokeParameterisedMethod))
            methodInvoker = nextChainedInvoker(methodInvoker);

        if (methodInvoker == null)
            throw new RuntimeException("Cannot find invoker for the parameterised method. Using wrong JUnit version?");

        return (InvokeParameterisedMethod) methodInvoker;
    }

    private Statement nextChainedInvoker(Statement methodInvoker) {
        Field[] declaredFields = methodInvoker.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            Statement statement = statementOrNull(methodInvoker, field);
            if (statement != null)
                return statement;
        }

        return null;
    }

    private Statement statementOrNull(Statement methodInvoker, Field field) {
        if (Statement.class.isAssignableFrom(field.getType()))
            return getOriginalStatement(methodInvoker, field);

        return null;
    }

    private Statement getOriginalStatement(Statement methodInvoker, Field field) {
        field.setAccessible(true);
        try {
            return (Statement) field.get(methodInvoker);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
