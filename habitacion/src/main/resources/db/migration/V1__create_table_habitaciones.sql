CREATE TABLE IF NOT EXISTS habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT NOT NULL,
    tipo_habitacion VARCHAR(100) NOT NULL,
    numero_habitacion VARCHAR(20) NOT NULL,
    capacidad_personas INT NOT NULL,
    cantidad_camas INT NOT NULL,
    cantidad_banos INT NOT NULL,
    precio_diario DOUBLE NOT NULL,
    estado_habitacion VARCHAR(30) NOT NULL
);
