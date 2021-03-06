package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProductNotFoundException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;


@Api( description="API pour es opérations CRUD sur les produits.")

@RestController
public class ProductController {



    @Autowired
    private ProductDao productDao;




    //Récupérer la liste des produits
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)

    public MappingJacksonValue listeProduits() {

        try {
            Iterable<Product> produits = productDao.findAll();

            SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

            FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

            MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

            produitsFiltres.setFilters(listDeNosFiltres);

            return produitsFiltres;
        }
        catch (Exception err) {
            throw new InternalError(err.getMessage());
        }


    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")

    public Product afficherUnProduit(@PathVariable int id) {

        try {
            Product produit = productDao.findById(id);

            if(produit==null) throw new ProductNotFoundException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

            return produit;
        }

        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }

    }

    //Recupération de la marge d'un produit
    @ApiOperation(value = "Récupérer la marge des produits")
    @GetMapping(value = "/MargeProduits")
    public ResponseEntity calculerMargeProduit() {
        try{
            Map<String, Integer> response = productDao.findAll().stream().collect(Collectors.toMap(Product::toString, Product::getMarge));
            return ResponseEntity.ok(response);
        }
        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }

    }

    //Récupérer produits alphabetiquement
    @ApiOperation(value = "Récupérer produits alphabetiquement")
    @GetMapping(value = "/ProduitsTries")
    public List<Product>  trierProduitsParOrdreAlphabetique() {
        try{
            Sort sort = new Sort(Sort.Direction.ASC, "nom");
            return productDao.findAll(sort);
        }

        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }
    }

    //ajouter un produit
    @PostMapping(value = "/Produits")

    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

      try{
          Product productAdded =  productDao.save(product);


          if(product.getPrix() <=0) throw new com.ecommerce.microcommerce.exceptions.ProduitGratuitException();

          if (productAdded == null)
              return ResponseEntity.noContent().build();

          URI location = ServletUriComponentsBuilder
                  .fromCurrentRequest()
                  .path("/{id}")
                  .buildAndExpand(productAdded.getId())
                  .toUri();

          return ResponseEntity.created(location).build();
      }

        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

       try{
           productDao.delete(id);
       }

        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {

        try{
            productDao.save(product);
        }

        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {

        try {
            return productDao.chercherUnProduitCher(400);

        }
        catch(Exception err) {
            throw new InternalError(err.getMessage());
        }
    }



}
