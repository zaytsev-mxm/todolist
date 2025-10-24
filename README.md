# ToDo App

Here we will try to create back-end for a ToDo application, where
we will be able to:

- register new users
- log in those users
- create ToDo lists
- edit those ToDo lists (add items, edit items, mark as done, etc.)

## Basic setup

> Look into the `.env.sample` file for more details.

Everything is as simple as:
```sh
# Create the .env file (amend its content if needed)
cp .env.sample .env
```
and
```sh
# Run the app
docker compose up --build
```
😃 Enjoy!

### How to run via IntelliJ IDEA

If you are using IntelliJ IDEA, you can run the app by pressing the green play button in your IDE.
For a detailed guide, please see [this](./docs/LAUNCHING_WITH_INTELLIJ_IDEA.md) document.

### How to run via manually via Bash

If you want (for any reason) to be able to run the app manually via Bash, you can do so by:

- Run the DB via
```sh
sh tools/db.sh --up
# or
# sh tools/db.sh --down
# to turn it off
```
- Run the app via
```sh
sh tools/dev.sh --load-env
```

## Git Hooks

Please set up the Git hooks before you commit or push anything.

To do so, run this script [./tools/git/setup-hooks.sh](./tools/git/setup-hooks.sh)

## Testing the API
You can easily test the API using Postman. To do that, just:
- Install [Postman](https://www.postman.com/)
- Import the request collection that you can find [here](./tools/postman/ToDo%20List.postman_collection.json), and also use the environment setup from this [file](./tools/postman/ToDoEr%20Local.postman_environment.json).

## Publishing new Docker image versions cheatsheet

New versions of the Docker images you build should be controlled via the `DOCKER_IMAGE_VERSION` environment variable.
Make sure you have it set before you build the image. Refer to [.env.sample](.env.sample) for more details.
```sh
# Build the image
sh tools/docker/build.sh
```

```sh
# Tag the built image
sh tools/docker/tag.sh
```

```sh
# Push the image
sh tools/docker/push.sh
```