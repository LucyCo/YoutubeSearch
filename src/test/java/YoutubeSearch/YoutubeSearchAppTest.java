package YoutubeSearch;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@SpringBootTest
class YoutubeSearchAppTest {
    YoutubeSearchApp app = new YoutubeSearchApp();

    @SneakyThrows
    @Test
    @DisplayName("Get video from title - positive cases")
    void testGetYoutubeVideoFromTitle_PositiveCases() {
        Assertions.assertTrue(app.getYoutubeVideoFromTitle("The beatles - Hello").getTitle()
                .toLowerCase().contains("hello"));
        Assertions.assertTrue(app.getYoutubeVideoFromTitle("hello").getTitle()
                .toLowerCase().contains("hello"));
        Assertions.assertTrue(app.getYoutubeVideoFromTitle("The beatles - strawberry fields").getTitle()
                .toLowerCase().contains("strawberry"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get video from title - test empty title")
    void testGetYoutubeVideoFromTitle_EmptyTitle() {
        Assertions.assertNull(app.getYoutubeVideoFromTitle(""));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get video from title - search no results")
    void testGetYoutubeVideoFromTitle_NoResults() {
        Assertions.assertNull(app.getYoutubeVideoFromTitle("jfnkadbakfekfjandhakwl"));
    }

    List<String> getListOfTitles() {
        List<String> listTitles = new ArrayList<>();
        listTitles.add("Bob Marley & The Wailers – ‘No Woman, No Cry’");
        listTitles.add("Sex Pistols – ‘God Save The Queen’");
        listTitles.add("Fleetwood Mac – ‘Go Your Own Way’");
        listTitles.add("The Clash – ‘London Calling’");
        listTitles.add("Blondie – ‘Heart Of Glass’");
        return listTitles;
    }

    @SneakyThrows
    @Test
    @DisplayName("Get playlist from list of titles - positive case")
    void testGetYoutubePlaylistFromTitleList_CheckSize() {
        List<String> listTitles = getListOfTitles();
        Optional<String> listOfTitles = listTitles.stream().reduce((a, b) -> a + "\n" + b);
        if (listOfTitles.isPresent()) {
            Assertions.assertEquals(5, app.getYoutubePlaylistFromTitleList(listOfTitles.get()).size());
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Get playlist from title - negative case")
    void testGetYoutubePlaylistFromTitleList_CheckSize_NoResult() {
        List<String> listTitles = getListOfTitles();
        listTitles.add("jsdhkjdhkfnweifnkafnlad");
        listTitles.add("Talking Heads – ‘Psycho Killer’");
        Optional<String> listOfTitles = listTitles.stream().reduce((a, b) -> a + "\n" + b);
        if (listOfTitles.isPresent()) {
            Assertions.assertEquals(6, app.getYoutubePlaylistFromTitleList(listOfTitles.get()).size());
        }
    }
}