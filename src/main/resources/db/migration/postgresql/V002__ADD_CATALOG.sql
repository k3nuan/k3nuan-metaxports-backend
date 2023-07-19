
INSERT INTO country (id, name, short_name, lang) VALUES (1, N'Venezuela', N'VE', N've');

INSERT INTO state (code, name, country_id) VALUES (N'1', N'Distrito Capital', 1);


INSERT INTO country (id, name, short_name, lang) VALUES (2, N'United States', N'US', N'us');

INSERT INTO state (code, name, country_id) VALUES (N'AK', N'Alaska', 2);
INSERT INTO state (code, name, country_id) VALUES (N'AL', N'Alabama', 2);
INSERT INTO state (code, name, country_id) VALUES (N'AR', N'Arkansas', 2);
INSERT INTO state (code, name, country_id) VALUES (N'AZ', N'Arizona', 2);
INSERT INTO state (code, name, country_id) VALUES (N'CA', N'California', 2);
INSERT INTO state (code, name, country_id) VALUES (N'CO', N'Colorado', 2);
INSERT INTO state (code, name, country_id) VALUES (N'CT', N'Connecticut', 2);
INSERT INTO state (code, name, country_id) VALUES (N'DC', N'District of Columbia', 2);
INSERT INTO state (code, name, country_id) VALUES (N'DE', N'Delaware', 2);
INSERT INTO state (code, name, country_id) VALUES (N'FL', N'Florida', 2);
INSERT INTO state (code, name, country_id) VALUES (N'GA', N'Georgia', 2);
INSERT INTO state (code, name, country_id) VALUES (N'HI', N'Hawaii', 2);
INSERT INTO state (code, name, country_id) VALUES (N'IA', N'Iowa', 2);
INSERT INTO state (code, name, country_id) VALUES (N'ID', N'Idaho', 2);
INSERT INTO state (code, name, country_id) VALUES (N'IL', N'Illinois', 2);
INSERT INTO state (code, name, country_id) VALUES (N'IN', N'Indiana', 2);
INSERT INTO state (code, name, country_id) VALUES (N'KS', N'Kansas', 2);
INSERT INTO state (code, name, country_id) VALUES (N'KY', N'Kentucky', 2);
INSERT INTO state (code, name, country_id) VALUES (N'LA', N'Louisiana', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MA', N'Massachusetts', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MD', N'Maryland', 2);
INSERT INTO state (code, name, country_id) VALUES (N'ME', N'Maine', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MI', N'Michigan', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MN', N'Minnesota', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MO', N'Missouri', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MS', N'Mississippi', 2);
INSERT INTO state (code, name, country_id) VALUES (N'MT', N'Montana', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NC', N'North Carolina', 2);
INSERT INTO state (code, name, country_id) VALUES (N'ND', N'North Dakota', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NE', N'Nebraska', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NH', N'New Hampshire', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NJ', N'New Jersey', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NM', N'New Mexico', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NV', N'Nevada', 2);
INSERT INTO state (code, name, country_id) VALUES (N'NY', N'New York', 2);
INSERT INTO state (code, name, country_id) VALUES (N'OH', N'Ohio', 2);
INSERT INTO state (code, name, country_id) VALUES (N'OK', N'Oklahoma', 2);
INSERT INTO state (code, name, country_id) VALUES (N'OR', N'Oren', 2);
INSERT INTO state (code, name, country_id) VALUES (N'PA', N'Pennsylvania', 2);
INSERT INTO state (code, name, country_id) VALUES (N'PR', N'Puerto Rico', 2);
INSERT INTO state (code, name, country_id) VALUES (N'RI', N'Rhode Island', 2);
INSERT INTO state (code, name, country_id) VALUES (N'SC', N'South Carolina', 2);
INSERT INTO state (code, name, country_id) VALUES (N'SD', N'South Dakota', 2);
INSERT INTO state (code, name, country_id) VALUES (N'TN', N'Tennessee', 2);
INSERT INTO state (code, name, country_id) VALUES (N'TX', N'Texas', 2);
INSERT INTO state (code, name, country_id) VALUES (N'UT', N'Utah', 2);
INSERT INTO state (code, name, country_id) VALUES (N'VA', N'Virginia', 2);
INSERT INTO state (code, name, country_id) VALUES (N'VI', N'Virgin Islands', 2);
INSERT INTO state (code, name, country_id) VALUES (N'VT', N'Vermont', 2);
INSERT INTO state (code, name, country_id) VALUES (N'WA', N'Washington', 2);
INSERT INTO state (code, name, country_id) VALUES (N'WI', N'Wisconsin', 2);
INSERT INTO state (code, name, country_id) VALUES (N'WV', N'West Virginia', 2);
INSERT INTO state (code, name, country_id) VALUES (N'WY', N'Wyoming', 2);

INSERT INTO role (id, name) VALUES (7, N'ADMIN');
INSERT INTO role (id, name) VALUES (8, N'USER');
INSERT INTO role (id, name) VALUES (9, N'ALIADO');



INSERT INTO user_info ( email,password,role_id,status,first_name,last_name,saldo,created,ip_address,last_login) VALUES ( N'migueld2310@gmail.com',N'1c27d101f7acfa7cd64f127ad8278bfecde6aa6b59ed13723726c8be64759846b2785e67a2b924b4d483f103e8c1759e60551087569e9de6b99c68dbe5cf7eb9',7,N'A',N'Miguel',N'david',1000,'2023-03-21 00:00:00.000',N'0:0:0:0:0:0:0:1','2023-03-25 18:50:31.146');

INSERT INTO eventos ( descripcion,opcion_uno,opcion_dos,opcion_tres) VALUES ( N'¿Qué equipo ejecuta el primer tiro al arco?',N'Local',N'Visitante',null);
INSERT INTO eventos ( descripcion,opcion_uno,opcion_dos,opcion_tres) VALUES ( N'¿Qué equipo ejecuta el primer tiro de esquina?',N'Local',N'Visitante',null);
INSERT INTO eventos ( descripcion,opcion_uno,opcion_dos,opcion_tres) VALUES ( N'¿Qué equipo comete la primera falta?',N'Local',N'Visitante',null);
INSERT INTO eventos ( descripcion,opcion_uno,opcion_dos,opcion_tres) VALUES ( N'¿Qué equipo comete la primera tarjeta amarilla?',N'Local',N'Visitante',null);
INSERT INTO eventos ( descripcion,opcion_uno,opcion_dos,opcion_tres) VALUES ( N'¿Qué equipo comete el primer fuera de juego?',N'Local',N'Visitante',null);

