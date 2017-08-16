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
package org.apache.uima.fit.osgi.utils;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;

/**
 * A collection of static methods for creating UIMA {@link AnalysisEngineDescription
 * AnalysisEngineDescriptions} and {@link AnalysisEngine AnalysisEngines}.
 *
 * @see <a href="package-summary.html#InstancesVsDescriptors">Why are descriptors better than
 *      component instances?</a>
 */
public class AnalysisEngineFactoryOSGi {

	public static AnalysisEngine createEngine(AnalysisEngineDescription desc, ResourceManager resourceManager) throws ResourceInitializationException {
		return UIMAFramework.produceAnalysisEngine(desc, resourceManager, null);
	}

	public static AnalysisEngine createEngine(Class<? extends AnalysisComponent> componentClass, ResourceManager resourceManager, Object... configurationData) throws ResourceInitializationException {
		AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(componentClass, configurationData);

		// create the AnalysisEngine, initialize it and return it
		return createEngine(desc, resourceManager);
	}
}
