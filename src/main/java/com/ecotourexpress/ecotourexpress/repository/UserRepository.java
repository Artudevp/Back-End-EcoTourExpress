package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.model.DTO.UserDTO;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByCorreo(String correo);
    boolean existsByUsername(String username);
    
    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.DTO.UserDTO(u.id, u.nombre, u.apellido, u.correo, u.username, u.contraseña, u.rol) FROM User u WHERE u.id = :id")
    UserDTO findUserDTOById(@Param("id") Long id);

    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.DTO.UserDTO(u.id, u.nombre, u.apellido, u.correo, u.username, u.contraseña, u.rol) FROM User u")
    List<UserDTO> findAllUsers();

}
