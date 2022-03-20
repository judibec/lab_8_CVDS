package edu.eci.cvds.sampleprj.dao;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.Date;
import java.util.List;

public interface ClienteDAO {
    public Cliente load(int id) throws PersistenceException;

    public void save(int id,int idit,Date fechainicio,Date fechafin) throws PersistenceException;

    public List<Cliente> clientes() throws PersistenceException;
}
