CREATE TABLE IF NOT EXISTS app_dynamic_table_definitions(
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(255) UNIQUE NOT NULL,
    user_friendly_name VARCHAR(255) UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE Not Null Default CURRENT_TIMESTAMP
);

CREATE TABLE app_dynamic_column_definitions(
    id BIGSERIAL PRIMARY KEY,
    table_definition_id BIGINT NOT NULL,
    column_name VARCHAR(255) NOT NULL,
    column_type VARCHAR(50) NOT NULL,
    postgres_column_type VARCHAR(100) NOT NULL,
    is_nullable BOOLEAN NOT NULL DEFAULT TRUE,
    is_primary_key_internal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_table_definition
       FOREIGN KEY (table_definition_id)
       REFERENCES app_dynamic_table_definitions (id)
       ON DELETE CASCADE
)

