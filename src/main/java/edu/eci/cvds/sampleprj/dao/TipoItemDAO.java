package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface TipoItemDAO {

    public List<TipoItem> load() throws PersistenceException;

    public TipoItem load(int id) throws PersistenceException;

    public void save(String tipoItem) throws PersistenceException;
}
