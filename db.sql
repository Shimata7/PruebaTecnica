CREATE DATABASE db_clientes;

\c db_clientes;

CREATE TABLE IF NOT EXISTS persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    genero VARCHAR(10),
    edad INT,
    identificacion VARCHAR(10) UNIQUE NOT NULL, 
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS cliente (
    cliente_id INT PRIMARY KEY,
    contrasena VARCHAR(200) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_persona FOREIGN KEY (cliente_id) REFERENCES persona(id) ON DELETE CASCADE
);

CREATE DATABASE db_cuentas;

\c db_cuentas;

CREATE TABLE IF NOT EXISTS cuenta (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    cliente_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL,
    cuenta_id INT NOT NULL,
    CONSTRAINT fk_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON DELETE CASCADE
);