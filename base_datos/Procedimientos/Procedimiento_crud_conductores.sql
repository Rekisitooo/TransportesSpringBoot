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

-- CALL crud_conductores('0, 1');
use transportesspringboot;

SELECT 
	ftpp.FECHA_TRANSPORTE, ftpp.NOMBRE_EVENTO, viajero.nombre
		FROM Transporte t
			INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				ON t.COD_FECHA_TRANSPORTE = ftpp.ID
			INNER JOIN INVOLUCRADO viajero
				ON t.COD_VIAJERO = viajero.ID
			INNER JOIN INVOLUCRADO conductor
				ON t.COD_CONDUCTOR = conductor.ID
		WHERE
			t.COD_CONDUCTOR = 13
			AND ftpp.COD_PLANTILLA = 1
		ORDER BY FECHA_TRANSPORTE ASC;
            
            SELECT 
	ftpp.FECHA_TRANSPORTE, ftpp.NOMBRE_EVENTO, conductor.nombre
		FROM Transporte t
			INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp
				ON t.COD_FECHA_TRANSPORTE = ftpp.ID
			INNER JOIN INVOLUCRADO viajero
				ON t.COD_VIAJERO = viajero.ID
			INNER JOIN INVOLUCRADO conductor
				ON t.COD_CONDUCTOR = conductor.ID
		WHERE
			t.COD_VIAJERO = 1
			AND ftpp.COD_PLANTILLA = 1;