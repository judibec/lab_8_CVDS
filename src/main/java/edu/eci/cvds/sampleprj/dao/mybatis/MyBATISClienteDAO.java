package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.Date;
import java.util.List;

public class MyBATISClienteDAO implements ClienteDAO {
    @Inject
    private ClienteMapper clienteMapper;

    @Override
    public Cliente load(int id) throws PersistenceException {
        try {
            return clienteMapper.consultarCliente(id);
        } catch (org.apache.ibatis.exceptions.PersistenceException e) {
            throw new PersistenceException("Error al consultar el cliente " + id, e);
        }
    }

    @Override
    public void save(int id, int idit, Date fechainicio, Date fechafin) throws PersistenceException {
        try {
            clienteMapper.agregarItemRentadoACliente(id,idit,fechainicio,fechafin);
        } catch (org.apache.ibatis.exceptions.PersistenceException e) {
            throw new PersistenceException("Error al añadir el cliente" + id, e);
        }
    }

    @Override
    public List<Cliente> clientes() throws PersistenceException {
        try{
            return clienteMapper.consultarClientes();
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e) {
            throw new PersistenceException("Error al consultar la lista de clientes", e);
        }
    }
}
