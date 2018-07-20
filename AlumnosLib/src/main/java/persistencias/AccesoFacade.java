/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencias;

import entidades.Acceso;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Usuario
 */
@Stateless
public class AccesoFacade extends AbstractFacade<Acceso> {

    @Inject ParametroFacade parametro;
    
    @PersistenceContext(unitName = "franco_AlumnosLib_ejb_0.1alphaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccesoFacade() {
        super(Acceso.class);
    }
    
    public Acceso pedirToken(Usuario usuario) {
        
        //Pido todos los tokens del usuario.
        List<Acceso> toks = new ArrayList<>(usuario.getAccesoCollection());
        
        //Los ordeno por fecha de vencimiento.
        Collections.sort(toks, new Comparator<Acceso>(){
            @Override
            public int compare(Acceso o1, Acceso o2) {
                return o1.getFechaDesde().compareTo(o2.getFechaHasta());
            }
        });
        
        //Separo en vencidos y vigentes.
        List<Acceso> tokensVencidos = new ArrayList<>();
        List<Acceso> tokensVigentes = new ArrayList<>();
        
        Date hoy = new Date();
        if(!toks.isEmpty()) {
            for(Acceso ut : toks){
                if(ut.getFechaHasta().before(hoy))
                    tokensVencidos.add(ut);
                else
                    tokensVigentes.add(ut);
            }
        }
        
        if(!tokensVencidos.isEmpty()) {
            //Borro los tokens vencidos.
            for(Acceso ut : tokensVencidos){
                ut = em.merge(ut);
                em.remove(ut);
            }
        }
        //Defino el token que voy a terminar devolviendo
        Acceso ut;
        //Si hay un token vigente, lo doy. Por el momento no hay
        //ningún requerimineto sobre cual token vigente hay que dar.
        
        if(tokensVigentes.size() > 0){
            ut = tokensVigentes.get(0);
            
        } else {

            //Si no hay, lo creo
            ut = new Acceso();
            ut.setUsuario(usuario);
            ut.setFechaDesde(hoy);

            //Calculo la fecha de vigencia:
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(hoy);
            calendario.add(Calendar.HOUR, Integer.parseInt(parametro.get("DURACION_TOKEN")));
            ut.setFechaHasta(calendario.getTime());

            //Genero el hashId y lo guardo.
            String hashed = BCrypt.hashpw(usuario.getClave(), BCrypt.gensalt());
            //ver que la respuesta no tenga el caracter / en el string, si lo tiene pedir nuevamente
            while ( hashed.contains("/")){  
                hashed = BCrypt.hashpw(usuario.getClave(), BCrypt.gensalt());
            }
            ut.setToken(hashed);

            //Guardo el token
            em.persist(ut);
            em.flush();
        }
        //Actualizo el token
        em.merge(ut);
        return ut;
    }
    
    public boolean validarToken(Acceso token, Usuario usuario) {
        if(token == null) {
            return false;
        }
        if(token.getFechaHasta().before(new Date())){
            return false;
        }
        return Objects.equals(token.getUsuario(), usuario);
    }
    
    public Acceso findByToken(String token) {
        Acceso ret;
        try {

            ret = (Acceso) em.createNamedQuery("Acceso.findByToken").setParameter("token", token).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
        return ret;
    }
    
}
