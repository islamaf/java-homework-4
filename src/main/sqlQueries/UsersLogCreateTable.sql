CREATE TABLE users_logs
(
    user_ip VARCHAR(255) NOT NULL,
    query_time TIMESTAMP WITH TIME ZONE  NOT NULL,
    http_query VARCHAR(255) NOT NULL,
    page_size INTEGER NOT NULL,
    status INTEGER NOT NULL,
    browser VARCHAR(255) NOT NULL
);