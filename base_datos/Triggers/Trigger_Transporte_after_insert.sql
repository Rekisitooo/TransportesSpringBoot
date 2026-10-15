DROP TRIGGER IF EXISTS after_insert_TRANSPORTE;
delimiter $$
CREATE TRIGGER after_insert_TRANSPORTE
	AFTER INSERT ON TRANSPORTE
FOR EACH ROW
BEGIN

	DECLARE numero_plazas_viajero INT;
    DECLARE numero_plazas_conductor_disponibles INT;
	DECLARE nombre_viajero TEXT;
    DECLARE nombre_conductor TEXT;
    
    SELECT ipp.PLAZAS
		FROM INVOLUCRADO_POR_PLANTILLA ipp
			INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				ON ipp.COD_PLANTILLA = ftpp.COD_PLANTILLA
			INNER JOIN TRANSPORTE t
				ON ftpp.ID = t.COD_FECHA_TRANSPORTE
			WHERE 
				ipp.COD_INVOLUCRADO = NEW.COD_VIAJERO
                AND t.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE
			LIMIT 1
					INTO numero_plazas_viajero;
    
	SELECT pocpft.PLAZAS_DISPONIBLES
		FROM PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE pocpft
			INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				ON pocpft.COD_FECHA_TRANSPORTE = ftpp.ID
			WHERE 
				pocpft.COD_CONDUCTOR = NEW.COD_CONDUCTOR
                AND ftpp.ID = NEW.COD_FECHA_TRANSPORTE
					INTO numero_plazas_conductor_disponibles;
            
	IF numero_plazas_viajero > numero_plazas_conductor_disponibles THEN
		SELECT concat(ipp.NOMBRE, ' ' , ipp.APELLIDOS)
			FROM INVOLUCRADO_POR_PLANTILLA ipp
				INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
					ON ipp.COD_PLANTILLA = ftpp.COD_PLANTILLA
				INNER JOIN TRANSPORTE t
					ON ftpp.ID = t.COD_FECHA_TRANSPORTE
				WHERE 
					ipp.COD_INVOLUCRADO = NEW.COD_VIAJERO
                    AND t.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE
						INTO nombre_viajero;
                    
		SELECT concat(ipp.NOMBRE, ' ' , ipp.APELLIDOS)
			FROM INVOLUCRADO_POR_PLANTILLA ipp
				INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
					ON ipp.COD_PLANTILLA = ftpp.COD_PLANTILLA
				INNER JOIN TRANSPORTE t
					ON ftpp.ID = t.COD_FECHA_TRANSPORTE
				WHERE 
					ipp.COD_INVOLUCRADO = NEW.COD_CONDUCTOR
                    AND t.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE
						INTO nombre_conductor;
                    
        SET @errorMsg = CONCAT(nombre_conductor, ' no dispone de plazas suficientes para llevar a ', nombre_viajero, '.');
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = @errorMsg;
        
	ELSE 
		UPDATE PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE pocpft
			SET pocpft.PLAZAS_OCUPADAS = pocpft.PLAZAS_OCUPADAS + numero_plazas_viajero
				WHERE 
					pocpft.COD_CONDUCTOR = NEW.COD_CONDUCTOR
                    AND pocpft.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE;
    END IF;
            
END; $$

delimiter ;
