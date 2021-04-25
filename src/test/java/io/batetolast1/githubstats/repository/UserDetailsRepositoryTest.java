package io.batetolast1.githubstats.repository;


import io.batetolast1.githubstats.model.github.UserDetails;
import io.batetolast1.githubstats.repository.github.UserDetailsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static io.batetolast1.githubstats.util.UserDetailsCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@DisplayName("UserDetails JPA Repository test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserDetailsRepositoryTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    @DisplayName("save() persists UserDetails when successful")
    void should_PersistUserDetails_When_Save() {
        UserDetails userDetailsToBeSaved = createValidUserDetailsToBeSaved();

        UserDetails savedUserDetails = userDetailsRepository.save(userDetailsToBeSaved);

        assertThat(savedUserDetails).isNotNull();
        assertThat(savedUserDetails.getId()).isNotNull();
        assertThat(savedUserDetails.getUsername()).isNotNull();
        assertThat(savedUserDetails.getUsername()).isEqualTo(userDetailsToBeSaved.getUsername());
    }

    @Test
    @DisplayName("save() updates an existing UserDetails when successful")
    void should_UpdatePersistedUserDetails_When_Save() {
        UserDetails userDetailsToBeSaved = createValidUserDetailsToBeSaved();

        UserDetails savedUserDetails = userDetailsRepository.save(userDetailsToBeSaved);

        Integer newStargazersCount = 101;
        savedUserDetails.setAllStargazersCount(newStargazersCount);

        UserDetails updatedUserDetails = userDetailsRepository.save(savedUserDetails);

        assertThat(updatedUserDetails).isNotNull();
        assertThat(updatedUserDetails.getId()).isNotNull().isEqualTo(savedUserDetails.getId());
        assertThat(updatedUserDetails.getUsername()).isNotNull().isEqualTo(savedUserDetails.getUsername());
        assertThat(updatedUserDetails.getAllStargazersCount()).isEqualTo(newStargazersCount);
    }

    @Test
    @DisplayName("save() throws ConstraintViolationException when invalid UserDetails")
    void should_ThrowConstraintViolationException_When_UserDetailsIsInvalid() {
        UserDetails invalidUserDetails = createInvalidUserDetailsToBeSaved();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> userDetailsRepository.save(invalidUserDetails));
    }


    @Test
    @DisplayName("findByUsername() finds UserDetails by username when successful")
    void should_FindByNamePersistedUserDetails_When_FindByUsername() {
        UserDetails userDetailsToBeSaved = createValidUserDetailsToBeSaved();

        UserDetails savedUserDetails = userDetailsRepository.save(userDetailsToBeSaved);

        String username = savedUserDetails.getUsername();

        Optional<UserDetails> userDetails = userDetailsRepository.findByUsername(username);

        assertThat(userDetails).isNotEmpty().contains(savedUserDetails);
    }

    @Test
    @DisplayName("findByName() returns empty list when no UserDetails is found")
    void should_ReturnEmptyList_When_FindByUsername() {
        Optional<UserDetails> userDetails = userDetailsRepository.findByUsername(VALID_USERNAME);

        assertThat(userDetails).isEmpty();
    }
}