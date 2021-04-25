package io.batetolast1.githubstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.batetolast1.githubstats.model.github.UserDetails;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicUserDetailsDto {

    private String username;
    @JsonProperty("all_stargazers_count")
    private int allStargazersCount;
    @JsonProperty("latest_fetch")
    private LocalDateTime latestFetch;

    public static BasicUserDetailsDto from(UserDetails userDetails) {
        return BasicUserDetailsDto.builder()
                .username(userDetails.getUsername())
                .allStargazersCount(userDetails.getAllStargazersCount())
                .latestFetch(userDetails.getLatestFetch())
                .build();
    }
}
