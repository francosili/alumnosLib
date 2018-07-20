/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencias;

import entidades.Alumno;
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
public class AlumnoFacade extends AbstractFacade<Alumno> {

    @PersistenceContext(unitName = "franco_AlumnosLib_ejb_0.1alphaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlumnoFacade() {
        super(Alumno.class);
    }
    
     public Alumno getByLegajo(String legajo) {
        Alumno ret;
        try {
            ret = (Alumno) em.createNamedQuery("Alumno.findByLegajo").setParameter("legajo", legajo).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
        return ret;
    }
     
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean setAlumnoNuevo(Alumno alumno) throws NamingException, NotSupportedException, SystemException {
        boolean transaccion = true;
        try {
            em.persist(alumno);
            em.flush();
            return transaccion;
        } catch (Exception e){
            e.printStackTrace();
            transaccion = false;
            return transaccion;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean editAlumno(Alumno alumno) throws NamingException, NotSupportedException, SystemException {
        boolean transaccion = true;
        try {
            em.merge(alumno);
            em.flush();
            return transaccion;
        } catch (Exception e){
            e.printStackTrace();
            transaccion = false;
            return transaccion;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteAlumno(Alumno alumno) throws NamingException, NotSupportedException, SystemException {
        boolean transaccion = true;
        try {
            em.remove(alumno);
            em.flush();
            return transaccion;
        } catch (Exception e){
            e.printStackTrace();
            transaccion = false;
            return transaccion;
        }
    } 
}
