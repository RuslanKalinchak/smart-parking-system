DELETE FROM check_in_tickets
WHERE id IN (4, 5, 6);
DELETE FROM vehicles
WHERE license_plate IN ('AA8672BB', 'RYA6741OP', 'AY2134LN');
UPDATE parking_slots
SET status = 'AVAILABLE'
WHERE id IN (2, 6, 8);
