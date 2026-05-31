// ─── ESTADO GLOBAL ────────────────────────────────────────────────────────────
let operadorAtual   = null;
let nivelAcesso     = null;
let mapa            = null;
let droneAtivo      = 'DRONE-1';
let tipoMissao      = 'RECONHECIMENTO';
let alvoLatLng      = null;
let alvoMarker      = null;
let rotaLinha       = null;
let retornoLinha    = null;
let timerInterval   = null;
let otpInterval     = null;
let segundosMissao  = 0;
let nomeParaMfa     = null;

const BASE_LAT = -23.54729;
const BASE_LON = -46.65259;

const drones = {
    'DRONE-1': { marker: null, status: 'DISPONIVEL', base: [BASE_LAT, BASE_LON] },
    'DRONE-2': { marker: null, status: 'DISPONIVEL', base: [BASE_LAT, BASE_LON + 0.001] }
};

// ─── LOGIN ────────────────────────────────────────────────────────────────────
function efetuarLogin() {
    const nome  = document.getElementById('inp-nome').value.trim();
    const senha = document.getElementById('inp-senha').value;
    const erro  = document.getElementById('login-erro');
    erro.classList.add('hidden');

    if (!nome || !senha) {
        erro.textContent = 'ERRO: Preencha todos os campos.';
        erro.classList.remove('hidden');
        return;
    }

    fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `nome=${encodeURIComponent(nome)}&senha=${encodeURIComponent(senha)}`
    })
    .then(res => {
        if (!res.ok) return res.json().then(d => { throw new Error(d.erro); });
        return res.json();
    })
    .then(dados => {
        nomeParaMfa = nome;
        document.getElementById('otp-display').textContent = dados.otp;
        document.getElementById('tela-login').classList.add('hidden');
        document.getElementById('tela-mfa').classList.remove('hidden');
        iniciarContagemOtp();
    })
    .catch(err => {
        erro.textContent = 'ACESSO NEGADO: ' + err.message;
        erro.classList.remove('hidden');
    });
}

function iniciarContagemOtp() {
    let segundos = 60;
    document.getElementById('otp-segundos').textContent = segundos;
    otpInterval = setInterval(() => {
        segundos--;
        document.getElementById('otp-segundos').textContent = segundos;
        if (segundos <= 0) {
            clearInterval(otpInterval);
            document.getElementById('otp-display').textContent = '------';
            document.getElementById('otp-segundos').textContent = '0';
            renovarOtp();
        }
    }, 1000);
}

function renovarOtp() {
    fetch('/api/renovar-otp', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `nome=${encodeURIComponent(nomeParaMfa)}`
    })
    .then(res => res.json())
    .then(dados => {
        document.getElementById('otp-display').textContent = dados.otp;
        iniciarContagemOtp();
    })
    .catch(() => {
        document.getElementById('otp-display').textContent = 'ERRO';
    });
}

function verificarMfa() {
    const codigo = document.getElementById('inp-otp').value.trim();
    const erro   = document.getElementById('mfa-erro');
    erro.classList.add('hidden');

    if (!codigo) {
        erro.textContent = 'ERRO: Insira o código MFA.';
        erro.classList.remove('hidden');
        return;
    }

    fetch('/api/verificar-mfa', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `nome=${encodeURIComponent(nomeParaMfa)}&codigo=${encodeURIComponent(codigo)}`
    })
    .then(res => {
        if (!res.ok) return res.json().then(d => { throw new Error(d.erro); });
        return res.json();
    })
    .then(dados => {
        clearInterval(otpInterval);
        operadorAtual = dados.operador;
        nivelAcesso   = dados.nivelAcesso;
        entrarSistema();
    })
    .catch(err => {
        erro.textContent = 'ERRO: ' + err.message;
        erro.classList.remove('hidden');
    });
}

function voltarLogin() {
    clearInterval(otpInterval);
    document.getElementById('tela-mfa').classList.add('hidden');
    document.getElementById('tela-login').classList.remove('hidden');
    document.getElementById('inp-otp').value = '';
}

