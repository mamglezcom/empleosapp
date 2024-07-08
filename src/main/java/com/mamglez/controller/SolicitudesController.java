package com.mamglez.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mamglez.model.Solicitud;
import com.mamglez.model.Usuario;
import com.mamglez.model.Vacante;
import com.mamglez.service.ISolicitudesService;
import com.mamglez.service.IUsuariosService;
import com.mamglez.service.IVacantesService;
import com.mamglez.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
	
	@Value("${empleosapp.ruta.cv}")
	private String rutaCv;
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private ISolicitudesService solicitudesService;
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Solicitud> lista = solicitudesService.buscarTodas(page);
		model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";
	}
	
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable("idVacante") Integer idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		System.out.println("idVacante: " + idVacante);
		return "solicitudes/formSolicitud";
	}
	
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, 
			@RequestParam("archivoCV") MultipartFile multipart, 
			Authentication authentication, RedirectAttributes attributes) {
		
		String username = authentication.getName();
		
		if(result.hasErrors()) {
			System.out.println("Existen errores");
			return "solicitudes/formSolicitud";
		}
		
		if(!multipart.isEmpty()) {
			String nombreArchivo = Utileria.guardarArchivo(multipart, rutaCv);
			if(nombreArchivo != null) {
				solicitud.setArchivo(nombreArchivo);
			}
		}
		
		Usuario usuario = serviceUsuario.buscarPorUsername(username);
		solicitud.setUsuario(usuario);
		
		// Verifica si ya existe una solicitud para este usuario y esta vacante
	    if (solicitudesService.existeSolicitud(usuario.getId(), solicitud.getVacante().getId())) {
	        // Si existe, agregar un mensaje de error y regresar al formulario
	    	attributes.addFlashAttribute("error", "Ya has aplicado anteriormente a "+solicitud.getVacante().getNombre());
	        return "redirect:/";
	    }
		
		solicitudesService.guardar(solicitud);
		attributes.addFlashAttribute("msg", "Gracias por enviar el CV");
		
		System.out.println(solicitud);
		return "redirect:/";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes) {
		solicitudesService.eliminar(idSolicitud);
		attributes.addFlashAttribute("msg", "Solicitud eliminada");
		return "redirect:/solicitudes/indexPaginate";
	}
	
	@GetMapping("/aplicadas/{id}")
	public String buscarPorUsuario(@PathVariable("id") int idUsuario, Model model) {
		List<Solicitud> solicitudes = solicitudesService.buscarTodasPorUsuario(idUsuario);
		for(Solicitud sol : solicitudes) {
			System.out.println(sol);
		}
		model.addAttribute("aplicadas", solicitudes);
		return "solicitudes/listAplicadas";
	}

}
