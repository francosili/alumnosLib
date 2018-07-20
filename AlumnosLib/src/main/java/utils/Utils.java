package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import persistencias.ParametroFacade;

/**
 *
 * @author FrancoSili
 */

@ApplicationScoped
public class Utils {
    @Inject ParametroFacade parametro;
    
    public static JsonObject inputStreamToJson(InputStream inputStream) throws IOException {
        try {
            
            JsonParser parser = new JsonParser();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            br.lines().forEach(linea -> {
                sb.append(linea);
            });

            if (sb.length() > 0) {
                return parser.parse(sb.toString()).getAsJsonObject();
            }
        }
        catch(Exception ex) {
            generarLogError(ex);
            System.out.println(ex);
        }
        
        return null;
        
    }

    public static JsonObject getJsonObjectFromRequest(HttpServletRequest request) throws Exception {
        if (request.getInputStream() == null) {
            throw new Exception("InputStream de request es null");
        }
        
        JsonObject jsonObj = inputStreamToJson(request.getInputStream());
        
        if (jsonObj == null) {
            throw new Exception("inputStreamToJson falló; no se puede pasar el InputStream a Json");
        }
        
        return jsonObj;
    }
    
    public static Serializable getKeyFromJsonObject (
        String clave, 
        JsonObject jsonObj,
        String tipoRespuesta
    ) throws Exception {
        try{
            JsonPrimitive jsonPri = jsonObj.getAsJsonPrimitive(clave);           
            if (jsonPri == null) { 
                return null;
            }
            Serializable claveBody = null;
            switch (tipoRespuesta) {
                case "String":
                    claveBody = jsonPri.getAsString();
                    break;
                case "Integer":
                    claveBody = jsonPri.getAsInt();
                    break;
                case "Boolean":
                    claveBody = jsonPri.getAsBoolean();
                    break;
                case "BigDecimal":
                    claveBody = jsonPri.getAsBigDecimal();
                    break; 
                case "Date":
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(jsonPri.getAsString());
                    claveBody = date;
                    break;
                default:
                    break;
            }
            if (claveBody == null) {
                throw new Exception("No se puede pasar "+clave+" a "+tipoRespuesta);
            }
            return claveBody;
        } catch (Exception e) {
            return null;
        }       
    }
    
    public static List<JsonElement> getKeyFromJsonObjectArray (
        String clave, 
        JsonObject jsonObj,
        String tipoRespuesta
    ) throws Exception {
        try{          
            List<JsonElement> claveBodyArray = new ArrayList<>();
            if (tipoRespuesta.equals("ArrayList")) {
                for(JsonElement j :jsonObj.getAsJsonArray(clave)) {                    
                    System.out.println(j);
                    claveBodyArray.add(j);
                }
            }
            if (claveBodyArray.isEmpty()) {
                throw new Exception("No se puede pasar "+clave+" a "+tipoRespuesta);
            }
            return claveBodyArray;
        } catch (Exception e) {
            return null;
        }       
    }

    public static void generarLogError(Exception ex) {
        
        // Declaro variable auxiliar
        String descripcionError ="";
        
        // Obtengo la causa
        Throwable cause = getRootCause(ex.getCause());
        
        // Valido que no este en null
        if (cause != null) {
            
            // Pregunto si es un error de validacion del bean
            if (cause instanceof ConstraintViolationException) {
                
                // Obtengo la restriccion
                ConstraintViolationException excp = (ConstraintViolationException) cause;
                
                // La recorro imprimiendo el error
                for (ConstraintViolation item : excp.getConstraintViolations()) {

                    System.out.println("----------------------- INICIO VIOLACION ----------------------");
                    descripcionError+="Propiedad: " + item.getPropertyPath().toString() + " ";                    
                    System.out.println("Propiedad: " + item.getPropertyPath().toString());

                    descripcionError+=" - " + item.getMessage() + " ";                    

                    descripcionError+="Valor Invalido: " + item.getInvalidValue() + " ";                    
                    System.out.println("Valor Invalido: " + item.getInvalidValue());                    

                    descripcionError+= "Clase: " + item.getRootBeanClass() + " ";
                    System.out.println("Clase: " + item.getRootBeanClass());

                    System.out.println("Entidad: " + item.getRootBean().toString());                        
                    System.out.println("Item: " + item.toString());                                        
                    System.out.println("------------------------ FIN VIOLACION ------------------------");      

                }
        
                // Imprimo el error en la consola
                System.out.println(descripcionError);
                
            } else {
                System.out.println(cause.getLocalizedMessage());
            }
        }    
    }
    
    /**
     * Este metodo obtiene la causa del error
     * 
     * @param cause
     * @return 
     */
    private static Throwable getRootCause(Throwable cause) {
        if (cause != null) {
            Throwable source = cause.getCause();
            if (source != null) {
                return getRootCause(source);
            } else {
                return cause;
            }
        }
        return null;
    }
    
    // Para usar en filter. Distinc by key
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
    
    // Retorna la fecha de hoy en formato dd-MM-yyyy HH:mm:ss
    public static Date getFechaHoy() {
        try {
            // Busco fecha de hoy
            SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return dateFormater.parse(
                dateFormater.format(new Date())
            );
        }
        catch(Exception ex) {
            generarLogError(ex);
            return null;
        }
    }
    
    public static String getMD5(String entrada) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(entrada.getBytes("UTF-8"));
        BigInteger hash = new BigInteger(1, digest);
        return hash.toString(16);
    }
}
