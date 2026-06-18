CREATE TABLE public.taxonomy (
    code varchar(20) NOT NULL,
    "name" varchar(200) NOT NULL,
    is_hierarchical bool NULL,
    description varchar(500) DEFAULT NULL::character varying NULL,
    remark varchar(500) DEFAULT NULL::character varying NULL,
    user_id uuid DEFAULT '550e8400-e29b-41d4-a716-446655440000'::uuid NOT NULL,
    created_at timestamptz DEFAULT now() NULL,
    updated_at timestamptz NULL,
    deleted_at timestamptz NULL,
    status varchar(10) NULL,
    CONSTRAINT taxonomy_pkey PRIMARY KEY (code)
);