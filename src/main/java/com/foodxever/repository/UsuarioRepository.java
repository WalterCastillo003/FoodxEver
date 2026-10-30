package com.foodxever.repository;

import com.foodxever.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByOrderByNombreAsc();
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);
    boolean existsByRolRef_Id(Long rolId);
    long countByRolRef_NombreIgnoreCase(String nombreRol);
}
