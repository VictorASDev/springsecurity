# Twitter Clone

Este projeto é uma aplicação de exemplo usando Spring Boot, Spring Security, OAuth2, JWT, JPA e MySQL, simulando funcionalidades de uma rede social semelhante ao Twitter.

## Funcionalidades

- Cadastro e autenticação de usuários com JWT
- Perfis de usuário, seguidores e seguindo
- Postagem e remoção de tweets
- Feed paginado de tweets
- Controle de permissões por papéis (ADMIN, BASIC)
- Documentação automática via Swagger/OpenAPI

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.x
- Spring Security (OAuth2 Resource Server)
- Spring Data JPA
- MySQL
- JWT (com chaves RSA)
- Docker (para banco de dados)
- Swagger/OpenAPI

## Configuração

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker (opcional, para rodar o MySQL)

### Banco de Dados

O projeto espera um banco MySQL rodando em `localhost:3306` com o nome `twitter_db`.  
Você pode subir rapidamente usando Docker Compose:

```sh
docker-compose -f [docker-compose.yml](http://_vscodecontentref_/0) up -d
