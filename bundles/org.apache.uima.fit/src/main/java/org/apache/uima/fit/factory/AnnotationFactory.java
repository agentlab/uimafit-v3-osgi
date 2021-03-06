/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.uima.fit.factory;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * Convenience methods to create annotations
 */
public final class AnnotationFactory {
  private AnnotationFactory() {
    // This class is not meant to be instantiated
  }

  /**
   * Provides a convenient way to create an annotation and addToIndexes in a single line.
   * 
   * @param <T>
   *          the annotation type
   * @param jCas
   *          the JCas to create the annotation in
   * @param begin
   *          the begin offset
   * @param end
   *          the end offset
   * @param cls
   *          the annotation class as generated by JCasGen
   * @return the new annotation
   */
  public static <T extends Annotation> T createAnnotation(JCas jCas, int begin, int end,
          Class<T> cls) {
    @SuppressWarnings("unchecked")
    T annotation = (T) jCas.getCas().createAnnotation(JCasUtil.getAnnotationType(jCas, cls), begin,
            end);
    annotation.addToIndexes();
    return annotation;
  }
}
