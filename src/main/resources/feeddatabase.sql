create table feed_entries (
    id      varchar(500)     not null unique primary key,
    title   varchar(500)  not null,
    author  varchar(500),
    published varchar(500),
    updated varchar(500),
    summary text,
    content text,
    source varchar(1000)
);