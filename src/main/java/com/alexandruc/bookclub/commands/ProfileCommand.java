package com.alexandruc.bookclub.commands;

import com.alexandruc.bookclub.DatabaseManager;
import com.alexandruc.bookclub.scraper.Scraper;
import com.alexandruc.bookclub.scraper.models.Book;
import com.alexandruc.bookclub.scraper.models.Profile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

public class ProfileCommand extends ListenerAdapter {
    private final Scraper scraper = new Scraper();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("profil")) return;

        event.deferReply().queue();

        OptionMapping userOption = event.getOption("utilizator");
        User targetUser = (userOption != null) ? userOption.getAsUser() : event.getUser();

        Optional<String> goodreadsIdOpt = DatabaseManager.getGoodreadsId(targetUser.getIdLong());

        if (goodreadsIdOpt.isEmpty()) {
            String msg = (targetUser.equals(event.getUser()))
                    ? "Nu ai contul asociat! Folosește `/goodreads` pentru a-ți asocia profilul."
                    : "Utilizatorul " + targetUser.getAsMention() + " nu și-a asociat contul de Goodreads.";

            event.getHook().sendMessage(msg).queue();
            return;
        }

        Optional<Profile> profileOpt = scraper.scrapeProfile(goodreadsIdOpt.get());
        if (profileOpt.isEmpty()) {
            event.getHook().sendMessage("Nu s-au putut prelua datele de pe Goodreads.").queue();
            return;
        }

        Profile profile = profileOpt.get();

        String thumbnail = profile.getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Profil Goodreads: " + profile.getName(), profile.getProfileUrl())
                .setColor(new Color(0xFFEED2))
                .setThumbnail(thumbnail);

        embed.addField(
                "În curs de citire (" + profile.getCurrentlyReading().size() + ")",
                formatBookList(profile.getCurrentlyReading(), 3, false),
                false
        );

        embed.addField(
                "Recent Citite (" + profile.getRead().size() + ")",
                formatBookList(profile.getRead(), 4, true),
                false
        );

        embed.addField(
                "Vreau să citeasc (" + profile.getToRead().size() + ")",
                formatBookList(profile.getToRead(), 3, false),
                false
        );

        if (!profile.getDidNotFinish().isEmpty()) {
            embed.addField(
                    "Abandonate (" + profile.getDidNotFinish().size() + ")",
                    formatBookList(profile.getDidNotFinish(), 3, false),
                    false
            );
        }

        if (!profile.getCurrentlyReading().isEmpty()) {
            String cover = profile.getCurrentlyReading().get(0).getCoverUrl();
            if (cover != null && !cover.isBlank()) {
                embed.setImage(cover);
            }
        }


        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }

    private String formatBookList(List<Book> books, int limit, boolean showRating) {
        if (books == null || books.isEmpty()) {
            return "*Nicio carte în această bibliotecă.*";
        }

        StringBuilder sb = new StringBuilder();
        int count = Math.min(books.size(), limit);

        for (int i = 0; i < count; i++) {
            Book book = books.get(i);

            String titleLink = (book.getLink() != null && !book.getLink().isBlank())
                    ? "[" + escapeMarkdown(book.getTitle()) + "](" + book.getLink() + ")"
                    : "**" + escapeMarkdown(book.getTitle()) + "**";

            sb.append("• ").append(titleLink);

            if (book.getAuthor() != null && !book.getAuthor().isBlank()) {
                sb.append(" de *").append(escapeMarkdown(book.getAuthor())).append("*");
            }

            if (showRating && book.getRating() > 0) {
                sb.append(" • ⭐ `").append(book.getRating()).append("`");
            }

            sb.append("\n");
        }

        if (books.size() > limit) {
            sb.append("*...și încă ").append(books.size() - limit).append(" cărți.*");
        }

        return sb.toString();
    }

    private String escapeMarkdown(String text) {
        if (text == null) return "";
        return text.replace("*", "\\*").replace("_", "\\_").replace("~", "\\~");
    }
}