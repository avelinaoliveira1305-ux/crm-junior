# CRM Júnior - Sistema Corporativo de Gestão de Leads

Projeto desenvolvido como atividade avaliativa da disciplina de **Desenvolvimento de Sistemas Corporativos**, ministrada pelo **Prof. Francisco Chagas de Lima Júnior**, no curso de Sistemas para Internet (modalidade a distância) da **Universidade do Estado do Rio Grande do Norte (UERN)**.

---

## 1. Cenário da Empresa Fictícia e Objetivo

A **CRM Júnior** é uma organização fictícia voltada para a prestação de serviços de consultoria empresarial e soluções digitais. O sistema implementado atua como um módulo corporativo simplificado de CRM (*Customer Relationship Management*), focado na captação, triagem e conversão de potenciais clientes (*leads* comerciais).

### Arquitetura em Três Camadas (MVC)
O projeto adota o padrão arquitetural em camadas:
1. **Camada de Visão (View / Aplicação):** Desenvolvida em HTML5, CSS3 moderno (Tailwind CSS via CDN) e JavaScript nativo com API `fetch`, proporcionando uma interface responsiva para interação com o usuário.
2. **Camada de Controle e Negócios (Controller / Service):** Implementada em Java com Spring Boot 3 e Apache Tomcat embutido, expondo endpoints RESTful padronizados para o gerenciamento do fluxo negocial.
3. **Camada de Modelo e Persistência (Model / Repository):** Mapeamento Objeto-Relacional (ORM) com Spring Data JPA e Hibernate, conectado a um banco relacional MariaDB.

---

## 2. Transações e Caso de Uso

### Caso de Uso: Gestão do Funil de Leads
* **Ator Principal:** Consultor Comercial / Gestor.
* **Fluxo Principal:**
  1. O usuário acessa a aplicação web (`/`) e visualiza o formulário de cadastro e a lista atualizada de leads.
  2. O usuário preenche os dados do contato comercial (Nome, E-mail, Telefone, Empresa e Status inicial) e clica em **Cadastrar Lead**.
  3. A camada de visão envia uma requisição `POST` para o endpoint `/api/leads`.
  4. A camada de controle recebe o objeto, delega a validação à camada de serviço e persiste o registro no MariaDB via repositório JPA.
  5. A lista em tela é atualizada automaticamente exibindo o novo registro.
  6. É possível atualizar o estágio negocial do lead via requisições `PUT` (`NOVO`, `EM_CONTATO`, `PROPOSTA`, `CONVERTIDO`) ou remover registros via `DELETE`.

---

## 3. Tecnologias Utilizadas

* **Linguagem:** Java 21 LTS
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Servidor Web:** Apache Tomcat (embutido)
* **Gerenciador de Dependências:** Apache Maven
* **Banco de Dados:** MariaDB / MySQL (executado via XAMPP na porta 3307)
* **Front-end:** HTML5, Tailwind CSS, JavaScript (`fetch`)
* **IDE:** Eclipse IDE

---

## 4. Endpoints da API REST

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/leads` | Lista todos os leads cadastrados |
| `GET` | `/api/leads/{id}` | Busca um lead específico por identificador |
| `POST` | `/api/leads` | Cadastra um novo lead no banco |
| `PUT` | `/api/leads/{id}` | Atualiza dados e status de um lead existente |
| `DELETE` | `/api/leads/{id}` | Remove um lead do sistema |
| `GET` | `/api/leads/relatorio` | Retorna métricas consolidadas do funil |
---

## 5. Instruções de Execução

### Pré-requisitos
* Java JDK 17 ou superior instalado.
* Servidor MariaDB/MySQL ativo via XAMPP escutando na porta `3307`.
* Base de dados criada com o nome `crm_db`.

### Configuração do Banco de Dados

No painel do phpMyAdmin (`http://localhost/phpmyadmin/`), crie a base de dados:

```sql
CREATE DATABASE crm_db;
As credenciais configuradas em `src/main/resources/application.properties` são:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3307/crm_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### Execução da Aplicação

1. Clone o repositório ou importe o projeto no Eclipse como **Existing Maven Projects**.
2. Localize a classe principal: `src/main/java/br/com/crm/CrmJuniorApplication.java`.
3. Clique com o botão direito > **Run As** > **Spring Boot App** (ou **Java Application**).
4. O servidor iniciará na porta `8080`.

### Acesso à Interface Web

Abra o navegador e acesse:

```text
http://localhost:8080/
```
#### Acesso ao Sistema e Autenticação
O sistema conta com controlo de sessão e restrição de acesso:
* **Página de Autenticação:** `http://localhost:8080/login.html`
* **Utilizador Padrão:** `admin`
* **Palavra-passe Padrão:** `admin123`
* Após autenticar com sucesso, a sessão é registada e o utilizador é redirecionado para o painel principal (`index.html`). O painel dispõe também da opção de encerramento seguro de sessão (**Terminar Sessão**).