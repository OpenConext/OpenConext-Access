package access.manage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class RemoteManage implements Manage {

    private static final Log LOG = LogFactory.getLog(RemoteManage.class);

    private final String url;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Object> queries;

    public RemoteManage(String url, String user, String password, ObjectMapper objectMapper) throws IOException {
        this.url = url;
        this.queries = objectMapper.readValue(new ClassPathResource("/manage/query_templates.json").getInputStream(), new TypeReference<>() {
        });
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user, password));
        ResponseErrorHandler resilientErrorHandler = new ResilientErrorHandler();
        restTemplate.setErrorHandler(resilientErrorHandler);
    }

    @Override
    public List<Map<String, Object>> providers(EntityType... entityTypes) {
        LOG.debug("Providers for entityTypes: " + List.of(entityTypes));
        return Stream.of(entityTypes).map(entityType -> this.getRemoteMetaData(entityType.collectionName()))
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public Map<String, Object> providerById(EntityType entityType, String id) {
        LOG.debug("providerById: " + entityType);
        String queryUrl = String.format("%s/manage/api/internal/metadata/%s/%s", url, entityType.collectionName(), id);
        return restTemplate.getForEntity(queryUrl, Map.class).getBody();
    }


    private List<Map<String, Object>> getRemoteMetaData(String type) {
        Map<String, Object> baseQuery = getBaseQuery();
        String url = String.format("%s/manage/api/internal/search/%s", this.url, type);
        return restTemplate.postForObject(url, baseQuery, List.class);
    }

    private Map<String, Object> getBaseQuery() {
        HashMap<String, Object> baseQuery = new HashMap<>((Map<String, Object>) this.queries.get("base_query"));
        baseQuery.put("REQUESTED_ATTRIBUTES", baseQuery.get("REQUESTED_ATTRIBUTES"));
        return baseQuery;
    }


}
