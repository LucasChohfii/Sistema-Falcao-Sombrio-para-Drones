# Sistema Falcão Sombrio para Drones

**Lucas Chohfi Nigro — 10437138**  
**Sofia de Oliveira Cavalcanti — 10723361**  
Ciência da Computação — Universidade Presbiteriana Mackenzie

---

## Descrição

O Sistema Falcão Sombrio é uma plataforma web para operação remota de drones bélicos. O sistema permite que operadores autenticados despachem missões de reconhecimento e ataque, acompanhem os drones em tempo real em um mapa interativo e consultem o histórico de operações registrado em banco de dados.

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

### Configuração do banco de dados

Crie o banco e o usuário no PostgreSQL conforme o arquivo `src/main/resources/application.properties`. As tabelas são criadas automaticamente pelo Spring Boot na primeira execução.

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

## Estrutura do projeto

```
src/main/java/
├── controle/
│   ├── CentralDeControle.java     # Orquestrador da frota
│   ├── FalcaoApplication.java     # Entry point Spring Boot
│   ├── FalcaoController.java      # Endpoints REST
│   └── SistemaAutenticacao.java   # Autenticação + OTP
├── modelo/
│   ├── BancoDeDados.java          # Implementa IPersistivel
│   ├── ComunicacaoSegura.java     # Implementa IComunicavel
│   ├── Drone.java                 # Implementa IRastreavel
│   ├── LogAuditoria.java          # Hash chain de auditoria
│   ├── MissaoAtaque.java          # Estende MissaoBase
│   ├── MissaoBase.java            # Classe abstrata de missão
│   ├── MissaoReconhecimento.java  # Estende MissaoBase
│   ├── Operador.java              # Implementa IAutenticavel
│   ├── SistemaNavegacao.java      # Navegação autônoma
│   └── Telemetria.java            # Dados de posição do drone
├── banco/
│   ├── LogAuditoriaDAO.java
│   ├── MissaoDAO.java
│   └── TelemetriaDAO.java
├── interfaces/
│   ├── IAutenticavel.java
│   ├── IComunicavel.java
│   ├── IPersistivel.java
│   └── IRastreavel.java
├── enums/
│   ├── NivelAcesso.java
│   ├── StatusDrone.java
│   └── StatusMissao.java
└── seguranca/
    └── SegurancaUtil.java

src/main/resources/
├── schema.sql
├── application.properties
└── static/
    ├── index.html
    ├── style.css
    └── app.js
```

---

## Banco de dados

| Tabela | Descrição |
|---|---|
| `missao` | Registro de cada missão executada |
| `telemetria` | Posições registradas durante o voo |
| `log_auditoria` | Eventos do sistema com cadeia de hash imutável |
