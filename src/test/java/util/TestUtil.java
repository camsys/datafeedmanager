package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtil {
    public static String getResourceAsString(String resourceName) throws IOException {
        try (InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            String content = bos.toString(UTF_8);
            return content;
        } catch (Exception e){
            throw e;
        }
    }
}
