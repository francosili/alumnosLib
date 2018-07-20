/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencias;

import entidades.Acceso;
import entidades.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Usuario
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "franco_AlumnosLib_ejb_0.1alphaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario getByCuenta(String nombre) {
        Usuario ret;
        try {

            ret = (Usuario) em.createNamedQuery("Usuario.findByNombreUsuario").setParameter("nombreUsuario", nombre).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
        return ret;
    }
    
     public Usuario getByToken(Acceso token) {
        if(token.getUsuario() == null) {
            return null;
        }
        return token.getUsuario();
    }
}
