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

import static org.apache.uima.UIMAFramework.getXMLParser;
import static org.apache.uima.fit.descriptor.OperationalProperties.MODIFIES_CAS_DEFAULT;
import static org.apache.uima.fit.descriptor.OperationalProperties.MULTIPLE_DEPLOYMENT_ALLOWED_DEFAULT;
import static org.apache.uima.fit.descriptor.OperationalProperties.OUTPUTS_NEW_CASES_DEFAULT;
import static org.apache.uima.fit.factory.ConfigurationParameterFactory.createConfigurationData;
import static org.apache.uima.fit.factory.ConfigurationParameterFactory.ensureParametersComeInPairs;
import static org.apache.uima.fit.factory.ConfigurationParameterFactory.setParameters;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindExternalResource;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDependencies;
import static org.apache.uima.fit.factory.FsIndexFactory.createFsIndexCollection;
import static org.apache.uima.fit.factory.TypePrioritiesFactory.createTypePriorities;
import static org.apache.uima.util.CasCreationUtils.mergeTypeSystems;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.LogFactory;
import org.apache.uima.Constants;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.UimaContextAdmin;
import org.apache.uima.UimaContextHolder;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.fit.factory.CapabilityFactory;
import org.apache.uima.fit.factory.ConfigurationParameterFactory.ConfigurationData;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.FsIndexFactory;
import org.apache.uima.fit.factory.ResourceCreationSpecifierFactory;
import org.apache.uima.fit.factory.ResourceMetaDataFactory;
import org.apache.uima.fit.internal.ReflectionUtil;
import org.apache.uima.fit.internal.ResourceManagerFactory;
import org.apache.uima.fit.internal.ResourceManagerFactory.ResourceManagerCreator;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.FsIndexCollection;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

/**
 * A collection of static methods for creating UIMA {@link AnalysisEngineDescription
 * AnalysisEngineDescriptions} and {@link AnalysisEngine AnalysisEngines}.
 *
 * @see <a href="package-summary.html#InstancesVsDescriptors">Why are descriptors better than
 *      component instances?</a>
 */
public class AnalysisEngineFactoryOSGi {
	public static class DefaultResourceManagerCreator2 implements ResourceManagerCreator {
		protected ResourceManager resourceManager;

		public DefaultResourceManagerCreator2(ResourceManager resourceManager) {
			this.resourceManager = resourceManager;
		}

		public ResourceManager newResourceManager() throws ResourceInitializationException {
			UimaContext activeContext = UimaContextHolder.getContext();
			if (activeContext != null) {
				// If we are already in a UIMA context, then we re-use it. Mind that the JCas cannot
				// handle switching across more than one classloader.
				// This can be done since UIMA 2.9.0 and starts being handled in uimaFIT 2.3.0
				// See https://issues.apache.org/jira/browse/UIMA-5056
				return ((UimaContextAdmin) activeContext).getResourceManager();
			} else {
				return resourceManager;
			}
		}
	}

	/**
	 * Prevent class instantiation
	 */
	private AnalysisEngineFactoryOSGi() {}

	public static AnalysisEngine createEngine(Class<? extends AnalysisComponent> componentClass,
	          Object... configurationData) throws ResourceInitializationException, MalformedURLException {

		ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();
		ClassLoader cl = componentClass.getClassLoader();
		resourceManager.setExtensionClassPath(cl, "", true);
		ResourceManagerFactory.setResourceManagerCreator(new DefaultResourceManagerCreator2(resourceManager));

	    AnalysisEngineDescription desc = createEngineDescription(componentClass, configurationData);

	    // create the AnalysisEngine, initialize it and return it
	    return createEngine(desc);
	  }

	public static AnalysisEngine createEngine(AnalysisEngineDescription desc,
	          Object... configurationData) throws ResourceInitializationException {
	    if (configurationData == null || configurationData.length == 0) {
	      return UIMAFramework.produceAnalysisEngine(desc, ResourceManagerFactory.newResourceManager(),
	              null);
	    } else {
	      AnalysisEngineDescription descClone = (AnalysisEngineDescription) desc.clone();
	      ResourceCreationSpecifierFactory.setConfigurationParameters(descClone, configurationData);
	      return UIMAFramework.produceAnalysisEngine(descClone,
	              ResourceManagerFactory.newResourceManager(), null);
	    }
	  }

	public static AnalysisEngineDescription createEngineDescription(
	          Class<? extends AnalysisComponent> componentClass, Object... configurationData)
	          throws ResourceInitializationException {
	    TypeSystemDescription typeSystem = createTypeSystemDescription();
	    TypePriorities typePriorities = createTypePriorities();
	    FsIndexCollection fsIndexCollection = createFsIndexCollection();

	    return createEngineDescription(componentClass, typeSystem, typePriorities, fsIndexCollection,
	            (Capability[]) null, configurationData);
	  }

