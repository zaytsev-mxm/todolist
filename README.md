# ToDo App

Here we will try to create back-end for a ToDO application, where
we will be able to:

- register new users
- login those users
- create ToDo lists
- edit those ToDo lists (add items, edit items, mark as done, etc.)

## Basic setup

Everything is as simple as:
```
docker compose up
```
😃 Enjoy!

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