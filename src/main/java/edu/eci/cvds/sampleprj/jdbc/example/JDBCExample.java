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
package edu.eci.cvds.sampleprj.jdbc.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="prueba2019";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            /*
            int suCodigoECI=1111110;
            registrarNuevoProducto(con, suCodigoECI, "El Rolo", 45300);
            /*
            suCodigoECI=2165710;
            registrarNuevoProducto(con, suCodigoECI, "Juan Becerra", 16900);
            con.commit();
            */
            evidencia(con,2165781,2165710);

            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        //Crear preparedStatement
        //Asignar parámetros
        //usar 'execute'
        PreparedStatement newProduct;
        String proToInsert = "INSERT INTO ORD_PRODUCTOS " +
                    "VALUES (?,?,?);";
        newProduct = con.prepareStatement(proToInsert);
        newProduct.setInt(1, codigo);
        newProduct.setString(2, nombre);
        newProduct.setInt(3, precio);
        newProduct.executeUpdate();
        con.commit();
        
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return 
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido) throws SQLException{
        List<String> np=new LinkedList<>();
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultados del ResultSet
        //Llenar la lista y retornarla
        PreparedStatement productsOrder;
        String productsToConsult = "SELECT Pr.nombre " +
                "FROM ORD_PRODUCTOS AS Pr " +
                "INNER JOIN ORD_DETALLE_PEDIDO AS Dp " +
                "ON Dp.producto_fk = Pr.codigo " +
                "WHERE Dp.pedido_fk = ? ;";
        productsOrder = con.prepareStatement(productsToConsult);
        productsOrder.setInt(1, codigoPedido);
        ResultSet products = productsOrder.executeQuery();
        while (products.next()){
            np.add(products.getString(1));
        }
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     */
    public static int valorTotalPedido(Connection con, int codigoPedido) throws SQLException{
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultado del ResultSet

        int  total =0;
        PreparedStatement cost = null;

        String productsCost = "SELECT SUM(Po.precio*Op.cantidad) " +
                "FROM ORD_PRODUCTOS Po " +
                "INNER JOIN  ORD_DETALLE_PEDIDO Op " +
                " ON Po.codigo = Op.producto_fk " +
                "WHERE Op.pedido_fk = ? ;";
        cost = con.prepareStatement(productsCost);
        cost.setInt(1, codigoPedido);
        ResultSet resultSet = cost.executeQuery();
        while ( resultSet.next() ){
            total+= resultSet.getInt(1);
        }
        return total;
    }







    public static void evidencia(Connection con, int codigo1, int codigo2) throws SQLException {

        PreparedStatement productosPedido;
        String select = "SELECT Pr.nombre " +
                "FROM ORD_PRODUCTOS AS Pr " +
                "WHERE Pr.codigo IN (?,?);";

        productosPedido = con.prepareStatement(select);
        productosPedido.setInt(1, codigo1);
        productosPedido.setInt(2, codigo2);
        ResultSet resultSet = productosPedido.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }
}
