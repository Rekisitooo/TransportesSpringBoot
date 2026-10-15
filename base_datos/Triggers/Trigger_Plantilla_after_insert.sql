DROP TRIGGER IF EXISTS after_insert_plantilla;
DELIMITER //
CREATE TRIGGER after_insert_plantilla
AFTER INSERT ON PLANTILLA
FOR EACH ROW
BEGIN

	-- Variables para el cursor de insertar fechas de transporte de la plantilla
    DECLARE cursor_ft_terminado INT DEFAULT FALSE;
    DECLARE cod_dia_de_la_semana INT;

    DECLARE cursor_insertar_en_FECHA_TRANSPORTE_POR_PLANTILLA CURSOR FOR
        SELECT 
            dts.COD_DIA_DE_LA_SEMANA
			FROM DIA_TRANSPORTE_SEMANAL dts
				WHERE dts.ACTIVO = 1;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET cursor_ft_terminado = 1;    
	
    
	-- Inserta las fechas de transporte para la plantilla
    OPEN cursor_insertar_en_FECHA_TRANSPORTE_POR_PLANTILLA;    
		read_loop: LOOP
			FETCH cursor_insertar_en_FECHA_TRANSPORTE_POR_PLANTILLA INTO cod_dia_de_la_semana;

			IF cursor_ft_terminado THEN
				LEAVE read_loop;
			END IF;
            
			INSERT INTO FECHA_TRANSPORTE_POR_PLANTILLA (FECHA_TRANSPORTE, COD_DIA_DE_LA_SEMANA, COD_PLANTILLA, NOMBRE_EVENTO)
			SELECT dias_del_mes.dia_de_la_semana, cod_dia_de_la_semana, NEW.ID, dts.DESCRIPCION
				FROM (
					SELECT 
						DATE_FORMAT(fecha, '%Y-%m-%d') AS dia_de_la_semana,
                        cod_dia_de_la_semana
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
				) dias_del_mes
                INNER JOIN DIA_TRANSPORTE_SEMANAL dts
					ON dts.COD_DIA_DE_LA_SEMANA = dias_del_mes.cod_dia_de_la_semana;
            
		END LOOP;
    CLOSE cursor_insertar_en_FECHA_TRANSPORTE_POR_PLANTILLA;
    
    -- Inserta los involucrados activos que haya en ese momento en la tabla INVOLUCRADO
    INSERT INTO INVOLUCRADO_POR_PLANTILLA (COD_INVOLUCRADO, COD_PLANTILLA, NOMBRE, APELLIDOS, PLAZAS, COD_ROL, GRUPO_MINISTERIO, COD_COLOR)	
    SELECT inv.ID, NEW.ID, inv.NOMBRE, inv.APELLIDOS, inv.NUMERO_PLAZAS, inv.COD_ROL, inv.COD_GRUPO_MINISTERIO, inv.COD_COLOR
		FROM INVOLUCRADO inv
			WHERE inv.ACTIVO = 1;
   
	CALL procedure_insert_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE(NEW.ID);
    -- CALL procedure_insert_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE(NEW.ID);
END //
DELIMITER ;


