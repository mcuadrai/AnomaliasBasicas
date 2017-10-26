package com.megaAnomaly.report;

import java.util.ArrayList;
import java.util.Date;

import com.megaAnomaly.dao.CatalogoGateWay;
import com.megaAnomaly.dao.ConexionBaseDeDatos;


public class App 
{




	static private void analisis(ArrayList<ConexionBaseDeDatos> listaConexiones) {

		Integer cantidadTablas_total = 0;
		Integer cantidadTablasConPK_total = 0;
		Integer cantidadTablasSinPK_total = 0;
		Integer cantidadTablasAisladas_total = 0;
		Integer cantidadTablasAisladasEnEsquema_total = 0;

		printEncabezadoHaciaLado();
		for (ConexionBaseDeDatos conexion : listaConexiones) {
			Integer cantidadTablas  = 0;
			Integer cantidadTablasConPK = 0;
			Integer cantidadTablasSinPK = 0;
			Integer cantidadTablasAisladas = 0;
			Integer cantidadTablasAisladasEnEsquema = 0;

			try {

				CatalogoGateWay catalogoGW = new CatalogoGateWay(conexion.getCadenaDeConexion(), conexion.getNombreUsuario(), conexion.getContrasena());
				cantidadTablas = catalogoGW.buscarCantidadTablas();
				cantidadTablasConPK = catalogoGW.buscarCantidadTablasConPK();
				cantidadTablasSinPK = catalogoGW.buscarCantidadTablasSinPK();
				//toda tabla debe tener una FK de destino, o FK de origen, sino está aislada. revisa si hay constraints desde otros esquemas
				cantidadTablasAisladas = catalogoGW.buscarCantidadTablasAisladas();
				//buscar tablas aisladas en esquema.
				//cantidadTablasAisladasEnEsquema = catalogoGW.buscarCantidadTablasAisladasEnEsquema();
				//print_determinaIgualdad(cantidadTablasAisladas, cantidadTablasAisladasEnEsquema);

				// 
				// TODO falta conocer: nombres de tablas sin PK, 
				//  nombres de tablas aisladas, relación (tabla de origen y tabla de destino) de tablas entre esquemas, grado de relaciones entre esquemas, 
				//  cantidad de relaciones (de tablas) entre esquemas, cantidad de interacción entre esquemas
				//  cantidad de tablas relacionadas a otros esquemas por esquema

				printHaciaLado(conexion.getNombreUsuario(), cantidadTablas, cantidadTablasConPK, cantidadTablasSinPK, cantidadTablasAisladas);
			} catch(Exception e) {
				//e.printStackTrace();
				cantidadTablas  = 0;
				cantidadTablasConPK = 0;
				cantidadTablasSinPK = 0;
				cantidadTablasAisladas = 0;

				System.out.println("Error en esquema :"+e.getMessage());

			}

			cantidadTablas_total = cantidadTablas_total+ cantidadTablas;
			cantidadTablasConPK_total = cantidadTablasConPK_total + cantidadTablasConPK;
			cantidadTablasSinPK_total = cantidadTablasSinPK_total + cantidadTablasSinPK;
			cantidadTablasAisladasEnEsquema_total = cantidadTablasAisladasEnEsquema_total + cantidadTablasAisladasEnEsquema;
			cantidadTablasAisladas_total          = cantidadTablasAisladas_total + cantidadTablasAisladas;		

		}

		printHaciaLado("ResumenTotal",cantidadTablas_total, cantidadTablasConPK_total, cantidadTablasSinPK_total, cantidadTablasAisladas_total);



	}

	static void print(Integer cantidadTablas, Integer cantidadTablasConPK, Integer cantidadTablasSinPK, Integer cantidadTablasAisladas){
		System.out.println("cantidad de tablas       = "+cantidadTablas);
		System.out.println("cantidad de tablas con PK= "+cantidadTablasConPK);
		System.out.println("cantidad de tablas sin PK= "+cantidadTablasSinPK);
		//System.out.print("Determina completitud de montos:");
		//determinaCompletitud(cantidadTablasConPK, cantidadTablasSinPK, cantidadTablas);

		System.out.println("% con PK= "+   (cantidadTablasConPK.floatValue()/cantidadTablas.floatValue())*100);
		float porcentajeSinPK =  (cantidadTablasSinPK.floatValue()/cantidadTablas.floatValue())*100;
		System.out.println("% sin PK= "+ porcentajeSinPK);
		print_determinaEstadoDeCalidad(porcentajeSinPK);

		System.out.println("cantidad de tablas aisladas= "+cantidadTablasAisladas);
		float porcentajeTablasAisladas =  (cantidadTablasAisladas.floatValue()/cantidadTablas.floatValue())*100;
		System.out.println("% tablas aisladas= "+  porcentajeTablasAisladas);
		print_determinaEstadoDeCalidad(porcentajeTablasAisladas);
	}

