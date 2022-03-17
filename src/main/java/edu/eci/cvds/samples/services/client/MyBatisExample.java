/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cvds.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;

import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejemplo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();

        
        //Crear el mapper y usarlo: 
        //ClienteMapper cm=sqlss.getMapper(ClienteMapper.class)
        //cm...
        ClienteMapper cm=sqlss.getMapper(ClienteMapper.class);
        System.out.println(cm.consultarClientes());
        System.out.println("--");
        int documentoPrueba = 1234567899;
        System.out.println(cm.consultarCliente(documentoPrueba));
        System.out.println("--");
        //cm.agregarItemRentadoACliente(1121212121,1, Date.valueOf("2022-03-16"), Date.valueOf("2022-03-20"));
        int documentoPrueba1 = 1121212121;
        System.out.println(cm.consultarCliente(documentoPrueba1));
        System.out.println("--");
        ItemMapper im = sqlss.getMapper(ItemMapper.class);
        //Date dateItem = DateFormat.parse("2022/01/20");
        im.insertarItem(new Item(new TipoItem(3, "sadasf"), 123456, "fdshdgh", "hytjgm", Date.valueOf("2022-03-20"), 50000, "kiyuil", "kukyk"));

        System.out.println(im.consultarItems());
        System.out.println("--");
        int ItemPrueba = 123456;
        System.out.println(im.consultarItem(ItemPrueba));


        sqlss.commit();
        
        
        sqlss.close();

        
        
    }


}
