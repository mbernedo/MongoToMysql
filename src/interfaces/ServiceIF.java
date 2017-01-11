/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import bean.Usuario;
import java.util.List;

/**
 *
 * @author mbernedo
 */
public interface ServiceIF {
    
    public List<Usuario> getMongo();
    
    public void subirMysql(Usuario usuario);
    
    public List<Usuario> getUser();
    
}
