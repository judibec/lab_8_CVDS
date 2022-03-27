import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquilerFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ServiciosAlquilerTest {

//    @Inject
//    private SqlSession sqlSession;

    ServiciosAlquiler serviciosAlquiler;

    public ServiciosAlquilerTest() {
        serviciosAlquiler = ServiciosAlquilerFactory.getInstance().getServiciosAlquilerTesting();
    }

    @Before
    public void setUp() {
        ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
        try {
            serviciosAlquiler.registrarCliente(new Cliente("ClienteConsultado", 2165712, "ClienteConsultado", "ClienteConsultado", "ClienteConsultado"));


        }catch (ExcepcionServiciosAlquiler e){

        }
    }

//    @Test
//    public void emptyDB() {
//        documento = 2165085;
//        for (int i = 0; i < 100; i += 10) {
//            boolean r = false;
//            try {
//                Cliente cliente = serviciosAlquiler.consultarCliente(2165085);
//            } catch (ExcepcionServiciosAlquiler e) {
//                r = true;
//            } catch (IndexOutOfBoundsException e) {
//                r = true;
//            }
//            // Validate no Client was found;
//            Assert.assertTrue(r);
//        };
//    }

    @Test
    public void deberiaConsultarCliente() {
        try {
            Assert.assertEquals("ClienteConsultado", serviciosAlquiler.consultarCliente(2165712).getNombre());
        } catch (Exception e) {
            Assert.fail("Error al consultar Cliente");
        }
    }

    @Test
    public void deberiaConsultarItem() {
        try {
            Item item = new Item(new TipoItem(80, "SoyPrueba"), 80, "SoyPruebaItem", "SoyPrueba", new SimpleDateFormat("yyyy/MM/dd").parse("2022/12/01"), 20, "Prueba", "Prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals("SoyPruebaItem", serviciosAlquiler.consultarItem(80).getNombre());
        } catch (Exception e) {
            Assert.fail("Error al consultar item");
        }
    }

    @Test
    public void deberiaConsultarTipoItem() {
        try {
            Item item = new Item(new TipoItem(30, "SoyPruebaTipo"), 30, "SoyPruebaTipo", "SoyPrueba", new SimpleDateFormat("yyyy/MM/dd").parse("2022/12/01"), 20, "Prueba", "Prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals("SoyPruebaTipo", serviciosAlquiler.consultarTipoItem(30).getDescripcion());
        } catch (Exception e) {
            Assert.fail("Error al consultar tipo de item");
        }
    }

    @Test
    public void noDeberiaConsultarElCostoDeUnItemDesconocido() {
        try {
            Assert.assertEquals(101010101, serviciosAlquiler.consultarCostoAlquiler(91919191, 818181));
            Assert.fail("No entro a la excepcion");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
    @Test
    public void deberiaConsultarTarifaxDia(){
        try {
            Item item = new Item(new TipoItem(1, "SoyPruebaTarifa" ),6,
                    "prueba", "pruebatarifa", new SimpleDateFormat("yyyy/MM/dd").parse("2022/10/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals(5000, serviciosAlquiler.valorMultaRetrasoxDia(6));
        } catch (Exception e) {
            Assert.fail("Error al consultar tarifas");
        }
    }

    @Test
    public void deberiaActualizarTarifaItem(){
        try {
            Item item = new Item(new TipoItem(1, "pruebaAcTarifa" ),2,
                    "prueba", "pruebaAcTarifa", new SimpleDateFormat("yyyy/MM/dd").parse("2022/10/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            serviciosAlquiler.actualizarTarifaItem(2, 200);
            Assert.assertEquals(200, serviciosAlquiler.consultarItem(2).getTarifaxDia());
        } catch (Exception e) {
            Assert.fail("Entro a la excepcion");
        }
    }


    @Test
    public void noDeberiaRegistrarAlquilerDeClienteQueNoExiste(){
        try {
            Item item = new Item(new TipoItem(8, "pruebaClienteN" ),10,
                    "prueba", "pruebaClienteN", new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/04"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(LocalDate.parse("2022-10-04")) , serviciosAlquiler.consultarCliente(1010101010).getDocumento() , item , 4 );
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void deberiRegistrarItemAlCliente() throws ExcepcionServiciosAlquiler {
        try {
            Item item = new Item(new TipoItem(8, "pruebaItemCliente" ),1010,
                    "prueba", "pruebaItemCliente", new SimpleDateFormat("yyyy/MM/dd").parse("2022/10/04"),
                    100,"prueba","prueba");
            Cliente cliente = new Cliente("prueba", 1989, "prueba", "prueba","prueba");
            serviciosAlquiler.registrarCliente(cliente);
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(LocalDate.parse("2022-10-04")) , serviciosAlquiler.consultarCliente(1989).getDocumento() , item , 4 );
            Assert.assertEquals(Date.valueOf(LocalDate.parse("2022-10-04")), serviciosAlquiler.consultarCliente(1989).getRentados().get(0).getFechainiciorenta());
        } catch (Exception e) {
            Assert.fail("Error al registrar item al cliente");
        }
    }

    @Test
    public void deberiaVetarCliente(){
        try {
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("PruebaClienteVetado",7775,"telefono","direccion","email",false,itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            serviciosAlquiler.vetarCliente(7775,true);
            Assert.assertTrue(serviciosAlquiler.consultarCliente(7775).isVetado());
        } catch (Exception e) {
            Assert.fail("Error al vetar cliente");
        }
    }
}