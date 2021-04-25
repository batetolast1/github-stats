package io.batetolast1.githubstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("io.batetolast1.githubstats.config")
public class GithubStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubStatsApplication.class, args);
    }

}
