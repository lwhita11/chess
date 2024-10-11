# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

My Sequence Diagram:

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdjAFYtlap1JqtAzqBJoIei0azF5vDgHK8FfMlnqvig776K8SwAucwKXnKKDlAgB48j0+6Hqi6KxN+0EamGbpkhSBq0uWo4mkS4YFJaMDcryBqCsKMCAZoOidq6LLlJEYA+LcdoCtoobEW6SZFtBLEUuxSRUdxCaGLKaowTAIAQMMvCGLCYnyC4H5LMBCxLNiEmYbxzGybqmSxv604hkx6iRhyMDRjAZniRh-FnECJZITy2a5pgf4glJJRXFMgErF806zs2EyTOh1Adk52Q9jA-aDr0AUEaMQU1kGoXzuFMBLpwq7eH4gReCg6B7gevjMMe6SZJgsV5AJUVphU0gAKK7i19Qtc0LQPqoT7dCFjboO2IKFN55SDXOXkQdFDVOvKMBwfYFWwktFUoRiOmOQSWEGRwKDcMZQYBhlQ1oERTJuha1n7Ydhj2fIQphJNw3GIxu2kjAirKjxl0svxEkKkqKq6U541fcD00uT5c3-pFxwxeeYB9gOQ4HGYeWeAVG6Qrau7QjAADio6slVp61UjbJXuUFSEx13X2KOA2nVNV5suDL1oFDqZU4Ji3Qqt0IbbEW3SXpf2feSYAmSddZnRdZoRoUZEUbawDKjAjOjIyrI0YBMA8jA3Q6AgoANgadystAMAnpkFsk-R736Z9mxgCA6QOJro4679iudoDXva9oGig8mM1psgsQeQgebh-7xT-lMWtqOMlT9MnACS0ipwAjL2ADMAAsTy2-qKX3H0azdH0Jtm-bqXhU8ycAHKjuFewwI08PRacdXI5UCXNEOSck6nFTp6OWe5wXxdTKX9cV1X861yA5vl2lI+jK3ozt53uUrlj66BNg7HYNw8BGYYxOjCk1VnjkzC+SWN4NAzTPBCz6BDi3o7d6Nzmpgmp-XoP9RjgWhrzaS5RPR6lhDAzIwssSOmdDtZ27pDJekyNfFAsJk7mQ+krdkE0bSBxQA6CyEZfLgghj9UOY1w7QMvtHWOECnLU1LJvFAU9yh5yLjlEanY+400HsPPomds48Jnvw5c+Uj4BEsAdOCyQYAACkICG2wYEFeDYKYP0gX5SoVRKR3haMnZmcs5xDjPsABRUA4AQDglAWY4i-6-gYaWaxtj7GOOcZPaQ4DUxQSgTAAAVuotAsIwk8mwaLeakl8gUJwmAbBuC-EK3DPiMiAAzHkto8HaCeqQrOvsMlsL5t9EG20ZAEPKJSbAWgsGjlSUHeQsxObvlNl4hx0B0lXSsuUPwDTDD5MeskDIqRaLlxgFk7wwxzEzjOnZXknjKDeOgMglQ-FwZqPcmoTy3kAYJzTD0P+gikbCIHI0NGGMD5rkKgELwNiuxelgMAbAZ9CDxESLfcmfd9HP1au1Tq3VjACLDtDK4ASCxUIWtIA6FIUCgokgURJRh4U1QoddcoWTrbqwQJMrirS7JBkZL4lpwBZiQmGBAGgGzJLgsAWiu6zDubQqfsc05iMH4XKcM0E5NzMBAA
