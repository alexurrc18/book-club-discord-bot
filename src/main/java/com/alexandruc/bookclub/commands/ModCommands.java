package com.alexandruc.bookclub.commands;

import com.alexandruc.bookclub.scraper.Scraper;
import com.alexandruc.bookclub.scraper.models.Profile;
import com.alexandruc.bookclub.DatabaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Optional;

public class ModCommands extends ListenerAdapter {

    private final Scraper scraper = new Scraper();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        Member executor = event.getMember();

        boolean isMod = executor != null && (
                executor.hasPermission(Permission.MODERATE_MEMBERS) ||
                        executor.hasPermission(Permission.BAN_MEMBERS) ||
                        executor.hasPermission(Permission.ADMINISTRATOR)
        );

        if (!isMod) {
            event.reply("Nu ai permisiunea necesară pentru a rula comenzi de moderare.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        switch (commandName) {
            case "link" -> handleLink(event);
            case "unlink" -> handleUnlink(event);
        }
    }

    private void handleLink(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        OptionMapping userOpt = event.getOption("utilizator");
        OptionMapping idOpt = event.getOption("id_goodreads");

        if (userOpt == null || idOpt == null) {
            event.getHook().sendMessage("Trebuie să specifici atât utilizatorul, cât și ID-ul Goodreads.").queue();
            return;
        }

        User targetUser = userOpt.getAsUser();
        String goodreadsId = idOpt.getAsString().trim();

        if (!goodreadsId.matches("^\\d+$")) {
            event.getHook().sendMessage("ID-ul `" + goodreadsId + "` este invalid. Trebuie să fie compus doar din cifre.").queue();
            return;
        }

        Optional<Profile> profileOpt = scraper.scrapeProfile(goodreadsId);
        if (profileOpt.isEmpty()) {
            event.getHook().sendMessage("Nu s-a putut găsi niciun profil public de Goodreads cu ID-ul `" + goodreadsId + "`.").queue();
            return;
        }

        boolean saved = DatabaseManager.saveUser(targetUser.getIdLong(), goodreadsId);

        if (!saved) {
            event.getHook().sendMessage("ID-ul de Goodreads `" + goodreadsId + "` este deja conectat la un alt cont de Discord.").queue();
            return;
        }

        event.getHook().sendMessage("Contul lui " + targetUser.getAsMention() + " a fost asociat cu succes profilului: **" + profileOpt.get().getName() + "** (ID: `" + goodreadsId + "`).").queue();
    }

    private void handleUnlink(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        OptionMapping userOpt = event.getOption("utilizator");
        OptionMapping idOpt = event.getOption("id_goodreads");

        if (userOpt == null && idOpt == null) {
            event.getHook().sendMessage("Trebuie să furnizezi cel puțin un parametru: fie `@utilizator`, fie `id`.").queue();
            return;
        }

        if (userOpt != null) {
            User targetUser = userOpt.getAsUser();
            boolean deleted = DatabaseManager.deleteUser(targetUser.getIdLong());

            if (deleted) {
                event.getHook().sendMessage("Asocierea de Goodreads pentru " + targetUser.getAsMention() + " a fost ștearsă.").queue();
            } else {
                event.getHook().sendMessage(targetUser.getAsMention() + " nu are niciun cont de Goodreads asociat în baza de date.").queue();
            }
            return;
        }

        if (idOpt != null) {
            String goodreadsId = idOpt.getAsString().trim();
            boolean deleted = DatabaseManager.deleteByGoodreadsId(goodreadsId);

            if (deleted) {
                event.getHook().sendMessage("Asocierea pentru profilul Goodreads cu ID-ul `" + goodreadsId + "` a fost ștearsă.").queue();
            } else {
                event.getHook().sendMessage("Nu a fost găsită nicio înregistrare în baza de date pentru ID-ul Goodreads `" + goodreadsId + "`.").queue();
            }
        }
    }
}