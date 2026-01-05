# 🚀 Sistema de Gestão de Benefícios (Desafio Técnico)

Este projeto consiste em uma aplicação completa (Fullstack) para gerenciamento de benefícios e transferências financeiras entre contas.

O objetivo principal foi demonstrar a capacidade de integração entre tecnologias modernas de Front-end (**Angular**) com uma arquitetura de Back-end robusta e corporativa, unindo a agilidade do **Spring Boot** com a confiabilidade de regras de negócio em **EJB (Enterprise JavaBeans)**.

---

## 🛠️ Tecnologias Utilizadas

### Back-end (API & Core)

* **Java 17+**
* **Spring Boot 3.x** (Camada de API REST e Injeção de Dependências)
* **Jakarta EE / EJB** (Camada de Regras de Negócio e Transações)
* **JPA / Hibernate** (Persistência de dados)
* **H2 Database** (Banco em memória para facilidade de execução)
* **Maven** (Gerenciamento de dependências)
* **JUnit 5 & Mockito** (Testes Unitários)

### Front-end (Client)

* **Angular 15+**
* **TypeScript**
* **Bootstrap 5** (Estilização e Responsividade)
* **RxJS** (Programação Reativa)

---

## 🏗️ Arquitetura e Decisões Técnicas

O sistema foi desenhado seguindo uma arquitetura em camadas para garantir a separação de responsabilidades:

1. **API Layer (Spring Boot):** Responsável por expor os endpoints REST, validação básica de entrada (DTOs) e tratamento de erros HTTP.
2. **Service Layer (EJB):** O coração do sistema. As regras de negócio (cálculos, validações de saldo, transferências) estão encapsuladas aqui.
* *Destaque:* Utilização de `LockModeType.PESSIMISTIC_WRITE` para garantir a integridade das transações e evitar condições de corrida (Race Conditions) em transferências simultâneas.


3. **Persistence Layer (JPA):** Acesso ao banco de dados.

---

## ⚙️ Pré-requisitos

* **Wildfly 37** ou superior instalado.
* **Java JDK 17** ou superior instalado.
* **Node.js** (v18+) e **NPM** instalados.
* **Maven** instalado (ou usar o wrapper `mvnw`).
* **Git**.

---

## 🚀 Como Executar o Projeto

### 1. EJB + Wildfly

1. Acesse a pasta do ejb (beneficio)
```bash
cd beneficio

```


2. Compile o projeto e baixe as dependências:
```bash
mvn clean install

```

3. Copie o .jar e cole no seu wildfly
```bash
cp target/beneficio-0.0.1-SNAPSHOT.jar \wildfly\standalone\deployments

```


4. Acesse a pasta /bin do wildfly e execute o standalone (Win/.bat - Linux/.sh)
```bash
\wildfly\bin\standalone.bat

```

### 2. Back-end (Servidor)

1. Acesse a pasta do backend:
```bash
cd api

```


2. Compile o projeto e baixe as dependências:
```bash
mvn clean install

```


3. Execute a aplicação:
```bash
mvn spring-boot:run

```


*O servidor iniciará na porta `8080` (padrão).*

### 3. Front-end (Interface)

1. Acesse a pasta do frontend em um novo terminal:
```bash
cd frontend/api-beneficio

```


2. Instale as dependências:
```bash
npm install

```


3. Inicie o servidor de desenvolvimento:
```bash
ng serve

```


4. Acesse no navegador: **`http://localhost:4200`**

---

## 🧪 Executando os Testes

O projeto conta com testes unitários cobrindo as regras de negócio críticas no EJB, utilizando **Mockito** para isolar a camada de persistência.

Para rodar os testes:

```bash
# Dentro da pasta do backend
mvn test

```

### O que está sendo testado?

* ✅ Transferências com sucesso (atualização de saldos).
* ✅ Bloqueio de transferências com saldo insuficiente.
* ✅ Validação de contas inexistentes ou iguais.
* ✅ Validação de valores negativos ou zero.
* ✅ Comportamento do `EntityManager` (Verificação de chamadas de `merge` e `lock`).

---

## ✨ Funcionalidades Implementadas

* **Listagem de Benefícios:** Visualização clara com formatação de moeda (BRL).
* **Cadastro:** Inclusão de novos beneficiários.
* **Exclusão:** Remoção de beneficiários (com confirmação).
* **Transferência:**
* Interface intuitiva (Modal).
* Seleção dinâmica de destino (filtra a própria origem).
* Feedback visual de "Carregando" para evitar cliques duplos.
* Feedback de Sucesso/Erro com scroll automático para visualização.



---

## 📝 Documentação da API (Endpoints)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `GET` | `/api/beneficios` | Lista todos os benefícios. |
| `POST` | `/api/beneficios` | Cria um novo benefício. |
| `GET` | `/api/beneficios/{id}` | Busca um benefício por ID. |
| `DELETE` | `/api/beneficios/{id}` | Remove um benefício. |
| `POST` | `/api/beneficios/transferir` | Realiza transferência de valor. |

---

## Observações:

* Não foi implementado os testes de integração pois não é de meu domínio atualmente;
* Foi utilizado ChatGPT e Gemini PRO como revisores e professores durante o teste, todo código feito é de conhecimento do autor;
* O front-end com Angular foi gerado pela IA assim como o README, com revisão do autor, pois não é a especialidade do mesmo porém conhece o que está no código;

---

## 👨‍💻 Autor

**Jhoan Melo**

* [LinkedIn](https://www.linkedin.com/in/jhoan-melo/)
* [GitHub](https://github.com/jhoanvictor)

