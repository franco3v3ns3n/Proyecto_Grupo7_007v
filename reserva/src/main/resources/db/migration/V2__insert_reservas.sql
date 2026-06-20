INSERT INTO reservas (
    id_cliente,
    id_habitacion,
    fecha_inicio,
    fecha_fin,
    estado_reserva,
    fecha_creacion
)
VALUES
(1, 1, '2026-04-10', '2026-04-13', 'FINALIZADA', NOW()),
(2, 2, '2026-04-15', '2026-04-18', 'FINALIZADA', NOW()),
(1, 3, '2026-07-01', '2026-07-04', 'CANCELADA', NOW()),

(4, 4, '2026-04-23', '2026-04-26', 'FINALIZADA', NOW()),
(2, 4, '2026-07-10', '2026-07-15', 'NO_SHOW', NOW()),

(1, 6, '2026-04-25', '2026-04-27', 'FINALIZADA', NOW()),
(3, 5, '2026-08-01', '2026-08-05', 'CONFIRMADA', NOW()),

(3, 8, '2026-05-01', '2026-05-04', 'FINALIZADA', NOW()),
(4, 4, '2026-05-05', '2026-05-08', 'FINALIZADA', NOW()),

(5, 1, '2026-05-10', '2026-05-12', 'FINALIZADA', NOW()),
(1, 2, '2026-05-15', '2026-05-18', 'FINALIZADA', NOW()),
(5, 2, '2026-09-10', '2026-09-15', 'CONFIRMADA', NOW());
