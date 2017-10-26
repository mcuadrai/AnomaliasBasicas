package com.megaAnomaly.dao;

public class ConexionBaseDeDatos {

	
	    private String cadenaDeConexion;
	    private String nombreUsuario;
	    private String contrasena;
	    
		public ConexionBaseDeDatos(String cadenaDeConexion, String nombreUsuario, String contrasena) {
			super();
			this.cadenaDeConexion = cadenaDeConexion;
			this.nombreUsuario = nombreUsuario;
			this.contrasena = contrasena;
		}
		public String getCadenaDeConexion() {
			return cadenaDeConexion;
		}
		public void setCadenaDeConexion(String cadenaDeConexion) {
			this.cadenaDeConexion = cadenaDeConexion;
		}
		public String getNombreUsuario() {
			return nombreUsuario;
		}
		public void setNombreUsuario(String nombreUsuario) {
			this.nombreUsuario = nombreUsuario;
		}
		public String getContrasena() {
			return contrasena;
		}
		public void setContrasena(String contrasena) {
			this.contrasena = contrasena;
		}
	    
	    
}
