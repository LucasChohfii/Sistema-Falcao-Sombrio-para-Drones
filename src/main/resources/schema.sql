CREATE TABLE IF NOT EXISTS missao (
    id              SERIAL PRIMARY KEY,
    tipo            VARCHAR(50)  NOT NULL,
    drone_id        VARCHAR(50)  NOT NULL,
    operador_nome   VARCHAR(100) NOT NULL,
    destino_lat     DOUBLE PRECISION NOT NULL,
    destino_lon     DOUBLE PRECISION NOT NULL,
    duracao_segundos INT          NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'CONCLUIDA',
    registrado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS telemetria (
    id              SERIAL PRIMARY KEY,
    drone_id        VARCHAR(50)  NOT NULL,
    missao_id       INT          REFERENCES missao(id),
    posicao_lat     DOUBLE PRECISION NOT NULL,
    posicao_lon     DOUBLE PRECISION NOT NULL,
    altitude        REAL         NOT NULL CHECK (altitude >= 0),
    velocidade      REAL         NOT NULL CHECK (velocidade >= 0),
    registrado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS log_auditoria (
    id              SERIAL PRIMARY KEY,
    evento          TEXT         NOT NULL,
    operador_nome   VARCHAR(100) NOT NULL,
    missao_id       INT          REFERENCES missao(id),
    hash_anterior   VARCHAR(64),
    hash_atual      VARCHAR(64)  NOT NULL,
    registrado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