function entrarSistema() {
    document.getElementById('tela-mfa').classList.add('hidden');
    document.getElementById('tela-missao').classList.remove('hidden');
    document.getElementById('tb-operador').textContent = 'OPERADOR: ' + operadorAtual.toUpperCase();

    if (nivelAcesso === 'ADMINISTRADOR') {
        document.getElementById('log-auditoria-wrap').classList.remove('hidden');
        carregarLogs();
    }

    adicionarLog('SISTEMA', 'Operador ' + operadorAtual + ' autenticado. Nível: ' + nivelAcesso);
    iniciarMapa();
    carregarHistorico();
}

function logout() { location.reload(); }

// ─── MAPA ─────────────────────────────────────────────────────────────────────
function iniciarMapa() {
    mapa = L.map('map').setView([BASE_LAT, BASE_LON], 14);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
    }).addTo(mapa);

    const iconeBase = L.divIcon({
        className: '',
        html: '<div style="background:#2C5F34;width:10px;height:10px;border:2px solid #fff;border-radius:50%"></div>',
        iconAnchor: [5, 5]
    });

    const COMPENSA = 'hue-rotate(-80deg) brightness(1.35) saturate(4)';

    const iconeDrone = (cor) => L.divIcon({
        className: '',
        html: `<div style="background:${cor};width:12px;height:12px;border:2px solid #fff;filter:${COMPENSA}"></div>`,
        iconAnchor: [6, 6]
    });

    L.marker(drones['DRONE-1'].base, { icon: iconeBase }).addTo(mapa).bindPopup('BASE AQUILA-X1');
    L.marker(drones['DRONE-2'].base, { icon: iconeBase }).addTo(mapa).bindPopup('BASE AQUILA-X2');

    drones['DRONE-1'].marker = L.marker(drones['DRONE-1'].base, { icon: iconeDrone('#1155ee') }).addTo(mapa);
    drones['DRONE-2'].marker = L.marker(drones['DRONE-2'].base, { icon: iconeDrone('#9922cc') }).addTo(mapa);

    mapa.on('click', function(e) {
        if (drones[droneAtivo].status !== 'DISPONIVEL') {
            adicionarLog('AVISO', droneAtivo + ' está em missão. Selecione outro drone.');
            return;
        }
        definirAlvo(e.latlng);
    });

    adicionarLog('MAPA', 'Sistema de navegação inicializado. Dois drones prontos.');
}

function definirAlvo(latlng) {
    alvoLatLng = [latlng.lat, latlng.lng];
    if (alvoMarker)  { mapa.removeLayer(alvoMarker); }
    if (rotaLinha)   { mapa.removeLayer(rotaLinha); }
    if (retornoLinha){ mapa.removeLayer(retornoLinha); }

    const iconeAlvo = L.divIcon({
        className: '',
        html: '<div style="background:#ff4444;width:12px;height:12px;border:2px solid #fff;border-radius:2px;box-shadow:0 0 8px #ff4444"></div>',
        iconAnchor: [6, 6]
    });

    alvoMarker = L.marker(alvoLatLng, { icon: iconeAlvo }).addTo(mapa);
    document.getElementById('coord-alvo').innerHTML =
        `DRONE: ${droneAtivo}<br>TIPO: ${tipoMissao}<br>LAT: ${latlng.lat.toFixed(5)}<br>LON: ${latlng.lng.toFixed(5)}`;
    document.getElementById('btn-confirmar-wrap').classList.remove('hidden');
}

function cancelarAlvo() {
    if (alvoMarker) { mapa.removeLayer(alvoMarker); alvoMarker = null; }
    alvoLatLng = null;
    document.getElementById('btn-confirmar-wrap').classList.add('hidden');
}

// ─── SELEÇÃO ──────────────────────────────────────────────────────────────────
function selecionarDrone(id) {
    droneAtivo = id;
    document.getElementById('btn-d1').classList.toggle('ativo', id === 'DRONE-1');
    document.getElementById('btn-d2').classList.toggle('ativo', id === 'DRONE-2');
    cancelarAlvo();
}

