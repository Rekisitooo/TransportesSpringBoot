DROP TRIGGER IF EXISTS after_update_TRANSPORTE;
DELIMITER //
CREATE TRIGGER after_update_TRANSPORTE
	AFTER UPDATE ON TRANSPORTE
FOR EACH ROW
BEGIN

	DECLARE plazas_ocupadas_por_nuevo_viajero INT;

	SELECT ipp.PLAZAS as plazas_ocupadas_por_nuevo_viajero
		FROM INVOLUCRADO_POR_PLANTILLA ipp
			INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				ON ipp.COD_PLANTILLA = ftpp.COD_PLANTILLA
		WHERE 
			ipp.COD_INVOLUCRADO = NEW.COD_VIAJERO
            AND ftpp.ID = NEW.COD_FECHA_TRANSPORTE
				INTO plazas_ocupadas_por_nuevo_viajero;
                
	-- añade las plazas que se quedan libres al anterior conductor
	UPDATE PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE pocpft
		SET pocpft.PLAZAS_OCUPADAS = (pocpft.PLAZAS_OCUPADAS - plazas_ocupadas_por_nuevo_viajero)
			WHERE 
				pocpft.COD_CONDUCTOR = OLD.COD_CONDUCTOR
                AND pocpft.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE;
                
	-- quita las plazas que se ocupan al nuevo conductor
	UPDATE PLAZAS_OCUPADAS_CONDUCTOR_POR_FECHA_TRANSPORTE pocpft
		SET pocpft.PLAZAS_OCUPADAS = (pocpft.PLAZAS_OCUPADAS + plazas_ocupadas_por_nuevo_viajero)
			WHERE 
				pocpft.COD_CONDUCTOR = NEW.COD_CONDUCTOR
                AND pocpft.COD_FECHA_TRANSPORTE = NEW.COD_FECHA_TRANSPORTE;
                
END //
DELIMITER ;