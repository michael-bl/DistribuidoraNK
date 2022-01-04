-- Script SQLite para la base de datos local de la app
-- Alberto L. 02/11/2021 16:35:00

CREATE TABLE IF NOT EXISTS "cabecera_factura" (

	"id"			                        INTEGER NOT NULL,
	"tipo"			                        INTEGER NOT NULL,
	"fk_cliente"		                    VARCHAR(12) NOT NULL,
	"fecha"			                        DATETIME NOT NULL,
    "ultima_actualizacion"	DATETIME NOT NULL,

	PRIMARY KEY("id")
);

 CREATE TABLE IF NOT EXISTS "cliente" (

 	"id"			                                VARCHAR(12) NOT NULL,
 	"fk_localidad"		            INTEGER NOT NULL,
 	"nombre"		                        VARCHAR(16) NOT NULL,
 	"telefono"		                    VARCHAR(8) NOT NULL,
 	"email"			                        VARCHAR(60) NOT NULL,
 	"direccion"		                    VARCHAR(60) NOT NULL,
 	"estado"                            INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id"),
 	FOREIGN KEY("fk_localidad") REFERENCES "localidad"("id")
 );

 CREATE TABLE IF NOT EXISTS "detalle_factura" (

 	"id"			                                INTEGER NOT NULL,
 	"fk_cabecera"		            INTEGER NOT NULL,
 	"fk_producto"		            INTEGER NOT NULL,
 	"utilidad"		                        TEXT NOT NULL,
    "precio_compra"              TEXT NOT NULL,
    "precio_venta"                  TEXT NOT NULL,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id"),
 	FOREIGN KEY("fk_producto") REFERENCES "producto"("id"),
 	FOREIGN KEY("fk_cabecera") REFERENCES "cabecera_factura"("id")
 );

 CREATE TABLE IF NOT EXISTS "localidad" (

 	"id"			                                INTEGER NOT NULL,
 	"localidad"		                    VARCHAR(50) NOT NULL,
 	"estado"                            INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id")
 );

 CREATE TABLE IF NOT EXISTS "producto" (

 	"id"			                                INTEGER NOT NULL,
 	"fk_unidad"		                INTEGER NOT NULL,
 	"descripcion"		                VARCHAR(40) NOT NULL,
 	"utilidad"		                        REAL NOT NULL,
 	"precio_compra"			                        REAL NOT NULL,
 	"precio_venta"		            REAL,
 	"estado"                        INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id"),
 	FOREIGN KEY("fk_unidad") REFERENCES "unidad"("id")
 );

 CREATE TABLE IF NOT EXISTS "proveedor" (

 	"id"			                                VARCHAR(12) NOT NULL,
 	"nombre"		                        VARCHAR(16) NOT NULL,
 	"telefono"		                    VARCHAR(8) NOT NULL,
 	"email"			                        VARCHAR(60) NOT NULL,
 	"estado"                            INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id")
 );

 CREATE TABLE IF NOT EXISTS "unidad" (

 	"id"			                                INTEGER NOT NULL,
 	"detalle"		                        VARCHAR(20) NOT NULL,
 	"estado"                            INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id")
 );

 CREATE TABLE IF NOT EXISTS "usuario" (

 	"id"			                    VARCHAR(12) NOT NULL,
 	"fk_localidad"		                INTEGER NOT NULL,
 	"nombre"		                    VARCHAR(16) NOT NULL,
 	"pass"			                    VARCHAR(70) NOT NULL,
 	"telefono"		                    VARCHAR(8) NOT NULL,
 	"email"			                    VARCHAR(60) NOT NULL,
 	"direccion"		                    VARCHAR(60) NOT NULL,
 	"estado"                            INTEGER,
    "ultima_actualizacion"	DATETIME NOT NULL,

 	PRIMARY KEY("id"),
 	FOREIGN KEY("fk_localidad") REFERENCES "localidad"("id")
 );

  CREATE TABLE IF NOT EXISTS "log_eventos" (

 	"id"			                    INTEGER NOT NULL,
	"fecha"			                    DATETIME NOT NULL,
  	"string_sql"		                TEXT NOT NULL,
 	"actualizado"		                INTEGER NOT NULL,

  	PRIMARY KEY("id")
  );

  CREATE TABLE IF NOT EXISTS "modo" (
  "id" INTEGER NOT NULL,
  "mode" INTEGER NOT NULL,
  PRIMARY KEY("id")
  );
