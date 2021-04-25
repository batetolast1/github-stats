package io.batetolast1.githubstats.service.github.impl;

import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.service.github.ApiService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GraphQLApiService implements ApiService {

    @Override
    public List<Repo> getAllRepos(String username) {
        // TODO implement GraphQL API
        return Collections.emptyList();
    }
}
