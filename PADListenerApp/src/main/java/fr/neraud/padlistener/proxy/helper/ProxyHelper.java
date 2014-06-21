
package fr.neraud.padlistener.proxy.helper;

import java.io.File;

import org.sandrop.webscarab.model.StoreException;
import org.sandrop.webscarab.plugin.Framework;
import org.sandrop.webscarab.plugin.proxy.IClientResolver;
import org.sandrop.webscarab.plugin.proxy.Proxy;
import org.sandrop.webscarab.plugin.proxy.ProxyPlugin;
import org.sandroproxy.utils.NetworkHostNameResolver;
import org.sandroproxy.utils.PreferenceUtils;
import org.sandroproxy.utils.network.ClientResolver;
import org.sandroproxy.webscarab.store.sql.SqlLiteStore;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.exception.ListenerSetupException;
import fr.neraud.padlistener.proxy.plugin.PADPlugin;

/**
 * Helper to start / stop the proxy
 * 
 * @author Neraud
 */
public class ProxyHelper {

	private final Context context;

	private Framework framework = null;
	private NetworkHostNameResolver networkHostNameResolver = null;
	private IClientResolver clientResolver = null;

	public ProxyHelper(Context context) {
		this.context = context;
	}

	public void activateProxy() throws ListenerSetupException {
		Log.d(getClass().getName(), "activateProxy");
		framework = new Framework(context);
		setStore(context);
		networkHostNameResolver = new NetworkHostNameResolver(context);
		clientResolver = new ClientResolver(context);
		final Proxy proxy = new Proxy(framework, networkHostNameResolver, clientResolver);
		framework.addPlugin(proxy);

		final ProxyPlugin plugin = new PADPlugin(context);
		proxy.addPlugin(plugin);

		proxy.run();
	}

	public void deactivateProxy() {
		Log.d(getClass().getName(), "deactivateProxy");

		if (framework != null) {
			framework.stop();
		}
		if (networkHostNameResolver != null) {
			networkHostNameResolver.cleanUp();
		}
		networkHostNameResolver = null;
		framework = null;
	}

	private void setStore(Context context) throws ListenerSetupException {
		Log.d(getClass().getName(), "setStore");
		if (framework != null) {
			try {
				final File file = PreferenceUtils.getDataStorageDir(context);
				if (file != null) {
					final File rootDir = new File(file.getAbsolutePath() + "/content");
					if (!rootDir.exists()) {
						rootDir.mkdir();
					}
					framework.setSession("Database", SqlLiteStore.getInstance(context, rootDir.getAbsolutePath()), "");
				}
			} catch (final StoreException e) {
				throw new ListenerSetupException(e);
			}
		}
	}
}
