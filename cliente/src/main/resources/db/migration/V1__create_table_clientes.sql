CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150)
);
