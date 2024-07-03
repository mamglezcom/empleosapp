package com.mamglez.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mamglez.model.Vacante;
import com.mamglez.service.IVacantesService;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@GetMapping("/create/{idVacante}")
	public String crear(@PathVariable("idVacante") Integer idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		System.out.println("idVacante: " + idVacante);
		return "solicitudes/formSolicitud";
	}

}
