package io.batetolast1.githubstats.controller;

import io.batetolast1.githubstats.controller.github.GithubStatsController;
import io.batetolast1.githubstats.dto.BasicUserDetailsDto;
import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.model.github.UserDetails;
import io.batetolast1.githubstats.service.github.RepoService;
import io.batetolast1.githubstats.service.github.UserDetailsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static io.batetolast1.githubstats.util.RepoCreator.createValidRepo;
import static io.batetolast1.githubstats.util.UserDetailsCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@DisplayName("Github Stats Controller test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GithubStatsControllerTest {

    @InjectMocks
    private GithubStatsController githubStatsController;

    @Mock
    private RepoService repoServiceMock;
    @Mock
    private UserDetailsService userDetailsServiceMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(userDetailsServiceMock.getUserDetailsWithAllRepos(ArgumentMatchers.any(String.class)))
                .thenReturn(createValidUserDetails());

        BDDMockito.when(userDetailsServiceMock.getUserDetailsWithAllRepos(ArgumentMatchers.eq("")))
                .thenThrow(ConstraintViolationException.class);

        BDDMockito.when(userDetailsServiceMock.getBasicUserDetails(ArgumentMatchers.any(String.class)))
                .thenReturn(createValidUserDetails());

        BDDMockito.when(repoServiceMock.getRepos(ArgumentMatchers.any(String.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(createValidRepo())));
    }

    @Test
    void should_Return_UserDetails_When_GetAllRepos() {
        ResponseEntity<UserDetails> responseEntity = githubStatsController.getAllRepos(VALID_USERNAME);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isNotNull();
        assertThat(responseEntity.getBody().getUsername()).isEqualTo(VALID_USERNAME);
        assertThat(responseEntity.getBody().getAllStargazersCount()).isEqualTo(VALID_STARGAZERS_COUNT);

        assertThat(responseEntity.getBody().getRepos().get(0)).isNotNull();
        assertThat(responseEntity.getBody().getRepos().get(0).getId()).isNotNull();
    }

    @Test
    void should_Throw_ConstraintValidationException_When_GetAllRepos() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> githubStatsController.getAllRepos(""));
    }

    @Test
    void should_Return_ListOfReposInsidePageObject_When_GetRepos() {
        String expectedRepoName = createValidRepo().getName();

        Page<Repo> repoPage = githubStatsController.getRepos(VALID_USERNAME, PageRequest.of(0, 5)).getBody();

        assertThat(repoPage).isNotNull();
        assertThat(repoPage.toList()).isNotEmpty();
        assertThat(repoPage.toList().get(0).getName()).isEqualTo(expectedRepoName);
    }

    @Test
    void should_Return_BasicUserDetails_When_GetBasicUserDetails() {
        Integer expectedStargazersCount = createValidUserDetails().getAllStargazersCount();

        BasicUserDetailsDto basicUserDetailsDto = githubStatsController.getBasicUserDetails(VALID_USERNAME).getBody();

        assertThat(basicUserDetailsDto).isNotNull();
        assertThat(basicUserDetailsDto.getAllStargazersCount()).isEqualTo(expectedStargazersCount);
    }
}