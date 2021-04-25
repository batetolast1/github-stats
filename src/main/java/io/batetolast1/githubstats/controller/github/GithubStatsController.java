package io.batetolast1.githubstats.controller.github;

import io.batetolast1.githubstats.dto.BasicUserDetailsDto;
import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.model.github.UserDetails;
import io.batetolast1.githubstats.service.github.RepoService;
import io.batetolast1.githubstats.service.github.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/githubStats")
@RequiredArgsConstructor
@Validated
public class GithubStatsController {

    private final RepoService repoService;
    private final UserDetailsService userDetailsService;

    @GetMapping(value = "/allRepos", params = "username")
    public ResponseEntity<UserDetails> getAllRepos(@RequestParam @Valid @NotBlank String username) {
        return ResponseEntity.ok(userDetailsService.getUserDetailsWithAllRepos(username));
    }

    @GetMapping(value = "/repos", params = "username")
    public ResponseEntity<Page<Repo>> getRepos(@RequestParam @Valid @NotBlank String username,
                                               Pageable pageable) {
        return ResponseEntity.ok(repoService.getRepos(username, pageable));
    }

    @GetMapping(value = "/userDetails", params = "username")
    public ResponseEntity<BasicUserDetailsDto> getBasicUserDetails(@RequestParam @Valid @NotBlank String username) {
        return ResponseEntity.ok(BasicUserDetailsDto.from(userDetailsService.getBasicUserDetails(username)));
    }
}
