package com.espendwise.manta.web.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.espendwise.manta.util.Utility;

/**
 * @author Alexey Lukovnikov
 *
 */
public class HttpClientUtil {
	/**
	 * Try load data (array of bytes) from specified URL (HTTP|HTTPS).
	 *
	 * @param urlString
	 *            The specified URL string.
	 * @return Content, array of bytes.
	 */
    public final static byte[] load(final String urlString)
            throws MalformedURLException, IOException {
        return load(urlString, null);
    }



	public final static byte[] load(final String urlString, String requiredMimeType)
            throws MalformedURLException, IOException {
		checkInitSSLConfiguration();
		HttpURLConnection httpConnection = null;
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			final URL url = new URL(urlString);
			httpConnection = (HttpURLConnection) url.openConnection();
            if (Utility.isSet(requiredMimeType)) {
                String contentType = httpConnection.getContentType();
                if ((contentType==null) ||
                    (contentType!=null && !contentType.startsWith(requiredMimeType))) {
                    throw new IOException("Cannot load data from " + urlString);
                }
            }

            final InputStream is = httpConnection.getInputStream();
            final byte data[] = new byte[1024];
            int count = is.read(data);
            while (count > 0) {
				baos.write(data, 0, count);
				count = is.read(data);
			}
            baos.flush();
			final byte[] result = baos.toByteArray();
            return result;
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			try {
				baos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * First attempt to init SSL-configuration.
	 */
	static {
		init();
	}

	/**
	 * Initialization error.
	 */
	private static Throwable initSSLConfigurationError;

	/**
	 * Check init SSL Configuration
	 *
	 */
	private final static void checkInitSSLConfiguration() {
		if (initSSLConfigurationError != null) {
			init();
		}
	}

	/**
	 * Configurate default SSL-environment
	 *
	 */
	private final static void init() {
		try {
			initSSLConfigurationError = null;
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(verifier);
		} catch (Throwable t) {
			initSSLConfigurationError = t;
			t.printStackTrace();
		}
	}

	/**
	 * Default array of trust managers.
	 */
	private final static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		/**
		 *
		 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		/**
		 *
		 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[],
		 *      java.lang.String)
		 */
		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		/**
		 *
		 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[],
		 *      java.lang.String)
		 */
		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };

	/**
	 * Default hostname verifier.
	 */
	private final static HostnameVerifier verifier = new HostnameVerifier() {
		public boolean verify(String string, SSLSession sSLSession) {
			return true;
		}
	};
}
