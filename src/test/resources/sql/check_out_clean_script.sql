DELETE FROM check_out_tickets;
UPDATE check_in_tickets
SET status = 'ACTIVE'
WHERE id IN (1, 2, 3);
