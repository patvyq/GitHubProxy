# GitHubProxy
GitHubProxy project fetches all non fork repositories with their branches for given user.

Main and only endpoint: </br>
http://host:port/user/{name}/repositories/info </br>
above endpoint accepts only media type application/json and returns 404 for non existing users (return empty list for users without repositories)

swager address: http://host:port/swagger-ui/index.html
