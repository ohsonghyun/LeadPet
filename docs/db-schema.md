# User

## Users

```sql
CREATE TABLE users
(
    user_id                   VARCHAR(30)  NOT NULL,
    created_date              TIMESTAMP,
    modified_date             TIMESTAMP,
    email                     VARCHAR(60),
    login_method              VARCHAR(20)  NOT NULL,
    name                      VARCHAR(50)  NOT NULL,
    password                  VARCHAR(50),
    profile_image             VARCHAR(255),
    shelter_account           VARCHAR(150),
    shelter_address           VARCHAR(150),
    shelter_assessment_status VARCHAR(20),
    shelter_home_page         VARCHAR(255),
    shelter_intro             VARCHAR(255),
    shelter_manager           VARCHAR(30),
    shelter_name              VARCHAR(50),
    shelter_phone_number      VARCHAR(30),
    uid                       VARCHAR(100) NOT NULL,
    user_type                 VARCHAR(20)  NOT NULL,
    PRIMARY KEY (user_id)
);
```

## Saved Post

```sql
CREATE TABLE saved_post
(
    saved_post_id VARCHAR(30) NOT NULL,
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    post_id       VARCHAR(30),
    post_type     VARCHAR(20),
    user_id       VARCHAR(30),
    PRIMARY KEY (saved_post_id)
);
```

-----------------------------------------------------------------------
-----------------------------------------------------------------------

# Posts

## Normal Post

```sql
CREATE TABLE normal_posts
(
    normal_post_id VARCHAR(30)  NOT NULL,
    created_date   TIMESTAMP,
    modified_date  TIMESTAMP,
    contents       CLOB         NOT NULL,
    images         VARCHAR(255),
    title          VARCHAR(100) NOT NULL,
    user_id        VARCHAR(30)  NOT NULL,
    PRIMARY KEY (normal_post_id)
);
```

## Adoption Post

```sql
CREATE TABLE adoption_posts
(
    adoption_post_id VARCHAR(30)  NOT NULL,
    created_date     TIMESTAMP,
    modified_date    TIMESTAMP,
    age              INTEGER,
    animal_type      VARCHAR(20),
    breed            VARCHAR(20),
    contents         CLOB,
    disease          VARCHAR(255),
    end_date         TIMESTAMP,
    euthanasia_date  TIMESTAMP,
    gender           VARCHAR(10),
    images           VARCHAR(255),
    neutering        VARCHAR(20),
    start_date       TIMESTAMP,
    title            VARCHAR(100) NOT NULL,
    user_id          VARCHAR(30)  NOT NULL,
    PRIMARY KEY (adoption_post_id)
);
```

## Donation Post

```sql
CREATE TABLE donation_posts
(
    donation_post_id VARCHAR(30) not null,
    created_date     TIMESTAMP,
    modified_date    TIMESTAMP,
    contents         CLOB,
    donation_method  VARCHAR(20),
    end_date         TIMESTAMP,
    images           VARCHAR(255),
    start_date       TIMESTAMP,
    title            VARCHAR(100),
    user_id          VARCHAR(30) NOT NULL,
    PRIMARY KEY (donation_post_id)
);
```

-----------------------------------------------------------------------
-----------------------------------------------------------------------

# Reply

## Normal Reply

```sql
CREATE TABLE normal_reply
(
    normal_reply_id VARCHAR(30) NOT NULL,
    created_date    TIMESTAMP,
    modified_date   TIMESTAMP,
    content         CLOB,
    normal_post_id  VARCHAR(30) NOT NULL,
    user_id         VARCHAR(30) NOT NULL,
    PRIMARY KEY (normal_reply_id)
);
```

-----------------------------------------------------------------------
-----------------------------------------------------------------------

# Liked

```sql
CREATE TABLE liked
(
    liked_id      VARCHAR(30) NOT NULL,
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    post_id       VARCHAR(30) NOT NULL,
    user_id       VARCHAR(30) NOT NULL,
    PRIMARY KEY (liked_id)
);
```

-----------------------------------------------------------------------
-----------------------------------------------------------------------

# Breed

```sql
CREATE TABLE breed
(
    breed_id      VARCHAR(255) NOT NULL,
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    animal_type   VARCHAR(255) NOT NULL,
    breed_name    VARCHAR(255),
    category      VARCHAR(255),
    PRIMARY KEY (breed_id)
);
```

