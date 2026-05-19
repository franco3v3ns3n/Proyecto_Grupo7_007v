CREATE TABLE IF NOT EXISTS centros_eventos (
    id_centro_evento INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    capacidad_personas INT NOT NULL,
    precio_centro_evento DOUBLE NOT NULL,
    estado_centro_evento VARCHAR(30) NOT NULL
);