package com.alexandruc.bookclub;

import com.alexandruc.bookclub.commands.GoodreadsCommand;
import com.alexandruc.bookclub.commands.ModCommands;
import com.alexandruc.bookclub.commands.ProfileCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Main implements EventListener {

    public static void main(String[] args) throws InterruptedException {
        DatabaseManager.initDatabase();
        String token = System.getenv("BOT_TOKEN");

        JDA jda = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("Scrolling on Goodreads"))
                .addEventListeners(new GoodreadsCommand(), new ProfileCommand(), new ModCommands())
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("goodreads", "Leagă profilul tău de Discord cu profilul de Goodreads"),
                Commands.slash("profil", "Afișează detalii despre profilul tău de Goodreads")
                        .addOption(OptionType.USER, "utilizator", "pentru a vizualiza un utilizator specific"),
                Commands.slash("link", "Leagă manual un utilizator de un profil Goodreads")
                        .addOption(OptionType.USER, "utilizator", "Utilizatorul de Discord", true)
                        .addOption(OptionType.STRING, "id", "ID-ul numeric Goodreads", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)),
                Commands.slash("unlink", "Șterge asocierea de Goodreads a unui utilizator sau după ID Goodreads")
                        .addOption(OptionType.USER, "utilizator", "Utilizatorul căruia îi elimini asocierea", false)
                        .addOption(OptionType.STRING, "id_goodreads", "ID-ul de Goodreads pe care vrei să îl deconectezi", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS))
        ).queue();

        jda.awaitReady();


    }

    @Override
    public void onEvent(GenericEvent event)
    {
        if (event instanceof ReadyEvent)
            System.out.println("Bot-ul s-a deschis");
    }
}
