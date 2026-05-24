CREATE TABLE IF NOT EXISTS estadias (
    id_estadia INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_habitacion INT NOT NULL,
    id_reserva INT,
    fecha_checkin DATETIME NOT NULL,
    fecha_checkout DATETIME,
    estado_estadia VARCHAR(30) NOT NULL
);
