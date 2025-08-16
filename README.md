# ToDo App

Here we will try to create back-end for a ToDo application, where
we will be able to:

- register new users
- log in those users
- create ToDo lists
- edit those ToDo lists (add items, edit items, mark as done, etc.)

## Basic setup

Everything is as simple as:
```sh
# Create the .env file (amend its content if needed)
cp .env.sample .env

# Run the app
docker compose up
```
😃 Enjoy!

## Git Hooks

Please set up the Git hooks before you commit or push anything.

To do so, run this script [./tools/git/setup-hooks.sh](./tools/git/setup-hooks.sh)

## Testing the API
You can easily test the API using Postman. To do that, just:
- Install [Postman](https://www.postman.com/)
- Import the request collection that you can find [here](./tools/postman/ToDo%20List.postman_collection.json)

## Publishing new Docker image versions cheatsheet

Let's assume that the version we need to publish is `1.0.1`
```sh
# Build the image
docker buildx build . --tag=todolist-backend:1.0.1

# Tag the built image
docker tag todolist-backend:1.0.1 zaytsevmxm/todolist-backend:1.0.1

# Push the image
docker push zaytsevmxm/todolist-backend:1.0.1
```