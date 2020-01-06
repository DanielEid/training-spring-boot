package com.ecommerce.microcommerce.web.controller;

import static org.junit.Assert.*;

import com.ecommerce.microcommerce.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ProductControllerTest {

    private String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listeProduits() {

        ResponseEntity request200 = this.restTemplate.getForEntity(baseUrl + port + "/Produits", String.class);
        assertEquals("200",request200.getStatusCode().toString());
    }

    @Test
    public void afficherUnProduit() {
        ResponseEntity request200 = this.restTemplate.getForEntity(baseUrl + port + "/Produits/1", String.class);
        assertEquals("200",request200.getStatusCode().toString());

        ResponseEntity request404 = this.restTemplate.getForEntity(baseUrl + port + "/Produits/4", String.class);
        assertEquals("404",request404.getStatusCode().toString());    }

    @Test
    public void calculerMargeProduit() {
        //ResponseEntity requestUnAuth = this.restTemplate.getForEntity(baseUrl + port + "/AdminProduits", String.class);
        //assertEquals("200",requestUnAuth.getStatusCode().toString());

    }

    @Test
    public void trierProduitsParOrdreAlphabetique() {
        ResponseEntity request200 = this.restTemplate.getForEntity(baseUrl + port + "/ProduitsTries", String.class);
        assertEquals("200",request200.getStatusCode().toString());

    }

    @Test
    public void ajouterProduit() {
        Product bodyRequestCreated = new Product(5, "Produit", 10, 10);
        ResponseEntity request201 = this.restTemplate.postForEntity(baseUrl + port + "/Produits", bodyRequestCreated, String.class);
        assertEquals("201",request201.getStatusCode().toString()); //Created

        Product bodyRequestFree = new Product(5, "Produit", 0, 0);
        ResponseEntity request400 = this.restTemplate.postForEntity(baseUrl + port + "/Produits", bodyRequestFree, String.class);
        assertEquals("400",request400.getStatusCode().toString()); //NotAcceptable, free

    }

    @Test
    public void supprimerProduit() {
        ResponseEntity request200 = this.restTemplate.exchange(baseUrl + port + "/Produits/1", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertEquals("200",request200.getStatusCode().toString());

        ResponseEntity request500 = this.restTemplate.exchange(baseUrl + port + "/Produits/4", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertEquals("500",request500.getStatusCode().toString());

    }

    @Test
    public void updateProduit() {
        Product bodyRequestOk = new Product(1, "Produit", 10, 10);
        ResponseEntity request200 = this.restTemplate.exchange(baseUrl + port + "/Produits/", HttpMethod.PUT, new HttpEntity<>(bodyRequestOk), String.class);
        assertEquals("200",request200.getStatusCode().toString());
    }

    @Test
    public void testeDeRequetes() {
        ResponseEntity request200 = this.restTemplate.getForEntity(baseUrl + port + "/test/produits/100", String.class);
        assertEquals("200",request200.getStatusCode().toString());

       // ResponseEntity request404= this.restTemplate.getForEntity(baseUrl + port + "/test/produits/1000", String.class);//assertEquals("400",request404.getStatusCode().toString()); //Not found
    }
}