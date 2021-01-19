package YoutubeSearch;


import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class YoutubeSearchApp {
    private final Logger m_logger = Logger.getLogger(YoutubeVideo.class.getName());
    public final int MAX_SIZE = 100;
    private AuthValues auth;

    public YoutubeSearchApp() {
        init();
    }

    /**
     * calls youtube api search and videos functions and returns
     * YoutubeVideo object with the params of the first search result
     * @param title - title of the song
     * @return null if title is empty or not found
     */
    public YoutubeVideo getYoutubeVideoFromTitle(String title) throws IOException, NoAuthKeyFoundException {
        if (title.isEmpty()) {
            return null;
        }
        SearchEngine engine = new SearchEngine();
        YoutubeVideo vid = new YoutubeVideo();
        vid = engine.SearchByQueryTerm(title, auth);
        return vid;
    }

    //splits string with stream api by the given char - returns a list of strings
    public List<String> getListOfTitles(String titleList) {
        return Arrays.stream(titleList.split("\n"))
                .map(s -> s.trim()).collect(Collectors.toList());
    }

    private Callable<YoutubeVideo> getVideoCallable(String title) {
        return () -> {
            YoutubeVideo youtubeVideoFromTitle = getYoutubeVideoFromTitle(title);
            return youtubeVideoFromTitle;
        };
    }

    public List<YoutubeVideo> getYoutubePlaylistFromTitleList(String titleList) throws InterruptedException, ExecutionException {
        List<YoutubeVideo> playlist = new ArrayList<>();
        List<String> titles = getListOfTitles(titleList);
        ExecutorService executor = Executors.newFixedThreadPool(titles.size());
        List<Future<YoutubeVideo>> list = new ArrayList<Future<YoutubeVideo>>();

        for (String title : titles) {
            Future<YoutubeVideo> future = executor.submit(getVideoCallable(title));
            list.add(future);
        }

        for (Future<YoutubeVideo> future : list) {
            YoutubeVideo result = future.get();
            if (result != null) {
                playlist.add(result);
            }
        }
        executor.shutdown();
        return playlist;
    }

    @SneakyThrows
    public void init() {
        this.auth = LoadAuthValues.getAuthValues();
    }

    public static void main(String[] args) {
        String playlist = "The Beatles - Hello\nThe Beatles - Strawberry Fields";
        YoutubeSearchApp app = new YoutubeSearchApp();
        List<YoutubeVideo> listOfVids = null;
        try {
            listOfVids = app.getYoutubePlaylistFromTitleList(playlist);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (listOfVids != null) {
            System.out.println("Not null!");
            listOfVids.forEach(System.out::println);
        }
    }
}

