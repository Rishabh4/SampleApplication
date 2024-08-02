insert into users values (1, 'Rishabh', 'Jain', 'rishabh', '$2a$10$kH4br/yQ03nvwYM2t4gYlu4R0qTp4CU.kAQSk6FX2gtzr9FwWJE5i', 1);
insert into users values (2, 'Riya', 'Jain', 'riya', '$2a$10$kH4br/yQ03nvwYM2t4gYlu4R0qTp4CU.kAQSk6FX2gtzr9FwWJE5i', 1);

insert into roles values (1, 'ADMIN');
insert into roles values (2, 'USER');

insert into user_role values (1, 1, 1);
insert into user_role values (2, 2, 2);