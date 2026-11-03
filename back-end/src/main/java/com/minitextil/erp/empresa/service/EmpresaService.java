package com.minitextil.erp.empresa.service;

import org.springframework.stereotype.Service;

@Service
public class EmpresaService {
	
	//@Autowired
	//private EmpresaRepository empresaRepository;
	
	public String unformatCnpj(String cnpj) {
	    if (cnpj == null) return null;
	    return cnpj.replaceAll("\\D", "");
	}
	
	public String formatCnpj(String cnpj) {
	    if (cnpj == null || cnpj.length() != 14) return cnpj;
	    return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
	}
}
