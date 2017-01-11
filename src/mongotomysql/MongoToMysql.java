/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongotomysql;

import bean.Usuario;
import dao.serviceDAO.ServiceDAO;
import interfaces.ServiceIF;
import java.util.List;

/**
 *
 * @author mbernedo
 */
public class MongoToMysql {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        ServiceIF serv = new ServiceDAO();
        List<Usuario> lista = serv.getMongo();
        for (Usuario usuario : lista) {
            serv.subirMysql(usuario);
            //System.out.println(usuario.getDate_act() + "----" + usuario.getDate_reg());
        }
    }

}
