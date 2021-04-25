package io.batetolast1.githubstats.service.github;

import io.batetolast1.githubstats.model.github.UserDetails;

public interface UserDetailsService {

    void fetchOrRefreshDataIfNeeded(String username);

    UserDetails getBasicUserDetails(String username);

    UserDetails getUserDetailsWithAllRepos(String username);
}
