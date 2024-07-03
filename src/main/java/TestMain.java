import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestMain {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("1234"));
        System.out.println(encoder.encode("a"));
        System.out.println(encoder.encode("b"));
        System.out.println(encoder.encode("c"));
        System.out.println(encoder.encode("asdd"));
    }
}
