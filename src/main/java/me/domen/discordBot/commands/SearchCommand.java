package me.domen.discordBot.commands;

import me.domen.discordBot.Bot;
import me.domen.discordBot.models.SearchModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SearchCommand implements CommandExecutor{

    public static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();
    private static final String searchChannel = Bot.properties.getProperty("searchChannel");
    private static final String posterPath = Bot.properties.getProperty("posterPath");

    @Override
    public void execute(MessageReceivedEvent event) {
        final MessageChannel channel = event.getChannel();
        final MessageChannel privateChannel = event.getGuild().getTextChannelById(searchChannel);

        final String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length == 1) {
            channel.sendMessage("Correct usage: !search <keywords> <page - optional>. Example: !search iron_man").queue();
            return;
        }
        String page = "1";
        if (args.length == 3) page = args[2];
        final Request request = createMovieSearchRequest(args[1].replace("_", " "), page);
        final SearchModel searchModel = SearchModel.retrieveSearchResults(client, request);
        if (searchModel == null) {
            channel.sendMessage("Something went wrong with the request :(").queue();
            return;
        }
        if (privateChannel == null) {
            channel.sendMessage("Incorrect channel ID").queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Searching for keyword(s):" + args[1]);
        embed.addField("Results found:", searchModel.getTotal_results() + "", true);
        embed.addField("Total pages:", searchModel.getTotal_pages() + "", true);
        embed.addField("Page:", searchModel.getPage() + "", true);
        privateChannel.sendMessageEmbeds(embed.build()).queue();

        embed.clear();

        for (SearchModel.Results results : searchModel.getResults()) {
            if (results.getPoster_path() == null) continue;
            try {
                embed.setTitle(results.getTitle());
                embed.setDescription("Movie id: " + results.getId());
                InputStream file = new URL(posterPath + results.getPoster_path()).openStream();
                embed.setImage("attachment://" + results.getPoster_path());
                privateChannel.sendFile(file, results.getPoster_path(), AttachmentOption.SPOILER).setEmbeds(embed.build()).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Request createMovieSearchRequest(final String query,@Nullable String page) {
        final String tmdbLink = "https://api.themoviedb.org/3/search/movie";
        final HttpUrl.Builder urlBuilder = HttpUrl.parse(tmdbLink).newBuilder();
        urlBuilder.addQueryParameter("api_key", Bot.loadProperties().getProperty("tmdb.apikey"));
        urlBuilder.addQueryParameter("query", query);
        urlBuilder.addQueryParameter("include_adult", "false");
        if (page == null) page = "1";
        urlBuilder.addQueryParameter("page", page);

        final String url = urlBuilder.build().toString();
        return new Request.Builder().url(url).build();
    }
}
