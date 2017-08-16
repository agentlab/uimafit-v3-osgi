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
package org.apache.uima.fit.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.component.Resource_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.type.Sentence;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.junit.Test;

/**
 */
public class SimplePipelineTest {

  public static final String SENTENCE_TEXT = "Some text";

  public static class Reader extends JCasCollectionReader_ImplBase {

    @ExternalResource(mandatory=false)
    private static DummySharedResource resource;
    
    private int size = 1;

    private int current = 0;

    private boolean initTypeSystemCalled = false;
    
    @Override
    public void typeSystemInit(TypeSystem aTypeSystem) throws ResourceInitializationException {
      initTypeSystemCalled = true;
    }
    
    public Progress[] getProgress() {
      return null;
    }

    public boolean hasNext() throws IOException, CollectionException {
      assertTrue("typeSystemInit() has not been called", initTypeSystemCalled);
      return this.current < this.size;
    }

    @Override
    public void getNext(JCas jCas) throws IOException, CollectionException {
      jCas.setDocumentText(SENTENCE_TEXT);
      this.current += 1;
    }

  }

  public static class Annotator extends JCasAnnotator_ImplBase {
    @ExternalResource(mandatory=false)
    private static DummySharedResource resource;

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
      String text = jCas.getDocumentText();
      Sentence sentence = new Sentence(jCas, 0, text.length());
      sentence.addToIndexes();
    }

  }

  public static class Writer extends JCasAnnotator_ImplBase {

    public static List<String> SENTENCES = new ArrayList<String>();

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
      super.initialize(context);
      SENTENCES = new ArrayList<String>();
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
      for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
        SENTENCES.add(sentence.getCoveredText());
      }
    }

  }

  public static final class DummySharedResource extends Resource_ImplBase {
  }

  @Test
  public void testWithInstances() throws Exception {
    SimplePipeline.runPipeline(createReader(Reader.class),
            createEngine(Annotator.class),
            createEngine(Writer.class));
    assertEquals(Arrays.asList(SENTENCE_TEXT), Writer.SENTENCES);
  }

  @Test
  public void testWithDescriptors() throws Exception {
    SimplePipeline.runPipeline(createReaderDescription(Reader.class),
            createEngineDescription(Annotator.class),
            createEngineDescription(Writer.class));
    assertEquals(Arrays.asList(SENTENCE_TEXT), Writer.SENTENCES);
  }

  @Test
  public void testResourceSharing() throws Exception {
    Reader.resource = null;
    Annotator.resource = null;
    
    ExternalResourceDescription res = createExternalResourceDescription(DummySharedResource.class);
    SimplePipeline.runPipeline(createReaderDescription(Reader.class, "resource", res),
            createEngineDescription(Annotator.class, "resource", res));
    
    assertNotNull(Reader.resource);
    assertNotNull(Annotator.resource);
    assertTrue(Reader.resource == Annotator.resource);
    
    Reader.resource = null;
    Annotator.resource = null;
  }
}
