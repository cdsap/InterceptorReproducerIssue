import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomClassLoaderTest {

    @Test
    public void classLoaderTest() {
        String loader = this.getClass().getClassLoader().getClass().getName();
        assertThat(loader).isEqualTo("ExampleClassLoader");
    }
}
