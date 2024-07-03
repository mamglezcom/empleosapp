package com.mamglez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mamglez.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {

}
