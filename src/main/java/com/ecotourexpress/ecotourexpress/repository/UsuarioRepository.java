package com.ecotourexpress.ecotourexpress.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecotourexpress.ecotourexpress.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
}
