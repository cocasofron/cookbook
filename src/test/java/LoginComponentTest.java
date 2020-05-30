import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginComponentTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode("parola03");
        System.out.println(encodedPass);
    }
}
