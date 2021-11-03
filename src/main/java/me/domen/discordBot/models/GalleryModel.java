package me.domen.discordBot.models;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.List;

public final class GalleryModel {


    public static GalleryModel retrieveGalleryModels(OkHttpClient client, Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) System.out.println("Response unsuccessful");
            ResponseBody body = response.body();
            if (body == null) {
                System.out.println("Response body is null");
                return null;
            }
            return new Gson().fromJson(body.string(), GalleryModel.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private final List<Backdrops> backdrops;
    private final int id;
    private final List<Posters> posters;

    public GalleryModel(List<Backdrops> backdrops, int id, List<Posters> posters) {
        this.backdrops = backdrops;
        this.id = id;
        this.posters = posters;
    }

    public List<Backdrops> getBackdrops() {
        return backdrops;
    }

    public int getId() {
        return id;
    }

    public List<Posters> getPosters() {
        return posters;
    }

    public static final class Backdrops {
        private final double aspect_ratio;
        private final int height;
        private final String file_path;
        private final int width;

        public Backdrops(double aspect_ratio, int height, String file_path, int width) {
            this.aspect_ratio = aspect_ratio;
            this.height = height;
            this.file_path = file_path;
            this.width = width;
        }

        public double getAspect_ratio() {
            return aspect_ratio;
        }

        public int getHeight() {
            return height;
        }

        public String getFile_path() {
            return file_path;
        }

        public int getWidth() {
            return width;
        }
    }

    public static final class Posters {
        private final double aspect_ratio;
        private final int height;
        private final String file_path;
        private final int width;

        public Posters(double aspect_ratio, int height, String file_path, int width) {
            this.aspect_ratio = aspect_ratio;
            this.height = height;
            this.file_path = file_path;
            this.width = width;
        }

        public double getAspect_ratio() {
            return aspect_ratio;
        }

        public int getHeight() {
            return height;
        }

        public String getFile_path() {
            return file_path;
        }

        public int getWidth() {
            return width;
        }
    }
}
