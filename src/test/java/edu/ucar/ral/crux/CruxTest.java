/*
 * Copyright (c) 2016. University Corporation for Atmospheric Research (UCAR). All rights reserved.
 */

package edu.ucar.ral.crux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for different validation methods using Crux
 */
public class CruxTest {
  @Test
  public void testCruxLocal() throws Exception{
    Crux crux = new Crux();
    crux.validate( null, null, "src/test/resources/shiporder-pass1.xml" );
  }

  @Test
  public void testCruxLocalWithCatalog() throws Exception{
    Crux crux = new Crux();
    crux.validate( "src/test/resources/gml-system-catalog.xml", null, "src/test/resources/simplegml.xml" );
  }

  @Test
  public void testCruxLocalWithRelativeCatalog() throws Exception{
    Crux crux = new Crux();
    crux.validate( "src/test/resources/gml-rewritesystem-catalog.xml", null, "src/test/resources/simplegml.xml" );
  }

  @Test
  public void testCruxLocalMultQuestionFiles() throws Exception{
    Crux crux = new Crux();
    //should match shiporder-pass1.xml and shiporder-pass2.xml
    crux.validate( null, null, "src/test/resources/shiporder-pass?.xml" );
  }

  @Test
  public void testCruxLocalMultStarFiles() throws Exception{
    Crux crux = new Crux();
    //should match shiporder-pass1.xml and shiporder-pass2.xml
    crux.validate( null, null, "src/test/resources/shiporder-p*.xml" );
  }

  @Test
  public void testCruxBothLocal() throws Exception{
    Crux crux = new Crux();
    crux.validate( null, "src/test/resources/shiporder.sch", "src/test/resources/shiporder-pass1.xml" );
  }

  @Test
  public void testCruxSchemaFail() throws Exception{
    Crux crux = new Crux();
    try {
      crux.validate( null, null, "src/test/resources/shiporder-fail-schema.xml" );
    }catch( ValidationException e ){
      Assert.assertEquals( "Unexpected number of validation failures", 3, e.getValidationErrors().size() );
      return;
    }
    Assert.fail("Should have encountered validation failures");
  }

  @Test
  public void testCruxSchematronFail() throws Exception{
    Crux crux = new Crux();
    try{
    crux.validate( null, "src/test/resources/shiporder.sch", "src/test/resources/shiporder-fail-schematron.xml" );
    }catch( ValidationException e ){
      junit.framework.Assert.assertEquals( "Incorrect # of validation failures", 3, e.getValidationErrors().size() );
      junit.framework.Assert.assertEquals( "Incorrect schematron failure message 1", "Ship to name and address must both be present ((if(shiporder:name) then( shiporder:address ) else true()))", e.getValidationErrors().get( 0 ).getError() );
      junit.framework.Assert.assertTrue( "Incorrect schematron failure message 2", e.getValidationErrors().get( 1 ).getError().contains( "Item price cannot exceed 10" ) );
      junit.framework.Assert.assertTrue( "Incorrect schematron failure message 3", e.getValidationErrors().get( 2 ).getError().contains( "Item quantity must be present" ) );
      return;
    }
    Assert.fail("Should have encountered validation failures");
  }

}