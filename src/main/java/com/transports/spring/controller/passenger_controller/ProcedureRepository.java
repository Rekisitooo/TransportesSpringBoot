package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcedureRepository {

    @Autowired
    private DataSource dataSource;

    public List<DtoGetAllPassengers> getAllPassengers(final int user_id, final Integer group_id) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final CallableStatement callableStatement = connection.prepareCall("{CALL crud_viajeros('0,1', " + user_id + ", " + group_id + ")}")
        ) {
            final boolean hasResultSet = callableStatement.execute();
            final List<DtoGetAllPassengers> results = new ArrayList<>();

            if (hasResultSet) {
                try (final ResultSet resultSet = callableStatement.getResultSet()) {
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        final DtoGetAllPassengersBuilder passengersBuilder = new DtoGetAllPassengersBuilder();
                        passengersBuilder.id(resultSet.getInt("Id")).
                                name(resultSet.getString("Nombre")).
                                surname(resultSet.getString("Apellidos")).
                                occupiedSeats(resultSet.getInt("plazas_ocupadas")).
                                active(resultSet.getBoolean("activo")).
                                shared(resultSet.getBoolean("usuario_compartido_grupo"));

                        for (int i = 7; i <= columnCount; i++) {
                            final String columnName = metaData.getColumnName(i);
                            final boolean value = resultSet.getBoolean(i);
                            passengersBuilder.availableInWeeklyTransportDay(value);
                        }

                        results.add(passengersBuilder.build());
                    }
                }
            }

            return results;
        }
    }
}
