package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Item;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface ItemDAO {

   public void save(Item it) throws PersistenceException;

   public Item load(int id) throws PersistenceException;

   public List<Item> items() throws PersistenceException;

   public void registrarItem(Item item) throws PersistenceException;

}