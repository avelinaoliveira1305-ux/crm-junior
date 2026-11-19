# CRM Júnior - Sistema Corporativo de Gestão de Leads

Projeto desenvolvido como atividade avaliativa da disciplina de **Desenvolvimento de Sistemas Corporativos**, ministrada pelo **Prof. Francisco Chagas de Lima Júnior**, no curso de Sistemas para Internet (modalidade a distância) da **Universidade do Estado do Rio Grande do Norte (UERN)**.

---

## 1. Cenário da Empresa Fictícia e Objetivo

A **CRM Júnior** é uma organização fictícia voltada para a prestação de serviços de consultoria empresarial e soluções digitais. O sistema implementado atua como um módulo corporativo simplificado de CRM (*Customer Relationship Management*), focado na captação, triagem e conversão de potenciais clientes (*leads* comerciais), bem como na emissão de relatórios executivos consolidados em tempo real.

### Arquitetura em Três Camadas (MVC)
O projeto adota o padrão arquitetural corporativo em três camadas com separação estrita de responsabilidades:
1. **Camada de Visão (Apresentação / View):** Desenvolvida em HTML5, CSS3 moderno (Tailwind CSS via CDN) e JavaScript nativo estruturado com a API `fetch`. Provê uma interface gráfica responsiva tanto para o painel operacional e dashboard (`index.html`) quanto para o controle de acesso e autenticação corporativa (`login.html`).
2. **Camada de Negócios e Controle (Business Logic & Security / Controller & Service):** Implementada em Java com Spring Boot 3 e servidor Apache Tomcat embutido. Expõe endpoints RESTful padronizados para o gerenciamento negocial e incorpora camada de segurança com **Spring Security**, gerenciando autenticação baseada em sessão segura (`JSESSIONID`) e suporte a HTTP Basic Authentication.
3. **Camada de Persistência e Dados (Persistence / Model & Repository):** Mapeamento Objeto-Relacional (ORM) estruturado com Spring Data JPA e Hibernate, integrado a um banco de dados relacional MariaDB.

---

## 2. Transações e Casos de Uso

### Caso de Uso 01: Gestão do Funil de Leads
- **Ator Principal:** Consultor Comercial / Gestor Autenticado.
- **Fluxo Principal:**
  1. O usuário devidamente autenticado acessa o painel web (`/index.html`) e visualiza o formulário de entrada e a listagem atualizada de leads cadastrados.
  2. O usuário preenche os dados do contato comercial (Nome, E-mail, Telefone, Empresa e Status inicial) e clica em **Cadastrar Lead**.
  3. A camada de visão despacha uma requisição assíncrona `POST` para o endpoint `/api/leads`.
  4. A camada de controle recebe o payload, delega a validação de integridade à camada de serviço e persiste o registro no MariaDB via repositório JPA.
  5. A tabela em tela é atualizada dinamicamente exibindo o novo registro.
  6. É possível atualizar o estágio negocial do lead via requisições `PUT` (`NOVO`, `EM_CONTATO`, `PROPOSTA`, `CONVERTIDO`) ou excluir registros via `DELETE`.

### Caso de Uso 02: Relatório Executivo Consolidado
- **Ator Principal:** Gestor / Consultor Comercial Autenticado.
- **Fluxo Principal:**
  1. Durante o carregamento do painel (`index.html`), o script cliente efetua uma chamada assíncrona `GET` para o endpoint `/api/leads/relatorio`.
  2. A camada de serviço processa os dados armazenados no banco relacional e calcula as métricas do funil, retornando uma resposta estruturada em JSON (Total de Leads, Em Andamento, Propostas e Convertidos).
  3. O frontend consome o JSON e preenche em tempo real os cards de indicadores executivos no topo da página.

### Caso de Uso 03: Autenticação Corporativa e Encerramento de Sessão
- **Ator Principal:** Qualquer usuário do sistema.
- **Fluxo Principal:**
  1. Tentativas de acesso não autenticado a rotas protegidas (`/index.html`, `/api/**`) são interceptadas pelo filtro de segurança e redirecionadas para a tela de autenticação (`/login.html`).
  2. O usuário submete suas credenciais corporativas via formulário seguro `POST /login`.
  3. O Spring Security valida as credenciais criptografadas via `BCryptPasswordEncoder`, cria a sessão de autenticação no servidor e redireciona o usuário para o painel principal (`/index.html`).
  4. Ao acionar o botão **Terminar Sessão**, a rota `/logout` é requisitada, invalidando a sessão corporativa no servidor e retornando o navegador para `/login.html?logout=true`.

---

## 3. Tecnologias Utilizadas

- **Linguagem:** Java 21 LTS
- **Framework:** Spring Boot 3.x
- **Segurança da Aplicação:** Spring Security (autenticação baseada em sessão corporativa, proteção de endpoints e criptografia via `BCryptPasswordEncoder`)
- **Persistência de Dados:** Spring Data JPA / Hibernate
- **Servidor de Aplicação:** Apache Tomcat (embutido)
- **Gerenciador de Dependências:** Apache Maven
- **Banco de Dados Relacional:** MariaDB / MySQL (executado via XAMPP na porta `3307`)
- **Front-end:** HTML5, Tailwind CSS, Vanilla JavaScript (API `fetch`)
- **Ambiente de Desenvolvimento (IDE):** Eclipse IDE for Enterprise Java and Web Developers

