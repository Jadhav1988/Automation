/**
 *
 *
 */
package com.fission.callx.utilis;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Mohan Jadhav
 */
public class JerseyClient extends CommonSettings {

	/** The log. */
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	/** The http client. */
	private static volatile JerseyClient httpClient = null;

	/**
	 * Instantiates a new jersey client.
	 */
	private JerseyClient() {

	}

	/**
	 * Gets the single instance of JerseyClient.
	 *
	 */
	public static JerseyClient getInstance() {
		if (httpClient == null) {
			synchronized (JerseyClient.class) {
				if (httpClient == null) {
					httpClient = new JerseyClient();
				}
			}
		}

		return httpClient;
	}

	/**
	 * Gets the client.
	 *
	 * @param url
	 *            the url
	 * @return the client
	 */
	public String getClient(String url) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

	/**
	 * Get Request With AccessKey Header
	 *
	 * @param url
	 * @param accessTokenKey
	 * @return
	 */
	public String getClient(String url, String accessTokenKey) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.header("access-token", accessTokenKey).accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

	/**
	 * Post client.
	 *
	 * @param url
	 *            the url
	 * @param payload
	 *            the payload
	 * @return the string
	 */
	public String postClient(String url, String payload) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, payload);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

	/**
	 * Post Client
	 *
	 * @param url
	 * @param payload
	 * @param accessTokenKey-Header
	 *            access-token
	 * @return
	 */
	public String postClient(String url, String payload, String accessTokenKey) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);
		// webResource.header("access-token", accessTokenKey);

		ClientResponse response = webResource.header("access-token", accessTokenKey).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, payload);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

	/**
	 * Delete client.
	 *
	 * @param url
	 *            the url
	 * @return the string
	 */
	public String deleteClient(String url) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

	/**
	 * Put client.
	 *
	 * @param url
	 *            the url
	 * @return the string
	 */
	public String putClient(String url) {
		String jsonResponse = null;

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class);
		jsonResponse = response.getEntity(String.class);

		if (jsonResponse.length() <= 1000) {
			log.debug("Response: " + jsonResponse);
		} else {
			log.debug("Truncated response (as string length is much higher => " + jsonResponse.length() + ") -> "
					+ jsonResponse.substring(0, 1000));
		}

		return jsonResponse;
	}

}