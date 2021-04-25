package io.batetolast1.githubstats.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "github.rest")
@ConstructorBinding
@Getter
public class GithubRestProperties {

    private final String personalAccessToken;
    private final String mediaType;
    private final String appOwnerUsername;
    private final String scheme;
    private final String host;
    private final String maxResultsPerPage;

    public GithubRestProperties(String personalAccessToken,
                                String mediaType,
                                String appOwnerUsername,
                                String scheme,
                                String host,
                                String maxResultsPerPage) {
        this.personalAccessToken = personalAccessToken;
        this.mediaType = mediaType;
        this.appOwnerUsername = appOwnerUsername;
        this.scheme = scheme;
        this.host = host;
        this.maxResultsPerPage = maxResultsPerPage;
    }
}
