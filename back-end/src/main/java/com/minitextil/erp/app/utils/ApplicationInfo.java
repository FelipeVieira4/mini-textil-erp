package com.minitextil.erp.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfo {
	@Value("${app.version}")
	private String appVersion;
	
	public String getVersion() {
		return this.appVersion==null?"Não Informado":this.appVersion;
	}
}
