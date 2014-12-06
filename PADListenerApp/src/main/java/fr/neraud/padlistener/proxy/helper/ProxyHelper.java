package fr.neraud.padlistener.proxy.helper;

import android.content.Context;

import org.sandrop.webscarab.model.StoreException;
import org.sandrop.webscarab.plugin.Framework;
import org.sandrop.webscarab.plugin.proxy.IClientResolver;
import org.sandrop.webscarab.plugin.proxy.Proxy;
import org.sandroproxy.utils.NetworkHostNameResolver;
import org.sandroproxy.utils.PreferenceUtils;
import org.sandroproxy.utils.network.ClientResolver;
import org.sandroproxy.webscarab.store.sql.SqlLiteStore;

import java.io.File;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.exception.ListenerSetupException;
import fr.neraud.padlistener.proxy.plugin.PADPlugin;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Helper to start / stop the proxy
 *
 * @author Neraud
 */
public class ProxyHelper {

	private final Context context;

	private Framework framework = null;
	private NetworkHostNameResolver networkHostNameResolver = null;

	public ProxyHelper(Context context) {
		this.context = context;
	}

	public void activateProxy(ListenerService.CaptureListener captureListener) throws ListenerSetupException {
		MyLog.entry();

		framework = new Framework(context);
		setStore(context);
		networkHostNameResolver = new NetworkHostNameResolver(context);
		final IClientResolver clientResolver = new ClientResolver(context);
		final Proxy proxy = new Proxy(framework, networkHostNameResolver, clientResolver);
		framework.addPlugin(proxy);

		final PADPlugin plugin = new PADPlugin(context, captureListener);
		proxy.addPlugin(plugin);

		proxy.run();

		MyLog.exit();
	}

	public void deactivateProxy() {
		MyLog.entry();

		if (framework != null) {
			framework.stop();
		}
		if (networkHostNameResolver != null) {
			networkHostNameResolver.cleanUp();
		}
		networkHostNameResolver = null;
		framework = null;

		MyLog.exit();
	}

	private void setStore(Context context) throws ListenerSetupException {
		MyLog.entry();

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

		MyLog.exit();
	}
}
