package financas.rest.client;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import financas.model.Usuario;
import financas.util.AuthenticationFeature;
import financas.util.Md5;
import financas.util.SessionContext;

public class UsuarioRESTClient implements RESTClientInterface<Usuario> {

	private Response response;
	
	@Override
	public List<Usuario> findAll() {
		this.response = ClientBuilder.newClient()
				.target(REST_WEBSERVICE_URL + REST_USUARIO_PROTECTED_URL)
				.request(MediaType.APPLICATION_JSON)
	    		.get();
		List<Usuario> usuarios = this.response
				.readEntity(new GenericType<List<Usuario>>() {});
		return usuarios;
	}

	@Override
	public Usuario find(Long id) {
		this.response = ClientBuilder.newClient()
				.register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_USUARIO_PROTECTED_URL + id)
				.request(MediaType.APPLICATION_JSON)
				.get();
		if (response.getStatus() == STATUS_OK) {
			Usuario usuario = this.response.readEntity(Usuario.class);
			return usuario;
		}
		return null;
	}

	public boolean authenticate(Usuario usuario) {
		usuario.setSenha(Md5.convertStringToMd5(usuario.getSenha()));
		this.response = ClientBuilder.newClient().
				target(REST_WEBSERVICE_URL + REST_USUARIO_URL + "auth").
	    		queryParam("usuario", usuario).
	    		request(MediaType.APPLICATION_JSON).
	    		post(Entity.entity(usuario, MediaType.APPLICATION_JSON));
		if (response.getStatus() == STATUS_OK) {
			Usuario _usuario = this.response.readEntity(Usuario.class);
			SessionContext.getInstance().setAttribute("usuario", _usuario);
			return true;
		}	    
		return false;
	}
	
	@Override
	public Usuario create(Usuario obj) {
		obj.setSenha(Md5.convertStringToMd5(obj.getSenha()));
	    this.response = ClientBuilder.newClient().
	    		target(REST_WEBSERVICE_URL + REST_USUARIO_URL).
	    		queryParam("usuario", obj).
	    		request(MediaType.APPLICATION_JSON).
	    		post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    if (response.getStatus() == STATUS_NOT_ACCEPTABLE) {
	    	return null;
	    }
	    Usuario usuario = this.response.readEntity(Usuario.class);		
		return usuario;		
	}

	@Override
	public Usuario edit(Usuario obj) {
		this.response = ClientBuilder.newClient().register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_USUARIO_PROTECTED_URL)
				.queryParam("categoria", obj)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(obj, MediaType.APPLICATION_JSON));
		if (this.response.getStatus() == STATUS_OK) {
			Usuario usuario = this.response.readEntity(Usuario.class);
			return usuario;
		}
		return null;
	}

	@Override
	public boolean delete(Long id) {
		this.response = ClientBuilder.newClient()
				.register(AuthenticationFeature.getFeature())
				.target(REST_WEBSERVICE_URL + REST_USUARIO_PROTECTED_URL + id)
				.request(MediaType.APPLICATION_JSON)
				.delete();
		return this.response.getStatus() == STATUS_OK;
	}

}
