INSERT INTO mantenimientos (
    id_habitacion,
    tipo_mantenimiento,
    fecha_inicio,
    fecha_fin,
    estado_mantenimiento
)
VALUES
(1, 'Limpieza profunda', '2026-04-01 09:00:00', '2026-04-01 12:00:00', 'FINALIZADO'),
(2, 'Revisión eléctrica', '2026-04-03 10:00:00', '2026-04-03 13:30:00', 'FINALIZADO'),
(3, 'Reparación de aire acondicionado', '2026-04-05 08:30:00', '2026-04-05 15:00:00', 'FINALIZADO'),
(4, 'Cambio de cerradura', '2026-04-08 11:00:00', '2026-04-08 12:30:00', 'FINALIZADO'),
(5, 'Pintura de habitación', '2026-04-10 09:00:00', '2026-04-10 17:00:00', 'FINALIZADO'),
(6, 'Revisión de baño', '2026-04-15 10:00:00', '2026-04-15 13:00:00', 'FINALIZADO'),
(7, 'Mantención de calefacción', '2026-04-18 08:00:00', '2026-04-18 14:00:00', 'FINALIZADO'),
(8, 'Cambio de cortinas', '2026-04-20 09:30:00', '2026-04-20 11:30:00', 'FINALIZADO'),
(7, 'Reparación de filtración', '2026-04-27 09:00:00', NULL, 'EN_PROCESO'),
(8, 'Revisión de minibar', '2026-04-27 14:00:00', NULL, 'EN_PROCESO'),
(1, 'Inspección preventiva', '2026-05-02 10:00:00', NULL, 'PROGRAMADO'),
(2, 'Cambio de colchón', '2026-05-04 09:00:00', NULL, 'PROGRAMADO'),
(3, 'Revisión de ventanas', '2026-05-06 11:00:00', NULL, 'PROGRAMADO'),
(4, 'Mantención general', '2026-05-08 08:30:00', NULL, 'PROGRAMADO'),
(5, 'Limpieza técnica', '2026-05-10 15:00:00', NULL, 'PROGRAMADO');
