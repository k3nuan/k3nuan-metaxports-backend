CREATE TABLE "role"(
	"id" int NOT NULL,
	"name" varchar(200) NOT NULL);

CREATE SEQUENCE "role_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "role"."id";
ALTER TABLE "role" ADD PRIMARY KEY ("id");
ALTER TABLE "role" ALTER COLUMN "id" SET DEFAULT nextval('"role_id_seq"');

CREATE TABLE "user_info"(
	"id" int NOT NULL,
	"email" varchar(500) NOT NULL,
	"password" varchar NOT NULL,
	"otp" varchar(30),
	"role_id" int NOT NULL,
	"status" varchar(3) NOT NULL,
	"first_name" varchar(500) NOT NULL,
	"last_name" varchar(500) NOT NULL,
	"saldo" INTEGER NOT NULL,
	"phone" varchar(20),
	"img_profile" varchar,
	"created_by" int,
	"created" timestamp,
	"modified_by" int,
	"modified" timestamp,
	"ip_address" varchar(50),
	"last_login" timestamp,
    "gender" varchar(10),
    "birth_date" date,
    "otp_time" timestamp,
    "codigo_referido" varchar(50),
    "codigo_referente" varchar(50),
    "id_referente" int);


CREATE SEQUENCE "user_info_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "user_info"."id";
ALTER TABLE "user_info" ADD PRIMARY KEY ("id");
CREATE UNIQUE INDEX "ux_user_info_1" ON "user_info" ("email" ASC);
ALTER TABLE "user_info" ALTER COLUMN "id" SET DEFAULT nextval('"user_info_id_seq"');
ALTER TABLE "user_info" ALTER COLUMN "last_login" SET DEFAULT NULL;

ALTER TABLE "user_info" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ( "id");
ALTER TABLE "user_info" ADD FOREIGN KEY ("modified_by") REFERENCES "user_info" ( "id");
ALTER TABLE "user_info" ADD FOREIGN KEY ("id_referente") REFERENCES "user_info" ( "id");

ALTER TABLE "user_info" ADD FOREIGN KEY ("role_id") REFERENCES "role" ( "id");


CREATE TABLE "country"(
	"id" int NOT NULL,
	"name" varchar(120) NOT NULL,
	"short_name" varchar(120) NOT NULL,
	"lang" varchar(4));
CREATE SEQUENCE "country_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "country"."id";
ALTER TABLE "country" ADD PRIMARY KEY ("id");
ALTER TABLE "country" ALTER COLUMN "id" SET DEFAULT nextval('"country_id_seq"');

CREATE TABLE "state"(
	"code" varchar(2) NOT NULL,
	"name" varchar(200) NOT NULL,
	"country_id" int NOT NULL);

ALTER TABLE "state" ADD PRIMARY KEY ("code");
ALTER TABLE "state" ADD CONSTRAINT "state_country" FOREIGN KEY ("country_id") REFERENCES "country" ( "id");
ALTER TABLE "state" ALTER COLUMN "country_id" SET DEFAULT 1;

CREATE TABLE "user_address"(
	"id" int NOT NULL,
	"user_id" int NOT NULL,
	"street_address" varchar NOT NULL,
	"suite" varchar(50),
	"city" varchar(200) NOT NULL,
	"zip_code" int,
	"state_code" varchar(2) NOT NULL,
	"country_id" int NOT NULL);

CREATE SEQUENCE "user_address_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "user_address"."id";
ALTER TABLE "user_address" ADD PRIMARY KEY ("id");
ALTER TABLE "user_address" ALTER COLUMN "id" SET DEFAULT nextval('"user_address_id_seq"');
ALTER TABLE "user_address" ADD CONSTRAINT "address_country" FOREIGN KEY ("country_id") REFERENCES "country" ( "id");
ALTER TABLE "user_address" ADD FOREIGN KEY ("state_code") REFERENCES "state" ( "code");
ALTER TABLE "user_address" ADD FOREIGN KEY ("user_id") REFERENCES "user_info" ( "id");


