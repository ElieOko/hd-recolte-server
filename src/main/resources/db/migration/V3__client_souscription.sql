ALTER TABLE clients ADD COLUMN montant_souscription BIGINT NOT NULL DEFAULT 0;
ALTER TABLE clients ADD COLUMN nombre_tranches INT NOT NULL DEFAULT 1;

ALTER TABLE clients ADD CONSTRAINT chk_clients_nombre_tranches CHECK (nombre_tranches BETWEEN 1 AND 4);
ALTER TABLE clients ADD CONSTRAINT chk_clients_montant_souscription CHECK (montant_souscription >= 0);
