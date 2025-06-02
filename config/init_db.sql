create table public.resume
(
    uuid      varchar(36) not null
        constraint resume_pk
            primary key,
    full_name text not null
);

alter table public.resume
    owner to postgres;

create table public.contact
(
    id          serial
        constraint contact_pk
            primary key,
    resume_uuid varchar(36) not null
        constraint contact_resume_uuid_fk
            references public.resume
            on update restrict on delete cascade,
    type        text        not null,
    value       text        not null
);

alter table public.contact
    owner to postgres;

create index contact_uuid_type_index
    on public.contact (resume_uuid, type);

create table public.section
(
    id          serial
        constraint section_pk
            primary key,
    resume_uuid varchar(36) not null
        constraint section_resume_uuid_fk
            references public.resume
            on update restrict on delete cascade,
    type        text        not null,
    value       text        not null
);

alter table public.section
    owner to postgres;

create index section_uuid_type_index
    on public.section (resume_uuid, type);