CREATE TABLE "spring_session"(
	"primary_id" char(36) NOT NULL,
	"session_id" char(36) NOT NULL,
	"creation_time" bigint NOT NULL,
	"last_access_time" bigint NOT NULL,
	"max_inactive_interval" int NOT NULL,
	"expiry_time" bigint NOT NULL,
	"principal_name" varchar(100));

ALTER TABLE "spring_session" ADD CONSTRAINT "spring_session_pk" PRIMARY KEY ("primary_id");
CREATE UNIQUE INDEX "spring_session_ix1" ON "spring_session" ("session_id" ASC);
CREATE INDEX "spring_session_ix2" ON "spring_session" ("expiry_time" ASC);
CREATE INDEX "spring_session_ix3" ON "spring_session" ("principal_name" ASC);


CREATE TABLE "spring_session_attributes"(
	"session_primary_id" char(36) NOT NULL,
	"attribute_name" varchar(200) NOT NULL,
	"attribute_bytes" bytea NOT NULL);


ALTER TABLE "spring_session_attributes" ADD CONSTRAINT "spring_session_attributes_pk" PRIMARY KEY ("session_primary_id","attribute_name");
ALTER TABLE "spring_session_attributes" ADD CONSTRAINT "spring_session_attributes_fk" FOREIGN KEY ("session_primary_id") REFERENCES "spring_session" ( "primary_id") ON DELETE CASCADE;

CREATE TABLE "catalog"(
	"id" int NOT NULL,
	"list_key" varchar(100) NOT NULL,
	"item" varchar(100) NOT NULL,
	"order" int,
	"parent_item" int);
CREATE SEQUENCE "catalog_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "catalog"."id";
ALTER TABLE "catalog" ADD PRIMARY KEY ("id");
ALTER TABLE "catalog" ADD CONSTRAINT "uk_catalog_key_item" UNIQUE ("list_key","item");
ALTER TABLE "catalog" ADD FOREIGN KEY ("parent_item") REFERENCES "catalog" ( "id");
ALTER TABLE "catalog" ALTER COLUMN "id" SET DEFAULT nextval('"catalog_id_seq"');

CREATE TABLE "event_type"(
	"id" int NOT NULL,
	"name" varchar(255) NOT NULL,
	"description" varchar(255) NOT NULL);
ALTER TABLE "event_type" ADD PRIMARY KEY ("id");


CREATE TABLE "event"(
    "source" varchar(255) NOT NULL,
    "origin" varchar(255) NULL,
	"type" int NOT NULL,
	"user_id" int NOT NULL,
	"detail" varchar(255) NULL,
	"data" json NULL,
	"error_code" int NOT NULL,
	"message" varchar(255) NULL,
	"created_by" int NOT NULL,
    "created" timestamp NOT NULL);

ALTER TABLE "event" ADD UNIQUE ("type", "user_id", "created");
ALTER TABLE "event" ADD FOREIGN KEY ("type") REFERENCES "event_type" ("id");
ALTER TABLE "event" ALTER COLUMN "error_code" SET DEFAULT 0;
ALTER TABLE "event" ALTER COLUMN "created" SET DEFAULT now();


