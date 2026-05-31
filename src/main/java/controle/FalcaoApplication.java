package controle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"controle", "banco"})
public class FalcaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FalcaoApplication.class, args);
    }
}
