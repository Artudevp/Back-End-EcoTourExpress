package com.ecotourexpress.ecotourexpress.repository;

import com.ecotourexpress.ecotourexpress.model.ClienteActividad;
import com.ecotourexpress.ecotourexpress.model.ClienteActividadKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteActividadRepository extends JpaRepository<ClienteActividad, ClienteActividadKey> {
}
