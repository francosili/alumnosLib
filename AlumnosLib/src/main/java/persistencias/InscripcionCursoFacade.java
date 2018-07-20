/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencias;

import entidades.InscripcionCurso;
import entidades.Persona;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

/**
 *
 * @author Usuario
 */
@Stateless
public class InscripcionCursoFacade extends AbstractFacade<InscripcionCurso> {

    @PersistenceContext(unitName = "franco_AlumnosLib_ejb_0.1alphaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InscripcionCursoFacade() {
        super(InscripcionCurso.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean setInscripcionNuevo(InscripcionCurso inscripcionCurso) throws NamingException, NotSupportedException, SystemException {
        boolean transaccion = true;
        try {
            em.persist(inscripcionCurso);
            em.flush();
            return transaccion;
        } catch (Exception e){
            e.printStackTrace();
            transaccion = false;
            return transaccion;
        }
    }
}
