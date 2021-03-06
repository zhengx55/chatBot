# Chatbot Backend

This backend will provide analyzed data based on request(query) from frontend

## Dependencies

* Apache Lucene
* crawler4j
* Spring framework
* SQLite
* OpenNLP(TBD)
* WordNet(TBD)
* Hadhoop(TBD)


## Before Build

add your ibm cloud natural language understanding api key to below and copy this block to /backend/src/main/resource/application.properties
``` 
nlu.version=2018-11-16
nlu.url=https://gateway.watsonplatform.net/natural-language-understanding/api
nlu.apikey=<Your ibm cloud natural language understanding api key>
```
## Build

run `bash backend-build.sh` in root directory

or

run `mvn clean install` in current directory

## Run

After Build success, run `java -jar backend/target/backend-0.0.1-SNAPSHOT.jar`
For now, send GET req to localhost:9000/helloworld will get hello world, frontend can use this as mock data at this moment.



## Authentication

get an access token by following command
```
curl chatbot:L80eUZjHnafVOG5TWRenSGfiMkPL2j03@localhost:8080/oauth/token -d grant_type=password -d username=<username> -d password=<password>
```
you will receive something like:
```
{
"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2hhdGJvdFJlc3RBcGkiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTYyODM5NzkyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiJmYTUyYTcxMy02OTQ3LTQ0ODMtOTNmMy03M2QyNzE4YzkzOGEiLCJjbGllbnRfaWQiOiJjaGF0Ym90In0.J85XvcjXP0qdBAGEcKEGrkAb29KdQ_b49fENKtaqciA",
"token_type":"bearer",
"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2hhdGJvdFJlc3RBcGkiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZmE1MmE3MTMtNjk0Ny00NDgzLTkzZjMtNzNkMjcxOGM5MzhhIiwiZXhwIjoxNTY1Mzg4NTkyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiIxODJiNzgwYi1jNmI2LTRiYjgtYjk0Mi00OTY5ZjQ3YmQxOGEiLCJjbGllbnRfaWQiOiJjaGF0Ym90In0.wV-FQji61Ou7sgBBuIK-tR4aKMT5a4onCaUo_QdjBmo",
"expires_in":43199,
"scope":"read write",
"jti":"fa52a713-6947-4483-93f3-73d2718c938a"}%
```


API request with Authorization header: Bearer <access_token>
```$xslt
curl http://localhost:8080/query/ -H “Authorization: Bearer GENERATED_AUTH_TOKEN” -d ......
```
