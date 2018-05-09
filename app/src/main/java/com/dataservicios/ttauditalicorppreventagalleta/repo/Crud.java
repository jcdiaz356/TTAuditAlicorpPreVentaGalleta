package com.dataservicios.ttauditalicorppreventagalleta.repo;

import java.util.List;

interface Crud
{

	public int create(Object item);
	public int update(Object item);
	public int delete(Object item);
	public int deleteAll();
	public Object findById(int id);
	public List<?> findAll();
	/**
	 * Obtiene la cantidad de registros
	 * @return Retorna un entero largo con la cantidad de registros de la tabla
	 */
	public long countReg();
	/**
	 * Obtien el primer registro
	 * @return Retorna un Objeto
	 */
	public Object findFirstReg();

}
