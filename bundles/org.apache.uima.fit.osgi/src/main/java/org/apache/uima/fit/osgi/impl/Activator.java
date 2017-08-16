/**
 *
 */
package org.apache.uima.fit.osgi.impl;

import org.apache.uima.fit.osgi.utils.MyBundleTracker;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 */
public class Activator implements BundleActivator{
	private MyBundleTracker bundleTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle Tracker");
		int trackStates = Bundle.STARTING | Bundle.STOPPING | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED;
		bundleTracker = new MyBundleTracker(context, trackStates, null);
		bundleTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle Tracker");
		bundleTracker.close();
		bundleTracker = null;
	}

}
