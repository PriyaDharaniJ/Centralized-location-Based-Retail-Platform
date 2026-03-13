package com.retailplatform.util;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class TrackingIdGenerator {
    private final Random random = new Random();

    public String generate() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        int rand = 1000 + random.nextInt(9000);
        return "RP" + timestamp + rand;
    }
}
