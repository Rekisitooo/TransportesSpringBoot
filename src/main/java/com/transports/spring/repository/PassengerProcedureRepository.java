package com.transports.spring.repository;

import com.transports.spring.dto.DtoGetAllPassengers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
public class PassengerProcedureRepository {

    public static final int FIELDS_OBTAINED_BEFORE_WEEKLY_TRANSPORT_DAYS = 9;
    private final DataSource dataSource;

    public PassengerProcedureRepository(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    public List<DtoGetAllPassengers> getAllPassengers(final int userId, final Integer groupId, final Pageable pageable) throws SQLException {
        final StringBuffer sorting = new StringBuffer();
        long offset = 1L;
        int pageSize = 10;

        if (pageable != null) {
            offset = pageable.getOffset();
            pageSize = pageable.getPageSize();
            pageable.getSort().forEach(order -> {
                sorting.append(order.getProperty());
                sorting.append(",");
                sorting.append(order.getDirection().toString());
            });
        }

        try (final Connection connection = dataSource.getConnection();
             final CallableStatement callableStatement = connection.prepareCall("{CALL crud_viajeros('0,1', :userId, :groupId, :offset, :pageSize, :sorting)}");
         ){
            callableStatement.setString("userId", String.valueOf(userId));
            callableStatement.setString("groupId", String.valueOf(groupId));
            callableStatement.setLong("offset", offset);
            callableStatement.setInt("pageSize", pageSize);
            callableStatement.setString("sorting", sorting.toString());

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
                                ownerAlias(resultSet.getString("alias_usuario_propietario")).
                                totalNumberOfElements(resultSet.getInt("total_registros"));

                        int appUserId = 1; // extract user form session
                        int involvedUserIdCreator = resultSet.getInt("codigo_usuario_propietario");
                        dtoGetAllPassengersBuilder.isSharedFieldToBeModified(involvedUserIdCreator == appUserId);

                        final Map<Integer, Integer> weeklyTransportDayAssitanceByTransportDayIdMap = new HashMap<>();
                        for (int i = 1 + FIELDS_OBTAINED_BEFORE_WEEKLY_TRANSPORT_DAYS; i <= columnCount; i=i+2) {
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
