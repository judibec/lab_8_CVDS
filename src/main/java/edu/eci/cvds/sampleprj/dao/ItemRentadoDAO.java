package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.ItemRentado;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface ItemRentadoDAO {

    public List<ItemRentado> loadC(long idcliente) throws PersistenceException;

//    public List<ItemRentado> load() throws PersistenceException;

    public ItemRentado load(int id) throws PersistenceException;
}
