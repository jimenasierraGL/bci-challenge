# BCI-CHALLENGE
Author: Maria Jimena Sierra

## Deployment

First we have to generate the .Jar file:
```bash
  gradle build
```
Then, to deploy this project with Docker open a terminal on the project folder and run
```bash
  docker-compose up -d
```

## API Reference

### POST User 

```http
  POST /user
```
**Request Body**

| Parameter  | Type     | Description                |
| :--------  | :------- | :------------------------- |
| `name    ` | `string` | User's name |
| `email   ` | `string` | **Required**. Email format should be aaaaaa@adomain.abc |
| `password` | `string` | **Required**. Password must have only one capital letter and only two numbers(not necessarily consecutive), in combination of lowercase letters, maximum length of 12 and minimum 8." |
| `phones `  | `List<Phone>` | User's list of Phones with|
| `phones.number `  | `Long` | Phone number|
| `phones.cityCode `  | `Integer` | City code|
| `phones.countryCode `  | `String` | Country code|

### Command example
```bash
curl --location 'http://localhost:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Jime Sierra",
    "email": "jimesierra@email.com",
    "password": "Contrasena12",
    "phones": [
        {
            "number": 155112233,
            "cityCode": 351,
            "countryCode": "54"
        }
    ]
}'
```
### Sequence diagram
<img src="POST - Sequence diagram.png" title="Sequence Diagram"/>

### GET User 

```http
  GET /user
```

| Header          | Type     | Description                |
|:----------------| :------- |:---------------------------|
| `Authorization` | `string` | **Required**. Bearer token |

### Command example
```bash
curl --location 'http://localhost:8080/user' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6ImFiYTZlNGJiLTBlNGUtNGI5ZC04MzIyLTIzNmY3MjNlNzU2YiIsImNyZWF0ZWQiOlsyMDIzLDExLDcsMTIsMjMsNTksMTI3MDAwMDAwXSwibGFzdExvZ2luIjpbMjAyMywxMSw3LDEyLDIzLDU5LDEyNzAwMDAwMF0sImlzQWN0aXZlIjp0cnVlLCJuYW1lIjoiSmltZSBTaWVycmEiLCJlbWFpbCI6Im1qaW1lc2llcnJhQGdtYWlsLmNvbSIsInBhc3N3b3JkIjoiJDJhJDEwJC9PYzNpeHl5MFVNa3lsbS5ueC5MdmUvNEcwaWFyLzJyQXdUMDVMMWJHcFlTUEUuRGEyTzFxIiwidG9rZW4iOiJleUpoYkdjaU9pSklVekkxTmlKOS5leUpwWkNJNmJuVnNiQ3dpWTNKbFlYUmxaQ0k2V3pJd01qTXNNVEVzTnl3eE1pd3lNeXcxT1N3eE1qY3dNREF3TURCZExDSnNZWE4wVEc5bmFXNGlPbHN5TURJekxERXhMRGNzTVRJc01qTXNOVGtzTVRJM01EQXdNREF3WFN3aWFYTkJZM1JwZG1VaU9uUnlkV1VzSW01aGJXVWlPaUpLYVcxbElGTnBaWEp5WVNJc0ltVnRZV2xzSWpvaWJXcHBiV1Z6YVdWeWNtRkFaMjFoYVd3dVkyOXRJaXdpY0dGemMzZHZjbVFpT2lJa01tRWtNVEFrTDA5ak0ybDRlWGt3VlUxcmVXeHRMbTU0TGt4MlpTODBSekJwWVhJdk1uSkJkMVF3TlV3eFlrZHdXVk5RUlM1RVlUSlBNWEVpTENKMGIydGxiaUk2Ym5Wc2JDd2ljR2h2Ym1WeklqcGJleUpwWkNJNmJuVnNiQ3dpYm5WdFltVnlJam94TlRVME1EZzJNRE1zSW1OcGRIbERiMlJsSWpvek5URXNJbU52ZFc1MGNubERiMlJsSWpvaU5UUWlmVjE5LkpGdm1mb2N4TXpyTnNUTGctRFZ3Q3FTSDBaenF0dzFFMEk3d2VqMmRORGMiLCJwaG9uZXMiOlt7ImlkIjoxLCJudW1iZXIiOjE1NTQwODYwMywiY2l0eUNvZGUiOjM1MSwiY291bnRyeUNvZGUiOiI1NCJ9XX0.UDUZqQMrGHCdsLUqA8pkP4TjaQGfwdoNmMkB-bAUB7E'
```
### Sequence diagram
<img src="GET - Sequence diagram.png" title="Sequence Diagram"/>

## Postman Collection
[See Postman Collection](BCI%20Challenge.postman_collection.json)

