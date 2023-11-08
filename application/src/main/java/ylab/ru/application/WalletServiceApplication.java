package ylab.ru.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.example.logging.EnableLoggingAspect;

@SpringBootApplication
@EnableLoggingAspect
public class WalletServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(WalletServiceApplication.class, args);
    }
}
