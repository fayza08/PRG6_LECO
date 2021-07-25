package id.ac.polman.astra.nim0320190008.leco.api;

public class ApiUtils {

    public static final String API_URL = "http://192.168.100.7:8080/";

    private ApiUtils(){

    }

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }
}
