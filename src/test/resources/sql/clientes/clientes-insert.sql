insert into USUARIOS (id, username, password, role) values (100, 'ana@email.com', '$2a$12$YYRiTYc2ssBR1xFZy4Xm7O1ThyvUgtMFa224k1b9Ywru/a.Z7IeNK', 'ROLE_ADMIN');
insert into USUARIOS (id, username, password, role) values (101, 'bia@email.com', '$2a$12$YYRiTYc2ssBR1xFZy4Xm7O1ThyvUgtMFa224k1b9Ywru/a.Z7IeNK', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (102, 'bob@email.com', '$2a$12$YYRiTYc2ssBR1xFZy4Xm7O1ThyvUgtMFa224k1b9Ywru/a.Z7IeNK', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (103, 'toby@email.com', '$2a$12$YYRiTYc2ssBR1xFZy4Xm7O1ThyvUgtMFa224k1b9Ywru/a.Z7IeNK', 'ROLE_CLIENTE');


insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'Bianca Silva', '46364359078', 101);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Roberto Gomes', '60318921030', 102);
