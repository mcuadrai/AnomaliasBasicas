package com.megaAnomaly.dao;

import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import com.megaAnomaly.model.dto.ProductComponentVersion;
import com.megaAnomaly.model.dto.UserTabColumn;
import com.megaAnomaly.model.dto.UserTable;

public class CatalogoGateWay {

	private Sql2o sql2o;

	

	public CatalogoGateWay(String cadenaDeConexion, String esquema, String contrasena ) {
		sql2o = new Sql2o(cadenaDeConexion, esquema, contrasena);
	}

	public List<ProductComponentVersion> getAllComponents(){
		String sql =
				"SELECT PRODUCT, VERSION, STATUS " +
						"FROM PRODUCT_COMPONENT_VERSION " ;

		// Ojo la conexion hasta acá.  
		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(ProductComponentVersion.class);
		}
	}


	public List<UserTabColumn> getColumnasUnicas(){
		String sql =
				"SELECT COLUMN_NAME " +
						"FROM user_tab_columns " ;

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(UserTabColumn.class);
		}
	}

	public List<UserTable> getTablas(){
		String sql =
				"SELECT TABLE_NAME FROM user_tables " ;

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(UserTable.class);
		}
	}


	//Constraints posibles
	public List<UserTabColumn> getClavesForaneasPosibles(String nombreTabla){
		String sql =
				"SELECT TABLE_NAME, COLUMN_NAME FROM  user_tab_columns " +
						"where   column_name = :nombre_tabla "+
						" and  (table_name <> :nombre_tabla and  column_name != 'codigo') ";


		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).addParameter("nombre_tabla", nombreTabla)
					.executeAndFetch(UserTabColumn.class);
		}
	}


	public Integer buscarCantidadTablasConPK(){
		String sql =
				" select count(distinct tab.table_name)    "+
						"    from  user_constraints cons, user_tables tab "+ 
						"    where cons.constraint_type in ('P','U')  "+
						"      and cons.table_name = tab.table_name  ";


		try(Connection con = sql2o.open()) {
			return con.createQuery(sql)
					.executeScalar(Integer.class);
		}
	}

	public Integer buscarCantidadTablasSinPK(){

		String sql = "SELECT count(*) "+
				" FROM "+
				"( "+
				" SELECT table_name "+  
				" FROM user_tables "+
				" MINUS "+
				" SELECT DISTINCT table_name "+   
				" FROM  user_constraints "+
				" WHERE constraint_type in('P','U') "+ 
				" )";



		try(Connection con = sql2o.open()) {
			return con.createQuery(sql)
					.executeScalar(Integer.class);
		}
	}

	/**
	 * determina una tabla aislada cuando no hay constraint en el esquema o cuando desde otos esquemas no hay constraints.
	 * Necesisa permisos de acceso a all_constraints
	 * @return
	 */
	public Integer buscarCantidadTablasAisladas(){

		String sql =	
				"SELECT count(*)        "
						+"FROM "
						+"( "
						+"SELECT table_name "
						+"FROM user_tables "
						+"MINUS "
						+"SELECT DISTINCT co.TABLE_NAME "
						+"FROM  user_constraints co "
						+"WHERE co.constraint_type in('R') "
						+"MINUS "
						+" SELECT distinct cd.table_name " 
						+" FROM   all_constraints co , all_constraints cd "
						+" WHERE  "
						+"   co.constraint_type in('R') "
						+"   AND co.r_owner = cd.owner "
						+"   AND co.R_CONSTRAINT_NAME = cd.constraint_name "
						+"   AND cd.owner = (SELECT distinct owner FROM  user_constraints) "
						+")";

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeScalar(Integer.class);
		}
	}



	public Integer buscarCantidadTablasAisladasEnEsquema(){

		String sql =	
				"SELECT count(*)        "
						+"FROM "
						+"( "
						+"SELECT table_name "
						+"FROM user_tables "
						+"MINUS "
						+"SELECT DISTINCT co.TABLE_NAME "
						+"FROM  user_constraints co "
						+"WHERE co.constraint_type in('R') "
						+"MINUS "
						+" SELECT distinct cd.table_name " 
						+" FROM   user_constraints co , user_constraints cd "
						+" WHERE  "
						+"   co.constraint_type in('R') "
						+"   AND co.r_owner = cd.owner "
						+"   AND co.R_CONSTRAINT_NAME = cd.constraint_name "
						+")"
						+ "";

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeScalar(Integer.class);
		}
	}


	public Integer buscarCantidadTablasRelacionadasEntreEsquemas() {
		String sql =	
				"SELECT  count(distinct cd.table_name) " 
						+"FROM   all_constraints co , all_constraints cd " 
						+"WHERE  "
						+"     co.constraint_type in('R') " 
						+" AND co.r_owner = cd.owner "
						+" AND co.R_CONSTRAINT_NAME = cd.constraint_name " 
						+" AND cd.owner != co.owner ";

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeScalar(Integer.class);
		}	
	}  

	public Integer buscarCantidadTablasRelacionadas(){

		String sql =	
				"SELECT count(*)        "
						+"FROM "
						+"( "
						+"SELECT DISTINCT co.TABLE_NAME "
						+"FROM  user_constraints co "
						+"WHERE co.constraint_type in('R') "
						+"MINUS "
						+"SELECT cd.table_name  "
						+"FROM  user_constraints co, all_constraints cd "
						+"WHERE cd.constraint_type in('R') "
						+"  AND co.R_CONSTRAINT_NAME = cd.CONSTRAINT_NAME "
						+"  AND co.r_owner = (SELECT distinct owner FROM  user_constraints) "
						+"  AND cd.table_name in ("
						+"SELECT DISTINCT co.TABLE_NAME "
						+"FROM  user_constraints co "
						+"WHERE co.constraint_type in('R') "
						+") "
						+")";

		try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeScalar(Integer.class);
		}
	}

	public Integer buscarCantidadTablas() {
		String sql =
				" SELECT count(*) "+
						" FROM user_tables ";


		try(Connection con = sql2o.open()) {
			return con.createQuery(sql)
					.executeScalar(Integer.class);
		}
	}


	






}
