/**
 *
 */
package org.apache.uima.fit.osgi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.uima.fit.internal.MetaDataType;
import org.apache.uima.resource.ResourceInitializationException;
import org.eclipse.gemini.blueprint.io.OsgiBundleResourcePatternResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Ivanov_AM
 *
 */
public class MyBundleTracker extends BundleTracker {

	public MyBundleTracker(BundleContext context, int stateMask, BundleTrackerCustomizer<?> customizer) {
		super(context, stateMask, customizer);
	}

	public Object addingBundle(Bundle bundle, BundleEvent event) {
		// Typically we would inspect bundle, to figure out if we want to
		// track it or not. If we don't want to track return null, otherwise
		// return an object.
		print(bundle, event);

		if(event.getType() == BundleEvent.RESOLVED) {
			URL url = bundle.getResource("META-INF/org.apache.uima.fit/types.txt");
			System.out.println(url.toString());

			OsgiBundleResourcePatternResolver resolver = new OsgiBundleResourcePatternResolver(bundle);

			try {
			  if(bundle.getSymbolicName().equals("com._1c.nlp.pipeline")) {
  				String[] tdLocations = org.apache.uima.fit.osgi.impl.MetaDataUtil.scanDescriptors(resolver, MetaDataType.TYPE_SYSTEM);
  				BufferedReader br =new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
  				while(br.ready()){
  				    System.out.println(br.readLine());
  				}
  				br.close();
			  }
			}
			catch (ResourceInitializationException | IOException e) {
				e.printStackTrace();
			}
		} else if(event.getType() == BundleEvent.UNRESOLVED) {

		}
		return bundle;
	}

	private void print(Bundle bundle, BundleEvent event) {
		String symbolicName = bundle.getSymbolicName();
		String state = stateAsString(bundle);
		String type = typeAsString(event);
		System.out.println("[BT] " + symbolicName + ", state: " + state + ", event.type: " + type);
	}

	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		print(bundle, event);
	}

	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		print(bundle, event);
	}

	private static String stateAsString(Bundle bundle) {
		if (bundle == null) {
			return "null";
		}
		int state = bundle.getState();
		switch (state) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.STARTING:
			return "STARTING";
		case Bundle.STOPPING:
			return "STOPPING";
		case Bundle.UNINSTALLED:
			return "UNINSTALLED";
		default:
			return "unknown bundle state: " + state;
		}
	}

	private static String typeAsString(BundleEvent event) {
		if (event == null) {
			return "null";
		}
		int type = event.getType();
		switch (type) {
		case BundleEvent.INSTALLED:
			return "INSTALLED";
		case BundleEvent.LAZY_ACTIVATION:
			return "LAZY_ACTIVATION";
		case BundleEvent.RESOLVED:
			return "RESOLVED";
		case BundleEvent.STARTED:
			return "STARTED";
		case BundleEvent.STARTING:
			return "Starting";
		case BundleEvent.STOPPED:
			return "STOPPED";
		case BundleEvent.UNINSTALLED:
			return "UNINSTALLED";
		case BundleEvent.UNRESOLVED:
			return "UNRESOLVED";
		case BundleEvent.UPDATED:
			return "UPDATED";
		default:
			return "unknown event type: " + type;
		}
	}
}
