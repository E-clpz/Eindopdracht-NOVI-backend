package nl.novi.eindopdracht.configurations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Admin123!";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}