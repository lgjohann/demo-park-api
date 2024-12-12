package com.johann.demoparkapi.repository;

import com.johann.demoparkapi.entity.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u.role from Usuario u where u.username LIKE :username")
    Usuario.Role findRoleByUsername(String username);
}
