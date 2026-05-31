# Checklist de Segurança — Sistema Falcão Sombrio

## Autenticação e Controle de Acesso

- [x] Senhas armazenadas com hash SHA-256 — nunca em texto puro
- [x] Autenticação em duas etapas (MFA) com OTP de 6 dígitos
- [x] OTP expira em 60 segundos e é renovado automaticamente
- [x] OTP é gerado com `Random` e nunca reutilizado após validação
- [x] Controle de acesso por nível (ADMINISTRADOR / OPERADOR / SUPERVISOR)
- [x] Log de auditoria acessível apenas ao nível ADMINISTRADOR

## Integridade dos Dados

- [x] Log de auditoria encadeado por hash SHA-256 — cada registro referencia o hash do anterior
- [x] Hash inicial definido como `0000000000000000` (hash gênese)
- [x] Qualquer alteração retroativa nos logs invalida toda a cadeia subsequente
- [x] Campos de missão validados no backend antes de persistir

## Banco de Dados

- [x] Todas as queries usam prepared statements via JdbcTemplate — prevenção de SQL Injection
- [x] Nenhuma query construída por concatenação de strings com input do usuário
- [x] Constraints definidas no schema: `CHECK (altitude >= 0)`, `CHECK (velocidade >= 0)`, `NOT NULL` nos campos obrigatórios
- [x] Tabelas com chaves estrangeiras para garantir integridade referencial

## Validação de Entrada

- [x] Campos obrigatórios validados no backend antes de qualquer processamento
- [x] Strings sanitizadas com `trim()` e verificação de `isBlank()`
- [x] Exceções tratadas nos endpoints — erros retornam mensagem genérica sem expor detalhes internos
- [x] Nível de bateria do drone validado no range 0–100

## Comunicação

- [x] API REST com endpoints bem definidos e sem exposição de dados internos desnecessários
- [x] Respostas de erro não revelam stack trace ou estrutura interna do sistema
- [x] Frontend valida campos antes de enviar requisições ao backend

## Código

- [x] `SegurancaUtil` com construtor privado — classe utilitária não instanciável
- [x] Atributos sensíveis (hash de credencial, hash de biometria) declarados como `private`
- [x] Nenhuma senha em texto puro no código — apenas hashes SHA-256
- [x] Interfaces definem contratos claros — `IAutenticavel`, `IRastreavel`, `IComunicavel`, `IPersistivel`
