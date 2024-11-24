package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import com.transports.spring.model.WeeklyTransportDay;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class GetAllPassengers {

    public static final String PASSENGER_CRUD_STORED_PROCEDURE = "crud_viajeros";
    public static final String PARAMETER_1_PASSENGER_CRUD = "mostrar_viajeros_activos";

    public static List<DtoGetAllPassengers> getOnlyActive(final List<WeeklyTransportDay> activeWeeklyTransportDays){
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nombreDeUnidadDePersistencia");
        final List<DtoGetAllPassengers> activePassengers = new ArrayList<>();

        try (emf; EntityManager em = emf.createEntityManager()) {
            final StoredProcedureQuery query = em.createStoredProcedureQuery(PASSENGER_CRUD_STORED_PROCEDURE);
            query.registerStoredProcedureParameter(PARAMETER_1_PASSENGER_CRUD, String.class, ParameterMode.IN);
            query.setParameter(PARAMETER_1_PASSENGER_CRUD, "1");
            query.execute();

            final List<Object[]> queryResultList = query.getResultList();
            for (final Object[] row : queryResultList) {
                final String name = (String) row[0];
                final String surname = (String) row[1];
                final Integer seatsRequider = (Integer) row[2];
                final HashMap<String, Boolean> availableInWeeklyTransportDayMap = new HashMap<>();

                for (final WeeklyTransportDay oWeeklyTransportDay : activeWeeklyTransportDays) {
                    final String description = oWeeklyTransportDay.getDescription();
                    boolean active = oWeeklyTransportDay.isActive();
                    availableInWeeklyTransportDayMap.put(description, active);
                }

                activePassengers.add(new DtoGetAllPassengers(name, surname, seatsRequider, availableInWeeklyTransportDayMap));
            }
        }

        return activePassengers;
    }
}
