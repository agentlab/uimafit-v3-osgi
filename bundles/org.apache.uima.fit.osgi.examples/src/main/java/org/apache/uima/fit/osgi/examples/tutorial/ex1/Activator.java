package org.apache.uima.fit.osgi.examples.tutorial.ex1;

import static org.apache.uima.fit.osgi.utils.AnalysisEngineFactoryOSGi.createEngine;
import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.admin.TypeSystemMgr;
import org.apache.uima.cas.impl.FSClassRegistry;
import org.apache.uima.fit.internal.ResourceManagerFactory;
import org.apache.uima.fit.osgi.examples.tutorial.type.RoomNumber;
import org.apache.uima.fit.osgi.utils.UIMABundleActivator;
import org.apache.uima.fit.osgi.utils.cl.ClerezzaUIMAExtensionClassLoader;
import org.apache.uima.fit.osgi.utils.cl.UIMAClassLoaderRepository;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class Activator extends UIMABundleActivator {
	@Reference(policy=ReferencePolicy.STATIC)
	protected UIMAClassLoaderRepository classLoaderRepository;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Activate
	public void start(BundleContext context) throws Exception {
		super.start(context);

		String text = "The meeting was moved from Yorktown 01-144 to Hawthorne 1S-W33.";

		FSClassRegistry ff;

		ResourceManager resourceManager = ResourceManagerFactory.newResourceManager();
	    ClassLoader cl = getClass().getClassLoader();
	    ClassLoader ccl = new ClerezzaUIMAExtensionClassLoader(cl, classLoaderRepository.getComponents());
	    resourceManager.setExtensionClassPath(ccl, "*", true);

	    //List<TypeSystemDescription> tsdList = new ArrayList<TypeSystemDescription>();
	    //TypeSystemDescription tsd = mergeTypeSystems(tsdList, resourceManager);

	    AnalysisEngine analysisEngine = createEngine(RoomNumberAnnotator.class, resourceManager);

	    JCas jCas = analysisEngine.newJCas();
	    jCas.setDocumentText(text);

	    //jCas.
	    //jCas.getCasImpl().commitTypeSystem();

	    //initTypeSystem(tsd, jCas);
	    analysisEngine.process(jCas);

	    for (RoomNumber roomNumber : select(jCas, RoomNumber.class)) {
	      System.out.println(roomNumber.getCoveredText() + "\tbuilding = " + roomNumber.getBuilding());
	    }
	}

	protected void classRegistered() {
		classLoaderRepository.registerComponent(RoomNumberAnnotator.class);
	}

	/**
	 * TODO JavaDoc
	 *
	 * @param tsd
	 * @param jCas
	 */
	private void initTypeSystem(JCas jCas) {
		TypeSystemDescription tsd = UIMAFramework.getResourceSpecifierFactory()
            .createTypeSystemDescription();

		TypeDescription type = tsd.addType("MyType", "", CAS.TYPE_NAME_TOP);
		TypeSystem ts = jCas.getTypeSystem();
	    TypeSystemMgr tmr = (TypeSystemMgr)ts;
	    //tmr.

	    tmr.addType("building", ts.getType(CAS.TYPE_NAME_STRING));
	    //tmr.add("building", ts.getType(CAS.TYPE_NAME_STRING));
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
