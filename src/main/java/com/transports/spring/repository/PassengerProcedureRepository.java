package com.transports.spring.repository;

import com.transports.spring.dto.DtoGetAllPassengers;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PassengerProcedureRepository {

    private final DataSource dataSource;

    public PassengerProcedureRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<DtoGetAllPassengers> getAllPassengers(final int userId, final Integer groupId) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final CallableStatement callableStatement = connection.prepareCall("{CALL crud_viajeros('0,1', " + userId + ", " + groupId + ")}")
        ) {
            final boolean hasResults = callableStatement.execute();
            final List<DtoGetAllPassengers> results = new ArrayList<>();

            if (hasResults) {
                try (final ResultSet resultSet = callableStatement.getResultSet()) {
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        final DtoGetAllPassengers.DtoGetAllPassengersBuilder dtoGetAllPassengersBuilder = DtoGetAllPassengers.builder().
                                id(resultSet.getInt("Id")).
                                name(resultSet.getString("Nombre")).
                                surname(resultSet.getString("Apellidos")).
                                occupiedSeats(resultSet.getInt("plazas_ocupadas")).
                                isActive(resultSet.getBoolean("activo")).
                                isShared(resultSet.getBoolean("usuario_compartido_grupo")).
                                ownerAlias(resultSet.getString("alias_usuario_propietario"));

                        int appUserId = 1; // extract user form session
                        int involvedUserIdCreator = resultSet.getInt("codigo_usuario_propietario");
                        dtoGetAllPassengersBuilder.isSharedFieldToBeModified(involvedUserIdCreator == appUserId);

                        final Map<Integer, Integer> weeklyTransportDayAssitanceByTransportDayIdMap = new HashMap<>();
                        for (int i = 10; i <= columnCount; i=i+2) {
                            //final String columnName = metaData.getColumnName(i); //DEBUG
                            final int weeklyTransportDayAssitance = resultSet.getInt(i);
                            final int weeklyTransportDayId = resultSet.getInt(i-1);
                            weeklyTransportDayAssitanceByTransportDayIdMap.put(weeklyTransportDayId, weeklyTransportDayAssitance);
                        }

                        dtoGetAllPassengersBuilder.availableInWeeklyTransportDayMap(weeklyTransportDayAssitanceByTransportDayIdMap);
                        results.add(dtoGetAllPassengersBuilder.build());
                    }
                }
            }

            return results;
        }
    }
}
