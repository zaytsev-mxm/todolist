# ToDo App

Here we will try to create back-end for a ToDO application, where
we will be able to:

- register new users
- login those users
- create ToDo lists
- edit those ToDo lists (add items, edit items, mark as done, etc.)

## Basic setup

You can clone this repo, and create a local database to connect to.
For now, we will use PostgreSQL. Find samle scripts here:

**Table "users"**
```sql
-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    login character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(50) COLLATE pg_catalog."default" NOT NULL,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (login)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;
```

**Table "tokens"**
```sql
-- Table: public.tokens

-- DROP TABLE IF EXISTS public.tokens;

CREATE TABLE IF NOT EXISTS public.tokens
(
    id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    login character varying(50) COLLATE pg_catalog."default" NOT NULL,
    token character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tokens_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tokens
    OWNER to postgres;
```

Then, put your own master-password to `src/main/kotlin/dev/todolist/Application.kt` and 
for now that should be enough.

Later we'll prove a more real-world looking-like way to run the project.