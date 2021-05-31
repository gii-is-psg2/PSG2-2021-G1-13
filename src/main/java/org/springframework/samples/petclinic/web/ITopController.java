
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.samples.petclinic.model.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/itop")
public class ITopController {

	private HttpHeaders getHeaders() {
		final String plainCredentials = "admin:admin";
		final String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Credentials);
		return headers;
	}

	@GetMapping()
	public String ITopControllerGet(final Map<String, Object> model) {

		final String uri = "https://psg2-g1-13-itop.enriquexperiments.com/webservices/rest.php?version=1.3&json_data={ \"operation\": \"core/get\", \"class\": \"Person\", \"key\": \"SELECT Person WHERE email LIKE '%'\", \"output_fields\": \"friendlyname, email,phone\" }";
		final CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		final RestTemplate rest = new RestTemplate(requestFactory);
		final HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

		final ResponseEntity<String> response = rest.exchange(uri, HttpMethod.GET, request, String.class);
		final List<Contact> list = new ArrayList<Contact>();
		final String[] splitsJSON = response.getBody().replace("{\"objects\":{", "").split("}},");

		for (int i = 0, j = 2; i < splitsJSON.length - 1; i++, j++) {
			splitsJSON[i] = splitsJSON[i].replace("\"Person::" + j + "\":{\"code\":0,\"message\":\"\",\"class\":\"Person\",\"key\":\"" + j + "\",\"fields\":{" + "\"friendlyname\":", "").replace("\"email\":", "").replace("\"phone\":", "").replaceAll("\"",
				"");

			final String[] splitsContact = splitsJSON[i].split(",");
			if (splitsContact[6].length() == 10)
				splitsContact[6] = splitsContact[6].replace("}", "");
			final Contact c = new Contact(splitsContact[4].split(":")[2], splitsContact[5], splitsContact[6]);
			list.add(c);
		}
		model.put("contacts", list);
		return "itop";
	}

}
