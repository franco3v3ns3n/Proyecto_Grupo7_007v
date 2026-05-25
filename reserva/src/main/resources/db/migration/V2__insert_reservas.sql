INSERT INTO reservas (
    id_cliente,
    id_habitacion,
    fecha_inicio,
    fecha_fin,
    estado_reserva,
    fecha_creacion
)
VALUES
(1, 1, '2026-06-01', '2026-06-05', 'CONFIRMADA', NOW()),
(1, 2, '2026-06-10', '2026-06-12', 'FINALIZADA', NOW()),
(1, 3, '2026-07-01', '2026-07-04', 'CANCELADA', NOW()),

(2, 1, '2026-06-15', '2026-06-20', 'CONFIRMADA', NOW()),
(2, 4, '2026-07-10', '2026-07-15', 'NO_SHOW', NOW()),

(3, 2, '2026-06-08', '2026-06-11', 'FINALIZADA', NOW()),
(3, 5, '2026-08-01', '2026-08-05', 'CONFIRMADA', NOW()),

(4, 3, '2026-06-18', '2026-06-22', 'CANCELADA', NOW()),
(4, 4, '2026-09-01', '2026-09-03', 'CONFIRMADA', NOW()),

(5, 1, '2026-07-20', '2026-07-25', 'FINALIZADA', NOW()),
(5, 5, '2026-08-15', '2026-08-18', 'NO_SHOW', NOW()),
(5, 2, '2026-09-10', '2026-09-15', 'CONFIRMADA', NOW());