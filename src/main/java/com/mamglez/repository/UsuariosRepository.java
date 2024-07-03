package com.mamglez.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mamglez.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

}