DROP PROCEDURE IF EXISTS procedure_insert_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE;
DELIMITER $$
CREATE PROCEDURE procedure_insert_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE(
    IN id_plantilla INT
)
BEGIN
    
    -- Variables para el cursor de insertar la disponibilidad de cada involucrado por cada fecha de transporte de la plantilla
    DECLARE cursor_dipft_terminado INT DEFAULT FALSE;
    DECLARE cod_fecha_transporte INT;
    DECLARE fecha_transporte TEXT;

	DECLARE cursor_insertar_en_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE CURSOR FOR
        SELECT ftpp.ID, ftpp.FECHA_TRANSPORTE
			FROM FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				WHERE ftpp.COD_PLANTILLA = id_plantilla;
                
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET cursor_dipft_terminado = 1;

	-- Inserta la dipsonibilidad de cada involucrado para cada fecha de transporte de la plantilla creada
    OPEN cursor_insertar_en_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE;
		read_loop: LOOP
			FETCH cursor_insertar_en_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE 
            INTO cod_fecha_transporte, fecha_transporte;

			IF cursor_dipft_terminado THEN
				LEAVE read_loop;
			END IF;
          
          INSERT INTO DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE (COD_INVOLUCRADO, COD_FECHA_TRANSPORTE, NECESITA_TRANSPORTE)
				SELECT DISTINCT
					ipp.COD_INVOLUCRADO, 
					cod_fecha_transporte as COD_FECHA_TRANSPORTE,
                    1 as NECESITA_TRANSPORTE
						FROM INVOLUCRADO_POR_PLANTILLA ipp
							LEFT JOIN AUSENCIA_POR_INVOLUCRADO api
								ON ipp.COD_INVOLUCRADO = api.COD_INVOLUCRADO
								AND fecha_transporte BETWEEN api.FECHA_INICIO AND api.FECHA_FIN
							INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_DIA_TRANSPORTE_SEMANAL dipdts
								ON dipdts.COD_INVOLUCRADO = ipp.COD_INVOLUCRADO
						WHERE 
							dipdts.DISPONIBLE = TRUE
							AND ipp.COD_PLANTILLA = id_plantilla
							AND api.COD_INVOLUCRADO IS NULL;
            
		END LOOP;
    CLOSE cursor_insertar_en_DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE;
    
END $$
DELIMITER ;


/**
*/
DROP PROCEDURE IF EXISTS procedure_insert_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE;
DELIMITER $$
CREATE PROCEDURE procedure_insert_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE(
	IN id_plantilla INT
)
BEGIN
    
    -- Variables para el cursor de insertar las plazas ocupadas de cada conductor por cada fecha de transporte de la plantilla
    DECLARE cursor_pocpft_terminado INT DEFAULT FALSE;
    DECLARE cod_fecha_transporte_pocpft INT;
    DECLARE cod_involucrado INT;
    
	DECLARE cursor_insertar_en_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE CURSOR FOR
        SELECT 
			dipft.COD_INVOLUCRADO as cod_involucrado,
            ftpp.ID as cod_fecha_transporte_pocpft
			FROM DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE dipft
				INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
					ON dipft.COD_FECHA_TRANSPORTE = ftpp.ID
				WHERE ftpp.ID = id_plantilla;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET cursor_pocpft_terminado = 1;
    
    -- Inserta la dipsonibilidad de cada involucrado para cada fecha de transporte de la plantilla creada
    OPEN cursor_insertar_en_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE;
		read_loop: LOOP
			FETCH cursor_insertar_en_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE INTO cod_involucrado, cod_fecha_transporte_pocpft;

			IF cursor_pocpft_terminado THEN
				LEAVE read_loop;
			END IF;

			-- Si el involucrado que se ha insertado es un conductor, inserta las plazas ocupadas
			IF EXISTS (
				SELECT * 
					FROM INVOLUCRADO_POR_PLANTILLA ipp
						INNER JOIN ROL_INVOLUCRADO roli
							ON ipp.COD_ROL = roli.ID
						INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
							ON ftpp.COD_PLANTILLA = ipp.COD_PLANTILLA
						WHERE
							roli.DESCRIPCION = 'Conductor' 
							AND ipp.COD_INVOLUCRADO = cod_involucrado
							AND ftpp.ID = cod_fecha_transporte_pocpft
			) THEN
			
				INSERT INTO PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE 
						(COD_CONDUCTOR, COD_FECHA_TRANSPORTE, PLAZAS_DISPONIBLES, PLAZAS_OCUPADAS)
					SELECT cod_involucrado, cod_fecha_transporte_pocpft, ipp.PLAZAS, 0
						FROM INVOLUCRADO_POR_PLANTILLA ipp
							INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
								ON ftpp.COD_PLANTILLA = ipp.COD_PLANTILLA
						WHERE 
							ipp.COD_INVOLUCRADO = cod_involucrado
							AND ftpp.ID = cod_fecha_transporte_pocpft;
			END IF;
            
		END LOOP;
    CLOSE cursor_insertar_en_PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE;
    
END $$
DELIMITER ;