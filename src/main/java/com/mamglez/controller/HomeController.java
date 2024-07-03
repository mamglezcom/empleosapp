package com.mamglez.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mamglez.model.Perfil;
import com.mamglez.model.Usuario;
import com.mamglez.model.Vacante;
import com.mamglez.service.ICategoriasService;
import com.mamglez.service.IUsuariosService;
import com.mamglez.service.IVacantesService;

@Controller
public class HomeController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("categoriasServiceJpa")
	private ICategoriasService categoriasService;
	
	@Autowired
	private IVacantesService vacantesService;
	
	@Autowired
   	private IUsuariosService serviceUsuarios;
	
	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "formRegistro";
	}
	
	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		String username = auth.getName();
		System.out.println("nombre de usuario: " + username);
		for(GrantedAuthority rol : auth.getAuthorities()) { 
			System.out.println("Rol: " + rol.getAuthority());
		}
		
		if(session.getAttribute("usuario") == null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			usuario.setPassword(null);
			System.out.println("usuario: " + usuario);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/";
	}
	
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		usuario.setPassword(pwdEncriptado);		
		
		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor
		
		// Creamos el Perfil que le asignaremos al usuario nuevo
		Perfil perfil = new Perfil();
		perfil.setId(3); // Perfil USUARIO
		usuario.agregar(perfil);
		
		/**
		 * Guardamos el usuario en la base de datos. El Perfil se guarda automaticamente
		 */
		serviceUsuarios.guardar(usuario);
				
		attributes.addFlashAttribute("msg", "El registro fue guardado correctamente!");
		
		return "redirect:/usuarios/index";
	}
	
	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = vacantesService.buscarTodas();
		model.addAttribute("vacantes", lista);
		return "tabla";
	}
	
	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		
		Vacante vacante = new Vacante();
		vacante.setNombre("Ingeniero de comunicaciones");
		vacante.setDescripcion("Se solicita ingeniero para soporte a intranet");
		vacante.setFecha(new Date());
		vacante.setSalario(9700.0);
		
		model.addAttribute("vacante", vacante);
		return "detalle";
		
	}
	
	@GetMapping("/listado")
	public String mostrarlistado(Model model) {
		List<String> lista = new ArrayList<>();
		lista.add("Ingeniero de Sistemas");
		lista.add("Auxiliar de Contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");
		
		model.addAttribute("empleos", lista);
		return "listado";
	}
	

	@GetMapping("/")
	public String mostrarHome(Model model) {
//		List<Vacante> lista = vacantesService.buscarTodas();
//		model.addAttribute("vacantes", lista);
		
		return "home";
	}
	
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("buscando por " + vacante);
		
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = vacantesService.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("search", vacanteSearch);
		model.addAttribute("vacantes", vacantesService.buscarDestacadas());
		model.addAttribute("categorias", categoriasService.buscarTodas());
	}
	
	@GetMapping("/bcrypt/{texto}")
	@ResponseBody
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " Encriptado en BCrypt: " + passwordEncoder.encode(texto);
	}
	
}
