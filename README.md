# CRM Júnior — Sistema Corporativo de Gestão de Leads

Projeto desenvolvido como atividade avaliativa da disciplina de **Desenvolvimento de Sistemas Corporativos**, ministrada pelo **Prof. Me. Francisco Chagas de Lima Júnior**, no curso de Sistemas para Internet (modalidade a distância) da **Universidade do Estado do Rio Grande do Norte (UERN)**.

---

## 1. Cenário da Empresa Fictícia e Objetivo

A **CRM Júnior** é uma empresa fictícia de consultoria empresarial e soluções digitais. O sistema desenvolvido é um CRM (*Customer Relationship Management*) simplificado para gerenciar potenciais clientes (*leads*), acompanhar negociações e consultar indicadores comerciais.

### 1.1. Arquitetura em Três Camadas

O projeto separa rigorosamente as responsabilidades em três camadas arquiteturais:

1. **Apresentação (View):** Páginas `login.html` e `index.html`, desenvolvidas com HTML5, CSS3, Tailwind CSS via CDN e Vanilla JavaScript. A comunicação assíncrona com o back-end é realizada via API `fetch()`.
2. **Controle e Regras de Negócio (Controller e Service):** 
   - `LeadController.java`: Endpoints REST e controle de requisições/respostas HTTP.
   - `LeadService.java`: Validações e regras de negócio de cálculo de relatórios e status.
   - `SecurityConfig.java`: Configuração dos filtros de segurança, autenticação baseada em sessão e suporte a HTTP Basic.
3. **Persistência (Model e Repository):** 
   - `Lead.java`: Entidade mapeada via Jakarta Persistence (JPA) / Hibernate.
   - `LeadRepository.java`: Interface Spring Data JPA para abstração de operações no banco de dados MariaDB.

---

## 2. Funcionalidades e Casos de Uso

### 2.1. Caso de Uso 01 — Gestão de Leads
**Ator principal:** Administrador autenticado.
1. O administrador acessa o painel do CRM Júnior.
2. Preenche os dados do lead: nome, e-mail, telefone, empresa e status.
3. Ao cadastrar, o front-end envia uma requisição `POST /api/leads`.
4. O back-end valida, processa e persiste o registro no banco MariaDB.
5. A listagem reflete imediatamente o lead cadastrado.
6. O administrador pode editar os dados, alterar o status (`NOVO`, `EM_CONTATO`, `PROPOSTA` ou `CONVERTIDO`) e excluir registros.

### 2.2. Caso de Uso 02 — Relatório e Indicadores Comerciais
**Ator principal:** Administrador autenticado.
1. O painel solicita os indicadores ao endpoint `GET /api/leads/relatorio`.
2. O serviço consulta a base de dados e calcula as métricas dinâmicas.
3. A API retorna um JSON estruturado com `totalLeads`, `emAndamento`, `concluidos` e `distribuicaoStatus`.
4. O painel apresenta os cartões de total de oportunidades, leads em andamento e negócios convertidos/concluídos.

### 2.3. Caso de Uso 03 — Autenticação e Encerramento de Sessão
**Ator principal:** Administrador.
1. O usuário acessa `/login.html` e submete as credenciais via `POST /login`.
2. O Spring Security intercepta e autentica as credenciais com `BCryptPasswordEncoder`.
3. Com o login válido, ocorre o redirecionamento automático para `/index.html`.
4. As rotas `/api/**` e o painel exigem a autoridade `ROLE_ADMIN`.
5. Tentativas inválidas redirecionam para `/login.html?error=true`.
6. O acionamento do botão **Terminar Sessão** invalida a sessão no servidor e redireciona para `/login.html?logout=true`.

---

## 3. Tecnologias Utilizadas

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java 21 (LTS)** | Linguagem de programação principal do back-end |
| **Spring Boot 4.1.2** | Framework para configuração, injeção de dependências e microsserviço |
| **Spring Security** | Camada de controle de acesso, filtros de autenticação e proteção |
| **Spring Data JPA / Hibernate** | Mapeamento Objeto-Relacional (ORM) e persistência de dados |
| **MariaDB** | Sistema Gerenciador de Banco de Dados Relacional (SGBD) |
| **Apache Maven** | Automação e gerenciamento de dependências do projeto |
| **Apache Tomcat Embutido** | Servidor web HTTP interno para execução da aplicação |
| **HTML5 / CSS3 / Tailwind CSS** | Estruturação e estilização da interface gráfica moderna |
| **Vanilla JavaScript** | Lógica assíncrona cliente (`fetch`) e manipulação do DOM |
| **Eclipse IDE** | Ambiente Integrado de Desenvolvimento |
| **Git / GitHub** | Controle de versão distribuído e hospedagem do código-fonte |

---

## 4. Documentação da API REST