CREATE TABLE "tournament"(
    "id"int NOT NULL,
    "name" varchar(255) NULL,
	"start_date" TIMESTAMP NOT NULL,
	"end_date" TIMESTAMP NOT NULL,
	"image" varchar(255));

	CREATE SEQUENCE "tournament_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "tournament"."id";
    ALTER TABLE "tournament" ADD PRIMARY KEY ("id");
    ALTER TABLE "tournament" ALTER COLUMN "id" SET DEFAULT nextval('"tournament_id_seq"');


    CREATE TABLE "soccer_games" (
        "id" INT NOT NULL,
        "campeonato_id" INT NOT NULL,
        "equipo_local" VARCHAR(50) NOT NULL,
        "equipo_visitante" VARCHAR(50) NOT NULL,
        "fecha_hora" TIMESTAMP NOT NULL,
        "marcador_local" INTEGER NOT NULL,
        "marcador_visitante" INTEGER NOT NULL,
        "imagen_local" VARCHAR(255),
        "imagen_visitante" VARCHAR(255)
    );

    CREATE SEQUENCE "soccer_games_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "soccer_games"."id";
    ALTER TABLE "soccer_games" ADD PRIMARY KEY ("id");
    ALTER TABLE "soccer_games" ALTER COLUMN "id" SET DEFAULT nextval('"soccer_games_id_seq"');

    ALTER TABLE "soccer_games" ADD FOREIGN KEY ("campeonato_id") REFERENCES "tournament" ("id");

    CREATE TABLE "eventos" (
        "id" INT NOT NULL,
        "descripcion" VARCHAR(250) NOT NULL,
        "opcion_uno" VARCHAR(50) NOT NULL,
        "opcion_dos" VARCHAR(50) NOT NULL,
        "opcion_tres" VARCHAR(50)
       );

    CREATE SEQUENCE "eventos_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "eventos"."id";
    ALTER TABLE "eventos" ADD PRIMARY KEY ("id");
    ALTER TABLE "eventos" ALTER COLUMN "id" SET DEFAULT nextval('"eventos_id_seq"');



    CREATE TABLE "apuestas" (
            "id" INT NOT NULL,
            "id_juego" INT NOT NULL,
            "id_evento" INT NOT NULL,
            "etapa" VARCHAR(50) NOT NULL,
            "opcion_ganadora" VARCHAR(50),
            "created_by" INT,
            "created" timestamp,
            "modified_by" INT,
            "modified" timestamp
           );

    CREATE SEQUENCE "apuestas_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "apuestas"."id";
    ALTER TABLE "apuestas" ADD PRIMARY KEY ("id");
    ALTER TABLE "apuestas" ALTER COLUMN "id" SET DEFAULT nextval('"apuestas_id_seq"');

    ALTER TABLE "apuestas" ADD FOREIGN KEY ("id_juego") REFERENCES "soccer_games" ("id");
    ALTER TABLE "apuestas" ADD FOREIGN KEY ("id_evento") REFERENCES "eventos" ("id");
    ALTER TABLE "apuestas" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ("id");
    ALTER TABLE "apuestas" ADD FOREIGN KEY ("modified_by") REFERENCES "user_info" ("id");


    CREATE TABLE "pot" (
                "id" INT NOT NULL,
                "id_apuesta" INT ,
                "id_apuesta_exacta" INT ,
                "saldo" INTEGER NOT NULL,
                "nro_apuestas" INTEGER,
                "nro_ganadores" INTEGER,
                "created_by" INT,
                "created" timestamp,
                "distributed_by" INT,
                "distribute" timestamp
               );

        CREATE SEQUENCE "pot_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "pot"."id";
        ALTER TABLE "pot" ADD PRIMARY KEY ("id");
        ALTER TABLE "pot" ALTER COLUMN "id" SET DEFAULT nextval('"pot_id_seq"');


        ALTER TABLE "pot" ADD FOREIGN KEY ("id_apuesta") REFERENCES "apuestas" ("id");

        ALTER TABLE "pot" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ("id");
        ALTER TABLE "pot" ADD FOREIGN KEY ("distributed_by") REFERENCES "user_info" ("id");


        CREATE TABLE "pronostico_user" (
                "id" INT NOT NULL,
                "id_apuesta" INT NOT NULL,
                "id_usuario" INT NOT NULL,
                "monto_apostado" INTEGER NOT NULL,
                "opcion_apuesta" varchar(50),
                "created" timestamp,
                "state_apuesta" varchar(50),
                "state_date" timestamp
               );
        CREATE SEQUENCE "pronostico_user_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "pronostico_user"."id";
        ALTER TABLE "pronostico_user" ADD PRIMARY KEY ("id");
        ALTER TABLE "pronostico_user" ALTER COLUMN "id" SET DEFAULT nextval('"pronostico_user_id_seq"');


        ALTER TABLE "pronostico_user" ADD FOREIGN KEY ("id_apuesta") REFERENCES "apuestas" ("id");
        ALTER TABLE "pronostico_user" ADD FOREIGN KEY ("id_usuario") REFERENCES "user_info" ("id");


        CREATE TABLE "recargas" (
                "id" INT NOT NULL,
                "id_usuario" INT NOT NULL,
                "monto_recargado" INT NOT NULL,
                "date" timestamp,
                "created_by" INT
               );

        CREATE SEQUENCE "recargas_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "recargas"."id";
        ALTER TABLE "recargas" ADD PRIMARY KEY ("id");
        ALTER TABLE "recargas" ALTER COLUMN "id" SET DEFAULT nextval('"recargas_id_seq"');

        ALTER TABLE "recargas" ADD FOREIGN KEY ("id_usuario") REFERENCES "user_info" ("id");
        ALTER TABLE "recargas" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ("id");


        CREATE TABLE "descuentos" (
                "id" INT NOT NULL,
                "id_usuario" INT NOT NULL,
                "monto_descontado" INT NOT NULL,
                "motivo" varchar(50),
                "date" timestamp,
                "created_by" INT
               );

        CREATE SEQUENCE "descuentos_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "descuentos"."id";
        ALTER TABLE "descuentos" ADD PRIMARY KEY ("id");
        ALTER TABLE "descuentos" ALTER COLUMN "id" SET DEFAULT nextval('"descuentos_id_seq"');

        ALTER TABLE "descuentos" ADD FOREIGN KEY ("id_usuario") REFERENCES "user_info" ("id");
        ALTER TABLE "descuentos" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ("id");


        CREATE TABLE "apuestas_exactas" (
            "id" INT NOT NULL,
            "id_juego" INT NOT NULL,
            "pregunta" VARCHAR(250) NOT NULL,
            "marcador_local" INT,
            "marcador_visitante" INT,
            "created" timestamp,
            "status" INTEGER,
            "created_by" INT,
            "modified_by" INT,
            "modified" timestamp
           );

    CREATE SEQUENCE "apuestas_exactas_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "apuestas_exactas"."id";
    ALTER TABLE "apuestas_exactas" ADD PRIMARY KEY ("id");
    ALTER TABLE "apuestas_exactas" ALTER COLUMN "id" SET DEFAULT nextval('"apuestas_exactas_id_seq"');

    ALTER TABLE "apuestas_exactas" ADD FOREIGN KEY ("id_juego") REFERENCES "soccer_games" ("id");
    ALTER TABLE "apuestas_exactas" ADD FOREIGN KEY ("created_by") REFERENCES "user_info" ("id");
    ALTER TABLE "apuestas_exactas" ADD FOREIGN KEY ("modified_by") REFERENCES "user_info" ("id");
    ALTER TABLE "pot" ADD FOREIGN KEY ("id_apuesta_exacta") REFERENCES "apuestas_exactas" ("id");



  CREATE TABLE "pronostico_exacto_user" (
                "id" INT NOT NULL,
                "id_apuesta_exacta" INT NOT NULL,
                "id_usuario" INT NOT NULL,
                "monto_apostado" INTEGER NOT NULL,
                "marcador_local" INTEGER,
                "marcador_visitante" INTEGER,
                "puntuacion" INTEGER,
                "created" timestamp,
                "state_apuesta" varchar(50),
                "state_date" timestamp
               );
        CREATE SEQUENCE "pronostico_exacto_user_id_seq" INCREMENT BY 1 START WITH 1 OWNED BY "pronostico_exacto_user"."id";
        ALTER TABLE "pronostico_exacto_user" ADD PRIMARY KEY ("id");
        ALTER TABLE "pronostico_exacto_user" ALTER COLUMN "id" SET DEFAULT nextval('"pronostico_exacto_user_id_seq"');


        ALTER TABLE "pronostico_exacto_user" ADD FOREIGN KEY ("id_apuesta_exacta") REFERENCES "apuestas_exactas" ("id");
        ALTER TABLE "pronostico_exacto_user" ADD FOREIGN KEY ("id_usuario") REFERENCES "user_info" ("id");