	static void printHaciaLado(String esquema, Integer cantidadTablas, Integer cantidadTablasConPK, Integer cantidadTablasSinPK, Integer cantidadTablasAisladas){
		int porcentajeSinPK =  Math.round ((cantidadTablasSinPK.floatValue()/cantidadTablas.floatValue())*100);
		int porcentajeTablasAisladas =  Math.round  ((cantidadTablasAisladas.floatValue()/cantidadTablas.floatValue())*100);

		System.out.print(esquema+";"+cantidadTablas+";"+cantidadTablasConPK+";"+cantidadTablasSinPK+";"+
				Math.round ((cantidadTablasConPK.floatValue()/cantidadTablas.floatValue())*100)
		+";"+porcentajeSinPK+";"
				);
		print_determinaEstadoDeCalidad(porcentajeSinPK);
		System.out.print(";"+cantidadTablas+";"+cantidadTablasAisladas+";"+porcentajeTablasAisladas+";");
		print_determinaEstadoDeCalidad(porcentajeTablasAisladas);
		System.out.println();
	}

	static void printEncabezadoHaciaLado(){
		System.out.print  ("descripcion;cantidad de tablas;cantidad de tablas con PK;cantidad de tablas sin PK; % con PK;% sin PK; calidad;");
		System.out.println("cantidad de tablas;cantidad de tablas aisladas; % tablas aisladas ; calidad ");
	}

	public static void main( String[] args )
	{

		System.out.println("Iniciado. "+new Date());
		analisis(buscarConexiones());
		System.out.println("Terminado."+new Date());    


	}

	static private ArrayList<ConexionBaseDeDatos> buscarConexiones() {
		//TODO leer desde archivo la conexion

		ArrayList<ConexionBaseDeDatos> listaEsquemas = new ArrayList<ConexionBaseDeDatos>();

		//String cadenaDeConexion_NombreServicio  = "jdbc:oracle:thin:%s@//localhost:puerto/nombre_servicio"; //Formato nombre de servicio
		//listaEsquemas.add(new ConexionBaseDeDatos(String.format(cadenaDeConexion_NombreServicio, "nombreEsquema"),"nombreEsquema","password"));
		
		String cadenaDeConexion_SID = "jdbc:oracle:thin:@localhost:puerto:sid"; //Formato SID
		listaEsquemas.add(new ConexionBaseDeDatos(cadenaDeConexion_SID,"nombre_esquema","password"));					


		return listaEsquemas;
	}

    
	static public void print_determinaEstadoDeCalidad(float porcentaje){
		if (porcentaje > (float) 50 ) {
			System.out.print("Muy baja");
		} else if (porcentaje <= (float) 50 && porcentaje > (float) 25) {
			System.out.print("Baja");
		} else if (porcentaje <= (float) 25 && porcentaje > (float) 10) {
			System.out.print("Intermedio");
		} else if (porcentaje <= (float) 10 && porcentaje > (float) 0) {
			System.out.print("Aceptable");
		} else {
			System.out.print("Excelente");
		}
	}


	static public void print_determinaCompletitud(Number numero1,  Number numero2, Number total){
		if (numero1.intValue() +numero2.intValue() == total.intValue()) {
			System.out.println("Test:OK");
		} else {
			System.out.println("Test:Error de completitud: diferencia="+(total.intValue() -(numero1.intValue() +numero2.intValue()))  );
		}

	}
	static public void print_determinaIgualdad(Number numero1,  Number numero2){
		if (numero1.intValue() == numero2.intValue()) {
			System.out.println("Test. Son iguales: OK.");
		} else {
			System.out.println("Test:Error de igualdad: diferencia="+(numero1.intValue() -numero2.intValue() )  );
		}

	}
}
