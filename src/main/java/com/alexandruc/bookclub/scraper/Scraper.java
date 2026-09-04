package com.alexandruc.bookclub.scraper;

import com.alexandruc.bookclub.scraper.models.Book;
import com.alexandruc.bookclub.scraper.models.Profile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scraper {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
    private static final int TIMEOUT_MS = 10000;

    public Optional<Profile> scrapeProfile(String userId){


        if (userId == null || !userId.matches("^\\d+$")) {
            System.err.println("Eroare: ID-ul '" + userId + "' este invalid. Trebuie să conțină doar cifre.");
            return Optional.empty();
        }

        try{
            String profileUrl = "https://www.goodreads.com/user/show/" + URLEncoder.encode(userId, StandardCharsets.UTF_8);

            Document doc = Jsoup.connect(profileUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .get();

            Profile profile = new Profile();
            profile.setProfileUrl(profileUrl);


            Element nume = doc.selectFirst("h1.userProfileName");
            if (nume != null) {
                profile.setName(nume.text().trim());
            } else {
                profile.setName("Nume Indisponibil");
            }

            Element imgEl = doc.selectFirst("img.profilePictureIcon");
            if (imgEl != null && imgEl.hasAttr("src")) {
                profile.setAvatarUrl(imgEl.absUrl("src"));
            }


            profile.setCurrentlyReading(fetchShelfBooks(userId, "currently-reading"));
            profile.setToRead(fetchShelfBooks(userId, "to-read"));
            profile.setRead(fetchShelfBooks(userId, "read"));
            profile.setDidNotFinish(fetchShelfBooks(userId, "did-not-finish"));


            return Optional.of(profile);

        } catch (IOException e) {
            System.err.println("Eroare la obținerea profilului (" + userId + "): " + e.getMessage());
            return Optional.empty();
        }

    }


    public List<Book> fetchShelfBooks(String userId, String shelfName){
        List<Book> books = new ArrayList<>();
        String rssUrl = "https://www.goodreads.com/review/list_rss/" + userId + "?shelf=" + shelfName;
        try{
            Document doc = Jsoup.connect(rssUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .parser(Parser.xmlParser())
                    .get();

            Elements items = doc.select("item");
            for(Element item : items){
                Book book = new Book();
                book.setTitle(item.select("title").text().trim());


                book.setAuthor(item.select("author_name").text().trim());


                book.setLink(item.select("link").text().trim());


                String cover = item.select("book_large_image_url").text().trim();
                if (cover.isBlank()) {
                    cover = item.select("book_medium_image_url").text().trim();
                }
                if (cover.isBlank()) {
                    cover = item.select("book_image_url").text().trim();
                }

                book.setCoverUrl(cover);

                double ratingValue = 0.0;

                String userRatingStr = item.select("user_rating").text().trim();
                String avgRatingStr = item.select("average_rating").text().trim();

                try {
                    if (!userRatingStr.isBlank() && !userRatingStr.equals("0")) {
                        ratingValue = Double.parseDouble(userRatingStr);
                    } else if (!avgRatingStr.isBlank()) {
                        ratingValue = Double.parseDouble(avgRatingStr);
                    }
                } catch (NumberFormatException ignored) {}

                book.setRating(ratingValue);

                books.add(book);
            }
        } catch (IOException e){
            System.err.println("Eroare la obținerea bibliotecii " + shelfName + ": " + e.getMessage());
        }

        return books;
    }


}
