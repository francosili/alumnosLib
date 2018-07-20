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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "InscripcionesCursos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InscripcionCurso.findAll", query = "SELECT i FROM InscripcionCurso i"),
    @NamedQuery(name = "InscripcionCurso.findByIdInscripcionCurso", query = "SELECT i FROM InscripcionCurso i WHERE i.idInscripcionCurso = :idInscripcionCurso"),
    @NamedQuery(name = "InscripcionCurso.findByFechaInscripcion", query = "SELECT i FROM InscripcionCurso i WHERE i.fechaInscripcion = :fechaInscripcion")})
public class InscripcionCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInscripcionCurso")
    private Integer idInscripcionCurso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @JoinColumn(name = "curso", referencedColumnName = "idCurso")
    @ManyToOne(optional = false)
    private Curso curso;
    @JoinColumn(name = "alumno", referencedColumnName = "idAlumno")
    @ManyToOne(optional = false)
    private Alumno alumno;

    public InscripcionCurso() {
    }

    public InscripcionCurso(Integer idInscripcionCurso) {
        this.idInscripcionCurso = idInscripcionCurso;
    }

    public InscripcionCurso(Integer idInscripcionCurso, Date fechaInscripcion) {
        this.idInscripcionCurso = idInscripcionCurso;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getIdInscripcionCurso() {
        return idInscripcionCurso;
    }

    public void setIdInscripcionCurso(Integer idInscripcionCurso) {
        this.idInscripcionCurso = idInscripcionCurso;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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
        hash += (idInscripcionCurso != null ? idInscripcionCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InscripcionCurso)) {
            return false;
        }
        InscripcionCurso other = (InscripcionCurso) object;
        if ((this.idInscripcionCurso == null && other.idInscripcionCurso != null) || (this.idInscripcionCurso != null && !this.idInscripcionCurso.equals(other.idInscripcionCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.InscripcionCurso[ idInscripcionCurso=" + idInscripcionCurso + " ]";
    }
    
}
