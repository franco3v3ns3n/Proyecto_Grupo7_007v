INSERT INTO estadias (
    id_cliente,
    id_habitacion,
    id_reserva,
    fecha_checkin,
    fecha_checkout,
    estado_estadia
)
VALUES
(1, 1, 1, '2026-04-10 15:30:00', '2026-04-13 10:45:00', 'FINALIZADA'),
(2, 2, 2, '2026-04-15 16:00:00', '2026-04-18 11:10:00', 'FINALIZADA'),
(4, 4, 4, '2026-04-23 14:30:00', '2026-04-26 10:30:00', 'FINALIZADA'),
(1, 6, 6, '2026-04-25 13:50:00', '2026-04-27 11:00:00', 'FINALIZADA'),
(2, 7, 7, '2026-04-27 15:00:00', NULL, 'EN_CURSO'),
(5, 10, NULL, '2026-04-27 18:00:00', NULL, 'EN_CURSO');
