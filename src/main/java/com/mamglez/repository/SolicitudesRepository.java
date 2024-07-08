package com.mamglez.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mamglez.model.Solicitud;
import com.mamglez.model.Usuario;
import com.mamglez.model.Vacante;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {


	boolean existsByUsuarioAndVacante(Usuario usuario, Vacante vacante);
	
	List<Solicitud> findAllByUsuario(Usuario usuario);
	
}
