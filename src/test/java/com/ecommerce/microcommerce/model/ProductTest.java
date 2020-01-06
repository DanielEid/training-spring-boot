package com.ecommerce.microcommerce.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void getMarge() {
        Product product = new Product(4,"bouteille",20,8);
        assertEquals(12,product.getMarge());
    }


}