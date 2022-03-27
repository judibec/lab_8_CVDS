package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;

import edu.eci.cvds.sampleprj.dao.ItemRentadoDAO;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;

import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import org.apache.ibatis.exceptions.PersistenceException;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

   @Inject
   private ItemDAO itemDAO;

   @Inject
   private ClienteDAO clienteDAO;

   @Inject
   private TipoItemDAO tipoItemDAO;

    @Inject
    private ItemRentadoDAO itemRentadoDAO;

    @Override
   public int valorMultaRetrasoxDia(int itemId) {
       try{
           return (int) itemDAO.load(itemId).getTarifaxDia();
       }catch (PersistenceException e){
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
       try{
           return clienteDAO.load((int)docu);
       }catch (PersistenceException e) {
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
       try{
//           return clienteDAO.load((int)idcliente).getRentados();
           return itemRentadoDAO.loadC(idcliente);
       }catch (PersistenceException e) {
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
       try {
           return clienteDAO.clientes();
       }catch (PersistenceException e) {
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
       try {
           return itemDAO.load(id);
       } catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id);
       }
   }

   @Override
   public List<Item> consultarItemsDisponibles() {
       try {
            return itemDAO.items();
       }catch (PersistenceException e) {
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
//       try {
//           consultarItem(iditem);
//           ItemRentado itemRentado = itemRentadoDAO.load(iditem);
//           if (itemRentado == null) throw new ExcepcionServiciosAlquiler("no existe item");
//           LocalDate fechaFinal = itemRentado.getFechafinrenta().toLocalDate();
//           long dias = ChronoUnit.DAYS.between(fechaFinal, fechaDevolucion.toLocalDate());
//           System.out.println("test" + dias);
//           if (dias < 0) dias = 0;
//           return dias * valorMultaRetrasoxDia(iditem);
//       }catch (PersistenceException e) {
//           throw new ExcepcionServiciosAlquiler("Error al consultar multa");
//       }
       try {
           List<Cliente> clientes = consultarClientes();
           for (int i=0 ; i<clientes.size() ; i++) {
               ArrayList<ItemRentado> rentados = clientes.get(i).getRentados();
               for (int j=0 ; j<rentados.size() ; j++) {
                   if(rentados.get(j).getItem()!=null){
                       if (rentados.get(j).getItem().getId() == iditem) {
                           long diasRetraso = ChronoUnit.DAYS.between(rentados.get(j).getFechafinrenta().toLocalDate(), fechaDevolucion.toLocalDate());
                           if (diasRetraso < 0) {
                               return 0;
                           }
                           return diasRetraso * valorMultaRetrasoxDia(rentados.get(j).getId());
                       }
                   }
               }
           }
           return iditem;
       } catch (Exception e) {
           throw  new ExcepcionServiciosAlquiler("Error al consultar multa de item con id: "+iditem);
       }
   }

   @Override
   public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
       try {
            return tipoItemDAO.load(id);
       }catch (PersistenceException e) {
           throw new UnsupportedOperationException("Not supported yet.");
       }
   }

   @Override
   public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.load();
        }catch (PersistenceException e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
   }

   @Override
   public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
       try{
           if(clienteDAO.load((int)docu)==null){
               throw new ExcepcionServiciosAlquiler("El cliente no existe") ;
           }
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           calendar.add(Calendar.DAY_OF_YEAR, numdias);
           clienteDAO.save((int)docu,item.getId(),date,new java.sql.Date(calendar.getTime().getTime()));
       }  catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al agregar item rentado al cliente");
       }
   }

   @Transactional
   @Override
   public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
        try{
            clienteDAO.saveCliente(c);
        }catch (PersistenceException e) {
            throw new UnsupportedOperationException("Ni modo.");
        }
   }

   @Override
   public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }
   @Override
   public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.registrarItem(i);
        }catch (PersistenceException e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
   }

   @Override
   public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}