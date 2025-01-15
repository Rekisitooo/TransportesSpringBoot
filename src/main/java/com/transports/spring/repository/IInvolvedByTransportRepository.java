package com.transports.spring.repository;

import com.transports.spring.model.InvolvedByTransport;
import com.transports.spring.model.WeeklyTransportDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInvolvedByTransportRepository extends JpaRepository<InvolvedByTransport, Integer> {

    @Query("SELECT " +
            "   i.NOMBRE," +
            "   i.APELLIDO," +
            "   i.ID" +
            " *     FROM INVOLUCRADO i" +
            "           INNER JOIN INVOLUCRADO_POR_TRANSPORTE ipt" +
            "               ON i.ID = ipt.COD_INVOLUCRADO")
    Optional<InvolvedByTransport> findByIdWithName(int id);
}
