

   
/* Apache UIMA v3 - First created by JCasGen Fri Jul 21 17:28:28 MSK 2017 */

package org.apache.uima.fit.osgi.examples.tutorial.type;

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
public class Meeting extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "org.apache.uima.fit.examples.tutorial.type.Meeting";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Meeting.class);
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
   
  public final static String _FeatName_room = "room";
  public final static String _FeatName_date = "date";
  public final static String _FeatName_startTime = "startTime";
  public final static String _FeatName_endTime = "endTime";


  /* Feature Adjusted Offsets */
  public final static int _FI_room = TypeSystemImpl.getAdjustedFeatureOffset("room");
  public final static int _FI_date = TypeSystemImpl.getAdjustedFeatureOffset("date");
  public final static int _FI_startTime = TypeSystemImpl.getAdjustedFeatureOffset("startTime");
  public final static int _FI_endTime = TypeSystemImpl.getAdjustedFeatureOffset("endTime");

   
  /** Never called.  Disable default constructor
   * @generated */
  protected Meeting() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Meeting(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Meeting(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Meeting(JCas jcas, int begin, int end) {
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
  //* Feature: room

  /** getter for room - gets 
   * @generated
   * @return value of the feature 
   */
  public RoomNumber getRoom() { return (RoomNumber)(_getFeatureValueNc(_FI_room));}
    
  /** setter for room - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setRoom(RoomNumber v) {
    _setFeatureValueNcWj(_FI_room, v);
  }    
    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated
   * @return value of the feature 
   */
  public DateAnnotation getDate() { return (DateAnnotation)(_getFeatureValueNc(_FI_date));}
    
  /** setter for date - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDate(DateAnnotation v) {
    _setFeatureValueNcWj(_FI_date, v);
  }    
    
   
    
  //*--------------*
  //* Feature: startTime

  /** getter for startTime - gets 
   * @generated
   * @return value of the feature 
   */
  public TimeAnnotation getStartTime() { return (TimeAnnotation)(_getFeatureValueNc(_FI_startTime));}
    
  /** setter for startTime - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStartTime(TimeAnnotation v) {
    _setFeatureValueNcWj(_FI_startTime, v);
  }    
    
   
    
  //*--------------*
  //* Feature: endTime

  /** getter for endTime - gets 
   * @generated
   * @return value of the feature 
   */
  public TimeAnnotation getEndTime() { return (TimeAnnotation)(_getFeatureValueNc(_FI_endTime));}
    
  /** setter for endTime - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEndTime(TimeAnnotation v) {
    _setFeatureValueNcWj(_FI_endTime, v);
  }    
    
  }

    