---

## 4. Endpoints da API REST e Política de Segurança

O controle de autorização e autenticação é gerenciado de maneira centralizada pela classe `SecurityConfig.java`. O acesso a regras de negócio e dados corporativos é condicionado à prévia autenticação.

| Método | Endpoint | Descrição da Operação | Regra de Autorização |
| :--- | :--- | :--- | :--- |
| `GET` | `/login.html` | Interface visual de acesso corporativo | **Público** (`permitAll`) |
| `POST` | `/login` | Processamento das credenciais do formulário | **Público** (`permitAll`) |
| `GET` | `/index.html` | Dashboard principal e gestão de leads | **Autenticado** (Sessão Web) |
| `GET` | `/api/leads` | Listagem completa de contatos comerciais | **Autenticado** (`ROLE_ADMIN`) |
| `GET` | `/api/leads/{id}` | Consulta detalhada de lead por ID | **Autenticado** (`ROLE_ADMIN`) |
| `POST` | `/api/leads` | Inclusão de novo lead no funil | **Autenticado** (`ROLE_ADMIN`) |
| `PUT` | `/api/leads/{id}` | Atualização cadastral e alteração de estágio | **Autenticado** (`ROLE_ADMIN`) |
| `DELETE`| `/api/leads/{id}` | Exclusão definitiva de registro de lead | **Autenticado** (`ROLE_ADMIN`) |
| `GET` | `/api/leads/relatorio`| Totalizadores e consolidação das etapas do funil | **Autenticado** (`ROLE_ADMIN`) |
| `GET` | `/logout` | Encerramento seguro de sessão e desautenticação | **Público** (`permitAll`) |

> **Consumo Direto / Testes de API:** Além do suporte a sessões web em navegadores, os endpoints `/api/**` contam com autenticação **HTTP Basic** habilitada, permitindo a validação de requisições através de clientes HTTP (cURL, Postman ou Insomnia) enviando cabeçalhos de autorização padrão.

---

## 5. Instruções de Execução

### Pré-requisitos
- **Java JDK 21** instalado e configurado na variável de ambiente `JAVA_HOME`.
- **XAMPP** com o módulo MariaDB/MySQL ativo e configurado na porta `3307`.
- Base de dados criada com o nome `crm_db`.

### Configuração do Banco de Dados

1. Inicie os módulos **Apache** e **MySQL** no XAMPP Control Panel.
2. Acesse a interface web do phpMyAdmin (`http://localhost/phpmyadmin/`) e execute a criação da base de dados:
   ```sql
   CREATE DATABASE crm_db;
O projeto já está configurado no arquivo src/main/resources/application.properties para conectar-se à porta 3307:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3307/crm_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### Execução da Aplicação

#### Via Eclipse IDE
1. Importe o projeto no Eclipse por meio do menu **File** > **Import...** > **Maven** > **Existing Maven Projects**.
2. No Project Explorer, localize o arquivo de inicialização: `src/main/java/br/com/crm/CrmJuniorApplication.java`.
3. Clique com o botão direito no arquivo > **Run As** > **Java Application** (ou **Spring Boot App**).
4. Acompanhe a inicialização na aba Console até a exibição da mensagem de conclusão:
   ```text
   Started CrmJuniorApplication in X.XXX seconds (process running for ...)
   
#### Via Linha de Comando (Terminal)
Na raiz do projeto (mesmo diretório do arquivo `pom.xml`), execute:
bash```bmvn clean install
mvn spring-boot:run
##6. Acesso ao Sistema e Validação de Segurança
###Credenciais Padrão do Sistema
Utilizador / Username: admin
Palavra-passe / Password: admin123
Perfil de Acesso: ROLE_ADMIN
### Validação dos Fluxos

1. **Acesso ao Painel:**
   - Acesse `http://localhost:8080/login.html` no navegador.
   - Preencha o formulário com `admin` e `admin123` e clique em **Entrar no Sistema**.
   - O Spring Security validará a requisição `POST /login` e redirecionará para `http://localhost:8080/index.html`.

2. **Encerramento de Sessão:**
   - Clique em **Terminar Sessão** no topo do painel.
   - O sistema invalidará a sessão e redirecionará para `http://localhost:8080/login.html?logout=true`.

3. **Teste de Proteção de Endpoints (Via cURL / Terminal):**
   - Tentativa sem credenciais (retorna HTTP 401 Unauthorized):
     ```bash
     curl -i http://localhost:8080/api/leads
     ```
   - Requisição autenticada com HTTP Basic (retorna HTTP 200 OK com os dados em JSON):
     ```bash
     curl -u admin:admin123 http://localhost:8080/api/leads
     ```