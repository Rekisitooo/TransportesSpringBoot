DROP DATABASE IF EXISTS transportesspringboot;
CREATE DATABASE IF NOT EXISTS transportesspringboot;
USE transportesspringboot;

DROP TABLE IF EXISTS GRUPO_USUARIO;
CREATE TABLE IF NOT EXISTS GRUPO_USUARIO (
	ID	INT UNSIGNED AUTO_INCREMENT,
    NOMBRE	VARCHAR (255) NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID (ID)
);

INSERT INTO GRUPO_USUARIO (NOMBRE)
	VALUES 
		('Fuencarral'), ('Tetuán');

DROP TABLE IF EXISTS ROL_USUARIO;
CREATE TABLE IF NOT EXISTS ROL_USUARIO (
	ID	TINYINT UNSIGNED AUTO_INCREMENT,
    NOMBRE	VARCHAR (100) NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID (ID)
);

INSERT INTO ROL_USUARIO (NOMBRE)
	VALUES 
		('Administrador total'), 
        ('Encargado del departamento'),
        ('Colaborador del departamento');

DROP TABLE IF EXISTS USUARIO;
CREATE TABLE IF NOT EXISTS USUARIO (
	ID	INT UNSIGNED AUTO_INCREMENT,
    NOMBRE	VARCHAR (100) NOT NULL,
    ALIAS	VARCHAR (200) NOT NULL,
    CONTRASENA	VARCHAR (200) NOT NULL,
    COD_ROL	TINYINT UNSIGNED NOT NULL,
    COD_GRUPO INT UNSIGNED,
    
    CONSTRAINT PRIMARY KEY PK_ID (ID),
	CONSTRAINT FOREIGN KEY FK_COD_ROL_USUARIO (COD_ROL) REFERENCES ROL_USUARIO (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_GRUPO_USUARIO (COD_GRUPO) REFERENCES GRUPO_USUARIO (ID) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO USUARIO (NOMBRE, CONTRASENA, ALIAS, COD_ROL, COD_GRUPO)
	VALUES 
		('iker', 'iker', 'ikeriano', 1, 1), 
        ('fuencarral1', 'fuencarral1', 'Usuario de Fuencarral 1', 3, 1),
		('tetuan1', 'tetuan1', 'Usuario de Tetuán 1', 2, 2),
		('tetuan2', 'tetuan2', 'Usuario de Tetuán 2', 3, 2);

DROP TABLE IF EXISTS ROL_INVOLUCRADO;
CREATE TABLE IF NOT EXISTS ROL_INVOLUCRADO (
	ID TINYINT UNSIGNED,
	DESCRIPCION VARCHAR (100) NOT NULL,
    
	CONSTRAINT PRIMARY KEY PK_ID_ROL_INVOLUCRADO (ID)
);

INSERT INTO ROL_INVOLUCRADO (ID, DESCRIPCION)
VALUES 
	(1, 'Viajero'),
	(2, 'Conductor');
    
DROP TABLE IF EXISTS INVOLUCRADO;
CREATE TABLE IF NOT EXISTS INVOLUCRADO (
	ID	INT UNSIGNED AUTO_INCREMENT,
    NOMBRE	VARCHAR (100) NOT NULL,
    APELLIDOS	VARCHAR (100),
    ACTIVO	BOOL NOT NULL,
    NUMERO_PLAZAS VARCHAR(2) NOT NULL,
    COD_ROL	TINYINT UNSIGNED NOT NULL,
    COD_USUARIO_PROPIETARIO INT UNSIGNED NOT NULL,
    COD_GRUPO_USUARIO INT UNSIGNED,
    
    CONSTRAINT PRIMARY KEY PK_ID (ID),
    CONSTRAINT FOREIGN KEY FK_COD_ROL_POR_INVOLUCRADO (COD_ROL) REFERENCES ROL_INVOLUCRADO (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY FK_COD_USUARIO_PROPIETARIO (COD_USUARIO_PROPIETARIO) REFERENCES USUARIO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY FK_COD_GRUPO_USUARIO (COD_GRUPO_USUARIO) REFERENCES GRUPO_USUARIO (ID) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT  INTO INVOLUCRADO (NOMBRE, APELLIDOS, ACTIVO, NUMERO_PLAZAS, COD_ROL, COD_USUARIO_PROPIETARIO, COD_GRUPO_USUARIO)
VALUES 
	('Ikeriano no compartido', '', FALSE, 1, 1, 1, NULL),
    ('Fuencarral1 no compartido', '', FALSE, 1, 1, 2, NULL),
	('Adolfo', '', TRUE, 1, 1, 1, 1),
    ('Julio', '', TRUE, 1, 1, 1, 1),
    ('Carmen', '', TRUE, 1, 1, 1, 1),
    ('Enriqueta', '', TRUE, 1, 1, 1, 1),
    ('Pilar', 'Pérez', TRUE, 1, 1, 1, 1),
	('Pepe y Bea', '', TRUE, 2, 1, 1, 1),
    ('Roselia', '', FALSE, 1, 1, 1, 1),
    ('Nicolás', '', TRUE, 1, 1, 1, 1),
    ('Ino', '', TRUE, 1, 1, 1, 1),
    ('Tolli', '', TRUE, 1, 1, 1, 1),
    ('Ramona', '', TRUE, 1, 1, 1, 1),
	('Silvia', '', TRUE, 1, 1, 1, 1),
    ('Iker', 'Quijano', TRUE, 4, 2, 1, 1),
    ('Paco y Carmela', '', TRUE, 3, 2, 1, 1),
    ('Juan Manuel', '', FALSE, 4, 2, 1, 1),
    ('Familia Cárceles', '', FALSE, 1, 2, 1, 1),
    ('Familia Conde', '', FALSE, 3, 2, 1, 1),
    ('Frutos y Rosi', '', TRUE, 2, 2, 1, 1),
    ('Luisi y Jacinto', '', TRUE, 2, 2, 1, 1),
    ('Verónica', '', TRUE, 4, 2, 1, 1),
    ('Abel y Mariana', '', TRUE, 2, 2, 1, 1),
    ('Familia Cerezo', '', TRUE, 1, 2, 1, 1),
    ('Vanessa', '', TRUE, 2, 2, 1, 1);

DROP TABLE IF EXISTS DIA_DE_LA_SEMANA;
CREATE TABLE IF NOT EXISTS DIA_DE_LA_SEMANA (
	ID TINYINT UNSIGNED,
    NOMBRE ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_DIA_DE_LA_SEMANA (ID)
);

INSERT INTO DIA_DE_LA_SEMANA (ID, NOMBRE)
VALUES 
	(1, 'Lunes'), 
    (2, 'Martes'),
    (3, 'Miércoles'),
    (4, 'Jueves'),
    (5, 'Viernes'),
    (6, 'Sábado'),
    (7, 'Domingo');


/*
 * Tabla que sirve para mostrar al usuario que tipo de dia especial se puede insertar.
*/
DROP TABLE IF EXISTS DIA_ESPECIAL;
CREATE TABLE IF NOT EXISTS DIA_ESPECIAL (
	ID SMALLINT UNSIGNED AUTO_INCREMENT,
    DESCRIPCION VARCHAR (200) NOT NULL,
    
	CONSTRAINT PRIMARY KEY PK_ID_DIA_ESPECIAL (ID)
);

INSERT INTO DIA_ESPECIAL (DESCRIPCION)
	VALUES 
		('Conmemoración'),
        ('Asamblea'),
        ('Cambio de día de reunión'),
        ('Otros');
        
DROP TABLE IF EXISTS DIA_TRANSPORTE_SEMANAL;
CREATE TABLE IF NOT EXISTS DIA_TRANSPORTE_SEMANAL (
	ID	TINYINT UNSIGNED AUTO_INCREMENT,
    DESCRIPCION	VARCHAR (100) NOT NULL,
    COD_DIA_DE_LA_SEMANA	ENUM('1', '2', '3', '4', '5', '6', '7') NOT NULL,
    ACTIVO	BOOL NOT NULL,
    COD_USUARIO_PROPIETARIO INT UNSIGNED NOT NULL,
    COD_GRUPO_USUARIO INT UNSIGNED,
    
    CONSTRAINT PRIMARY KEY PK_ID_DIA_TRANSPORTE_SEMANAL (ID),
    CONSTRAINT FOREIGN KEY FK_COD_DIA_DE_LA_SEMANA (COD_DIA_DE_LA_SEMANA) REFERENCES DIA_DE_LA_SEMANA (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_USUARIO_PROPIETARIO (COD_USUARIO_PROPIETARIO) REFERENCES USUARIO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_GRUPO_USUARIO (COD_GRUPO_USUARIO) REFERENCES GRUPO_USUARIO (ID) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO DIA_TRANSPORTE_SEMANAL (DESCRIPCION, COD_DIA_DE_LA_SEMANA, ACTIVO, COD_USUARIO_PROPIETARIO, COD_GRUPO_USUARIO)
VALUES 
	('Vida y Ministerio', 2, TRUE, 1, 1),
    ('Reunión Pública', 7, TRUE, 1, 1),
    ('Vida y Ministerio', 5, FALSE, 1, 1)
;
-- SELECT * FROM DIA_TRANSPORTE_SEMANAL;

DROP TABLE IF EXISTS PLANTILLA;
CREATE TABLE IF NOT EXISTS PLANTILLA (
	ID INT UNSIGNED AUTO_INCREMENT,
    NOMBRE	VARCHAR (200) NOT NULL,
    ANNO VARCHAR (4) NOT NULL,
    MES ENUM('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12') NOT NULL,
    COD_USUARIO_PROPIETARIO INT UNSIGNED NOT NULL,
    COD_GRUPO_USUARIO INT UNSIGNED,
    
    CONSTRAINT PRIMARY KEY PK_ID (ID),
    CONSTRAINT FOREIGN KEY FK_COD_USUARIO_PROPIETARIO (COD_USUARIO_PROPIETARIO) REFERENCES USUARIO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY FK_COD_GRUPO_USUARIO (COD_GRUPO_USUARIO) REFERENCES GRUPO_USUARIO (ID) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO PLANTILLA (NOMBRE, ANNO, MES, COD_USUARIO_PROPIETARIO, COD_GRUPO_USUARIO)
	VALUES 
		('mayo_2025', '2025', 5, 1, 1),
        ('junio_2025','2025', 6, 1, 1);
-- SELECT * FROM PLANTILLA;

/* 
 * Tabla que sirve para el historico de plantillas. Por ejemplo, puede ser que haya cambiado el dia de la reunion de un ano para otro.
 * Si se quiere consultar una plantilla de hace un ano, esta tabla almacena que dias de transporte habia para esa plantilla.
 * Se rellena al crear la plantilla con los dias que haya activos en la tabla DIA_TRANSPORTE_SEMANAL en ese momento. 
*/
DROP TABLE IF EXISTS DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (
	COD_PLANTILLA INT UNSIGNED,
    COD_DIA_TRANSPORTE_SEMANAL TINYINT UNSIGNED,
    
	CONSTRAINT PRIMARY KEY PK_ID_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_PLANTILLA, COD_DIA_TRANSPORTE_SEMANAL),
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE NO ACTION,
    CONSTRAINT FOREIGN KEY FK_COD_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_DIA_TRANSPORTE_SEMANAL) REFERENCES DIA_TRANSPORTE_SEMANAL (ID) ON DELETE NO ACTION
);
/*
INSERT INTO DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_PLANTILLA, COD_DIA_TRANSPORTE_SEMANAL)
	VALUES
		(1, 1),
        (1, 2),
        (2, 1),
        (2, 3);
*/
-- SELECT * FROM DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA;

/*
DELIMITER //

CREATE TRIGGER after_insert_plantilla
AFTER INSERT ON tabla_origen
FOR EACH ROW
BEGIN
	INSERT INTO tabla_b (nombre, email)
    SELECT nombre, email
    FROM tabla_a
    WHERE id = NEW.id; -- Suponiendo que se desea copiar los datos de tabla_a basados en el id
END //

DELIMITER ;
*/

/* 
 * Tabla que sirve para almacenar que dias de transporte diferentes a lo normal tiene la congregacion (por algun evento).
 * Por ejemplo, con la visita del super de otra congregacion del mismo salon, si la congregacion tiene la reunion el martes y la otra
 * el viernes, se cambiara la del martes al viernes. En esta tabla se refleja el viernes que va a tener la reunion.
 * Si hay cualquier otro evento, como por ejemplo, la visita de un hermano de la central mundial, que haga que haya una reunion
 * extra en el salon, ese dia se refleja en esta tabla.
*/
DROP TABLE IF EXISTS DIA_TRANSPORTE_ESPECIAL;
CREATE TABLE IF NOT EXISTS DIA_TRANSPORTE_ESPECIAL (
	ID	SMALLINT UNSIGNED AUTO_INCREMENT,
    DESCRIPCION	VARCHAR (200) NOT NULL,
    FECHA	DATE NOT NULL,
    COD_PLANTILLA INT UNSIGNED NOT NULL,
    
	CONSTRAINT PRIMARY KEY PK_ID_DIA_TRANSPORTE_ESPECIAL (ID),
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA_DIA_TRANSPORTE_ESPECIAL (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO DIA_TRANSPORTE_ESPECIAL (DESCRIPCION, FECHA, COD_PLANTILLA)
	VALUES 
		('Conmemoración.', '2024/05/10', 1),
		('Visita del representante de la central mundial: Joel Dellinger.', '2024/05/17', 1),
        ('Cambio de día de la reunión Vida y Ministerio Cristiano por la visita del superintendente de circuito de Tetuán.', '2024/05/30', 1),
		('Asamblea regional 2025 (Día 1)', '2024/06/13', 2),
        ('Asamblea regional 2025 (Día 2)', '2024/06/14', 2),
        ('Asamblea regional 2025 (Día 3)', '2024/06/15', 2);
-- SELECT * FROM DIA_TRANSPORTE_ESPECIAL;

DROP TABLE IF EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_ESPECIAL;
CREATE TABLE IF NOT EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_ESPECIAL (
	COD_DIA_TRANSPORTE_ESPECIAL SMALLINT UNSIGNED,
    COD_INVOLUCRADO INT UNSIGNED,
    DISPONIBLE BOOLEAN NOT NULL,
    
	CONSTRAINT PRIMARY KEY PK_ID_DISPONIBILIDAD_POR_DIA_TRANSPORTE_ESPECIAL (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_ESPECIAL),
    CONSTRAINT FOREIGN KEY FK_COD_DIA_TRANSPORTE_ESPECIAL (COD_DIA_TRANSPORTE_ESPECIAL) REFERENCES DIA_TRANSPORTE_ESPECIAL (ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_DIA_TRANSPORTE_ESPECIAL (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_ESPECIAL (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_ESPECIAL, DISPONIBLE)
	VALUES 
		(1, 1, TRUE),
        (2, 1, TRUE),
        (3, 1, TRUE),
        (4, 1, TRUE),
        (5, 1, TRUE),
        (6, 1, TRUE),
        (7, 1, TRUE),
        (8, 1, TRUE),
        (9, 1, TRUE),
        (10, 1, TRUE),
        (11, 1, TRUE),
        (12, 1, TRUE),
        (13, 1, TRUE),
        (14, 1, TRUE),
        (15, 1, TRUE),
        (16, 1, TRUE),
        (17, 1, TRUE),
        (18, 1, TRUE),
        (19, 1, TRUE),
        (20, 1, TRUE),
        (21, 1, TRUE),
        (22, 1, TRUE),
        (23, 1, TRUE),
        (1, 2, TRUE),
        (2, 2, TRUE),
        (3, 2, TRUE),
        (4, 2, TRUE),
        (5, 2, TRUE),
        (6, 2, TRUE),
        (7, 2, TRUE),
        (8, 2, TRUE),
        (9, 2, TRUE),
        (10, 2, TRUE),
        (11, 2, TRUE),
        (12, 2, TRUE),
        (13, 2, TRUE),
        (14, 2, TRUE),
        (15, 2, TRUE),
        (16, 2, TRUE),
        (17, 2, TRUE),
        (18, 2, TRUE),
        (19, 2, TRUE),
        (20, 2, TRUE),
        (21, 2, TRUE),
        (22, 2, TRUE),
        (23, 2, TRUE),
        (1, 3, TRUE),
        (2, 3, TRUE),
        (3, 3, TRUE),
        (4, 3, TRUE),
        (5, 3, TRUE),
        (6, 3, TRUE),
        (7, 3, TRUE),
        (8, 3, TRUE),
        (9, 3, TRUE),
        (10, 3, TRUE),
        (11, 3, TRUE),
        (12, 3, TRUE),
        (13, 3, TRUE),
        (14, 3, TRUE),
        (15, 3, TRUE),
        (16, 3, TRUE),
        (17, 3, TRUE),
        (18, 3, TRUE),
        (19, 3, TRUE),
        (20, 3, TRUE),
        (21, 3, TRUE),
        (22, 3, TRUE),
        (23, 3, TRUE),
        (1, 4, FALSE),
        (2, 4, FALSE),
        (3, 4, FALSE),
        (4, 4, FALSE),
        (5, 4, FALSE),
        (6, 4, FALSE),
        (7, 4, FALSE),
        (8, 4, FALSE),
        (9, 4, FALSE),
        (10, 4, FALSE),
        (11, 4, FALSE),
        (12, 4, FALSE),
        (13, 4, FALSE),
        (14, 4, FALSE),
        (15, 4, FALSE),
        (16, 4, FALSE),
        (17, 4, FALSE),
        (18, 4, FALSE),
        (19, 4, FALSE),
        (20, 4, FALSE),
        (21, 4, FALSE),
        (22, 4, FALSE),
        (23, 4, FALSE),
        (1, 5, FALSE),
        (2, 5, FALSE),
        (3, 5, FALSE),
        (4, 5, FALSE),
        (5, 5, FALSE),
        (6, 5, FALSE),
        (7, 5, FALSE),
        (8, 5, FALSE),
        (9, 5, FALSE),
        (10, 5, FALSE),
        (11, 5, FALSE),
        (12, 5, FALSE),
        (13, 5, FALSE),
        (14, 5, FALSE),
        (15, 5, FALSE),
        (16, 5, FALSE),
        (17, 5, FALSE),
        (18, 5, FALSE),
        (19, 5, FALSE),
        (20, 5, FALSE),
        (21, 5, FALSE),
        (22, 5, FALSE),
        (23, 5, FALSE),
        (1, 6, FALSE),
        (2, 6, FALSE),
        (3, 6, FALSE),
        (4, 6, FALSE),
        (5, 6, FALSE),
        (6, 6, FALSE),
        (7, 6, FALSE),
        (8, 6, FALSE),
        (9, 6, FALSE),
        (10, 6, FALSE),
        (11, 6, FALSE),
        (12, 6, FALSE),
        (13, 6, FALSE),
        (14, 6, FALSE),
        (15, 6, FALSE),
        (16, 6, FALSE),
        (17, 6, FALSE),
        (18, 6, FALSE),
        (19, 6, FALSE),
        (20, 6, FALSE),
        (21, 6, FALSE),
        (22, 6, FALSE),
        (23, 6, FALSE);

/* 
 * Tabla que sirve para almacenar que dias de transporte se anulan por algun evento de la congregacion.
 * Por ejemplo, con la visita del super de otra congregacion del mismo salon, si la congregacion tiene la reunion el martes, se cambiara
 * al dia que la tenga la congregacion que tiene la visita. La sustitucion se refleja en esta tabla
*/
DROP TABLE IF EXISTS DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL;
CREATE TABLE IF NOT EXISTS DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL (
	COD_DIA_TRANSPORTE_SEMANAL_ANULADO	TINYINT UNSIGNED,
    COD_DIA_ESPECIAL_REEMPLAZANTE	SMALLINT UNSIGNED,
    
	CONSTRAINT PRIMARY KEY PK_ID_DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL (COD_DIA_TRANSPORTE_SEMANAL_ANULADO, COD_DIA_ESPECIAL_REEMPLAZANTE),
    CONSTRAINT FOREIGN KEY FK_COD_DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL (COD_DIA_TRANSPORTE_SEMANAL_ANULADO) REFERENCES DIA_TRANSPORTE_SEMANAL (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_DIA_ESPECIAL_REEMPLAZANTE (COD_DIA_ESPECIAL_REEMPLAZANTE) REFERENCES DIA_TRANSPORTE_ESPECIAL (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL (COD_DIA_TRANSPORTE_SEMANAL_ANULADO, COD_DIA_ESPECIAL_REEMPLAZANTE)
	VALUE
		(1, 3),
        (1, 4),
        (1, 5),
        (1, 6),
        (2, 1);
-- SELECT * FROM DIA_TRANSPORTE_SEMANAL_ANULADO_POR_DIA_ESPECIAL;

/* 
 * Tabla que sirve para el historico de plantillas. Por ejemplo, puede ser que ya no estén algunos conductores o viajeros que 
 * estaban antes. Si se quiere consultar una plantilla de hace un ano, esta tabla almacena los involucrados que habia para esa plantilla.
 * Se rellena al crear la plantilla con los involucrados que haya activos en la tabla INVOLUCRADO en ese momento. 
*/
DROP TABLE IF EXISTS INVOLUCRADO_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS INVOLUCRADO_POR_PLANTILLA (
    COD_INVOLUCRADO INT UNSIGNED,
    COD_PLANTILLA INT UNSIGNED,
    PLAZAS CHAR(2) NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_INVOLUCRADO_POR_PLANTILLA (COD_INVOLUCRADO, COD_PLANTILLA),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_PLANTILLA (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA_POR_PLANTILLA (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO INVOLUCRADO_POR_PLANTILLA (COD_INVOLUCRADO, COD_PLANTILLA, PLAZAS)
	VALUES
		(1, 1, 1),
        (2, 1, 1),
        (3, 1, 1),
        (4, 1, 1),
        (5, 1, 1),
        (6, 1, 2),
		(8, 1, 1),
        (9, 1, 1),
        (10, 1, 1),
        (11, 1, 1),
        (12, 1, 1),
		(13, 1, 4),
        (14, 1, 3),
        (16, 1, 1),
        (17, 1, 2),
		(18, 1, 3),
        (19, 1, 2),
        (20, 1, 3),
        (21, 1, 2),
        (22, 1, 1),
        (23, 1, 2),
        (1, 2, 1),
        (2, 2, 1),
        (3, 2, 1),
        (4, 2, 1),
        (5, 2, 1),
        (6, 2, 2),
		(8, 2, 1),
        (9, 2, 1),
        (11, 2, 1),
        (12, 2, 1),
		(13, 2, 4),
        (14, 2, 3),
		(18, 2, 3),
        (19, 2, 2),
        (20, 2, 3),
        (21, 2, 2),
        (22, 2, 1),
        (23, 2, 2);
-- SELECT * FROM INVOLUCRADO_POR_PLANTILLA;
        
DROP TABLE IF EXISTS TRANSPORTE;
CREATE TABLE IF NOT EXISTS TRANSPORTE (
	ID INT UNSIGNED AUTO_INCREMENT,
    FECHA DATE NOT NULL,
    COD_PLANTILLA INT UNSIGNED NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_TRANSPORTE (ID),
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO TRANSPORTE (FECHA, COD_PLANTILLA)
	VALUES 
		('2025/05/04', 1),
        ('2025/05/04', 1),
        ('2025/05/04', 1),
        ('2025/05/04', 1),
        ('2025/05/04', 1),
        ('2025/05/06', 1),
        ('2025/05/06', 1),
        ('2025/05/06', 1),
        ('2025/05/13', 1),
        ('2025/05/13', 1),
        ('2025/05/13', 1),
        ('2025/05/17', 1),
        ('2025/05/17', 1),
        ('2025/05/17', 1),
        ('2025/05/17', 1),
        ('2025/05/17', 1),
        ('2025/05/18', 1),
        ('2025/05/18', 1),
        ('2025/05/18', 1),
        ('2025/05/18', 1),
        ('2025/05/18', 1),
        ('2025/05/20', 1),
        ('2025/05/20', 1),
        ('2025/05/20', 1),
        ('2025/05/25', 1),
        ('2025/05/25', 1),
        ('2025/05/25', 1),
        ('2025/05/25', 1),
        ('2025/05/25', 1),
        ('2025/05/30', 1),
        ('2025/05/30', 1),
        ('2025/05/30', 1),
		('2025/06/01', 2),
        ('2025/06/01', 2),
        ('2025/06/01', 2),
        ('2025/06/01', 2),
        ('2025/06/03', 2),
        ('2025/06/03', 2),
        ('2025/06/03', 2),
        ('2025/06/08', 2),
        ('2025/06/08', 2),
        ('2025/06/08', 2),
        ('2025/06/08', 2),
        ('2025/06/17', 2),
        ('2025/06/17', 2),
        ('2025/06/17', 2),
        ('2025/06/22', 2),
        ('2025/06/22', 2),
        ('2025/06/22', 2),
        ('2025/06/22', 2),
        ('2025/06/24', 2),
        ('2025/06/24', 2),
        ('2025/06/24', 2),
        ('2025/06/29', 2),
        ('2025/06/29', 2),
        ('2025/06/29', 2),
        ('2025/06/29', 2);
-- SELECT * FROM TRANSPORTE;

DROP TABLE IF EXISTS INVOLUCRADO_POR_TRANSPORTE;
CREATE TABLE IF NOT EXISTS INVOLUCRADO_POR_TRANSPORTE (
	COD_TRANSPORTE INT UNSIGNED,
    COD_INVOLUCRADO INT UNSIGNED NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_INVOLUCRADO_POR_TRANSPORTE (COD_TRANSPORTE, COD_INVOLUCRADO),
    CONSTRAINT FOREIGN KEY FK_COD_TRANSPORTE_INVOLUCRADO_POR_TRANSPORTE (COD_TRANSPORTE) REFERENCES TRANSPORTE (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_TRANSPORTE (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE NO ACTION ON UPDATE CASCADE
);
INSERT INTO INVOLUCRADO_POR_TRANSPORTE (COD_TRANSPORTE, COD_INVOLUCRADO)
	VALUES 
		(1, 11),
        (1, 16),
        (2, 3),
        (2, 4),
        (2, 17),
        (3, 5),
        (3, 1),
        (3, 2),
        (3, 14),
        (4, 9),
        (4, 12),
        (4, 18),
        (5, 9),
        (5, 6),
        (5, 20), -- 04/05 DOMINGO
        (6, 1),
        (6, 2),
        (6, 11),
        (6, 5),
        (7, 3),
        (7, 4),
        (7, 23),
        (8, 6),
        (8, 19), -- 06/05 MARTES
        (9, 1),
        (9, 2),
        (9, 11),
        (9, 5),
        (10, 3),
        (10, 4),
        (10, 23),
        (11, 6),
        (11, 19), -- 13/05 MARTES (El sabado 10 es la conme)
        (12, 11),
        (12, 16),
        (13, 3),
        (13, 4),
        (13, 17),
        (14, 5),
        (14, 1),
        (14, 2),
        (14, 14),
        (15, 9),
        (15, 12),
        (15, 18),	
        (16, 9),
        (16, 6),
        (16, 20), -- 17/05 SABADO VISITA DELINGER
        (17, 11),
        (17, 16),
        (18, 3),
        (18, 4),
        (18, 17),
        (19, 5),
        (19, 1),
        (19, 2),
        (19, 14),
        (20, 9),
        (20, 12),
        (20, 18),	
        (21, 9),
        (21, 6),
        (21, 20), -- 18/05 DOMINGO
        (22, 1),
        (22, 2),
        (22, 11),
        (22, 5),
        (23, 3),
        (23, 4),
        (23, 23),
        (24, 6),
        (24, 19), -- 20/05 MARTES
        (25, 11),
        (25, 16),
        (26, 3),
        (26, 4),
        (26, 17),
        (27, 5),
        (27, 1),
        (27, 2),
        (27, 14),
        (28, 9),
        (28, 12),
        (28, 18),	
        (29, 9),
        (29, 6),
        (29, 20), -- 25/05 DOMINGO
        (30, 1),
        (30, 2),
        (30, 11),
        (30, 10),
        (30, 5),
        (31, 3),
        (31, 4),
        (31, 23),
        (32, 6),
        (32, 19), -- 30/05 MARTES
        (33, 1),
        (33, 2),
        (33, 11),
        (33, 8),
        (33, 5),
        (34, 6),
        (34, 23),
        (35, 3),
        (35, 4),
        (35, 21),
        (36, 9),
        (36, 12),
        (36, 5),
        (36, 20), -- 01/06 DOMINGO
        (37, 6),
        (37, 11),
        (37, 5),
        (38, 3),
        (38, 4),
        (38, 18),
        (39, 1),
        (39, 2),
        (39, 19), -- 03/06 MARTES
        (40, 1),
        (40, 2),
        (40, 11),
        (40, 13),
        (41, 6),
        (41, 23),
        (42, 3),
        (42, 4),
        (42, 21),
        (43, 9),
        (43, 12),
        (43, 5),
        (43, 20), -- 08/06 DOMINGO
        (44, 6),
        (44, 11),
        (44, 5),
        (45, 3),
        (45, 4),
        (45, 18),
        (46, 1),
        (46, 2),
        (46, 19), -- 17/06 MARTES
        (47, 1),
        (47, 2),
        (47, 11),
        (47, 8),
        (47, 5),
        (48, 6),
        (48, 23),
        (49, 3),
        (49, 4),
        (49, 21),
        (49, 9),
        (50, 12),
        (50, 5),
        (50, 20), -- 22/06 DOMINGO
        (51, 6),
        (51, 11),
        (51, 5),
        (52, 3),
        (52, 4),
        (52, 18),
        (53, 1),
        (53, 2),
        (53, 19), -- 24/06 MARTES
        (54, 1),
        (54, 2),
        (54, 11),
        (54, 8),
        (54, 5),
        (55, 6),
        (55, 23),
        (56, 3),
        (56, 4),
        (56, 21),
        (56, 9),
        (57, 12),
        (57, 5),
        (57, 20); -- 29/06 DOMINGO
-- SELECT * FROM INVOLUCRADO_POR_TRANSPORTE;

DROP TABLE IF EXISTS AUSENCIA_POR_INVOLUCRADO;
CREATE TABLE IF NOT EXISTS AUSENCIA_POR_INVOLUCRADO (
	ID	SMALLINT UNSIGNED AUTO_INCREMENT,
    COD_INVOLUCRADO INT UNSIGNED NOT NULL,
    FECHA_INICIO DATE NOT NULL,
    FECHA_FIN DATE NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_AUSENCIA_INVOLUCRADO (ID),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_AUSENCIA (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE NO ACTION ON UPDATE CASCADE
);

INSERT INTO AUSENCIA_POR_INVOLUCRADO (COD_INVOLUCRADO, FECHA_INICIO, FECHA_FIN)
	VALUES 
		(5, '2024/06/16', '2024/06/22'),
        (6, '2024/06/22', '2024/06/27'),
        (14, '2024/05/11', '2024/05/13');
-- SELECT * FROM AUSENCIA_POR_INVOLUCRADO;

DROP TRIGGER IF EXISTS CHECK_FECHAS_INSERT;
delimiter $$
CREATE TRIGGER CHECK_FECHAS_INSERT BEFORE INSERT ON AUSENCIA_POR_INVOLUCRADO
FOR EACH ROW
BEGIN
    IF NEW.FECHA_INICIO >= NEW.FECHA_FIN THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de inicio debe ser menor que la fecha de fin de la ausencia .';
    END IF;
END; $$

delimiter ;

DROP TRIGGER IF EXISTS CHECK_FECHAS_UPDATE;
delimiter $$
CREATE TRIGGER CHECK_FECHAS_UPDATE BEFORE UPDATE ON AUSENCIA_POR_INVOLUCRADO
FOR EACH ROW
BEGIN
    IF NEW.FECHA_INICIO >= NEW.FECHA_FIN THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de inicio debe ser menor que la fecha de fin de la ausencia .';
    END IF;
END; $$

delimiter ;

DROP TABLE IF EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL;
CREATE TABLE IF NOT EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL (
	COD_INVOLUCRADO INT UNSIGNED,
    COD_DIA_TRANSPORTE_SEMANAL TINYINT UNSIGNED,
    DISPONIBLE BOOL NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_DIA_TRANSPORTE_POR_INVOLUCRADO (COD_DIA_TRANSPORTE_SEMANAL) REFERENCES DIA_TRANSPORTE_SEMANAL (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL, DISPONIBLE)
VALUES 
	(1, 1, TRUE),
    (1, 2, TRUE),
    (2, 1, TRUE),
    (2, 2, TRUE),
    (3, 1, TRUE),
    (3, 2, TRUE),
    (4, 1, TRUE),
    (4, 2, TRUE),
    (5, 1, FALSE),
    (5, 2, TRUE),
    (6, 1, TRUE),
    (6, 2, TRUE),
    (7, 1, FALSE),
    (7, 2, FALSE),
    (8, 1, FALSE),
    (8, 2, TRUE),
    (9, 1, FALSE),
    (9, 2, TRUE),
    (10, 1, TRUE),
    (10, 2, FALSE),
    (11, 1, TRUE),
    (11, 2, TRUE),
    (12, 1, FALSE),
    (12, 2, TRUE),
    (13, 1, TRUE),
    (13, 2, TRUE),
    (14, 1, FALSE),
    (14, 2, TRUE),
    (15, 1, FALSE),
    (15, 2, FALSE),
    (16, 1, FALSE),
    (16, 2, TRUE),
    (17, 1, TRUE),
    (17, 2, TRUE),
    (18, 1, TRUE),
    (18, 2, TRUE),
    (19, 1, TRUE),
    (19, 2, TRUE),
    (20, 1, FALSE),
    (20, 2, TRUE),
    (21, 1, FALSE),
    (21, 2, TRUE),
    (22, 1, TRUE),
    (22, 2, TRUE),
    (23, 1, FALSE),
    (23, 2, TRUE);
-- SELECT * FROM DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL;

-- Se rellena al crear la plantilla
DROP TABLE IF EXISTS DISPON_INVOL_POR_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS DISPON_INVOL_POR_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (
	COD_INVOLUCRADO INT UNSIGNED,
    COD_DIA_TRANSPORTE_SEMANAL TINYINT UNSIGNED,
    DISPONIBLE BOOL NOT NULL,
    COD_PLANTILLA INT UNSIGNED NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL, COD_PLANTILLA),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_DIA_TRANSPORTE_POR_INVOLUCRADO (COD_DIA_TRANSPORTE_SEMANAL) REFERENCES DIA_TRANSPORTE_SEMANAL (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO DISPON_INVOL_POR_DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL, DISPONIBLE, COD_PLANTILLA)
VALUES 
	(1, 1, TRUE, 1),
    (1, 2, TRUE, 1),
    (2, 1, TRUE, 1),
    (2, 2, TRUE, 1),
    (3, 1, TRUE, 1),
    (3, 2, TRUE, 1),
    (4, 1, TRUE, 1),
    (4, 2, TRUE, 1),
    (5, 1, FALSE, 1),
    (5, 2, TRUE, 1),
    (6, 1, TRUE, 1),
    (6, 2, TRUE, 1),
    (7, 1, FALSE, 1),
    (7, 2, FALSE, 1),
    (8, 1, FALSE, 1),
    (8, 2, TRUE, 1),
    (9, 1, FALSE, 1),
    (9, 2, TRUE, 1),
    (10, 1, TRUE, 1),
    (10, 2, FALSE, 1),
    (11, 1, TRUE, 1),
    (11, 2, TRUE, 1),
    (12, 1, FALSE, 1),
    (12, 2, TRUE, 1),
    (13, 1, TRUE, 1),
    (13, 2, TRUE, 1),
    (14, 1, FALSE, 1),
    (14, 2, TRUE, 1),
    (15, 1, FALSE, 1),
    (15, 2, FALSE, 1),
    (16, 1, FALSE, 1),
    (16, 2, TRUE, 1),
    (17, 1, TRUE, 1),
    (17, 2, TRUE, 1),
    (18, 1, TRUE, 1),
    (18, 2, TRUE, 1),
    (19, 1, TRUE, 1),
    (19, 2, TRUE, 1),
    (20, 1, FALSE, 1),
    (20, 2, TRUE, 1),
    (21, 1, FALSE, 1),
    (21, 2, TRUE, 1),
    (22, 1, TRUE, 1),
    (22, 2, TRUE, 1),
    (23, 1, FALSE, 1),
    (23, 2, TRUE, 1),
    (1, 1, TRUE, 2),
    (1, 2, TRUE, 2),
    (2, 1, TRUE, 2),
    (2, 2, TRUE, 2),
    (3, 1, TRUE, 2),
    (3, 2, TRUE, 2),
    (4, 1, TRUE, 2),
    (4, 2, TRUE, 2),
    (5, 1, FALSE, 2),
    (5, 2, TRUE, 2),
    (6, 1, TRUE, 2),
    (6, 2, TRUE, 2),
    (7, 1, FALSE, 2),
    (7, 2, FALSE, 2),
    (8, 1, FALSE, 2),
    (8, 2, TRUE, 2),
    (9, 1, FALSE, 2),
    (9, 2, TRUE, 2),
    (10, 1, TRUE, 2),
    (10, 2, FALSE, 2),
    (11, 1, TRUE, 2),
    (11, 2, TRUE, 2),
    (12, 1, FALSE, 2),
    (12, 2, TRUE, 2),
    (13, 1, TRUE, 2),
    (13, 2, TRUE, 2),
    (14, 1, FALSE, 2),
    (14, 2, TRUE, 2),
    (15, 1, FALSE, 2),
    (15, 2, FALSE, 2),
    (16, 1, FALSE, 2),
    (16, 2, TRUE, 2),
    (17, 1, TRUE, 2),
    (17, 2, TRUE, 2),
    (18, 1, TRUE, 2),
    (18, 2, TRUE, 2),
    (19, 1, TRUE, 2),
    (19, 2, TRUE, 2),
    (20, 1, FALSE, 2),
    (20, 2, TRUE, 2),
    (21, 1, FALSE, 2),
    (21, 2, TRUE, 2),
    (22, 1, TRUE, 2),
    (22, 2, TRUE, 2),
    (23, 1, FALSE, 2),
    (23, 2, TRUE, 2);

-- Se rellena al crear la plantilla
DROP TABLE IF EXISTS PLAZAS_OCUPADAS_CONDUCTOR_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS PLAZAS_OCUPADAS_CONDUCTOR_POR_PLANTILLA (
	ID INT UNSIGNED,
	COD_CONDUCTOR INT UNSIGNED NOT NULL,
    FECHA_TRANSPORTE DATE NOT NULL,
    PLAZAS_OCUPADAS	VARCHAR(2) NOT NULL DEFAULT 0,
    
    CONSTRAINT PRIMARY KEY PK_PLAZAS_DISPONIBLES_INVOLUCRADO_POR_PLANTILLA (ID),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_CONDUCTOR) REFERENCES INVOLUCRADO (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

/*
DROP PROCEDURE IF EXISTS crud_viajerosInicial;

DELIMITER $$
CREATE PROCEDURE crud_viajerosInicial(
	IN mostrar_viajeros_activos VARCHAR(4), -- Los valores que deberian llegarle son '0' o '1' o '0, 1' o '1, 0'
    IN cod_usuario INT, -- No puede ser nulo
    IN cod_grupo_usuario INT -- Puede ser nulo
)
BEGIN
    DECLARE query_sql TEXT;
    DECLARE disponibilidad_por_dia_transporte TEXT;

    -- Paso 1: Construir dinámicamente las columnas de dias de transporte
SELECT 
    GROUP_CONCAT(DISTINCT CONCAT(
				disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL, ' as id_dia_transporte, 
				MAX(CASE WHEN disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL = \'',
                disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL,
                '\' THEN disponibilidad_viajero.DISPONIBLE ELSE 0 END) 
                					AS \'Asiste a ',
                dia_transporte_semanal.DESCRIPCION,
                '\''))
	INTO disponibilidad_por_dia_transporte FROM
		DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_viajero
			INNER JOIN DIA_TRANSPORTE_SEMANAL dia_transporte_semanal 
				ON dia_transporte_semanal.ID = disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL
		WHERE
			dia_transporte_semanal.ACTIVO = TRUE;

    -- Paso 2: Construir la consulta SQL completa
    SET @query_sql = CONCAT(
        "SELECT 
			viajero.id as 'Id',
			viajero.NOMBRE AS 'Nombre', 
            viajero.APELLIDOS AS 'Apellidos',
            viajero.NUMERO_PLAZAS as plazas_ocupadas,
            viajero.ACTIVO as activo,
            IF(ISNULL(viajero.COD_GRUPO_USUARIO), 0, 1) as usuario_compartido_grupo,
            ", disponibilidad_por_dia_transporte, "
         FROM INVOLUCRADO viajero
			INNER JOIN ROL_INVOLUCRADO rol
				ON viajero.COD_ROL = rol.ID
			INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_viajero
				ON viajero.ID = disponibilidad_viajero.COD_INVOLUCRADO
			WHERE 
				(viajero.COD_USUARIO_PROPIETARIO = ", cod_usuario, " OR viajero.COD_GRUPO_USUARIO = ", cod_grupo_usuario, ")
				AND rol.DESCRIPCION LIKE 'Viajero'
				AND viajero.activo IN (", mostrar_viajeros_activos ,")
			GROUP BY viajero.ID
			ORDER BY viajero.activo DESC;"
    );

    -- Paso 3: Preparar y ejecutar la consulta dinámica
    PREPARE stmt FROM @query_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

CALL crud_viajerosInicial('0, 1', 2, 1);
*/

DROP PROCEDURE IF EXISTS crud_viajeros;

DELIMITER $$
CREATE PROCEDURE crud_viajeros(
	IN mostrar_viajeros_activos VARCHAR(4), -- Los valores que deberian llegarle son '0' o '1' o '0, 1' o '1, 0'
    IN cod_usuario TEXT,
    IN cod_grupo_usuario TEXT,
    IN elemento_inicial INT,
    IN tamano_pagina INT,
    IN orden TEXT
)

BEGIN
    DECLARE query_sql TEXT;
    DECLARE disponibilidad_por_dia_transporte TEXT;

	SET mostrar_viajeros_activos = COALESCE(mostrar_viajeros_activos, '0');
	SET cod_usuario = COALESCE(cod_usuario, '\'\'');
	SET cod_grupo_usuario = COALESCE(cod_grupo_usuario, '\'\'');
	SET elemento_inicial = COALESCE(elemento_inicial, 1);
	SET tamano_pagina = COALESCE(tamano_pagina, 10);
	SET orden = COALESCE(orden, 'viajero.ID DESC');

    -- Paso 1: Construir dinámicamente las columnas de dias de transporte
	SELECT 
		GROUP_CONCAT(DISTINCT CONCAT(
				disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL, ' as id_dia_transporte_', REPLACE(dia_transporte_semanal.DESCRIPCION, ' ', '_'), ',
				MAX(CASE WHEN disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL = \'',
                disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL,
                '\' THEN disponibilidad_viajero.DISPONIBLE ELSE 0 END) 
                					AS \'Asiste a ',
                dia_transporte_semanal.DESCRIPCION,
                '\' '))
	INTO disponibilidad_por_dia_transporte FROM
		DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_viajero
			INNER JOIN DIA_TRANSPORTE_SEMANAL dia_transporte_semanal 
				ON dia_transporte_semanal.ID = disponibilidad_viajero.COD_DIA_TRANSPORTE_SEMANAL
		WHERE
			dia_transporte_semanal.ACTIVO = TRUE;

    -- Paso 2: Construir la consulta SQL completa
    SET @query_sql = CONCAT(
        "SELECT 
			total.total_registros,
			resultados.*
				FROM 
                (
					SELECT 
						viajero.id as 'Id',
						viajero.NOMBRE AS 'Nombre', 
						viajero.APELLIDOS AS 'Apellidos',
						viajero.NUMERO_PLAZAS as plazas_ocupadas,
						viajero.ACTIVO as activo,
						IF(ISNULL(viajero.COD_GRUPO_USUARIO), 0, 1) as usuario_compartido_grupo,
						IF(ISNULL(u.ALIAS), 0, u.ALIAS) as alias_usuario_propietario,
						u.ID as codigo_usuario_propietario,
						", disponibilidad_por_dia_transporte, "
					 FROM INVOLUCRADO viajero
						INNER JOIN ROL_INVOLUCRADO rol
							ON viajero.COD_ROL = rol.ID
						INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_viajero
							ON viajero.ID = disponibilidad_viajero.COD_INVOLUCRADO
						INNER JOIN USUARIO u
							ON viajero.COD_USUARIO_PROPIETARIO = u.ID
						WHERE 
							(viajero.COD_USUARIO_PROPIETARIO = ", cod_usuario, " OR viajero.COD_GRUPO_USUARIO = ", cod_grupo_usuario, ")
							AND rol.DESCRIPCION LIKE 'Viajero'
							AND viajero.activo IN (", mostrar_viajeros_activos ,")
						GROUP BY viajero.ID
						ORDER BY 
							", orden, ",
							CASE 
								WHEN codigo_usuario_propietario = ", cod_usuario ," THEN 0
								ELSE 1
							END, 
							viajero.activo DESC
						LIMIT ", elemento_inicial, ", ", tamano_pagina,
				") AS resultados
			CROSS JOIN 
                (
					 SELECT 
					COUNT(count_agrupado.total) AS total_registros
						FROM 
							(
								SELECT
									count(*) as total
										FROM INVOLUCRADO viajero
											INNER JOIN ROL_INVOLUCRADO rol
												ON viajero.COD_ROL = rol.ID
											INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_viajero
												ON viajero.ID = disponibilidad_viajero.COD_INVOLUCRADO
											INNER JOIN USUARIO u
												ON viajero.COD_USUARIO_PROPIETARIO = u.ID
										WHERE
											(viajero.COD_USUARIO_PROPIETARIO = ", cod_usuario, " OR viajero.COD_GRUPO_USUARIO = ", cod_grupo_usuario, ")
											AND rol.DESCRIPCION LIKE 'Viajero'
											AND viajero.activo IN (", mostrar_viajeros_activos ,")
										GROUP BY viajero.ID
							) as count_agrupado
				) AS total;"
    );
    
    SELECT @query_sql;
    
    -- Paso 3: Preparar y ejecutar la consulta dinámica
    PREPARE stmt FROM @query_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;
            
CALL crud_viajeros(1, 1, 1, null, null, null);

DELIMITER $$
CREATE PROCEDURE crud_conductores(
	IN mostrar_conductores_activos VARCHAR(4) -- Los valores que deberian llegarle son '0' o '1' o '0, 1' o '1, 0'
)
BEGIN
    DECLARE query_sql TEXT;
    DECLARE disponibilidad_por_dia_transporte TEXT;

    -- Paso 1: Construir dinámicamente las columnas de dias de transporte
    SELECT 
        GROUP_CONCAT(DISTINCT 
            CONCAT(
                "MAX(CASE WHEN disponibilidad_conductor.COD_DIA_TRANSPORTE_SEMANAL = '", disponibilidad_conductor.COD_DIA_TRANSPORTE_SEMANAL , "' THEN disponibilidad_conductor.DISPONIBLE ELSE 0 END) 
					AS 'Disponible para llevar a ", dia_transporte_semanal.DESCRIPCION , "'"
            )
        ) INTO disponibilidad_por_dia_transporte
    FROM 
        DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_conductor
			INNER JOIN DIA_TRANSPORTE_SEMANAL dia_transporte_semanal
				ON dia_transporte_semanal.ID = disponibilidad_conductor.COD_DIA_TRANSPORTE_SEMANAL
			WHERE dia_transporte_semanal.ACTIVO = TRUE;

    -- Paso 2: Construir la consulta SQL completa
    SET @query_sql = CONCAT(
        "SELECT 
			conductor.NOMBRE AS 'Nombre', 
            conductor.APELLIDOS AS 'Apellidos',
            conductor.NUMERO_PLAZAS as 'Plazas ofrecidas',
            ", disponibilidad_por_dia_transporte, "
         FROM INVOLUCRADO conductor
			INNER JOIN ROL_INVOLUCRADO rol
				ON conductor.COD_ROL = rol.ID
			INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad_conductor
				ON conductor.ID = disponibilidad_conductor.COD_INVOLUCRADO
			WHERE 
				rol.DESCRIPCION LIKE 'Conductor'
                AND conductor.activo IN (", mostrar_conductores_activos ,")
                GROUP BY conductor.ID;"
    );

    -- Paso 3: Preparar y ejecutar la consulta dinámica
    PREPARE stmt FROM @query_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

CALL crud_conductores('0, 1');

/* INVOLUCRADOS */
-- UPDATE INVOLUCRADO SET ACTIVO = FALSE WHERE ID = 3; -- Operación de borrado para el usuario
-- SELECT * FROM INVOLUCRADO WHERE ID = 3;

-- UPDATE INVOLUCRADO SET NUMERO_PLAZAS = 4 WHERE ID = 5;
-- SELECT ID, NOMBRE, APELLIDOS, NUMERO_PLAZAS, ACTIVO FROM INVOLUCRADO WHERE ID = 5;

-- UPDATE DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL SET DISPONIBLE = TRUE WHERE COD_INVOLUCRADO = 1 AND COD_DIA_TRANSPORTE_SEMANAL = 2;
-- SELECT involucrado.ID, involucrado.NOMBRE, involucrado.APELLIDOS, disponibilidad.DISPONIBLE, dia_transporte.DESCRIPCION, involucrado.ACTIVO FROM DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL disponibilidad INNER JOIN INVOLUCRADO involucrado ON disponibilidad.COD_INVOLUCRADO = involucrado.ID INNER JOIN DIA_TRANSPORTE_SEMANAL dia_transporte ON disponibilidad.COD_DIA_TRANSPORTE_SEMANAL = dia_transporte.ID WHERE disponibilidad.COD_INVOLUCRADO = 2;

-- El DELETE es el UPDATE de activo.

-- INSERT INTO INVOLUCRADO (NOMBRE, APELLIDOS, ACTIVO) VALUES ('Luisi y Jacinto', '', TRUE);
-- SELECT * FROM INVOLUCRADO;


/* DIA ESPECIAL */
-- DELETE FROM DIA_TRANSPORTE_ESPECIAL WHERE ID = 1;

/* TRANSPORTE */
-- Consulta para sacar el valor de cada transporte en el combo del viajero y la fecha correspondiente.
SELECT involucrado.NOMBRE 
	FROM TRANSPORTE transporte 
		INNER JOIN INVOLUCRADO_POR_TRANSPORTE involucrado_por_transporte 
			ON transporte.ID = involucrado_por_transporte.COD_TRANSPORTE 
		INNER JOIN INVOLUCRADO involucrado 
			ON involucrado_por_transporte.COD_INVOLUCRADO = involucrado.id 
		INNER JOIN ROL_INVOLUCRADO rol 
			ON involucrado.COD_ROL = rol.ID 
	WHERE 
    transporte.ID = 
		(SELECT transporte.id 
			FROM TRANSPORTE transporte 
				INNER JOIN INVOLUCRADO_POR_TRANSPORTE involucrado_por_transporte 
					ON transporte.ID = involucrado_por_transporte.COD_TRANSPORTE 
			WHERE 
				transporte.FECHA = '2025/06/08' 
                AND involucrado_por_transporte.COD_INVOLUCRADO = 4
		) 
	AND rol.DESCRIPCION = 'Conductor';
	
/* MOSTRAR DIAS ESPECIALES AL CREAR UNA PLANTILLA */
-- SELECT DESCRIPCION FROM DIA ESPECIAL;
-- INSERT INTO DIA_ESPECIAL (DESCRIPCION) VALUES ('')
-- DELETE FROM DIA_ESPECIAL WHERE ID = ?;
-- UPDATE DIA_ESPCEIAL SET DESCRIPCION = '' WHERE ID = ?


/**
	FUMADON DE PROCEDIMIENTO PARA SACAR LA PLANTILLA DE CUALQUIER PLANTILLA
delimiter $$
CREATE PROCEDURE GenerarReporteTransporte(
	IN id_plantilla TINYINT
)
BEGIN
	DECLARE query_sql TEXT;
    DECLARE columnas TEXT;
	DECLARE mes TEXT;
    DECLARE ano TEXT;
    
    SELECT * INTO id_plantilla, ano, mes
		FROM PLANTILLA 
			WHERE ID = id_plantilla;
            
    -- Paso 1: Generar dinámicamente las columnas con fechas
    SELECT GROUP_CONCAT(
		DISTINCT CONCAT(
			"MAX(CASE WHEN transporte.FECHA = '", FECHA , "' THEN conductor.NOMBRE END) AS '", FECHA, "'"
		)
	) INTO columnas
		FROM TRANSPORTE
			WHERE
				YEAR(FECHA) = ano
                AND MONTH(FECHA) = mes;

    -- Paso 2: Construir y ejecutar la consulta dinámica
    SET @query_sql = CONCAT(
        "SELECT viajero.nombre AS 'Viajero', "
				, columnas, " ",	
        "	FROM INVOLUCRADO_POR_TRANSPORTE involucrado_por_transporte ",
        "		JOIN TRANSPORTE transporte 
					ON involucrado_por_transporte.COD_TRANSPORTE = transporte.ID",
        "		JOIN ROL_POR_INVOLUCRADO rol_por_involucrado 
					ON involucrado_por_transporte.COD_INVOLUCRADO = rol_por_involucrado.COD_INVOLUCRADO",
		"		JOIN ROL_INVOLUCRADO rol_involucrado 
					ON rol_por_involucrado.COD_ROL = rol_involucrado.ID",
        "		JOIN involucrado viajero 
					ON involucrado_por_transporte.COD_INVOLUCRADO = viajero.ID
						AND rol_involucrado.DESCRIPCION = 'Viajero' ",
        "		JOIN involucrado_por_transporte involucrado_por_transporte_2 
					ON transporte.ID = involucrado_por_transporte_2.COD_TRANSPORTE ",
        "		JOIN rol_por_involucrado rol_por_involucrado_2 
					ON involucrado_por_transporte_2.COD_INVOLUCRADO = rol_por_involucrado_2.COD_INVOLUCRADO ",
		"		JOIN ROL_INVOLUCRADO rol_involucrado_2 
					ON rol_por_involucrado_2.COD_ROL = rol_involucrado_2.ID",
        "		JOIN involucrado conductor
					ON involucrado_por_transporte_2.COD_INVOLUCRADO = conductor.ID
						AND rol_involucrado_2.DESCRIPCION = 'Conductor' ",
        "	GROUP BY viajero.NOMBRE;"
    );

	SELECT @query_sql;
    
-- Ejecutar la consulta dinámica
    PREPARE stmt FROM @query_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;
            
SET group_concat_max_len = 15000;
Call GenerarReporteTransporte(1);
SET GLOBAL group_concat_max_len = 1024;
-- SELECT @@global.group_concat_max_len;
*/

/* SACAR LOS CONDUCTORES DE UNA PLANTILLA */
SELECT conductor.nombre, conductor.id
	FROM INVOLUCRADO_POR_PLANTILLA ipp 
		INNER JOIN INVOLUCRADO conductor 
			ON ipp.COD_INVOLUCRADO = conductor.ID 
		INNER JOIN ROL_INVOLUCRADO rol_conductor
			ON conductor.COD_ROL = rol_conductor.ID
            AND rol_conductor.DESCRIPCION = 'Conductor'
	WHERE 
		ipp.COD_PLANTILLA = 1
	ORDER BY conductor.nombre DESC;

/* SACAR LOS CONDUCTORES DE UNA PLANTILLA DISPONIBLES PARA UN DIA DE LA SEMANA EN CONCRETO */
SELECT conductor.nombre, conductor.id
	FROM INVOLUCRADO_POR_PLANTILLA ipp 
		INNER JOIN INVOLUCRADO conductor 
			ON ipp.COD_INVOLUCRADO = conductor.ID 
		INNER JOIN ROL_INVOLUCRADO rol_conductor
			ON conductor.COD_ROL = rol_conductor.ID
            AND rol_conductor.DESCRIPCION = 'Conductor'
		INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL didts
			ON didts.COD_INVOLUCRADO = conductor.ID
            AND didts.DISPONIBLE = TRUE
	WHERE 
		ipp.COD_PLANTILLA = 1
        AND didts.COD_DIA_TRANSPORTE_SEMANAL = 1 -- VIDA Y MINISTERIO
        
	ORDER BY conductor.nombre DESC;
    
-- SELECT * FROM DIA_TRANSPORTE_SEMANAL;
SELECT * FROM DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL WHERE COD_INVOLUCRADO = 23;

-- Consulta que saca los conductores con plazas disponibles por día en el hueco de quien lleva al viajero en la plantilla.
DROP PROCEDURE IF EXISTS obtener_conductores_disponibles_para_dia;

DELIMITER $$

CREATE PROCEDURE obtener_conductores_disponibles_para_dia (
    IN fecha_transporte DATE,  -- La fecha del día de transporte
    IN plantilla_id INT         -- El ID de la plantilla
)
BEGIN

	DECLARE es_dia_especial INT;
    DECLARE dia_de_la_semana INT;
    
    -- Tabla temporal para almacenar los resultados de los conductores disponibles
    DROP TEMPORARY TABLE IF EXISTS conductores_disponibles_por_dia_seleccionado;
    CREATE TEMPORARY TABLE IF NOT EXISTS conductores_disponibles_por_dia_seleccionado (
        id_conductor INT,
        nombre_conductor VARCHAR(255),
        apellidos_conductor VARCHAR(255),
        plazas_disponibles INT,
        cod_rol TINYINT UNSIGNED,
        
        CONSTRAINT PRIMARY KEY pk_id_conductor (id_conductor)
    );
    
	-- consultar si la fecha existe en la tabla día especial
   --  SELECT COUNT(dte.ID) as 'select count' FROM DIA_TRANSPORTE_ESPECIAL dte WHERE dte.FECHA = fecha_transporte; -- DEBUG
        
    SELECT COUNT(dte.ID) INTO es_dia_especial
		FROM DIA_TRANSPORTE_ESPECIAL dte
		WHERE dte.FECHA = fecha_transporte;

    IF es_dia_especial > 0 THEN
		-- SELECT 'es dia especial'; -- DEBUG
        -- 	consultar los conductores disponibles para ese día y sus plazas incluyendo ausencias
        INSERT INTO conductores_disponibles_por_dia_seleccionado (id_conductor, nombre_conductor, apellidos_conductor, plazas_disponibles, cod_rol)
        SELECT 
			conductor.id,
			conductor.nombre,
			conductor.apellidos,
			conductor.numero_plazas,
            conductor.cod_rol
				FROM DIA_TRANSPORTE_ESPECIAL dte
					INNER JOIN disponibilidad_involucrado_por_dia_transporte_especial dcdte
						ON dte.ID = dcdte.COD_DIA_TRANSPORTE_ESPECIAL
					INNER JOIN INVOLUCRADO conductor
						ON dcdte.COD_INVOLUCRADO = conductor.ID
					INNER JOIN ROL_INVOLUCRADO rol_conductor
						ON conductor.COD_ROL = rol_conductor.ID
					INNER JOIN INVOLUCRADO_POR_PLANTILLA ipp
						ON conductor.ID = ipp.COD_INVOLUCRADO
					LEFT JOIN AUSENCIA_POR_INVOLUCRADO ac
						ON conductor.ID = ac.COD_INVOLUCRADO
						AND fecha_transporte NOT BETWEEN ac.FECHA_INICIO AND ac.FECHA_FIN
				WHERE 
					dte.FECHA = fecha_transporte
					AND ac.ID IS NULL
					AND rol_conductor.DESCRIPCION = 'Conductor'
				GROUP BY
					conductor.ID;
    ELSE 
		-- SELECT 'es dia semanal'; -- DEBUG
		-- Es un dia de la semana der reunión normal y corriente
		SET dia_de_la_semana = CASE 
			WHEN DAYOFWEEK(fecha_transporte) = 1 THEN 7  -- Sunday becomes 7
			ELSE DAYOFWEEK(fecha_transporte) - 1        -- Other days are shifted by -1
		 END;
		 
         SELECT dia_de_la_semana;
         
		 SELECT 
			dts.ID AS dia_transporte_semanal,
            dts.DESCRIPCION
				FROM DIA_TRANSPORTE_SEMANAL dts
				WHERE dts.COD_DIA_DE_LA_SEMANA = dia_de_la_semana;
			
		-- 	consultar los conductores disponibles para ese día y sus plazas incluyendo ausencias
		INSERT INTO conductores_disponibles_por_dia_seleccionado 
			(id_conductor, nombre_conductor, apellidos_conductor, plazas_disponibles, cod_rol)
        SELECT DISTINCT
			conductor.id,
			conductor.nombre,
			conductor.apellidos,
			conductor.numero_plazas,
            conductor.cod_rol
				FROM DIA_TRANSPORTE_SEMANAL dts
					INNER JOIN dispon_invol_por_dia_transporte_semanal_por_plantilla dcdtspp
						ON dts.ID = dcdtspp.COD_DIA_TRANSPORTE_SEMANAL
					INNER JOIN INVOLUCRADO conductor
						ON dcdtspp.COD_INVOLUCRADO = conductor.ID
					INNER JOIN ROL_INVOLUCRADO rol_conductor
						ON conductor.cod_rol = rol_conductor.ID
					INNER JOIN INVOLUCRADO_POR_PLANTILLA ipp
						ON conductor.ID = ipp.COD_INVOLUCRADO
					LEFT JOIN AUSENCIA_POR_INVOLUCRADO ac
						ON conductor.ID = ac.COD_INVOLUCRADO
						AND fecha_transporte NOT BETWEEN ac.FECHA_INICIO AND ac.FECHA_FIN
				WHERE 
					dts.COD_DIA_DE_LA_SEMANA = dia_de_la_semana
					AND dcdtspp.COD_PLANTILLA = plantilla_id
					AND ac.ID IS NULL
					AND rol_conductor.DESCRIPCION = 'Conductor'
				GROUP BY
					conductor.ID;
	END IF;
    
    -- select 'a'; -- DEBUG
    -- SELECT * FROM conductores_disponibles_por_dia_seleccionado; -- DEBUG
    
    -- consultar cuántas plazas tiene ocupadas ya y restárselas a las que tenga sumadas a las que ocupa el viajero
    DROP TEMPORARY TABLE IF EXISTS conductores_disponibles_por_dia_seleccionado_clon;
    CREATE TEMPORARY TABLE IF NOT EXISTS conductores_disponibles_por_dia_seleccionado_clon (
        id_conductor INT,
        nombre_conductor VARCHAR(255),
        apellidos_conductor VARCHAR(255),
        plazas_disponibles INT,
        cod_rol TINYINT UNSIGNED,
        
        CONSTRAINT PRIMARY KEY pk_id_conductor (id_conductor)
    );
    
    INSERT INTO conductores_disponibles_por_dia_seleccionado_clon 
		(SELECT * FROM conductores_disponibles_por_dia_seleccionado);
    
    UPDATE conductores_disponibles_por_dia_seleccionado AS conductores
		JOIN (
			-- Subconsulta que devuelve los valores de id y numero de otras tablas
            SELECT 
				c.id_conductor AS id_conductor,
				c.nombre_conductor,
				c.apellidos_conductor,
                c.cod_rol,
				IFNULL(SUM(v.numero_plazas), 0) AS plazas_ocupadas
					FROM 
						transporte t
					JOIN involucrado_por_transporte ipt 
						ON t.id = ipt.cod_transporte
					JOIN conductores_disponibles_por_dia_seleccionado_clon c 
						ON ipt.cod_involucrado = c.id_conductor
					JOIN rol_involucrado rc 
						ON c.cod_rol = rc.id
					LEFT JOIN involucrado_por_transporte ipt_viajeros 
						ON ipt.cod_transporte = ipt_viajeros.cod_transporte
					LEFT JOIN involucrado v 
						ON ipt_viajeros.cod_involucrado = v.id 
						AND v.cod_rol = (SELECT id FROM rol_involucrado WHERE descripcion = 'viajero')
				WHERE 
					rc.descripcion = 'conductor' 
					AND t.fecha = fecha_transporte
				GROUP BY 
					c.id_conductor
		) AS subconsulta
			ON conductores.id_conductor = subconsulta.id_conductor
			SET conductores.plazas_disponibles = conductores.plazas_disponibles - subconsulta.plazas_ocupadas;
            
    -- Hago esto para evitar el safe mode
    TRUNCATE TABLE conductores_disponibles_por_dia_seleccionado_clon;
    INSERT INTO conductores_disponibles_por_dia_seleccionado_clon
		(SELECT * FROM conductores_disponibles_por_dia_seleccionado WHERE plazas_disponibles != 0);
        
	-- select 'b'; -- DEBUG
    SELECT * FROM conductores_disponibles_por_dia_seleccionado_clon;

    DROP TEMPORARY TABLE IF EXISTS conductores_disponibles_por_dia_seleccionado;
	DROP TEMPORARY TABLE IF EXISTS conductores_disponibles_por_dia_seleccionado_clon;
END $$

DELIMITER ;

-- call obtener_conductores_disponibles_para_dia('2025-05-13', 1);
-- call obtener_conductores_disponibles_para_dia('2024-05-10', 1);