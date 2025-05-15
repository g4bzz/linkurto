CREATE TABLE tb_url(
    id INT PRIMARY KEY AUTO_INCREMENT,
    url VARCHAR(256) NOT NULL,
    short_url VARCHAR(256) NOT NULL,
    expiration_date DATETIME DEFAULT NOW(),

    CONSTRAINT unique_short_url UNIQUE (short_url),
    CONSTRAINT unique_url UNIQUE (url)
);