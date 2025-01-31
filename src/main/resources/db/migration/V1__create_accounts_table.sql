CREATE TABLE if not exists  accounts (
    id SERIAL PRIMARY KEY,
    document_number VARCHAR(255) NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE if not exists operation_types(
    id SERIAL PRIMARY KEY,
    description varchar(255),
    signal varchar(1),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE if not exists transactions(
    id serial primary key,
    account_id bigint not null references accounts(id),
    operation_type_id bigint not null references operation_types(id),
    amount numeric(19,2) not null,
    event_date_time timestamp default current_timestamp,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

