package financas.rest.client;

import java.util.List;

public interface RESTClientInterface<T> {
    public static final String REST_WEBSERVICE_URL = 
    	       "http://localhost:8080/financas/api/";
    public static final String REST_CATEGORIA_URL = "categoria/";

    public List<T> findAll();
    public T find(Long id);
    public T create(T obj);
    public T edit(T obj);
    public boolean delete(Long id);        
}
