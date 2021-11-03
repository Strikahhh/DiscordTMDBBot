package me.domen.discordBot.commands;

import me.domen.discordBot.Bot;
import me.domen.discordBot.models.GalleryModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.InputStream;
import java.net.URL;

public class GalleryCommand implements CommandExecutor {

    private static final String backdropPath = Bot.properties.getProperty("backdropPath");
    private static final String galleryId = Bot.properties.getProperty("galleryId");


    @Override
    public void execute(MessageReceivedEvent event) {
        final MessageChannel channel = event.getChannel();
        final MessageChannel privateChannel = event.getGuild().getTextChannelById(galleryId);

        final String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length == 1) {
            channel.sendMessage("Correct usage: !gallery <movie-id>").queue();
            return;
        }
        final GalleryModel galleryModel = GalleryModel.retrieveGalleryModels(SearchCommand.client, createMovieGalleryRequest(args[1]));
        if (galleryModel == null) {
            System.out.println("Something went wrong with search");
            return;
        }
        if (galleryModel.getBackdrops().size() == 0) {
            channel.sendMessage("There seems to be no backdrops for this query").queue();
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Search successful!");
        embed.addField("Pictures found:", galleryModel.getBackdrops().size() + "", true);
        embed.addField("Posters found:", galleryModel.getPosters().size() + "", true);
        for (GalleryModel.Backdrops backdrops : galleryModel.getBackdrops()) {
            try  {
                InputStream file = new URL(backdropPath + backdrops.getFile_path()).openStream();
                embed.setImage("attachment://" + backdrops.getFile_path());
                privateChannel.sendFile(file, backdrops.getFile_path(), AttachmentOption.SPOILER).setEmbeds(embed.build()).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private static Request createMovieGalleryRequest(final String id) {
        final String tmdbLink = "https://api.themoviedb.org/3/movie/" + id + "/images";
        final HttpUrl.Builder urlBuilder = HttpUrl.parse(tmdbLink).newBuilder();
        urlBuilder.addQueryParameter("api_key", Bot.loadProperties().getProperty("tmdb.apikey"));
        urlBuilder.addQueryParameter("language", "null");
        final String url = urlBuilder.build().toString();
        return new Request.Builder().url(url).build();
    }
}
