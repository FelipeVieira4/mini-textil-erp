package com.minitextil.erp.operador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.minitextil.erp.operador.repository.OperadorRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
    private OperadorRepository repository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByLoginName(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com: " + username));
	}
}
