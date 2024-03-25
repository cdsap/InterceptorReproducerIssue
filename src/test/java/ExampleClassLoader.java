import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class ExampleClassLoader extends URLClassLoader {
    public ExampleClassLoader(URL[] urls, ClassLoader classLoader) {
        super(urls, classLoader);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        var ex = findLoadedClass(name);
        if (ex != null) {
            return ex;
        }
        var res = getResource(name.replace(".", "/") + ".class");
        if (res != null && res.getProtocol().equals("file")) {
            try (var in = res.openStream()) {
                var data = in.readAllBytes();
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return super.loadClass(name, resolve);
    }
}
