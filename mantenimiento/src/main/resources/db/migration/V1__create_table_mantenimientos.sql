CREATE TABLE IF NOT EXISTS mantenimientos (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_habitacion INT NOT NULL,
    tipo_mantenimiento VARCHAR(100) NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME,
    estado_mantenimiento VARCHAR(30) NOT NULL
);
