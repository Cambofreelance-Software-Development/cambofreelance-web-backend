CREATE TABLE public.taxonomy_item (
    code varchar(20) NOT NULL,
    parent_code varchar(20) NULL,
    taxonomy_code varchar(20) NULL,
    display_km varchar(250) NOT NULL,
    display_en varchar(250) NOT NULL,
    metadata varchar(500) NULL,
    user_id uuid DEFAULT '550e8400-e29b-41d4-a716-446655440000'::uuid NOT NULL,
    created_at timestamptz DEFAULT now() NULL,
    updated_at timestamptz NULL,
    deleted_at timestamptz NULL,
    status varchar(10) NULL,
    CONSTRAINT taxonomy_item_pkey PRIMARY KEY (code),
    CONSTRAINT uq_taxonomy_item_taxonomy_code UNIQUE (taxonomy_code, code)
);