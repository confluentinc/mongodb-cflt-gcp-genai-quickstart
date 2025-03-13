CREATE TABLE medications_summarized (
    `id` STRING PRIMARY KEY NOT ENFORCED,
    `name` STRING,
    `generic_name` STRING,
    `brand_names` ARRAY<STRING NOT NULL>,
    `dosage_form` STRING,
    `strength` STRING,
    `route_of_administration` STRING,
    `frequency_per_day` BIGINT,
    `duration_in_days` BIGINT,
    `side_effects` ARRAY<STRING NOT NULL>,
    `disclaimers` ARRAY<STRING NOT NULL>,
    `illnesses_treated` ARRAY<STRING NOT NULL>,
    `drug_class` STRING,
    `prescription_required` BOOLEAN,
    `pregnancy_category` STRING,
    `alcohol_interaction` STRING,
    `contraindications` ARRAY<STRING NOT NULL>,
    `storage_instructions` STRING,
    `manufacturers` ARRAY<STRING NOT NULL>,
    `cost_per_unit` DOUBLE,
    `country_availability` ARRAY<STRING NOT NULL>,
    `expiry_date` STRING,
    `summary` STRING
) DISTRIBUTED INTO 1 BUCKETS
WITH (
        'changelog.mode' = 'append',
        'kafka.cleanup-policy' = 'compact',
        'value.fields-include' = 'all',
        'key.format' = 'json-registry',
        'value.format' = 'json-registry',
        'kafka.consumer.isolation-level' = 'read-uncommitted'
)