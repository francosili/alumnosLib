/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "InscripcionesCarreras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InscripcionCarrera.findAll", query = "SELECT i FROM InscripcionCarrera i"),
    @NamedQuery(name = "InscripcionCarrera.findByIdInscripcionCarrera", query = "SELECT i FROM InscripcionCarrera i WHERE i.idInscripcionCarrera = :idInscripcionCarrera"),
    @NamedQuery(name = "InscripcionCarrera.findByFechaInscripcion", query = "SELECT i FROM InscripcionCarrera i WHERE i.fechaInscripcion = :fechaInscripcion")})
public class InscripcionCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idInscripcionCarrera")
    private Integer idInscripcionCarrera;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @JoinColumn(name = "carrera", referencedColumnName = "idCarrera")
    @ManyToOne(optional = false)
    private Carrera carrera;
    @JoinColumn(name = "alumno", referencedColumnName = "idAlumno")
    @ManyToOne(optional = false)
    private Alumno alumno;

    public InscripcionCarrera() {
    }

    public InscripcionCarrera(Integer idInscripcionCarrera) {
        this.idInscripcionCarrera = idInscripcionCarrera;
    }

    public InscripcionCarrera(Integer idInscripcionCarrera, Date fechaInscripcion) {
        this.idInscripcionCarrera = idInscripcionCarrera;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getIdInscripcionCarrera() {
        return idInscripcionCarrera;
    }

    public void setIdInscripcionCarrera(Integer idInscripcionCarrera) {
        this.idInscripcionCarrera = idInscripcionCarrera;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInscripcionCarrera != null ? idInscripcionCarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InscripcionCarrera)) {
            return false;
        }
        InscripcionCarrera other = (InscripcionCarrera) object;
        if ((this.idInscripcionCarrera == null && other.idInscripcionCarrera != null) || (this.idInscripcionCarrera != null && !this.idInscripcionCarrera.equals(other.idInscripcionCarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.InscripcionCarrera[ idInscripcionCarrera=" + idInscripcionCarrera + " ]";
    }
    
}
