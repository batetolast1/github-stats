# github-stats

### Description
Back-end API service that allows to:

- list repositories (by name and stargazers count)
- get all stargazers count

for any GitHub user.

### Technologies used
- Java 16
- Spring Boot 2.4.5
- Spring Web
- Spring Data JPA, Hibernate, Hibernate Validator, H2 database
- Spring Devtools, Spring Configuration Processor
- Lombok
- JUnit5, AssertJ, Mockito

### Setup for UNIX systems
0. Prerequisites:
- cURL
- UnZip
- Docker

1. Get your own GitHub Personal access token - no special permissions needed (https://github.com/settings/tokens)

2. Run
```shell
curl -L -O https://github.com/batetolast1/github-stats/archive/refs/heads/main.zip
unzip main.zip
cd github-stats-main
nano Dockerfile
```

3. Edit Dockerfile
```shell
ENV H2_DB ${H2_DB} # H2 database name
ENV H2_USERNAME ${H2_USERNAME} # H2 database user
ENV H2_PASSWORD ${H2_PASSWORD} # H2 database password
ENV GITHUB_PERSONAL_ACCESS_TOKEN ${GITHUB_PERSONAL_ACCESS_TOKEN} # GitHub personal access token
ENV GITHUB_APP_OWNER_USERNAME ${GITHUB_APP_OWNER_USERNAME} # GitHub profile name
```
4. Run
```shell
docker build -t github-stats-main .
docker run -d -p 8080:8080 github-stats-main
```

### API Endpoints
- get all user repos: GET /githubStats/allRepos (params: username), i.e. http://localhost:8080/githubStats/allRepos?username=batetolast1
- get filtered user repos: GET /repos?username (params: username, page, size, sort), i.e. http://localhost:8080/githubStats/repos?username=batetolast1&page=2&size=5&sort=name,asc&sort=stargazersCount,desc
- get all stargazers count for user: GET /githubStats/userDetails (params: username), i.e. http://localhost:8080/githubStats/userDetails?username=batetolast1

### TODO
- add more DTOs
- add more unit tests
- add some integration tests
- add Spring Security for user authentication
- add documentation with SpringDoc OpenAPI
- add simple rate limiter
- add more features!!!

### Copyright
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)