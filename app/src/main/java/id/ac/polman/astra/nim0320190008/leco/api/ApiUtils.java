package id.ac.polman.astra.nim0320190008.leco.api;

public class ApiUtils {

    public static final String API_URL = "http://192.168.100.7:8080/";

    private ApiUtils(){

    }

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    public static ResepService getResepService(){
        return RetrofitClient.getClient(API_URL).create(ResepService.class);
    }

    public static DisukaiService getDisukaiService(){
        return RetrofitClient.getClient(API_URL).create(DisukaiService.class);
    }
}