	public static AnalysisEngineDescription createEngineDescription(
	          Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem,
	          TypePriorities typePriorities, FsIndexCollection indexes, Capability[] capabilities,
	          Object... configurationData) throws ResourceInitializationException {

	    ensureParametersComeInPairs(configurationData);

	    // Extract ExternalResourceDescriptions from configurationData
	    // <ParamterName, ExternalResourceDescription> will be stored in this map
	    Map<String, ExternalResourceDescription> externalResources = ExternalResourceFactory
	            .extractExternalResourceParameters(configurationData);

	    // Create primitive description normally
	    ConfigurationData cdata = createConfigurationData(configurationData);
	    return createEngineDescription(componentClass, typeSystem, typePriorities, indexes,
	            capabilities, cdata.configurationParameters, cdata.configurationValues,
	            externalResources);
	  }

	public static AnalysisEngineDescription createEngineDescription(
	          Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem,
	          TypePriorities typePriorities, FsIndexCollection indexes, Capability[] capabilities,
	          ConfigurationParameter[] configurationParameters, Object[] configurationValues,
	          Map<String, ExternalResourceDescription> externalResources)
	          throws ResourceInitializationException {

	    // create the descriptor and set configuration parameters
	    AnalysisEngineDescription desc = UIMAFramework.getResourceSpecifierFactory()
	            .createAnalysisEngineDescription();
	    desc.setFrameworkImplementation(Constants.JAVA_FRAMEWORK_NAME);
	    desc.setPrimitive(true);
	    desc.setAnnotatorImplementationName(componentClass.getName());
	    org.apache.uima.fit.descriptor.OperationalProperties componentAnno = ReflectionUtil
	            .getInheritableAnnotation(org.apache.uima.fit.descriptor.OperationalProperties.class,
	                    componentClass);
	    if (componentAnno != null) {
	      OperationalProperties op = desc.getAnalysisEngineMetaData().getOperationalProperties();
	      op.setMultipleDeploymentAllowed(componentAnno.multipleDeploymentAllowed());
	      op.setModifiesCas(componentAnno.modifiesCas());
	      op.setOutputsNewCASes(componentAnno.outputsNewCases());
	    } else {
	      OperationalProperties op = desc.getAnalysisEngineMetaData().getOperationalProperties();
	      op.setMultipleDeploymentAllowed(MULTIPLE_DEPLOYMENT_ALLOWED_DEFAULT);
	      op.setModifiesCas(MODIFIES_CAS_DEFAULT);
	      op.setOutputsNewCASes(OUTPUTS_NEW_CASES_DEFAULT);
	    }

	    // Configure resource meta data
	    AnalysisEngineMetaData meta = desc.getAnalysisEngineMetaData();
	    ResourceMetaDataFactory.configureResourceMetaData(meta, componentClass);

	    // set parameters
	    setParameters(desc, componentClass, configurationParameters, configurationValues);

	    // set the type system
	    if (typeSystem != null) {
	      desc.getAnalysisEngineMetaData().setTypeSystem(typeSystem);
	    }

	    if (typePriorities != null) {
	      desc.getAnalysisEngineMetaData().setTypePriorities(typePriorities);
	    }

	    // set indexes from the argument to this call and from the annotation present in the
	    // component
	    List<FsIndexCollection> fsIndexes = new ArrayList<FsIndexCollection>();
	    if (indexes != null) {
	      fsIndexes.add(indexes);
	    }
	    fsIndexes.add(FsIndexFactory.createFsIndexCollection(componentClass));
	    FsIndexCollection aggIndexColl = CasCreationUtils.mergeFsIndexes(fsIndexes,
	            ResourceManagerFactory.newResourceManager());
	    desc.getAnalysisEngineMetaData().setFsIndexCollection(aggIndexColl);

	    // set capabilities from the argument to this call or from the annotation present in the
	    // component if the argument is null
	    if (capabilities != null) {
	      desc.getAnalysisEngineMetaData().setCapabilities(capabilities);
	    } else {
	      Capability capability = CapabilityFactory.createCapability(componentClass);
	      if (capability != null) {
	        desc.getAnalysisEngineMetaData().setCapabilities(new Capability[] { capability });
	      }
	    }

	    // Extract external resource dependencies
	    desc.setExternalResourceDependencies(createExternalResourceDependencies(componentClass));

	    // Bind External Resources
	    if (externalResources != null) {
	      for (Entry<String, ExternalResourceDescription> e : externalResources.entrySet()) {
	        bindExternalResource(desc, e.getKey(), e.getValue());
	      }
	    }

	    return desc;
	  }

	public static TypeSystemDescription createTypeSystemDescription()
	          throws ResourceInitializationException {
	    List<TypeSystemDescription> tsdList = new ArrayList<TypeSystemDescription>();
	    for (String location : MyBundleTracker.getLocations()) {//scanTypeDescriptors()) {
	      try {
	        XMLInputSource xmlInputType1 = new XMLInputSource(location);
	        tsdList.add(getXMLParser().parseTypeSystemDescription(xmlInputType1));
	        LogFactory.getLog(TypeSystemDescription.class).debug(
	                "Detected type system at [" + location + "]");
	      } catch (IOException e) {
	        throw new ResourceInitializationException(e);
	      } catch (InvalidXMLException e) {
	        LogFactory.getLog(TypeSystemDescription.class).warn(
	                "[" + location + "] is not a type file. Ignoring.", e);
	      }
	    }

	    ResourceManager resMgr = ResourceManagerFactory.newResourceManager();
	    return mergeTypeSystems(tsdList, resMgr);
	  }
}
