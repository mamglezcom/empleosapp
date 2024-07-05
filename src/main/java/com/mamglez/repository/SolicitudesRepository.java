package com.mamglez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mamglez.model.Solicitud;
import com.mamglez.model.Usuario;
import com.mamglez.model.Vacante;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {


	boolean existsByUsuarioAndVacante(Usuario usuario, Vacante vacante);
	
}
