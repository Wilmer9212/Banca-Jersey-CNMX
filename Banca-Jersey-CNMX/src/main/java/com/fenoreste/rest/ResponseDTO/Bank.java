/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

/**
 *
 * @author wilmer
 */
public class Bank {
    
    Integer bankId;
    String description;
    String routingCode;
    String countryId;
    String headQuartersAddress;

    public Bank() {
    }

    public Bank(Integer bankId, String description, String routingCode, String countryId, String headQuartersAddress) {
        this.bankId = bankId;
        this.description = description;
        this.routingCode = routingCode;
        this.countryId = countryId;
        this.headQuartersAddress = headQuartersAddress;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getHeadQuartersAddress() {
        return headQuartersAddress;
    }

    public void setHeadQuartersAddress(String headquartersAddress) {
        this.headQuartersAddress = headquartersAddress;
    }

   

}
