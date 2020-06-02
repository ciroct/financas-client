package financas.control;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import financas.model.Usuario;
import financas.rest.client.UsuarioRESTClient;
import financas.util.SessionContext;

@ManagedBean
@RequestScoped
public class UsuarioBean {
	private Usuario usuario = new Usuario();

	public UsuarioBean() {
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String autenticar() {
		UsuarioRESTClient client = new UsuarioRESTClient();
		if (client.authenticate(usuario)) {
			return "/protected/categoria?faces-redirect=true";
		}
		FacesMessage msg = new FacesMessage("Login/senha inválidos");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}
	
	public String gravar() {
		UsuarioRESTClient client = new UsuarioRESTClient();
		if (client.create(usuario) == null) {
			FacesMessage msg = new FacesMessage("Já existe um usuário cadastrado com este e-mail (" + usuario.getEmail() + ")!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("frm:email", msg);
			return null;
		}
		return "/index?faces-redirect=true";
	}
	
	public String pag_usuairo() {
		usuario = new Usuario();
		return "/pag_usuario?faces-redirect=true";
	}
	
	public String logout() {
		SessionContext.getInstance().encerrarSessao();
		return "/index?faces-redirect=true";
	}
	
}
