/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Elliot
 */
@Cacheable(false)
@Embeddable
public class AuxiliaresDPK implements Serializable {

    @Column(name = "idorigenp")
    private int idorigenp;
    @Column(name = "idproducto")
    private int idproducto;
    @Column(name = "idauxiliar")
    private int idauxiliar;
    

    public AuxiliaresDPK() {
    }    

    public AuxiliaresDPK(int idorigenp, int idproducto, int idauxiliar) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
    }

    public int getIdorigenp() {
        return idorigenp;
    }

    public void setIdorigenp(int idorigenp) {
        this.idorigenp = idorigenp;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getIdauxiliar() {
        return idauxiliar;
    }

    public void setIdauxiliar(int idauxiliar) {
        this.idauxiliar = idauxiliar;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idorigenp);
        hash = 67 * hash + Objects.hashCode(this.idproducto);
        hash = 67 * hash + Objects.hashCode(this.idauxiliar);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuxiliaresDPK other = (AuxiliaresDPK) obj;
        if (!Objects.equals(this.idorigenp, other.idorigenp)) {
            return false;
        }
        if (!Objects.equals(this.idproducto, other.idproducto)) {
            return false;
        }
        if (!Objects.equals(this.idauxiliar, other.idauxiliar)) {
            return false;
        }
        return true;
    }

 

    @Override
    public String toString() {
        return "com.fenoreste.modelo.entidad.AuxiliaresDPK[ idorigenp=" + idorigenp + ", idproducto=" + idproducto + ", idauxiliar=" + idauxiliar + " ]";
    }

}
