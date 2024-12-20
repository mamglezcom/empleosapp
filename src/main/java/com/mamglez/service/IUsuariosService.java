package com.mamglez.service;

import java.util.List;
import com.mamglez.model.Usuario;

public interface IUsuariosService {

	void guardar(Usuario usuario);
	
	void eliminar(Integer idUsuario);
	
	List<Usuario> buscarTodos();
	
	Usuario buscarPorUsername(String username);
	
	int bloquear(int idUsuario);
	
	int activar(int idUsuario);
}
