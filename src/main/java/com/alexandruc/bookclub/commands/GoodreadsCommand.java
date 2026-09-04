package com.alexandruc.bookclub.commands;

import com.alexandruc.bookclub.DatabaseManager;
import com.alexandruc.bookclub.scraper.Scraper;
import com.alexandruc.bookclub.scraper.models.Profile;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.modals.Modal;

import java.util.Optional;

public class GoodreadsCommand extends ListenerAdapter {

    private final Scraper scraper = new Scraper();


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("goodreads")) {

            TextInput userId = TextInput.create("userId", TextInputStyle.SHORT)
                    .setPlaceholder("ID-ul profilului tău (9 cifre)")
                    .setMaxLength(9)
                    .setMinLength(9)
                    .build();

            Modal modal = Modal.create("linkUser", "Legare profil Goodreads")
                    .addComponents(Label.of("ID-ul profilului", userId))
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("linkUser")) return;
        event.deferReply(true).queue();

        String goodreadsId = event.getValue("userId").getAsString().trim();

        if (!goodreadsId.matches("^\\d+$")) {
            event.getHook().sendMessage("ID-ul introdus este invalid. Trebuie să conțină doar cifre!").queue();
            return;
        }

        Optional<Profile> profileOpt = scraper.scrapeProfile(goodreadsId);

        if (profileOpt.isEmpty()) {
            event.getHook().sendMessage("Nu s-a putut găsi un profil Goodreads public cu ID-ul `" + goodreadsId + "`.").queue();
            return;
        }

        Profile profile = profileOpt.get();
        long discordUserId = event.getUser().getIdLong();

        boolean salvat = DatabaseManager.saveUser(discordUserId, goodreadsId);

        if (salvat) {
            event.getHook().sendMessage(" Contul tău a fost asociat cu succes profilului Goodreads: **"
                    + profile.getName() + "**!").queue();
        } else {
            event.getHook().sendMessage("Acest profil Goodreads este deja conectat la un alt cont de Discord! Contactează un moderator dacă eroarea persistă.").queue();
        }

    }

}
