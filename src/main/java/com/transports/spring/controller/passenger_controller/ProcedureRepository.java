package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProcedureRepository {

    @Autowired
    private DataSource dataSource;

    public List<DtoGetAllPassengers> getAllPassengers(final int user_id, final Integer group_id) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final CallableStatement callableStatement = connection.prepareCall("{CALL crud_viajeros('0,1', " + user_id + ", " + group_id + ")}")
        ) {
            final boolean hasResults = callableStatement.execute();
            final List<DtoGetAllPassengers> results = new ArrayList<>();

            if (hasResults) {
                try (final ResultSet resultSet = callableStatement.getResultSet()) {
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        Map<Integer, Integer> weeklyTransportDayAssitanceByTransportDayIdMap = new HashMap<>();
                        final DtoGetAllPassengers.DtoGetAllPassengersBuilder dtoGetAllPassengersBuilder = DtoGetAllPassengers.builder().
                                id(resultSet.getInt("Id")).
                                name(resultSet.getString("Nombre")).
                                surname(resultSet.getString("Apellidos")).
                                occupiedSeats(resultSet.getInt("plazas_ocupadas")).
                                isActive(resultSet.getBoolean("activo")).
                                isShared(resultSet.getBoolean("usuario_compartido_grupo"));

                        //columnCount will always be an even number because for every day, it will bring transport day id and assistance (boolean)
                        for (int i = 8; i <= columnCount; i=i+2) {
                            //final String columnName = metaData.getColumnName(i); //DEBUG
                            final int weeklyTransportDayAssitance = resultSet.getInt(i);
                            final int weeklyTransportDayId = resultSet.getInt(i-1);
                            weeklyTransportDayAssitanceByTransportDayIdMap.put(weeklyTransportDayId, weeklyTransportDayAssitance);
                        }
                        DtoGetAllPassengers build = dtoGetAllPassengersBuilder.build();
                        boolean active = build.isActive();
                        dtoGetAllPassengersBuilder.availableInWeeklyTransportDayMap(weeklyTransportDayAssitanceByTransportDayIdMap);
                        results.add(dtoGetAllPassengersBuilder.build());
                    }
                }
            }

            return results;
        }
    }
}
