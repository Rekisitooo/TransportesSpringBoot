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
	(2, 'Lunes'), 
    (3, 'Martes'),
    (4, 'Miércoles'),
    (5, 'Jueves'),
    (6, 'Viernes'),
    (7, 'Sábado'),
    (1, 'Domingo');

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
	('Vida y Ministerio', 3, TRUE, 1, 1)
    ,('Reunión Pública', 1, TRUE, 1, 1)
    -- ,('Vida y Ministerio', 6, FALSE, 1, 1)
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

-- Se rellena al crear la plantilla y se va cambiando según se va haciendo la plantilla
DROP TABLE IF EXISTS FECHA_TRANSPORTE_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS FECHA_TRANSPORTE_POR_PLANTILLA (
	ID INT UNSIGNED,
    COD_PLANTILLA INT UNSIGNED NOT NULL,
    FECHA_TRANSPORTE DATE NOT NULL,
    COD_DIA_DE_LA_SEMANA ENUM('1', '2', '3', '4', '5', '6', '7') NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_DIAS_TRANSPORTE_POR_PLANTILLA (ID),
	CONSTRAINT FOREIGN KEY FK_COD_DIA_DE_LA_SEMANA (COD_DIA_DE_LA_SEMANA) REFERENCES DIA_DE_LA_SEMANA (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA(COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

/* 
 * Tabla que sirve para el historico de plantillas. Por ejemplo, puede ser que ya no estén algunos conductores o viajeros que 
 * estaban antes. Si se quiere consultar una plantilla de hace un ano, esta tabla almacena los involucrados que habia para esa plantilla.
 * Se rellena al crear la plantilla con los involucrados que haya activos en la tabla INVOLUCRADO en ese momento. 
 * 
 * El campo PLAZAS sirver para guardar cuantas plazas ocupaba u ofrecia el involucrado en ese momento.
*/
DROP TABLE IF EXISTS INVOLUCRADO_POR_PLANTILLA;
CREATE TABLE IF NOT EXISTS INVOLUCRADO_POR_PLANTILLA (
    COD_INVOLUCRADO INT UNSIGNED,
    COD_PLANTILLA INT UNSIGNED,
    PLAZAS CHAR(2) NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_INVOLUCRADO_POR_PLANTILLA (ID_INVOLUCRADO, COD_PLANTILLA),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO (COD_INVOLUCRADO) REFERENCES INVOLUCRADO (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_PLANTILLA_POR_PLANTILLA (COD_PLANTILLA) REFERENCES PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- SELECT * FROM INVOLUCRADO_POR_PLANTILLA;

-- Se rellena al crear la plantilla
DROP TABLE IF EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE;
CREATE TABLE IF NOT EXISTS DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE (
	COD_INVOLUCRADO INT UNSIGNED,
    COD_FECHA_TRANSPORTE INT UNSIGNED,
    
    CONSTRAINT PRIMARY KEY PK_INVOLUCRADO_POR_DIA_TRANSPORTE_POR_PLANTILLA (COD_INVOLUCRADO, COD_FECHA_TRANSPORTE),
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_INVOLUCRADO) REFERENCES INVOLUCRADO_POR_PLANTILLA (ID_INVOLUCRADO) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_FECHA_TRANSPORTE(COD_FECHA_TRANSPORTE) REFERENCES FECHA_TRANSPORTE_POR_PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Se rellena al crear la plantilla y se va cambiando según se va haciendo la plantilla
DROP TABLE IF EXISTS PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE;
CREATE TABLE IF NOT EXISTS PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE (
	COD_CONDUCTOR INT UNSIGNED NOT NULL,
    COD_FECHA_TRANSPORTE INT UNSIGNED NOT NULL,
    PLAZAS_DISPONIBLES	VARCHAR(2) NOT NULL,
    PLAZAS_OCUPADAS	VARCHAR(2) NOT NULL DEFAULT 0,
    
    CONSTRAINT PRIMARY KEY PK_PLAZAS_DISPONIBLES_INVOLUCRADO_POR_PLANTILLA (COD_CONDUCTOR, COD_FECHA_TRANSPORTE),
    CONSTRAINT FOREIGN KEY FK_COD_FECHA_TRANSPORTE (COD_FECHA_TRANSPORTE) REFERENCES FECHA_TRANSPORTE_POR_PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_DIA_TRANSPORTE (COD_CONDUCTOR) REFERENCES INVOLUCRADO_POR_PLANTILLA (ID_INVOLUCRADO) ON DELETE CASCADE ON UPDATE CASCADE
);
/*
SELECT conductor.id, conductor.plazas, dia_del_mes.dia_de_la_semana
				FROM (
					SELECT i.ID as id, i.NUMERO_PLAZAS as plazas
						FROM INVOLUCRADO i
							INNER JOIN ROL_INVOLUCRADO ri
								ON i.COD_ROL = ri.ID
						WHERE 
							ri.DESCRIPCION = 'Conductor'
							AND i.ACTIVO = 1
					) conductor
				CROSS JOIN
					(
						SELECT 
							DATE_FORMAT(fecha, '%d/%m/%Y') AS dia_de_la_semana
							FROM (
								SELECT CONCAT(NEW.ANNO, '-', NEW.MES, '-', 1) + INTERVAL (n.n + 10 * m.n) DAY AS fecha
									FROM (
										SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3
										UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
										UNION ALL SELECT 8 UNION ALL SELECT 9
									) n,
									(
										SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3
										UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
										UNION ALL SELECT 8 UNION ALL SELECT 9
									) m
								) as fechas
								WHERE 
									MONTH(fecha) = NEW.MES
									AND YEAR(fecha) = NEW.ANNO
									AND DAYOFWEEK(fecha) = dia_transporte_semanal
				) dia_del_mes;
  */              
DELIMITER //

CREATE TRIGGER after_insert_plantilla
AFTER INSERT ON PLANTILLA
FOR EACH ROW
BEGIN

	-- Variables para el cursor de insertar fechas de transporte de la plantilla
    DECLARE cursor_ft_terminado INT DEFAULT FALSE;
    DECLARE cod_dia_de_la_semana INT;

    DECLARE cursor_insertar_en_fecha_transporte_por_plantilla CURSOR FOR
        SELECT 
            dts.COD_DIA_DE_LA_SEMANA
			FROM DIA_TRANSPORTE_SEMANAL dts
				WHERE dts.ACTIVO = 1;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET cursor_ft_terminado = TRUE;

	-- Inserta las fechas de transporte para la plantilla
    OPEN cursor_insertar_en_fecha_transporte_por_plantilla;    
		read_loop: LOOP
			FETCH cursor_insertar_en_fecha_transporte_por_plantilla INTO cod_dia_de_la_semana;

			IF cursor_ft_terminado THEN
				LEAVE read_loop;
			END IF;

			INSERT INTO FECHA_TRANSPORTE_POR_PLANTILLA (FECHA_TRANSPORTE, COD_DIA_DE_LA_SEMANA, COD_PLANTILLA)
			SELECT dias_del_mes.dia_de_la_semana, cod_dia_de_la_semana, NEW.ID
				FROM (
					SELECT 
						DATE_FORMAT(fecha, '%d/%m/%Y') AS dia_de_la_semana
						FROM (
							SELECT CONCAT(NEW.ANNO, '-', NEW.MES, '-', 1) + INTERVAL (n.n + 10 * m.n) DAY AS fecha
								FROM (
								SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3
									UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
									UNION ALL SELECT 8 UNION ALL SELECT 9
								) n,
								(
									SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3
									UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
									UNION ALL SELECT 8 UNION ALL SELECT 9
								) m
							) as fechas
							WHERE 
								MONTH(fecha) = NEW.MES
								AND YEAR(fecha) = NEW.ANNO
								AND DAYOFWEEK(fecha) = cod_dia_de_la_semana
				) dias_del_mes;
            
		END LOOP;
    CLOSE cursor_insertar_en_fecha_transporte_por_plantilla;
    
    -- Inserta los involucrados activos que haya en ese momento en la tabla INVOLUCRADO
    INSERT INTO INVOLUCRADO_POR_PLANTILLA (COD_INVOLUCRADO, COD_PLANTILLA, PLAZAS)
    SELECT inv.ID, NEW.ID, inv.NUMERO_PLAZAS
		FROM INVOLUCRADO inv
			WHERE inv.ACTIVO = 1;
    
    -- Inserta los involucrados activos con la disponibilidad que tengan por cada dia de transporte en la plantilla actual.
    INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE 
		(COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL, DISPONIBLE, COD_PLANTILLA)
	SELECT i.ID, dtspp.ID, NEW.ID
		FROM INVOLUCRADO_POR_PLANTILLA ipp
			INNER JOIN INVOLUCRADO i
				ON ipp.COD_INVOLUCRADO = i.ID
			INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL dipdts
				ON dipdts.COD_INVOLUCRADO = i.ID
			INNER JOIN DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA dtspp
				ON dipdts.COD_DIA_TRANSPORTE_SEMANAL = dtspp.ID;
    
    -- PLazas de conductor disponibles
    
END //

DELIMITER ;

DROP TABLE IF EXISTS INVOLUCRADO_POR_TRANSPORTE;
CREATE TABLE IF NOT EXISTS INVOLUCRADO_POR_TRANSPORTE (
	COD_TRANSPORTE INT UNSIGNED,
    COD_INVOLUCRADO_POR_PLANTILLA INT UNSIGNED NOT NULL,
    
    CONSTRAINT PRIMARY KEY PK_ID_INVOLUCRADO_POR_TRANSPORTE (COD_TRANSPORTE, COD_INVOLUCRADO_POR_PLANTILLA),
    CONSTRAINT FOREIGN KEY FK_COD_TRANSPORTE_INVOLUCRADO_POR_TRANSPORTE (COD_TRANSPORTE) REFERENCES FECHA_TRANSPORTE_POR_PLANTILLA (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY FK_COD_INVOLUCRADO_POR_TRANSPORTE (COD_INVOLUCRADO_POR_PLANTILLA) REFERENCES INVOLUCRADO_POR_PLANTILLA (ID_INVOLUCRADO) ON DELETE CASCADE ON UPDATE CASCADE
);

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

DROP TRIGGER IF EXISTS CHECK_FECHAS_INSERT_AUSENCIA_POR_INVOLUCRADO;
delimiter $$
CREATE TRIGGER CHECK_FECHAS_INSERT_AUSENCIA_POR_INVOLUCRADO BEFORE INSERT ON AUSENCIA_POR_INVOLUCRADO
FOR EACH ROW
BEGIN
    IF NEW.FECHA_INICIO >= NEW.FECHA_FIN THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de inicio debe ser menor que la fecha de fin de la ausencia .';
    END IF;
END; $$

delimiter ;

DROP TRIGGER IF EXISTS CHECK_FECHAS_UPDATE_AUSENCIA_POR_INVOLUCRADO;
delimiter $$
CREATE TRIGGER CHECK_FECHAS_UPDATE_AUSENCIA_POR_INVOLUCRADO BEFORE UPDATE ON AUSENCIA_POR_INVOLUCRADO
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

INSERT INTO PLANTILLA (NOMBRE, ANNO, MES, COD_USUARIO_PROPIETARIO, COD_GRUPO_USUARIO)
	VALUES 
		('mayo_2025', '2025', 5, 1, 1),
        ('junio_2025','2025', 6, 1, 1);
        
-- SELECT * FROM PLANTILLA;

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
-- SELECT * FROM DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL WHERE COD_INVOLUCRADO = 23;

-- REVISAR
/*
INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE (COD_INVOLUCRADO, COD_FECHA_TRANSPORTE)
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
*/

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

/*
-- REVISAR
INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE (COD_INVOLUCRADO, COD_DIA_TRANSPORTE_SEMANAL, COD_PLANTILLA)
VALUES 
	(1, 1, 1),
    (1, 2, 1),
    (2, 1, 1),
    (2, 2, 1),
    (3, 1, 1),
    (3, 2, 1),
    (4, 1, 1),
    (4, 2, 1),
    (5, 1, 1),
    (5, 2, 1),
    (6, 1, 1),
    (6, 2, 1),
    (7, 1, 1),
    (7, 2, 1),
    (8, 1, 1),
    (8, 2, 1),
    (9, 1, 1),
    (9, 2, 1),
    (10, 1, 1),
    (10, 2, 1),
    (11, 1, 1),
    (11, 2, 1),
    (12, 1, 1),
    (12, 2, 1),
    (13, 1, 1),
    (13, 2, 1),
    (14, 1, 1),
    (14, 2, 1),
    (15, 1, 1),
    (15, 2, 1),
    (16, 1, 1),
    (16, 2, 1),
    (17, 1, 1),
    (17, 2, 1),
    (18, 1, 1),
    (18, 2, 1),
    (19, 1, 1),
    (19, 2, 1),
    (20, 1, 1),
    (20, 2, 1),
    (21, 1, 1),
    (21, 2, 1),
    (22, 1, 1),
    (22, 2, 1),
    (23, 1, 1),
    (23, 2, 1),
    (1, 1, 2),
    (1, 2, 2),
    (2, 1, 2),
    (2, 2, 2),
    (3, 1, 2),
    (3, 2, 2),
    (4, 1, 2),
    (4, 2, 2),
    (5, 1, 2),
    (5, 2, 2),
    (6, 1, 2),
    (6, 2, 2),
    (7, 1, 2),
    (7, 2, 2),
    (8, 1, 2),
    (8, 2, 2),
    (9, 1, 2),
    (9, 2, 2),
    (10, 1, 2),
    (10, 2, 2),
    (11, 1, 2),
    (11, 2, 2),
    (12, 1, 2),
    (12, 2, 2),
    (13, 1, 2),
    (13, 2, 2),
    (14, 1, 2),
    (14, 2, 2),
    (15, 1, 2),
    (15, 2, 2),
    (16, 1, 2),
    (16, 2, 2),
    (17, 1, 2),
    (17, 2, 2),
    (18, 1, 2),
    (18, 2, 2),
    (19, 1, 2),
    (19, 2, 2),
    (20, 1, 2),
    (20, 2, 2),
    (21, 1, 2),
    (21, 2, 2),
    (22, 1, 2),
    (22, 2, 2),
    (23, 1, 2),
    (23, 2, 2);
    
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
        
INSERT INTO DIA_TRANSPORTE_SEMANAL_POR_PLANTILLA (COD_PLANTILLA, COD_DIA_TRANSPORTE_SEMANAL)
	VALUES
		(1, 1),
        (1, 2),
        (2, 1),
        (2, 3);
*/