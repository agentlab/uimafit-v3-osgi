

   
/* Apache UIMA v3 - First created by JCasGen Fri Jul 21 17:28:28 MSK 2017 */

package org.apache.uima.fit.examples.tutorial.type;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jul 21 17:28:28 MSK 2017
 * XML source: C:/git/uimafit-v3-tmp/uimafit-examples/target/jcasgen/typesystem.xml
 * @generated */
public class DateTimeAnnotation extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "org.apache.uima.fit.examples.tutorial.type.DateTimeAnnotation";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DateTimeAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_shortDateString = "shortDateString";


  /* Feature Adjusted Offsets */
  public final static int _FI_shortDateString = TypeSystemImpl.getAdjustedFeatureOffset("shortDateString");

   
  /** Never called.  Disable default constructor
   * @generated */
  protected DateTimeAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public DateTimeAnnotation(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DateTimeAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public DateTimeAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: shortDateString

  /** getter for shortDateString - gets 
   * @generated
   * @return value of the feature 
   */
  public String getShortDateString() { return _getStringValueNc(_FI_shortDateString);}
    
  /** setter for shortDateString - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setShortDateString(String v) {
    _setStringValueNfc(_FI_shortDateString, v);
  }    
    
  }

    