CREATE TABLE MAESTRA(
    ID INTEGER NOT NULL PRIMARY KEY,
    ID_MAESTRA INTEGER NULL,
    ID_TABLA NUMBER(10) NOT NULL,
    ID_ITEM NUMBER(10) NOT NULL,
    ORDEN NUMBER(10) NOT NULL,
    CODIGO VARCHAR(10) NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    VALOR VARCHAR(100) NULL,
    FLAG_ACTIVO NUMBER(1) NOT NULL,
	DESCRIPCION VARCHAR(250) NULL,
    ID_USUARIO_CREA INTEGER NOT NULL,
    FEC_USUARIO_CREA DATE NOT NULL,
    ID_USUARIO_MOD INTEGER NULL,
    FEC_USUARIO_MOD DATE NULL
);

CREATE SEQUENCE SEQ_MAESTRA
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE TABLE PRODUCTO(
ID INTEGER NOT NULL PRIMARY KEY,
IDT_UNIDAD_MEDIDA INTEGER NOT NULL,
NOMBRE VARCHAR(100) NOT NULL,
CODIGO VARCHAR(50) NULL,
PRECIO NUMBER(8,2) NOT NULL,
FLG_ACTIVO NUMBER(1) NOT NULL,
ID_USUARIO_CREA INTEGER NOT NULL,
FEC_USUARIO_CREA DATE NOT NULL,
ID_USUARIO_MOD INTEGER NULL,
FEC_USUARIO_MOD DATE NULL
);

CREATE SEQUENCE SEQ_PRODUCTO
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE TABLE VENTA(
ID INTEGER NOT NULL PRIMARY KEY,
SERIE VARCHAR(10) NOT NULL,
NUMERO VARCHAR(10) NOT NULL,
TOTAL NUMBER(8,2) NOT NULL,
FLG_ACTIVO NUMBER(1) NOT NULL,
ID_USUARIO_CREA INTEGER NOT NULL,
FEC_USUARIO_CREA DATE NOT NULL,
ID_USUARIO_MOD INTEGER NULL,
FEC_USUARIO_MOD DATE NULL
);

CREATE SEQUENCE SEQ_VENTA
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE TABLE DETALLE_VENTA(
ID INTEGER NOT NULL PRIMARY KEY,
ID_VENTA INTEGER NOT NULL,
ID_PRODUCTO INTEGER NOT NULL,
CANTIDAD NUMBER(6,1) NOT NULL,
PRECIO NUMBER(8,2) NOT NULL,
SUBTOTAL NUMBER(8,2) NOT NULL,
FLG_ACTIVO NUMBER(1) NOT NULL,
FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO(ID),
FOREIGN KEY (ID_VENTA) REFERENCES VENTA(ID)
);

CREATE SEQUENCE SEQ_DETALLE_VENTA
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;