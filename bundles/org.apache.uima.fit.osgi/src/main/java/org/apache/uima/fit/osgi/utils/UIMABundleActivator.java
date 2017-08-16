package org.apache.uima.fit.osgi.utils;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public abstract class UIMABundleActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		UIMABundleActivator.context = bundleContext;
		classRegistered();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		UIMABundleActivator.context = null;
	}

	protected abstract void classRegistered();
}
