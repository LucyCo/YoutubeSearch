package YoutubeSearch; /**
 * Sample Java code for youtube.search.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SearchEngine {
    private static final String CLIENT_SECRETS= "/init/client_secret.json";
    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
    public static final int MAX_RESULTS = 1;
    YouTube youtubeService = null;

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = SearchEngine.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("lulugoose9@gmail.com");
        return credential;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static YouTube getServiceNoCred() {
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(com.google.api.client.http.HttpRequest httpRequest) throws IOException {

            }
        }).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Calls Youtube api search method with given query term and authentication values
     * returns null if the queryTerm is empty
     */
    public YoutubeVideo SearchByQueryTerm(String queryTerm, AuthValues authVals)
            throws IOException, NoAuthKeyFoundException {
        if (authVals == null || authVals.getApiKey().isEmpty()) {
            throw new NoAuthKeyFoundException();
        }

        youtubeService = getServiceNoCred();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list(Arrays.asList(new String[]{"id"}));
        request.setMaxResults(Long.valueOf(MAX_RESULTS));
        request.setKey(authVals.getApiKey());
        request.setQ(queryTerm);
        SearchListResponse response = request.execute();

        List<SearchResult> items = response.getItems();
        if (items.isEmpty()) {
            return null;
        }
        ResourceId rscId = (ResourceId) items.get(0).getId();
        String vidId = rscId.getVideoId();
        YoutubeVideo vid = getVideoDetailsFromId(vidId, authVals);
        return vid;
    }

    public YoutubeVideo getVideoDetailsFromId(String videoId, AuthValues authVals) throws IOException {
        YouTube.Videos.List request = youtubeService.videos()
                .list(Arrays.asList(new String[]{"snippet","contentDetails","statistics"}));
        request.setKey(authVals.getApiKey());
        VideoListResponse response = request.setId(Arrays.asList(new String[]{videoId})).execute();
        List<Video> items = response.getItems();
        Video vid = items.get(0);
        VideoSnippet snippet = vid.getSnippet();


        return new YoutubeVideo(vid.getId(),snippet.getTitle(),
                snippet.getChannelId(), snippet.getChannelTitle(), snippet.getDescription());
    }
}