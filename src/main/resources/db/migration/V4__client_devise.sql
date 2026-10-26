ALTER TABLE clients ADD COLUMN devise VARCHAR(3) NOT NULL DEFAULT 'FC';

ALTER TABLE clients ADD CONSTRAINT chk_clients_devise CHECK (devise IN ('FC', '$'));
