
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.platform.launcher.LauncherInterceptor;
public class CustomLauncherInterceptor implements LauncherInterceptor {

    private final URLClassLoader customClassLoader;

    public CustomLauncherInterceptor() throws Exception {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        List<URL> urls = new ArrayList<>();
        urls.add(this.getClass().getResource("/"));
        customClassLoader = new ExampleClassLoader(urls.toArray(URL[]::new), parent);
    }

    @Override
    public <T> T intercept(Invocation<T> invocation) {
        Thread currentThread = Thread.currentThread();
        ClassLoader originalClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(customClassLoader);
        try {
            return invocation.proceed();
        }
        finally {
            currentThread.setContextClassLoader(originalClassLoader);
        }
    }

    @Override
    public void close() {
        try {
            customClassLoader.close();
        }
        catch (IOException e) {
            throw new UncheckedIOException("Failed to close custom class loader", e);
        }
    }
}
