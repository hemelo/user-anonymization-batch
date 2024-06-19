import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hetech.batch.anonymization.useranonymization.models.User;
import com.hetech.batch.anonymization.useranonymization.processors.UserItemProcessor;
import com.hetech.batch.anonymization.useranonymization.utils.AESUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class UserItemProcessorTest {

    private UserItemProcessor userItemProcessor;
    private SecretKey secretKey;

    @BeforeEach
    public void setUp() throws Exception {
        secretKey = AESUtil.generateKey();
        userItemProcessor = new UserItemProcessor(secretKey);
    }

    @Test
    public void testProcess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("123-456-7890");
        user.setAddress("1234 Elm Street");

        User processedUser = userItemProcessor.process(user);

        assertNotNull(processedUser);
        assertNotNull(processedUser.getName());
        assertNotNull(processedUser.getEmail());
        assertNotNull(processedUser.getPhone());
        assertNotNull(processedUser.getAddress());

        // Decrypt the values to check if they match the original
        assertEquals("John Doe", AESUtil.decrypt(processedUser.getName(), secretKey));
        assertEquals("john.doe@example.com", AESUtil.decrypt(processedUser.getEmail(), secretKey));
        assertEquals("123-456-7890", AESUtil.decrypt(processedUser.getPhone(), secretKey));
        assertEquals("1234 Elm Street", AESUtil.decrypt(processedUser.getAddress(), secretKey));
    }
}