# Sistema Falcão Sombrio — Drones

Projeto de Software - 04G

**Grupo:** Lucas & Sofia  
**Integrantes:**  
- Lucas Chohfi Nigro — RA: 10437138  
- Sofia de Oliveira Cavalcanti — RA: 10723361  

Ciência da Computação — Universidade Presbiteriana Mackenzie

---

## Sobre o Projeto

A Securus Dynamics contratou a consultoria Cyber Bullet System para reformular a arquitetura do sistema Falcão Sombrio, uma plataforma para operação remota e autônoma de drones bélicos da frota Aquila-X.

---

## Etapas

### Etapa 1 — Proposta de Projeto
Definição do tema, equipe e repositório.

### Etapa 2 — Diagrama de Classes
Estrutura estática do sistema com as principais entidades e relacionamentos.

### Etapa 3 — Diagrama de Sequência
Fluxo de interações entre os componentes durante uma missão.

### Etapa 4 — Integração de Modelos (Classes + Banco de Dados)
Diagrama ER representando as tabelas e relacionamentos do banco de dados.

### Etapa 5 — Diagrama de Projeto (Abstrações e Interfaces)
Introdução de interfaces (`IAutenticavel`, `IRastreavel`, `IComunicavel`, `IPersistivel`) e classe abstrata `MissaoBase`.

### Etapa 6 — Integração Sequência + Colaboração
Diagramas de sequência e colaboração do ciclo completo de uma missão.

### Etapa 7 — Diagrama de Estados
Estados do Drone e da Missão com transições.

### Etapa 8 — Implementação
Aplicação web completa com backend Spring Boot, banco PostgreSQL e frontend com mapa interativo.

---

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Backend | Java 17 + Spring Boot 3.2.5 |
| Banco de dados | PostgreSQL 16 + JdbcTemplate |
| Frontend | HTML5 + CSS3 + JavaScript |
| Mapa | Leaflet.js 1.9.4 + OpenStreetMap |
| Build | Apache Maven |

---

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 16 rodando localmente

Copie o arquivo `application.properties.example` para `application.properties` e configure suas credenciais. As tabelas são criadas automaticamente na primeira execução.

---

## Como executar

```bash
mvn spring-boot:run
```

Acesse: `http://localhost:8080`

---

## Funcionalidades

- Autenticação em duas etapas — senha + OTP com expiração de 60 segundos e renovação automática
- Dois drones independentes (AQUILA-X1 e AQUILA-X2) operando simultaneamente
- Mapa interativo centrado na base operacional (Mackenzie)
- Missões de Reconhecimento e Ataque com animação de trajetória em tempo real
- Registro de telemetria por missão — partida, chegada ao alvo e retorno
- Log de auditoria com encadeamento de hash SHA-256 para garantir imutabilidade
- Controle de acesso por nível — ADMINISTRADOR visualiza log de auditoria, OPERADOR acessa apenas operações

---

## Banco de dados

| Tabela | Descrição |
|---|---|
| `missao` | Registro de cada missão executada |
| `telemetria` | Posições registradas durante o voo |
| `log_auditoria` | Eventos do sistema com cadeia de hash imutável |

---

---

# Shadow Falcon System — Drones

Software Engineering Project - 04G

**Team:** Lucas & Sofia  
**Members:**  
- Lucas Chohfi Nigro — Student ID: 10437138  
- Sofia de Oliveira Cavalcanti — Student ID: 10723361  

Computer Science — Universidade Presbiteriana Mackenzie

---

## About the Project

Securus Dynamics contracted the Cyber Bullet System consulting firm to redesign the architecture of the Falcão Sombrio system, a platform for remote and autonomous operation of military drones from the Aquila-X fleet.

---

## Stages

### Stage 1 — Project Proposal
Definition of the topic, team, and repository.

### Stage 2 — Class Diagram
Static structure of the system with the main entities and relationships.

### Stage 3 — Sequence Diagram
Interaction flow between components during a mission.

### Stage 4 — Model Integration (Classes + Database)
ER diagram representing the database tables and relationships.

### Stage 5 — Design Diagram (Abstractions and Interfaces)
Introduction of interfaces (`IAutenticavel`, `IRastreavel`, `IComunicavel`, `IPersistivel`) and abstract class `MissaoBase`.

### Stage 6 — Sequence + Collaboration Integration
Sequence and collaboration diagrams of a mission's complete cycle.

### Stage 7 — State Diagram
Drone and Mission states with transitions.

### Stage 8 — Implementation
Full web application with Spring Boot backend, PostgreSQL database, and interactive map frontend.

---

## Technologies Used

| Layer | Technology |
|---|---|
| Backend | Java 17 + Spring Boot 3.2.5 |
| Database | PostgreSQL 16 + JdbcTemplate |
| Frontend | HTML5 + CSS3 + JavaScript |
| Map | Leaflet.js 1.9.4 + OpenStreetMap |
| Build | Apache Maven |

---

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 16 running locally

Copy `application.properties.example` to `application.properties` and set your credentials. Tables are created automatically on first run.

---

## How to Run

```bash
mvn spring-boot:run
```

Access: `http://localhost:8080`

---

## Features

- Two-step authentication — password + OTP with 60-second expiration and automatic renewal
- Two independent drones (AQUILA-X1 and AQUILA-X2) operating simultaneously
- Interactive map centered on the operational base (Mackenzie)
- Reconnaissance and Attack missions with real-time trajectory animation
- Per-mission telemetry logging — departure, target arrival, and return
- Audit log with SHA-256 hash chaining to ensure immutability
- Role-based access control — ADMINISTRATOR views the audit log, OPERATOR accesses operations only

---

## Database

| Table | Description |
|---|---|
| `missao` | Record of each executed mission |
| `telemetria` | Positions logged during flight |
| `log_auditoria` | System events with immutable hash chain |
