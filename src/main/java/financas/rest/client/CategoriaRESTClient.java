package financas.rest.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import financas.model.Categoria;
import financas.util.AuthenticationFeature;

public class CategoriaRESTClient implements RESTClientInterface<Categoria> {
	private Response response;

	@Override
	public List<Categoria> findAll() {
		Client client = ClientBuilder.newClient();
		client.register(AuthenticationFeature.getFeature());

		WebTarget webTarget = client.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		this.response = invocationBuilder.get();

		List<Categoria> categorias = this.response
				.readEntity(new GenericType<List<Categoria>>() {});
		client.close();
		return categorias;
	}

	@Override
	public Categoria find(Long id) {
		this.response = ClientBuilder.newClient()
				.register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL + id)
				.request(MediaType.APPLICATION_JSON)
				.get();
		if (response.getStatus() == STATUS_OK) {
			Categoria categoria = this.response.readEntity(Categoria.class);
			return categoria;
		}
		return null;
	}

	public List<Categoria> findByName(String nome) {
		this.response = ClientBuilder.newClient()
				.register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL + "nome/" + nome)
				.request(MediaType.APPLICATION_JSON)
				.get();
		List<Categoria> categorias = this.response.readEntity(new GenericType<List<Categoria>>() {
		});

		return categorias;
	}

	@Override
	public Categoria create(Categoria obj) {
		this.response = ClientBuilder.newClient().register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL)
				.queryParam("categoria", obj)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(obj, MediaType.APPLICATION_JSON));
		Categoria categoria = this.response.readEntity(Categoria.class);

		return categoria;
	}

	@Override
	public Categoria edit(Categoria obj) {
		this.response = ClientBuilder.newClient().register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL)
				.queryParam("categoria", obj)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(obj, MediaType.APPLICATION_JSON));
		if (this.response.getStatus() == STATUS_OK) {
			Categoria categoria = this.response.readEntity(Categoria.class);
			return categoria;
		}
		return null;
	}

	@Override
	public boolean delete(Long id) {
		this.response = ClientBuilder.newClient()
				.register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_CATEGORIA_URL + id)
				.request(MediaType.APPLICATION_JSON)
				.delete();
		return this.response.getStatus() == STATUS_OK;
	}

}
