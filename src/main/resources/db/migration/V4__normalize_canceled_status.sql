UPDATE delivery_orders
SET status = 'CANCELLED'
WHERE status = 'CANCELED';
