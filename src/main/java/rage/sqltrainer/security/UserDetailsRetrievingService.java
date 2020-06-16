package rage.sqltrainer.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsRetrievingService {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    // This assumes that we're using mooc.fi for user details
    String TMCURL = "https://tmc.mooc.fi/api/v8/users/current.json";
    
    public TmcUserDetails getUserDetails() {
        return restTemplate.getForObject(TMCURL, TmcUserDetails.class);
    }

    public TmcUserDetails getUserDetails(String accesstoken) {
        if(accesstoken == null) {
            return getUserDetails();
        }
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", accesstoken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(TMCURL, HttpMethod.GET, entity, TmcUserDetails.class).getBody();
    }
}
