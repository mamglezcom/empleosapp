package com.mamglez.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mamglez.model.Solicitud;

public interface ISolicitudesService {
	
	void guardar(Solicitud solicitud);
	void eliminar(Integer idSolicitud);
	List<Solicitud> buscarTodas();
	Solicitud buscarPorId(Integer idSolicitud);
	Page<Solicitud> buscarTodas(Pageable page);
	boolean existeSolicitud(int idUsuario, int idVacante);
	
	List<Solicitud> buscarTodasPorUsuario(int idUsuario);

}
