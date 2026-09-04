<a name="readme-top"></a>
<div align="center">
  <h3 align="center">Book Club Discord Bot</h3>
  <p align="center">
    Connect your Goodreads profile to Discord, track it and share it with others!
    <br />
    <i>(Language used for the bot: Romanian 🇷🇴)</i>
  </p>
</div>

<br />
<div align="center">
  <a href="https://www.java.com/"> <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> </a>
  <a href="https://github.com/discord-jda/JDA"> <img src="https://img.shields.io/badge/Discord_JDA-5865F2?style=for-the-badge&logo=discord&logoColor=white"> </a>
  <a href="https://www.sqlite.org/"> <img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white"> </a>
  <a href="https://jsoup.org/"> <img src="https://img.shields.io/badge/Jsoup-000000?style=for-the-badge"> </a>
  <a href="https://www.goodreads.com/"> <img src="https://img.shields.io/badge/Goodreads-382110?style=for-the-badge&logo=goodreads&logoColor=white"> </a>
</div>

## Features
* **Goodreads Profile Linking:** Connect your Goodreads profile to your Discord account seamlessly via an interactive modal with the `/goodreads` slash command.
* **Rich Profile & Shelf Embeds:** View current books, recently completed reads with star ratings, want-to-read lists, and abandoned books with the `/profil` command (supports checking your own or another member's profile). Embeds include cover previews and direct links.
* **Real-time Web & RSS Scraping:** Powered by Jsoup to scrape public Goodreads profiles and parse shelf RSS feeds on-the-fly, displaying high-resolution book covers, author information, and ratings without requiring official API credentials.
* **Moderation Controls:** Dedicated admin/moderator commands (`/link` and `/unlink`) protected by member permissions to manage or remove profile links by user mention or Goodreads ID.
* **Persistent SQLite Storage:** Efficient local database storage managing member-to-profile mappings and preventing duplicate profile assignments across users.
* **Limba Română (Romanian Language):** Botul este configurat integral în limba română — toate comenzile slash, formularele modale, mesajele de confirmare/eroare și embed-urile sunt afișate nativ în limba română.
<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Acknowledgements
* [JDA (Java Discord API)](https://github.com/discord-jda/JDA)
* [Jsoup Java HTML Parser](https://jsoup.org/)
* [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)
* [Goodreads](https://www.goodreads.com/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
