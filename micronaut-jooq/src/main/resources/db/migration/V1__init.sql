-- Functions
CREATE OR REPLACE FUNCTION increment_version() RETURNS TRIGGER AS
$$
BEGIN
    NEW.version := NEW.version + 1;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION update_last_modified_date() RETURNS TRIGGER AS
    $$
BEGIN
    NEW.last_modified_date := NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- "Tmp" table

CREATE TYPE tmp_status AS ENUM ('OK', 'FAIL');

CREATE TABLE tmp(
    uid UUID PRIMARY KEY,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version INTEGER NOT NULL DEFAULT 0,
    name VARCHAR(10),
    status tmp_status NOT NULL
);

-- Comments
COMMENT ON TABLE tmp IS 'Tmp table';
COMMENT ON COLUMN tmp.uid IS 'UID';
COMMENT ON COLUMN tmp.created_date IS 'Created datetime';
COMMENT ON COLUMN tmp.last_modified_date IS 'Last modified datetime';
COMMENT ON COLUMN tmp.version IS 'Version number';
COMMENT ON COLUMN tmp.version IS 'Name of tmp';
COMMENT ON COLUMN tmp.status IS 'Status of tmp';

-- Triggers
CREATE TRIGGER tmp_increment_version
    BEFORE UPDATE
    ON tmp
    FOR EACH ROW
EXECUTE PROCEDURE increment_version();

CREATE TRIGGER tmp_update_last_modified_date
    BEFORE UPDATE
    ON tmp
    FOR EACH ROW
EXECUTE PROCEDURE update_last_modified_date();