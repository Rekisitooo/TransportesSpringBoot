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

	-- SELECT disponibilidad_por_dia_transporte;
    -- SELECT cod_usuario;
    -- SELECT cod_grupo_usuario;
    -- SELECT mostrar_viajeros_activos;
    -- SELECT orden;
    -- SELECT elemento_inicial;
    -- SELECT tamano_pagina;
    
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