package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengersBuilder;
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

    public List<DtoGetAllPassengers> getAllPassengers(final int user_id, final String group_id) throws SQLException {
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
                        passengersBuilder.name(resultSet.getString("Nombre")).
                                surname(resultSet.getString("Apellidos")).
                                ocuppiedSeats(resultSet.getInt("plazas_ocupadas")).
                                active(resultSet.getBoolean("activo")).
                                shared(resultSet.getBoolean("usuario_compartido_grupo"));

                        for (int i = 1; i <= columnCount; i++) {
                            final Boolean value = (Boolean) resultSet.getObject(i);
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
