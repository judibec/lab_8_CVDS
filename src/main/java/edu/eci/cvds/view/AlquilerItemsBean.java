package edu.eci.cvds.view;

import com.google.inject.Inject;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import org.apache.ibatis.exceptions.PersistenceException;
import org.checkerframework.checker.units.qual.C;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "AlquilerBean")
@SessionScoped
public class AlquilerItemsBean extends BasePageBean{
    @Inject
    private ServiciosAlquiler serviciosAlquiler;
    private Cliente cliente;
    private List<Cliente> listaCli;
    private int costoAlquiler;

    public AlquilerItemsBean(){
        listaCli = new ArrayList<Cliente>();
        costoAlquiler = 0;
    }
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler{
        try{
            List<Cliente> clientes = new ArrayList<Cliente>(listaCli);
            clientes.addAll(serviciosAlquiler.consultarClientes());
            return clientes;
        }catch (Exception e){
            throw new ExcepcionServiciosAlquiler("Error al consultar la tabla");

        }
    }

    public void registrarAlquiler(int idItem, int numDias) throws ExcepcionServiciosAlquiler {
        try{
            Item item = serviciosAlquiler.consultarItem(idItem);
            serviciosAlquiler.registrarAlquilerCliente(new Date(System.currentTimeMillis()), cliente.getDocumento(), item, numDias);
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error ",e.getMessage()));
        }
    }

    public void consultarCosto(int idItem, int numDias) throws ExcepcionServiciosAlquiler{
        costoAlquiler = 0;
        try{
            costoAlquiler = (int)serviciosAlquiler.consultarCostoAlquiler(idItem, numDias);
            System.out.println(costoAlquiler);
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error ",e.getMessage()));
        }
    }

    public long consultarMulta(int idItem) throws ExcepcionServiciosAlquiler{
        return serviciosAlquiler.consultarMultaAlquiler(idItem, new Date(System.currentTimeMillis()));
    }

    public void registrar(String nombre, long documento, String telefono, String direccion, String email) throws ExcepcionServiciosAlquiler{
       try{
           Cliente ncliente = new Cliente(nombre,documento,telefono,direccion,email);
           serviciosAlquiler.registrarCliente(ncliente);
           listaCli.add(ncliente);
       }catch (Exception e){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error Cliente", "ya hay un cliente registrado igual"));
       }
    }

    public void setCliente(Cliente cliente){
        this.cliente=cliente;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public void setCostoAlquiler(int costoAlquiler){
        this.costoAlquiler = costoAlquiler;
    }

    public long getCostoAlquiler(){
        return costoAlquiler;
    }

    public List<ItemRentado> consultarItemsCliente(Cliente cliente) throws ExcepcionServiciosAlquiler{
        return serviciosAlquiler.consultarItemsCliente(cliente.getDocumento());
    }

}
