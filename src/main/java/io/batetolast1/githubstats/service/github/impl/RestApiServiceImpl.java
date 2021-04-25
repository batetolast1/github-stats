package io.batetolast1.githubstats.service.github.impl;

import io.batetolast1.githubstats.config.property.GithubRestProperties;
import io.batetolast1.githubstats.handler.ApiResponseErrorHandler;
import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.service.github.ApiService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.LINK;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Primary
public class RestApiServiceImpl implements ApiService {

    private static final Pattern FIRST_NEXT_LINK_PATTERN = Pattern.compile("<(.*?)>; rel=\"next\"");
    private static final Pattern NEXT_LINK_PATTERN = Pattern.compile("rel=\"prev\", <(.*?)>; rel=\"next\"");

    private final GithubRestProperties githubRestProperties;
    private final RestTemplate restTemplate;
    private final HttpEntity<String> httpEntity;
    private final UriComponentsBuilder uriComponentsBuilder;

    public RestApiServiceImpl(GithubRestProperties githubRestProperties) {
        this.githubRestProperties = githubRestProperties;

        this.restTemplate = new RestTemplateBuilder()
                .errorHandler(new ApiResponseErrorHandler())
                .build();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(githubRestProperties.getPersonalAccessToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType(githubRestProperties.getMediaType())));
        httpHeaders.add(USER_AGENT, githubRestProperties.getAppOwnerUsername());

        this.httpEntity = new HttpEntity<>(httpHeaders);

        this.uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme(githubRestProperties.getScheme())
                .host(githubRestProperties.getHost())
                .path("users")
                .pathSegment("{username}")
                .path("repos")
                .query("per_page={results_per_page}&sort=created&direction=asc");
    }

    @Override
    public List<Repo> getAllRepos(String username) {
        ResponseEntity<List<Repo>> reposResponseEntity = getReposResponseEntity(prepareUserReposUri(username));

        List<Repo> repos = Objects.requireNonNull(reposResponseEntity.getBody());

        while (linkHeaderHasNextLink(reposResponseEntity.getHeaders())) {
            reposResponseEntity = getReposResponseEntity(getNextLinkUri(reposResponseEntity.getHeaders()));
            repos.addAll(Objects.requireNonNull(reposResponseEntity.getBody()));
        }

        repos.forEach(repo -> repo.setUsername(username));

        return repos;
    }

    private boolean linkHeaderHasNextLink(HttpHeaders httpHeaders) {
        List<String> linkHeader = httpHeaders.getOrEmpty(LINK);

        return !linkHeader.isEmpty() && linkHeader.get(0).contains("rel=\"next\"");
    }

    private URI getNextLinkUri(HttpHeaders httpHeaders) {
        List<String> linkHeader = httpHeaders.getOrEmpty(LINK);

        if (!linkHeader.isEmpty()) {
            var matcher = linkHeader.get(0).contains("rel=\"prev\"")
                    ? NEXT_LINK_PATTERN.matcher(linkHeader.get(0))
                    : FIRST_NEXT_LINK_PATTERN.matcher(linkHeader.get(0));

            if (matcher.find()) {
                return URI.create(matcher.group(1));
            }
        }

        throw new HttpServerErrorException(NOT_FOUND);
    }

    private ResponseEntity<List<Repo>> getReposResponseEntity(URI userReposUri) {
        return restTemplate.exchange(
                userReposUri, GET, httpEntity, new ParameterizedTypeReference<>() {});
    }

    private URI prepareUserReposUri(String username) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", username);
        uriVariables.put("results_per_page", githubRestProperties.getMaxResultsPerPage());

        return uriComponentsBuilder.buildAndExpand(uriVariables)
                .encode()
                .toUri();
    }
}
