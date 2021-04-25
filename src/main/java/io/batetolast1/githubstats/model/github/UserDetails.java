package io.batetolast1.githubstats.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@NamedEntityGraph(name = "graph.UserDetails.repos", attributeNodes = @NamedAttributeNode("repos"))
public class UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private Long id;
    @NotBlank
    private String username;
    @JsonProperty("all_stargazers_count")
    @Min(0)
    private Integer allStargazersCount;
    @JsonProperty("latest_fetch")
    private LocalDateTime latestFetch;
    @OneToMany
    @JsonInclude(NON_NULL)
    private List<Repo> repos;
}
