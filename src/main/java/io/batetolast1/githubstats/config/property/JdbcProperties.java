package io.batetolast1.githubstats.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate.jdbc")
@ConstructorBinding
@Getter
public class JdbcProperties {

    private final int batchSize;
    private final long updateThreshold;

    public JdbcProperties(int batchSize, long updateThreshold) {
        this.batchSize = batchSize;
        this.updateThreshold = updateThreshold;
    }
}
