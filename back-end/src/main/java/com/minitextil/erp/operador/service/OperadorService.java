package com.minitextil.erp.operador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minitextil.erp.operador.model.Operador;
import com.minitextil.erp.operador.repository.OperadorRepository;

@Service
public class OperadorService {
	@Autowired
	private OperadorRepository operadorRepository;
	
	@Autowired
	private PasswordEncoder	 passwordEncoder;
	
	public Operador salvarOperador(Operador operadorModel) {
		var senhaCriptografada=passwordEncoder.encode(operadorModel.getSenha());
		
		operadorModel.setSenha(senhaCriptografada);
		
		return operadorRepository.save(operadorModel);
	}
	
	//public boolean validarSenhaOperador(String senha,OperadorModel)
}
