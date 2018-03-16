# Proms Platform
A platform for recording and analysis of patient outcomes. Initially scoped to orthopaedics.

## Development

### Frontend
Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and builds for the front-end.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.
3. [RabbitMq](https://www.rabbitmq.com/): This application uses RabbitMq for asynchronous messaging.
    So you'll either need an instance of `RabbitMq` running on port `5672`. Our recommended approach is to use `docker-compose` to install `RabbitMq`. We have already included the necessary setup for you. So all you need to do is run the following command in a new tab of your commandline tool. It'll download a dockerised instance of `RabbitMq` and run it - you will of course need to install Docker first.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.

### Backend
We use Maven[] to manage backend dependencies and builds.

### Running software locally

Run the following commands in three separate terminals to launch the application locally.

    docker-compose -f src/main/docker/rabbitmq.yml up
    ./mvnw
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application.

## Building for production

To optimize the proms platform for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in [src/test/javascript/e2e](src/test/javascript/e2e)
and can be run by starting Spring Boot in one terminal (`./mvnw spring-boot:run`) and running the tests (`yarn run e2e`) in a second one.
### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling) and can be run with:

    ./mvnw gatling:execute

For more information, refer to the [Running tests page][].

## Using Docker to simplify development

A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.
For example, to start a mariadb database in a docker container, run:

    docker-compose -f src/main/docker/mariadb.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mariadb.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw verify -Pprod dockerfile:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d
