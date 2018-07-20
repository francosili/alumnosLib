/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencias;

import entidades.Parametro;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Usuario
 */
@Stateless
public class ParametroFacade extends AbstractFacade<Parametro> {

    @PersistenceContext(unitName = "franco_AlumnosLib_ejb_0.1alphaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametroFacade() {
        super(Parametro.class);
    }
    
     public String get(String s){
        List<Parametro> resultados = this.getEntityManager()
                            .createNamedQuery("Parametro.findByNombreParametro")
                            .setParameter("nombreParametro", s)
                            .getResultList();
        
        //Caso no está el parametro, según el entitymanager.
        if(resultados == null || resultados.size() == 0)
            return null;
        
        //Debería haber uno solo, el nombre es unique.
        return resultados.get(0).getValor();
    }
    
}
