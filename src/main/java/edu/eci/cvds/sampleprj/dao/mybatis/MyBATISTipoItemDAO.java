package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public class MyBATISTipoItemDAO implements TipoItemDAO {
    @Inject
    private TipoItemMapper tipoItemMapper;

    @Override
    public List<TipoItem> load() throws PersistenceException {
        try{
            return tipoItemMapper.getTiposItems();
        }catch (PersistenceException e){
            throw new PersistenceException("Error al consultar el tipo de los items");
        }
    }

    @Override
    public TipoItem load(int id) throws PersistenceException {
        try {
            return tipoItemMapper.getTipoItem(id);
        }catch (PersistenceException e){
            throw new PersistenceException("Error al consultar el tipo de item");
        }
    }

    @Override
    public void save(String tipoItem) throws PersistenceException {
        try {
            tipoItemMapper.addTipoItem(tipoItem);
        }catch (PersistenceException e){
            throw new PersistenceException("Error al agregar el item");
        }
    }
}