function selecionarTipo(tipo) {
    tipoMissao = tipo;
    document.getElementById('btn-recon').classList.toggle('ativo', tipo === 'RECONHECIMENTO');
    document.getElementById('btn-ataque').classList.toggle('ativo', tipo === 'ATAQUE');
    cancelarAlvo();
}

// ─── MISSÃO ───────────────────────────────────────────────────────────────────
function confirmarMissao() {
    if (!alvoLatLng) return;
    if (drones[droneAtivo].status !== 'DISPONIVEL') return;

    const droneId    = droneAtivo;
    const alvoLocal  = [alvoLatLng[0], alvoLatLng[1]];
    const baseAtual  = [drones[droneId].base[0], drones[droneId].base[1]];
    const tipoLocal  = tipoMissao;
    const cor        = droneId === 'DRONE-1' ? '#1155ee' : '#9922cc';

    drones[droneId].status = 'EM_MISSAO';
    atualizarStatusDrone(droneId, 'EM MISSÃO', 'amarelo');

    document.getElementById('btn-confirmar-wrap').classList.add('hidden');
    document.getElementById('tb-status').textContent = 'MISSÃO EM ANDAMENTO';
    document.getElementById('timer-display').classList.remove('hidden');

    adicionarLog('MISSÃO', droneId + ' — ' + tipoLocal + ' → ' + alvoLocal[0].toFixed(4) + ', ' + alvoLocal[1].toFixed(4));
    iniciarTimer();
    enviarTelemetria(droneId, baseAtual[0], baseAtual[1], 0, 0);

    const compensa = 'hue-rotate(-80deg) brightness(1.35) saturate(4)';

    rotaLinha = L.polyline([baseAtual, alvoLocal], {
        color: cor, weight: 4, dashArray: '10,5', opacity: 1
    }).addTo(mapa);
    const rotaEl = rotaLinha.getElement();
    if (rotaEl) rotaEl.style.filter = compensa;

    animarDrone(droneId, baseAtual, alvoLocal, 7000, function() {
        adicionarLog('DRONE', droneId + ' alcançou o alvo. Retornando.');
        enviarTelemetria(droneId, alvoLocal[0], alvoLocal[1], 120, 85);

        retornoLinha = L.polyline([alvoLocal, baseAtual], {
            color: '#ffaa00', weight: 4, dashArray: '10,5', opacity: 1
        }).addTo(mapa);
        const retEl = retornoLinha.getElement();
        if (retEl) retEl.style.filter = compensa;

        animarDrone(droneId, alvoLocal, baseAtual, 7000, function() {
            pararTimer();
            enviarTelemetria(droneId, baseAtual[0], baseAtual[1], 0, 0);
            concluirMissao(droneId, alvoLocal, tipoLocal);
        });
    });
}

function animarDrone(droneId, origem, destino, duracao, callback) {
    const passos   = 100;
    const intervalo = duracao / passos;
    let passo = 0;

    const anim = setInterval(() => {
        passo++;
        const lat = origem[0] + (destino[0] - origem[0]) * (passo / passos);
        const lon = origem[1] + (destino[1] - origem[1]) * (passo / passos);
        drones[droneId].marker.setLatLng([lat, lon]);
        if (passo >= passos) { clearInterval(anim); callback(); }
    }, intervalo);
}

// ─── TELEMETRIA ───────────────────────────────────────────────────────────────
function enviarTelemetria(droneId, lat, lon, altitude, velocidade) {
    fetch('/api/telemetria', {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify({ droneId, posicaoLat: lat, posicaoLon: lon, altitude, velocidade })
    }).catch(() => {});
}

// ─── TIMER ────────────────────────────────────────────────────────────────────
function iniciarTimer() {
    segundosMissao = 0;
    timerInterval = setInterval(() => {
        segundosMissao++;
        const min = String(Math.floor(segundosMissao / 60)).padStart(2, '0');
        const seg = String(segundosMissao % 60).padStart(2, '0');
        document.getElementById('tempo').textContent = min + ':' + seg;
    }, 1000);
}

function pararTimer() { clearInterval(timerInterval); }