| Método | Endpoint | Descrição | Nível de Acesso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/leads` | Listar todos os leads cadastrados | Autenticado (`ADMIN`) |
| `GET` | `/api/leads/{id}` | Consultar lead específico por identificador | Autenticado (`ADMIN`) |
| `POST` | `/api/leads` | Cadastrar novo lead | Autenticado (`ADMIN`) |
| `PUT` | `/api/leads/{id}` | Atualizar dados cadastrais de um lead | Autenticado (`ADMIN`) |
| `DELETE` | `/api/leads/{id}` | Excluir registro de lead da base de dados | Autenticado (`ADMIN`) |
| `GET` | `/api/leads/relatorio` | Obter consolidado de indicadores comerciais | Autenticado (`ADMIN`) |

### 4.1. Parâmetros de Segurança
- **Formulário Web:** Sessão baseada em cookies pós-autenticação.
- **Suporte a Clientes REST / Terminal:** Autenticação HTTP Basic (`Authorization: Basic ...`).
- **Endpoints Públicos:** `/login.html`, `/css/**`, `/js/**`.
- **Endpoints Protegidos:** `/index.html` e `/api/**`.
- *Nota acadêmica:* Usuário definido em memória com hash BCrypt e proteção CSRF desabilitada para simplificação de testes locais de API via cURL.

---

## 5. Como Executar o Projeto

### 5.1. Pré-requisitos
- JDK 21 instalado e configurado no PATH (`JAVA_HOME`).
- Eclipse IDE (ou terminal com Maven instalado).
- Instância do MariaDB ativa (via XAMPP na porta padrão ou porta `3307`).

### 5.2. Configuração da Base de Dados
No cliente SQL (phpMyAdmin, DBeaver ou MySQL Workbench), crie o esquema:
```sql
CREATE DATABASE IF NOT EXISTS crm_db;
O arquivo src/main/resources/application.properties está configurado para:
spring.application.name=crm-junior

spring.datasource.url=jdbc:mariadb://localhost:3307/crm_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

###5.3. Execução pelo Eclipse IDE
Abra o Eclipse e vá em File > Import... > Maven > Existing Maven Projects.

Selecione a pasta raiz do projeto e conclua.

Localize o arquivo src/main/java/br/com/crm/CrmJuniorApplication.java.

Clique com o botão direito > Run As > Java Application (ou Spring Boot App).

Aguarde a mensagem no Console: Started CrmJuniorApplication.

###5.4. Execução via Linha de Comando (Terminal)
Na raiz do projeto (onde reside o arquivo pom.xml), execute:
mvn clean install
mvn spring-boot:run
##6. Acesso ao Sistema e Credenciais

URL de Login: http://localhost:8080/login.html

Painel Administrativo: http://localhost:8080/index.html (requer login ativo)

### 6.1. Credenciais de Demonstração

| Campo | Valor | Observação |
| --- | --- | --- |
| **Usuário** | `admin` | Conta administrativa padrão |
| **Senha** | `admin123` | Criptografada via BCrypt no back-end |
| **Perfil** | `ROLE_ADMIN` | Permissão total aos endpoints `/api/**` |

##7. Testes e Validação da Aplicação

###7.1. Validação Visual e Fluxos Web

1. Acesso Protegido: Acesse http://localhost:8080/index.html em uma aba anônima. O sistema deve redirecionar automaticamente para /login.html.

2. Autenticação com Sucesso: Informe admin / admin123. O sistema autenticará via POST /login e redirecionará para o painel.

3. Credenciais Inválidas: Tente entrar com senha errada; a página exibirá aviso de erro em /login.html?error=true.

4. Encerramento de Sessão: Clique em Terminar Sessão. A sessão é destruída no servidor e o navegador vai para /login.html?logout=true.

###7.2. Teste dos Endpoints via Terminal (cURL)

Requisição não autorizada (sem credenciais):

curl -i http://localhost:8080/api/leads
Resultado esperado: HTTP/1.1 401 Unauthorized.

Requisição autenticada (via HTTP Basic):

curl -i -u admin:admin123 http://localhost:8080/api/leads
Resultado esperado: HTTP/1.1 200 OK com a lista de leads em formato JSON.

##8. Estrutura de Diretórios do Projeto

crm-junior/
├── src/
│   ├── main/
│   │   ├── java/br/com/crm/
│   │   │   ├── CrmJuniorApplication.java
│   │   │   ├── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   └── LeadController.java
│   │   │   ├── model/
│   │   │   │   └── Lead.java
│   │   │   ├── repository/
│   │   │   │   └── LeadRepository.java
│   │   │   └── service/
│   │   │       └── LeadService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           └── login.html
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md

##9. Considerações Finais

O projeto CRM Júnior consolida com rigor técnico a aplicação dos pilares de Desenvolvimento de Sistemas Corporativos:

Arquitetura corporativa em três camadas com desacoplamento de responsabilidades.

Segurança perimetral com controle de acesso, criptografia de senhas e proteção de endpoints REST.

Persistência mapeada com ORM/JPA e integridade relacional no MariaDB.

Interface limpa, responsiva e integrada por chamadas assíncronas assentes no protocolo HTTP.




