# CampusCoffee (SE SS25)

## Spring Boot Web Application

### Build and start application dev profile activated

**Note:** In the `dev` profile, the repositories are cleared before startup and the initial data is loaded (see [`LoadInitialData.java`](https://github.com/se-ubt/ase24-taskboard/blob/main/application/src/main/java/de/unibayreuth/se/taskboard/LoadInitialData.java)).

Build application:
```shell
mvn clean install
```

Start Postgres docker container:
```shell
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:17-alpine
```

Start application (data source configured via [`application.yaml`](application/src/main/resources/application.yaml)):
```shell
cd application
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### REST requests (POS)

#### Get POS

All POS:
```shell
curl http://localhost:8080/api/pos
```
POS by ID:
```shell
curl http://localhost:8080/api/pos/1 # add valid POS id here
```
POS by name:
```shell
curl http://localhost:8080/api/pos/filter?name=Cafeteria%20(Mensa) # add valid POS name here
```

#### Create POS

```shell
curl --header "Content-Type: application/json" --request POST --data '{"name":"New Café","description":"Description","type":"CAFE","campus":"MAIN","street":"Teststraße","houseNumber":"99","postalCode":12345,"city":"Bayreuth"}' http://localhost:8080/api/pos
```

See bean validation in action:
```shell
curl --header "Content-Type: application/json" --request POST --data '{"name":"","description":"","type":"CAFE","campus":"MAIN","street":"Teststraße","houseNumber":"99","postalCode":12345,"city":"Bayreuth"}' http://localhost:8080/api/pos
```

#### Update task

Update title and description:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":4,"name":"New Café (UBT)","description":"My description","type":"CAFE","campus":"MAIN","street":"Teststraße","houseNumber":"99","postalCode":12345,"city":"Bayreuth"}' http://localhost:8080/api/pos/4 # set correct POS id here and in the body
```

### REST requests (users)

#### Get users

All users:
```shell
curl http://localhost:8080/api/users
```
User by ID:
```shell
curl http://localhost:8080/api/users/1 # add valid user id here
```
User by login name:
```shell
curl http://localhost:8080/api/users/filter?login_name=jane_doe # add valid user login name here
```

#### Create users

```shell
curl --header "Content-Type: application/json" --request POST --data '{"loginName":"new_login_name","emailAddress":"new.person@uni-bayreuth.de","firstName":"New","lastName":"Person"}' http://localhost:8080/api/users
```

See bean validation in action:
```shell
curl --header "Content-Type: application/json" --request POST --data '{"loginName":"new_login_name!","emailAddress":"new.personATuni-bayreuth.de","firstName":"","lastName":""}' http://localhost:8080/api/users
```

#### Update user

Update login name and email address:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":1,"createdAt":"2025-05-27T17:11:49.272537","updatedAt":"2025-05-27T17:11:49.272541","loginName":"jane_doe_new","emailAddress":"jane.doe.new@uni-bayreuth.de","firstName":"Jane","lastName":"Doe"}' http://localhost:8080/api/users/1 # set correct user id here and in the body
```

### REST requests (reviews)

#### Get reviews

All reviews:
```shell
curl http://localhost:8080/api/reviews
```
Review by ID:
```shell
curl http://localhost:8080/api/reviews/1 # add valid user id here
```
Get approved reviews for a POS:
```shell
curl http://localhost:8080/api/reviews/filter?pos_id=1 # add valid POS id here
```

#### Create reviews

```shell
curl --header "Content-Type: application/json" --request POST --data '{"posId":1,"authorId":1,"review":"Great place!"}' http://localhost:8080/api/reviews # use existing IDs for posId and authorId
```

#### Approve reviews

Users cannot approve their own reviews:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":1,"loginName":"jane_doe_new","emailAddress":"jane.doe.new@uni-bayreuth.de","firstName":"Jane","lastName":"Doe"}' http://localhost:8080/api/reviews/4 # use existing review ID and user DTO
```

However, users can approve the same review twice (which is a limitation of the current implementation):
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":2,"loginName":"maxmustermann","emailAddress":"max.mustermann@campus.de","firstName":"Max","lastName":"Mustermann"}' http://localhost:8080/api/reviews/4 # use existing review ID and user DTO
```
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":2,"loginName":"maxmustermann","emailAddress":"max.mustermann@campus.de","firstName":"Max","lastName":"Mustermann"}' http://localhost:8080/api/reviews/4 # use existing review ID and user DTO
```