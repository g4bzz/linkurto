# Linkurto

> Linkurto é um encurtador básico de links, desenvolvido com Spring, Angular e MySQL.

![Java Version](https://img.shields.io/badge/Java-17-red)
![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-3.4.5-6db33f)
![Angular CLI Version](https://img.shields.io/badge/Angular-18.2.19-red)
![MySQL Version](https://img.shields.io/badge/MySQL-9.3.0-blue)
![Prometheus](https://img.shields.io/badge/Prometheus-v2.53.5-f15a22)
![Grafana](https://img.shields.io/badge/Grafana-12.0.2-FDDA0D)

## Características

- Encurtamento de links através de um método de _hashing_ baseado em MD5;
- Validação de URL no frontend e no backend;
- Rotina automática (cron) de exclusão de links expirados;
- Integração com o [Google reCAPTCHA](https://developers.google.com/recaptcha) para impedir sobrecargas de requisição.

## Utilização

### Como executar o projeto (via docker compose)
#### Pré requisitos
- Docker instalado com docker-compose;
- arquivo .env no diretório **docker** seguindo o padrão do arquivo [.env.example](/docker/.env.example).

> Observação: a aplicação está toda configurada para rodar no localhost.

```sh

# Entre na pasta docker
cd docker

# execute o arquivo compose com o parâmetro de build para gerar as imagens do backend e frontend de acordo com os seus respectivos arquivos Dockerfile

docker compose up --build

# Portas dos containers:
# - Frontend: 80 (localhost)
# - Backend: 8088 (localhost:8088)
# - Mysql: 3306 (localhost:3306) 
# - Grafana: 3000 (localhost:3000)

```

### Executar testes unitários

```sh
cd backend

./mvnw test -Punit-tests
```

### Executar testes de integração

```sh
cd backend

./mvnw test -Pintegration-tests
```

### Documentação (Swagger)

A documentação completa da API pode ser acessada em: [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html).

## Backend

O backend da aplicação foi construído utilizando Spring + Java 17, contando com validação nas APIs, testes unitários e testes de integração utilizando JUnit e Mockito.

Utilizou-se das boas práticas de programação como SOLID e Clean Code, bem como o emprego de alguns padrões de projeto. Com isso, foi possível desenvolver o projeto de maneira que o mesmo permitisse evoluções.

Para gerar os links encurtados, foi implementado uma lógica de hash baseada no algoritmo MD5 e com uma trativa especial para garantir que os códigos gerados não sejam repetidos. Por conta da forma na qual o sistema foi projeto, outra estratégia de geração de link curto pode ser facilmente implementada e adicionada ao sistema, desde que a nova classe estenda a classe base **Engine**.

A primeira versão do sistema se mantém com um escopo reduzido, dispensando autenticação e gerenciamento dos links gerados. Desta forma, essas e outras funcionalidades serão implementadas futuramente.

O banco de dados segue com seguinte schema:

```sql
CREATE DATABASE db_linkurto;

CREATE TABLE tb_url(
    id INT PRIMARY KEY AUTO_INCREMENT,
    url VARCHAR(256) NOT NULL,
    short_url VARCHAR(256) NOT NULL,
    expiration_date DATETIME DEFAULT NOW(),

    CONSTRAINT unique_short_url UNIQUE (short_url),
    CONSTRAINT unique_url UNIQUE (url)
);
```

A API de encurtamento possui integração com o sistema de captcha do google (reCAPTCHA v2), desta forma, só é possível encurtar um link mediante a posse de um token gerado através da resolução do desafio de captcha.

## Frontend

O frontend foi construído com o framework Angular + Typescript. Em relação a estilização dos componentes, foi utilizado a biblioteca Bootstrap.

A interface do sistema possui 3 telas que representam as 3 etapas do processo de encurtamento do link. São elas:

### Envio do link original

![Envio do link original](https://raw.githubusercontent.com/g4bzz/linkurto/9ac28238f06712ff4f0bf9d0e6ee1a8a98334e83/assets/enviando-link.png)

![Validação do Link](https://raw.githubusercontent.com/g4bzz/linkurto/9ac28238f06712ff4f0bf9d0e6ee1a8a98334e83/assets/validacao-link.png)

### Obtenção do link encurtado

![Obtenção do link encurtado](https://raw.githubusercontent.com/g4bzz/linkurto/9ac28238f06712ff4f0bf9d0e6ee1a8a98334e83/assets/link-encurtado.png)

### Acessando o link encurtado

![Acessando o link encurtado](https://raw.githubusercontent.com/g4bzz/linkurto/9ac28238f06712ff4f0bf9d0e6ee1a8a98334e83/assets/acessando-link.png)

## Observabilidade

Para poder visualizar as métricas da aplicação, utilizou-se o Grafana com auxílio do Prometheus como datasource. Ambos estão sendo executados a partir do arquivo Docker Compose.

A configuração do datasource é simples, basta criar um novo e informar o URL do Prometheus (http://prometheus:9090).

A dashboard usada para observar as métricas foi a [JVM (Micrometer)](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/).

![Importando o Dashbord JVM (Micrometer)](https://raw.githubusercontent.com/g4bzz/linkurto/6bd10f2132502ea62146c1a9cb908df3f1fae4c2/assets/importando-dashboard-grafana.png)

![Dashbord JVM (Micrometer)](https://raw.githubusercontent.com/g4bzz/linkurto/6bd10f2132502ea62146c1a9cb908df3f1fae4c2/assets/dashboard-grafana.png)
