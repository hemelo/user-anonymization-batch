import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hetech.batch.anonymization.useranonymization.repository.UserRepository;
import com.hetech.batch.anonymization.useranonymization.seeder.UserSeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserSeederTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSeeder databaseSeeder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        databaseSeeder.run();

        verify(userRepository, times(1)).saveAll(anyList());
    }
}
