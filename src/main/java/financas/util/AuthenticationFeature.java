package financas.util;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import financas.model.Usuario;

public class AuthenticationFeature {
	
	public static HttpAuthenticationFeature getFeature() {
		Usuario usuario = (Usuario) SessionContext.getInstance().getAttribute("usuario");
		if (usuario == null) {
			return null;
		}
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(usuario.getEmail(), usuario.getSenha());
		return feature;
	}

}
