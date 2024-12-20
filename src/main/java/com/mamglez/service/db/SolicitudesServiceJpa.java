package com.mamglez.service.db;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mamglez.model.Solicitud;
import com.mamglez.model.Usuario;
import com.mamglez.model.Vacante;
import com.mamglez.repository.SolicitudesRepository;
import com.mamglez.service.ISolicitudesService;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService {
	
	@Autowired
	private SolicitudesRepository solicitudesRepo;

	@Override
	public void guardar(Solicitud solicitud) {
		solicitudesRepo.save(solicitud);
	}

	@Override
	public void eliminar(Integer idSolicitud) {
		solicitudesRepo.deleteById(idSolicitud);
	}

	@Override
	public List<Solicitud> buscarTodas() {
		return solicitudesRepo.findAll();
	}

	@Override
	public Solicitud buscarPorId(Integer idSolicitud) {
		Optional<Solicitud> optional = solicitudesRepo.findById(idSolicitud);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Page<Solicitud> buscarTodas(Pageable page){
		return solicitudesRepo.findAll(page);
	}
	
	@Override
    public boolean existeSolicitud(int idUsuario, int idVacante) {
		Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        Vacante vacante = new Vacante();
        vacante.setId(idVacante);
        return solicitudesRepo.existsByUsuarioAndVacante(usuario, vacante);
    }

	@Override
	public List<Solicitud> buscarTodasPorUsuario(int idUsuario) {
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		return solicitudesRepo.findAllByUsuario(usuario);
		 
	}

}
