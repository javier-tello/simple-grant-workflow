# simple-grant-workflow

Simple grant workflow is a simple API that will allow you to create basic nonprofits and send basic emails.

This project was created using the [Ktor Project Generator](https://start.ktor.io).

## End Points 

Here's a list of features included in this project:

| Name             | Type | Description                                                                                                                                                                                                                                                       |
|------------------|------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /nonprofits      | GET  | Get a list of all nonprofits                                                                                                                                                                                                                                      |
| /nonprofits      | POST | Create a nonprofit                                                                                                                                                                                                                                                |
| /send-email      | POST | Sends an email to a nonprofit                                                                                                                                                                                                                                     |
| /send-email/bulk | POST | Sends emails to a list of nonprofits and allows templatized message. Currently only supports {{name}} and {{address}} which will be taken from existing nonprofits. If email is not associated with a nonprofit, it will not send an email to that email address. |
| /get-all-emails  | GET  | gets all emails sent to nonprofits                                                                                                                                                                                                                                |

## Definitions for POST requests

`/nonprofts`
```
{
    "name": "Sample Nonprofit",
    "email": "emailme@example.com",
    "address": "111 Second St"
}
 ```

`/send-email`
```
{
    "to": "nonprofit@nonprofit.com",
    "from": "me@myemail.com",
    "subject": "Import Info",
    "body": "I am doanting money to this nonprofit"
}
```

`/send-email/bulk`
```
{
    "to": ["emailme@example1.com", "emailme@example2.com", "emailme@example3.com"],
    "from": "me@myemail.com",
    "subject": "Import Info",
    "body": "Sending money to nonprofit {{name}} at address {{address}}"
}
```


## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

