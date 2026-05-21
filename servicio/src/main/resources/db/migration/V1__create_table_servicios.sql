CREATE TABLE IF NOT EXISTS servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    valor_diario DOUBLE NOT NULL,
    estado_servicio VARCHAR(30) NOT NULL
);