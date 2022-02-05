package me.domen.discordBot.models;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.List;

public final class SearchModel {

    public static SearchModel retrieveSearchResults(final OkHttpClient client, final Request request) {
        try (final Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Something went wrong with request");
                return null;
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) return new Gson().fromJson(responseBody.string(), SearchModel.class);
            else System.out.println("Response body is null");
        } catch (IOException e) {
            e.printStackTrace();
            ;
        }
        return null;
    }

    private final int           page;
    private final List<Results> results;
    private final int           total_pages;
    private final int           total_results;

    public SearchModel(int page, List<Results> results, int total_pages, int total_results) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public int getPage() {
        return page;
    }

    public List<Results> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public static final class Results {
        private final boolean adult;
        private final String  backdrop_path;
        private final int[]   genre_ids;
        private final int     id;
        private final String  title;
        private final String  release_date;
        private final String  poster_path;
        private final boolean video;

        public Results(boolean adult, String backdrop_path, int[] genre_ids, int id, String title, String release_date, String poster_path, boolean video) {
            this.adult = adult;
            this.backdrop_path = backdrop_path;
            this.genre_ids = genre_ids;
            this.id = id;
            this.title = title;
            this.release_date = release_date;
            this.poster_path = poster_path;
            this.video = video;
        }

        public boolean isAdult() {
            return adult;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getRelease_date() {
            return release_date;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public boolean isVideo() {
            return video;
        }
    }
}
