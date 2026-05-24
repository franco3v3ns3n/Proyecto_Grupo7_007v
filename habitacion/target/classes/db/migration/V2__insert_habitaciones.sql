INSERT INTO habitaciones (
    id_hotel,
    tipo_habitacion,
    numero_habitacion,
    capacidad_personas,
    cantidad_camas,
    cantidad_banos,
    precio_diario,
    estado_habitacion
)
VALUES
(1, 'Simple', '101', 1, 1, 1, 45000, 'DISPONIBLE'),
(1, 'Doble', '102', 2, 2, 1, 65000, 'DISPONIBLE'),
(1, 'Suite', '201', 4, 2, 2, 120000, 'DISPONIBLE'),

(2, 'Simple', '301', 1, 1, 1, 40000, 'DISPONIBLE'),
(2, 'Doble', '302', 2, 2, 1, 60000, 'DISPONIBLE'),
(2, 'Familiar', '401', 5, 3, 2, 95000, 'DISPONIBLE'),

(3, 'Doble', '501', 2, 2, 1, 62000, 'DISPONIBLE'),
(3, 'Suite Premium', '601', 4, 2, 2, 135000, 'DISPONIBLE');
