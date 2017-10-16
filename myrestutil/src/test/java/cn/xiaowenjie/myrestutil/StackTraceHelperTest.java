package cn.xiaowenjie.myrestutil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

@Slf4j
public class StackTraceHelperTest {

    @Test
    public void test() throws NoSuchMethodException, ClassNotFoundException {
        String baseName = StackTraceHelper.getBasePackage("cn.xiaowenjie.myrestutil", -1);
        assertEquals("cn.xiaowenjie.myrestutil",baseName);
    }

    @Test
    public void test1() throws NoSuchMethodException, ClassNotFoundException {
        String baseName = StackTraceHelper.getBasePackage("cn.xiaowenjie.myrestutil", 2);
        assertEquals("cn.xiaowenjie",baseName);
    }

    @Test
    public void test2() throws NoSuchMethodException, ClassNotFoundException {
        String baseName = StackTraceHelper.getBasePackage("cn.xiaowenjie.myrestutil", 5);
        assertEquals("cn.xiaowenjie.myrestutil",baseName);
    }

    @Test
    public void testGetMainClass() throws NoSuchMethodException, ClassNotFoundException {
        log.info(StackTraceHelper.getMainClass().getCanonicalName());
    }

    @Test
    public void testGetMainClassInMultiThread() throws ExecutionException, InterruptedException {
        Callable<Class<?>> callable = new Callable<Class<?>>() {
            @Override
            public Class<?> call() throws Exception {
                return StackTraceHelper.getMainClass();
            }
        };
        Future<Class<?>> result = Executors.newSingleThreadExecutor().submit(callable);
        Class<?> mainClass = result.get();
        log.info(mainClass.getCanonicalName());
    }

    @Test
    public void testIsRunByJunit() {
        boolean isRunByJunit = StackTraceHelper.isRunByJunit(StackTraceHelper.getMainThreadStackTraceElements());
        assertEquals(true,isRunByJunit);

    }

}
