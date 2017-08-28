package org.apache.uima.fit.osgi.examples.tutorial.ex1;

import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.UimaContextAdmin;
import org.apache.uima.UimaContextHolder;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.internal.ResourceManagerFactory;
import org.apache.uima.fit.internal.ResourceManagerFactory.ResourceManagerCreator;
import org.apache.uima.fit.osgi.examples.tutorial.type.RoomNumber;
import org.apache.uima.fit.osgi.utils.AnalysisEngineFactoryOSGi;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class Activator {
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
		        }
		        else {
		          return resourceManager;
		        }
		    }
		  }

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Activate
	public void start(BundleContext context) {
		try {
			System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
			original();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void original()  {
		try {
		    String text = "The meeting was moved from Yorktown 01-144 to Hawthorne 1S-W33.";

		    ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();
		    ClassLoader cl = getClass().getClassLoader();
		    resourceManager.setExtensionClassPath(cl, "", true);
		    ResourceManagerFactory.setResourceManagerCreator(new DefaultResourceManagerCreator2(resourceManager));

		    AnalysisEngine analysisEngine = AnalysisEngineFactoryOSGi.createEngine(RoomNumberAnnotator.class);
		    JCas jCas = analysisEngine.newJCas();
		    jCas.setDocumentText(text);
		    analysisEngine.process(jCas);

		    for (RoomNumber roomNumber : select(jCas, RoomNumber.class)) {
		      System.out.println(roomNumber.getCoveredText() + "\tbuilding = " + roomNumber.getBuilding());
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Deactivate
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye World!!");
	}

}
