package modified.dobjanschi.a.pattern.network.model;

/**
 * @author Rustem
 */
public class RequestItem {

    private final String request;

    private String response;

    public RequestItem(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String json) {
        this.response = json;
    }
}