// ─── CONCLUSÃO ────────────────────────────────────────────────────────────────
function concluirMissao(droneId, alvoLocal, tipoLocal) {
    adicionarLog('MISSÃO', droneId + ' retornou. Duração: ' + segundosMissao + 's.');

    drones[droneId].status = 'DISPONIVEL';
    atualizarStatusDrone(droneId, 'DISPONÍVEL', 'verde');

    document.getElementById('tb-status').textContent = 'AGUARDANDO ORDEM';
    document.getElementById('timer-display').classList.add('hidden');

    const payload = {
        tipo:            tipoLocal,
        droneId:         droneId,
        operadorNome:    operadorAtual,
        destinoLat:      alvoLocal[0],
        destinoLon:      alvoLocal[1],
        duracaoSegundos: segundosMissao,
        areaAlvo:        'Área selecionada no mapa',
        nivelAmeaca:     'ALTO'
    };

    fetch('/api/missao', {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(payload)
    })
    .then(res => res.json())
    .then(() => {
        adicionarLog('BANCO', 'Missão salva no banco de dados.');
        carregarHistorico();
        if (nivelAcesso === 'ADMINISTRADOR') carregarLogs();
    })
    .catch(() => adicionarLog('ERRO', 'Falha ao salvar missão.'));

    if (alvoMarker)  { mapa.removeLayer(alvoMarker);  alvoMarker  = null; }
    if (rotaLinha)   { mapa.removeLayer(rotaLinha);   rotaLinha   = null; }
    if (retornoLinha){ mapa.removeLayer(retornoLinha); retornoLinha = null; }

    alvoLatLng  = null;
}

// ─── STATUS DRONES ────────────────────────────────────────────────────────────
function atualizarStatusDrone(droneId, texto, classe) {
    const idEl  = droneId === 'DRONE-1' ? 'status-d1' : 'status-d2';
    const indEl = droneId === 'DRONE-1' ? 'ind-d1'    : 'ind-d2';

    [idEl, indEl].forEach(id => {
        const el = document.getElementById(id);
        el.textContent = texto;
        el.className   = classe;
    });
}

// ─── HISTÓRICO E LOGS ─────────────────────────────────────────────────────────
function carregarHistorico() {
    fetch('/api/missoes')
    .then(res => res.json())
    .then(lista => {
        const el = document.getElementById('historico-lista');
        el.innerHTML = '';
        lista.slice(0, 6).forEach(m => {
            const div = document.createElement('div');
            div.className = 'hist-item';
            div.innerHTML = `${m.tipo}<br>${m.drone_id} — ${m.duracao_segundos}s<br>${Number(m.destino_lat).toFixed(3)}, ${Number(m.destino_lon).toFixed(3)}`;
            el.appendChild(div);
        });
    })
    .catch(() => {});
}

function carregarLogs() {
    fetch('/api/logs')
    .then(res => res.json())
    .then(lista => {
        const el = document.getElementById('log-auditoria-lista');
        el.innerHTML = '';
        lista.slice(0, 6).forEach(l => {
            const div = document.createElement('div');
            div.className = 'log-item';
            div.innerHTML = `${l.evento}<br><span style="color:#336633">${l.hash_atual ? l.hash_atual.substring(0,16) + '...' : ''}</span>`;
            el.appendChild(div);
        });
    })
    .catch(() => {});
}

// ─── LOG INFERIOR ─────────────────────────────────────────────────────────────
function adicionarLog(tipo, msg) {
    const out  = document.getElementById('log-output');
    const span = document.createElement('span');
    const hora = new Date().toLocaleTimeString('pt-BR');
    span.textContent = `[${hora}][${tipo}] ${msg}`;
    out.appendChild(span);
    out.scrollLeft = out.scrollWidth;
}

// ─── TECLADO ──────────────────────────────────────────────────────────────────
document.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
        if (!document.getElementById('tela-login').classList.contains('hidden')) efetuarLogin();
        else if (!document.getElementById('tela-mfa').classList.contains('hidden')) verificarMfa();
    }
});
